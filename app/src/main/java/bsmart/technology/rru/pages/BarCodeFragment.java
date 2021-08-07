package bsmart.technology.rru.pages;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.xch.scanzxing.zxing.android.CaptureActivity;
import com.google.gson.JsonObject;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import bsmart.technology.rru.R;
import bsmart.technology.rru.base.App;
import bsmart.technology.rru.base.BaseFragment;
import bsmart.technology.rru.base.api.NetSubscriber;
import bsmart.technology.rru.base.api.NetTransformer;
import bsmart.technology.rru.base.api.RECDTSApi;
import bsmart.technology.rru.base.utils.ChannelUtil;
import bsmart.technology.rru.base.utils.HeaderView;
import bsmart.technology.rru.base.utils.HealthUtil;
import bsmart.technology.rru.base.utils.LocationUtils;
import bsmart.technology.rru.base.utils.ProfileUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.RequestBody;

public class BarCodeFragment extends BaseFragment {
    private static final int REQUEST_CODE_SCAN = 0x0000;// scan QRCode

    @BindView(R.id.header)
    HeaderView headerView;

    private String smart_driver_id;
    private String office_id;

    @BindView(R.id.spinner1)
    Spinner reason;
    @BindView(R.id.bookId)
    TextView bookId;
    @BindView(R.id.fullName)
    EditText fullName;
    @BindView(R.id.passport)
    EditText passport;
    @BindView(R.id.temperture)
    EditText temperture;

    @BindView(R.id.test1Result)
    Spinner test1Result;

    @BindView(R.id.test2Result)
    Spinner test2Result;

    @BindView(R.id.test3Result)
    Spinner test3Result;

    @BindView(R.id.test4Result)
    Spinner test4Result;

    @BindView(R.id.test5Result)
    Spinner test5Result;

    @BindView(R.id.test6Result)
    Spinner test6Result;

    @BindView(R.id.test7Result)
    Spinner test7Result;

    @BindView(R.id.test8Result)
    Spinner test8Result;

    @BindView(R.id.test9Result)
    Spinner test9Result;

    @BindView(R.id.test10Result)
    Spinner test10Result;

    @BindView(R.id.test11Result)
    Spinner test11Result;

    @BindView(R.id.test12Result)
    Spinner test12Result;

    @BindView(R.id.test13Result)
    Spinner test13Result;

    @BindView(R.id.test14Result)
    Spinner test14Result;

    @BindView(R.id.test15Result)
    Spinner test15Result;

    @BindView(R.id.healthDec)
    CheckBox healthDeclared;

    @BindView(R.id.remark)
    EditText comment;

    @BindView(R.id.tvSubmitBtn)
    View submitBtn;

    @BindView(R.id.inputLayout)
    View inputLayout;

    private View rootView;
    Unbinder unbinder;

    private ConnectivityManager connectivityManager;//用于判断是否有网络

    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_health_v2_input, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        View customRightView = LayoutInflater.from(getContext()).inflate(R.layout.action__health_right, null);
        customRightView.findViewById(R.id.flRefresh).setOnClickListener(v -> {
            this.openBarCodeActivity();
        });
        headerView.setRightCustomView(customRightView);
        String countryCode = ProfileUtils.getLoginCountryCode();
        String clinicCode = ProfileUtils.getLoginClinicCode();
        headerView.setTitle("Health Entry("+countryCode+","+clinicCode+")");

