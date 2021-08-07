package bsmart.technology.rru.base.api;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RECDTSApi {

    private RECDTSApi() {
    }
    private static OkHttpClient okHttpClient;
    private static AppHAHVDVService appHAHVDVService;
    private static AppRECDTS_HAHVDVService appRECDTS_hahvdvService;

    static {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                .build();

        appHAHVDVService =  new Retrofit.Builder()
                .baseUrl(base_ha_hv_dv_Url())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(AppHAHVDVService.class);

        appRECDTS_hahvdvService =  new Retrofit.Builder()
                .baseUrl(base_recdts_ha_hv_dv_Url())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(AppRECDTS_HAHVDVService.class);

    }

    public static AppHAHVDVService getAppHAHVDVService () { return  appHAHVDVService ;}

    public static AppRECDTS_HAHVDVService getAppRECDTS_hahvdvService() {
        return appRECDTS_hahvdvService;
    }

    public static String base_ha_hv_dv_Url() {
        return "https://taapp.eacpass.eac.int/api/";
    }
    public static String base_recdts_ha_hv_dv_Url() {
        return "https://app.recdts.eac.int/api/";
    }
}
