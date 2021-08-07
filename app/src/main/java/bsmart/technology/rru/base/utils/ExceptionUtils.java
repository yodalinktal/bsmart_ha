package bsmart.technology.rru.base.utils;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import bsmart.technology.rru.base.App;
import rx.Observable;
import rx.schedulers.Schedulers;

public class ExceptionUtils {
  private final String SPNAME = "exceptions";

  private static final String EMAIL_USERNAME = "";
  private static final String EMAIL_PASSWORD = "";
  private static final String EMAIL_SMTP_HOST = "smtp.gmail.com";
  private static final String EMAIL_SMTP_PORT = "587";
  private static final String EMAIL_RECEIVER = "i@timqi.com";

  private ExceptionUtils() {
  }

  private static ExceptionUtils INSTANCE = new ExceptionUtils();

  public static ExceptionUtils getINSTANCE() {
    return INSTANCE;
  }

  private Context mCtx;

  private boolean mServiceIsOn = false;

  public void initialize(Context ctx) {
    this.mCtx = ctx;
//    mServiceIsOn = true;
//    triggerRequest();
  }

  private void triggerRequest() {
    Map<String, ?> all = SPUtils.getInstance(SPNAME).getAll();
    for (Map.Entry<String, ?> entry : all.entrySet()) {
      String value = (String) entry.getValue();
      BEAN bean = App.gson.fromJson(value, BEAN.class);
      handleBean(bean);
    }
  }

  public void catchError(String errorInformation) {
    if (!mServiceIsOn) {
      return;
    }

    BEAN bean = createProcessingMeta();
    LogUtils.d(App.gson.toJson(bean));
    bean.content = errorInformation;
    handleBean(bean);
  }

  private void saveBean(BEAN bean) {
    SPUtils.getInstance(SPNAME).put(bean.hash(), App.gson.toJson(bean));
  }

  private void handleBean(BEAN bean) {
    if (!NetworkUtils.isConnected()) {
      bean.isProcessing = false;
      saveBean(bean);
      return;
    }

    if (bean.isProcessing ) {

      try {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bean.datetime);
        long diff = Calendar.getInstance().getTimeInMillis() - date.getTime();
        if (diff < 5*60*1000) {
          // Do not process in 5 mins
          return;
        }

      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    bean.isProcessing = true;
    saveBean(bean);

    Properties props = new Properties();
    props.put("mail.smtp.host", EMAIL_SMTP_HOST);
    props.put("mail.smtp.port", EMAIL_SMTP_PORT);
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.socketFactory.port", EMAIL_SMTP_PORT);
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


    Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
          }
        });


    Observable.just(session)
        .subscribeOn(Schedulers.io())
        .subscribe(session1 -> {

          try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_RECEIVER));
            message.setSubject("[RRU] Error Info: " + bean.content);
            message.setText("Content:\n" +
                "From Error Information: " + bean.content + " Please note!\n" +
                "\n" +
                "ip: " + bean.ip + "\n" +
                "Network Type:" + bean.networkType + "\n" +
                "Mobile Phone Type:" + bean.mobilePhoneType + "\n" +
                "Account: " + bean.account + "\n" +
                "Datetime: " + bean.datetime);

            Transport.send(message);
            deleteBean(bean);

          } catch (Exception e) {
            e.printStackTrace();
            bean.isProcessing = false;
            saveBean(bean);
          }

        });
  }

  private void deleteBean(BEAN bean) {
    SPUtils.getInstance(SPNAME).remove(bean.hash());
  }

  private BEAN createProcessingMeta() {
    BEAN bean = new BEAN();
    bean.datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    bean.networkType = NetworkUtils.getNetworkType().toString();
    bean.mobilePhoneType = DeviceUtils.getModel();
    bean.ip = TextUtils.isEmpty(NetworkUtils.getIpAddressByWifi()) ? NetworkUtils.getIpAddressByWifi() : NetworkUtils.getIPAddress(true);
    bean.ip = NetworkUtils.getIPAddress(true);
    bean.account = ProfileUtils.getOfficerId();
    return bean;
  }

  private static class BEAN {

    public String datetime;
    public String networkType;
    public String mobilePhoneType;
    public String account;
    public String ip;

    public boolean isProcessing = false;
    public String content;

    private String randomStr; // for encode

    public BEAN() {
      String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz|!£$%&/=@#";
      Random RANDOM = new Random();

      int len = 16;
      StringBuilder sb = new StringBuilder(len);
      for (int i = 0; i < len; i++) {
        sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
      }
      this.randomStr = sb.toString();
    }

    public String hash() {
      return EncryptUtils.encryptMD5ToString(this.datetime+this.randomStr);
    }
  }
}
