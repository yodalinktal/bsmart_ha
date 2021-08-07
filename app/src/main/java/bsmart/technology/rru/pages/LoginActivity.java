package bsmart.technology.rru.pages;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import bsmart.technology.rru.R;
import bsmart.technology.rru.base.App;
import bsmart.technology.rru.base.BaseActivity;
import bsmart.technology.rru.base.utils.BSmartUtil;
import bsmart.technology.rru.base.utils.LocationUtils;
import bsmart.technology.rru.base.utils.ProfileUtils;
import bsmart.technology.rru.base.utils.download.DownloadReceiver;
import butterknife.BindView;
import butterknife.ButterKnife;
public class LoginActivity extends BaseActivity {


    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.countryCode)
    Spinner countryCode;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;

    @BindView(R.id.ca_title)
    TextView ca_title;

    @BindView(R.id.passwordAgainZone)
    LinearLayout passwordAgainZone;
    @BindView(R.id.etPhoneNumber2)
    EditText etPhoneNumber2;

    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;

    private boolean noDevice = false;


    private DownloadReceiver downloadReceiver;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setWhiteStatusBar();
        ButterKnife.bind(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        btnLogin.setTypeface(typeface);

        context = this;
        loginLayout.setVisibility(View.VISIBLE);
        ca_title.setVisibility(View.GONE);
        requestPermissions();
        btnLogin.setOnClickListener(onLoginRectsListener);

    }

    private void checkAutoFetchPhoneNumber(){
        String orginPhoneNumber = BSmartUtil.getPhoneNum(getApplicationContext());
        String devicePhoneNumber = BSmartUtil.getAvailableAfricaMobileNo(orginPhoneNumber);
        if (TextUtils.isEmpty(devicePhoneNumber)){
            noDevice = true;
            passwordAgainZone.setVisibility(View.VISIBLE);
        }else{
            noDevice = false;
            passwordAgainZone.setVisibility(View.GONE);
            etPhoneNumber2.setText(devicePhoneNumber);
        }
    }

    private void openMainActivity() {
        Intent i = new Intent(this, HAActivity.class);
        startActivity(i);
    }

    public void requestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            checkAutoFetchPhoneNumber();
                        } else {
                            ToastUtils.showShort("Please allow the permission");
                        }
                    });
        }else{
            new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            checkAutoFetchPhoneNumber();
                        } else {
                            ToastUtils.showShort("Please allow the permission");
                        }
                    });
        }
    }

    private View.OnClickListener onLoginRectsListener = view -> {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            performLoginRects(view);
                        } else {
                            ToastUtils.showShort("Please allow the permission");
                        }
                    });
        }else{
            new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            performLoginRects(view);
                        } else {
                            ToastUtils.showShort("Please allow the permission");
                        }
                    });
        }
    };

    private void firstLocation(){
        Location mLocation = LocationUtils.getInstance(this.getApplication()).showLocation();
        Log.d("mLocation", ""+mLocation);
        if (null == mLocation){
            LocationUtils.getInstance(this.getApplication()).refreshLocation();
            App.resetLocation();
        }
    }

    private void performLoginRects(View view) {


        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            ToastUtils.showShort("Username must not be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("Password must not be empty");
            return;
        }

        if (TextUtils.isEmpty(etPhoneNumber.getText())) {
            ToastUtils.showShort("Phone Number must not be empty");
            return;
        } else if (etPhoneNumber.getText().length() < 9) {
            ToastUtils.showShort("Phone number must be at least 9 digits long");
            return;
        }

        if(!etPhoneNumber.getText().toString().equals(etPhoneNumber2.getText().toString())){
            if(noDevice){
                ToastUtils.showShort("Phone numbers are inconsistent");
            }else{
                ToastUtils.showShort("Phone number not match device phone number");
            }
            return;
        }
        //开始计算
        if (view!=null){
            view.setEnabled(false);
        }
        String country_code = BSmartUtil.getCodeByCountry(countryCode.getSelectedItem().toString());

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Entry,Please wait...");
        progressDialog.show();

        firstLocation();

        String type = "HA";

        ProfileUtils.saveLoginCountryCode(etUsername.getText().toString());
        ProfileUtils.saveLoginClinicCode(etPassword.getText().toString());
        ProfileUtils.saveCountryCode(country_code);
        ProfileUtils.saveMobilePhone(etPhoneNumber2.getText().toString());
        openMainActivity();

    }

}