        submitBtn.setOnClickListener(view -> {
            this.submit();
        });
        return rootView;
    }


    private void openBarCodeActivity() {

        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.VIBRATE)
                .subscribe(granted -> {
                    if (granted) {
                        goScan();
                    } else {
                        ToastUtils.showShort("please grant the permission.");
                    }
                });
    }

    /**
     * redirect scan
     */
    private void goScan() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        getActivity().startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScan();
                } else {
                    Toast.makeText(getActivity(), "You have rejected the permission application, you may not be able to open the camera to scan the code!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SCAN:// QRCode
                if (resultCode == getActivity().RESULT_OK) {
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        String result = bundle.getString("content");
                        Log.i("BarCodeFragment", "scan content:" + result);
                        judgeQRCode(result);
                    }
                }
                break;
            default:
                break;
        }
    }

    private String personalInfo = "";

    private boolean hasNetwork(){
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (null != info) {
            return true;
        }else{
            return false;
        }
    }

    private boolean isAESHeader(String channel){
        Pattern p1 = Pattern.compile("^"+ChannelUtil.channel_eacpass_value+"\\d+");
        Pattern p2 = Pattern.compile("^"+ChannelUtil.channel_eacpass_value.toLowerCase()+"\\d+");
        boolean b1 = p1.matcher(channel).matches();
        boolean b2 = p2.matcher(channel).matches();
        return b1 || b2;
    }

    private final Runnable scanLoggerWorker = new Runnable() {

        @Override
        public void run() {
            Map<String, Object> requestData = new HashMap<>();
            String countryCode = ProfileUtils.getLoginCountryCode();
            String clinicCode = ProfileUtils.getLoginClinicCode();
            requestData.put("countryCode", countryCode);
            requestData.put("clinicCode", clinicCode);
            RECDTSApi.getAppHAHVDVService().scanLogger(requestData)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {
                        System.out.println("scanLogger:"+bean.toString());

                    }, e -> {
                        System.out.println("scanLogger:"+e.getMessage());
                    }));

        }
    };

    private void judgeQRCode(String text){
        if (TextUtils.isEmpty(text)){
            ToastUtils.showLong("Scan result is empty.");
        }

        new Thread(scanLoggerWorker).start();

        String[] result = text.split(":");
        String channel = result[0];
        Log.i("BarCodeFragment", "scan channel:" + channel);

        if (isAESHeader(channel)){
            if(!hasNetwork()){
                ToastUtils.showLong("Please check if the mobile device network is available!");
                return;
            }

            //Request Remote Server Decode AES QRCode
            JSONObject bodyJson = new JSONObject();
            try {
                bodyJson.put("qrCode",text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Parsing... Please wait.");
            progressDialog.show();
            String strEntity = bodyJson.toString();
            System.out.println("strEntity:"+strEntity);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);
            RECDTSApi.getAppHAHVDVService().parseCode(body)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {
                        progressDialog.dismiss();
                        System.out.println("bean:"+bean.toString());
                        if (bean.has("qrCode")){
                            String realCode = bean.get("qrCode").getAsString();
                            showResult(realCode);

                        }else{
                            ToastUtils.showLong("Parse QRCode Failed.");

                        }

                    }, e -> {
                        ToastUtils.showLong(e.getMessage());
                        progressDialog.dismiss();
                    }));

        }else{
            showResult(text);
        }
    }
    private String channel = "";
    private void showResult(String text) {
        String[] result = text.split(";");
        if (result.length < 2 || !(result[0].equals(ChannelUtil.channel_recdts_value)
                || result[0].equals(ChannelUtil.channel_eacpass_value)
                || result[0].equals(ChannelUtil.channel_eacpass_value.toLowerCase())
        )) {
            ToastUtils.showLong("Invalid Scan Result.");
            submitBtn.setClickable(false);
            return;
        } else {
            submitBtn.setClickable(true);
            inputLayout.setVisibility(View.VISIBLE);

            ToastUtils.showShort("You can fill form now.");
        }
        channel = result[0];
        personalInfo = result[1];
        String[] t = personalInfo.split("\\|");
        smart_driver_id = t[3];
        office_id = t[1];

        String digit = t[2];
        bookId.setText(digit);
        clearInput();

        if (result.length > 3) {
            fullName.setText(result[3]);
        }
        if (result.length > 4) {
            passport.setText(result[4]);
        }
    }

    //HA Post Result
    private void submit() {

        String beanId = bookId.getText().toString();
        if (beanId.isEmpty()) {
            ToastUtils.showShort("Please check QRCode and retry again.");
            return;
        }

        if (comment.getText().toString().contains(";")){
            ToastUtils.showShort("remark can't contains ; keyword!.");
            return;
        }

        String reasonText = reason.getSelectedItem().toString();
        String name = fullName.getText().toString().trim();
        String pass = passport.getText().toString().trim();
        String temper = temperture.getText().toString().trim();
        String test1 = test1Result.getSelectedItemId() + "";
        String test2 = test2Result.getSelectedItemId() + "";
        String test3 = test3Result.getSelectedItemId() + "";
        String test4 = test4Result.getSelectedItemId() + "";
        String test5 = test5Result.getSelectedItemId() + "";
        String test6 = test6Result.getSelectedItemId() + "";
        String test7 = test7Result.getSelectedItemId() + "";
        String test8 = test8Result.getSelectedItemId() + "";
        String test9 = test9Result.getSelectedItemId() + "";
        String test10 = test10Result.getSelectedItemId() + "";
        String test11 = test11Result.getSelectedItemId() + "";
        String test12 = test12Result.getSelectedItemId() + "";
        String test13 = test13Result.getSelectedItemId() + "";
        String test14 = test14Result.getSelectedItemId() + "";
        String test15 = test15Result.getSelectedItemId() + "";
        String declared = healthDeclared.isChecked() ? "1" : "0";
        String comm = comment.getText().toString().trim();

        if (temper.isEmpty()) {
            ToastUtils.showShort("temperure value must not be empty");
            return;
        }
        float temperFloat = Float.parseFloat(temper);
        if (temperFloat < 32.0 || temperFloat > 42.0) {
            ToastUtils.showShort("temperure value invalid(32.0~42.0)");
            return;
        }

        if (temperFloat>37.79){
            test13 = "0";
        }

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Processing... Please wait.");
        progressDialog.show();

        Map<String, Object> requestData =  HealthUtil.healthStringMetaData(smart_driver_id,bookId.getText().toString(),
                test1,test2,test3,test4,
                test5,test6,test7,test8,
                test9,test10,test11,test12,
                test13,test14,test15,
                temperFloat,
                ProfileUtils.getCUserRid(),
                comm);
        Location mLocation = LocationUtils.getInstance(this.getContext()).showLocation();
        if (null != mLocation){
            requestData.put("n_latitude",mLocation.getLatitude());
            requestData.put("n_longitude",mLocation.getLongitude());
        }
        //[end] build real post data in clinic_data table;
        if (channel.equals(ChannelUtil.channel_recdts_value)){
            RECDTSApi.getAppRECDTS_hahvdvService().updateHealth(requestData)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {
                        progressDialog.dismiss();
                        ToastUtils.showShort("Submit RECDTS successfully!");
                        bookId.setText("");
                        clearInput();

                    }, e -> {
                        progressDialog.dismiss();
                        ToastUtils.showLong("Submit RECDTS Failed, Please retry.");

                    }));
        }else if(channel.equals(ChannelUtil.channel_eacpass_value)
        ||channel.equals(ChannelUtil.channel_eacpass_value.toLowerCase()) ){
            RECDTSApi.getAppHAHVDVService().updateHealth(requestData)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {
                        progressDialog.dismiss();
                        ToastUtils.showShort("Submit EACPass successfully!");
                        bookId.setText("");
                        clearInput();

                    }, e -> {
                        progressDialog.dismiss();
                        ToastUtils.showLong("Submit EACPass Failed, Please retry.");

                    }));
        }

    }

    private void clearInput() {
        reason.setSelection(0);
        fullName.setText(null);
        passport.setText(null);
        temperture.setText(null);
        comment.setText(null);

        test1Result.setSelection(1);
        test2Result.setSelection(1);
        test3Result.setSelection(1);
        test4Result.setSelection(1);
        test5Result.setSelection(1);
        test6Result.setSelection(1);
        test7Result.setSelection(1);
        test8Result.setSelection(1);
        test9Result.setSelection(1);
        test10Result.setSelection(1);
        test11Result.setSelection(1);
        test12Result.setSelection(1);
        test13Result.setSelection(1);
        test14Result.setSelection(1);
        test15Result.setSelection(1);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
