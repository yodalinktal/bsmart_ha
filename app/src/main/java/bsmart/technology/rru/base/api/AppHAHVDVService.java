package bsmart.technology.rru.base.api;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * For HA API URL
 * HOST: https://taapp.eacpass.eac.int
 */
public interface AppHAHVDVService {

    @FormUrlEncoded
    @POST("mco/da/health/update")
    Observable<Response<JsonObject>> updateHealth(@FieldMap Map<String, Object> meta);

    @Headers({"Content-type:application/json; charset=UTF-8"})
    @POST("mco/v2/parse/qrCode")
    Observable<Response<JsonObject>> parseCode(@Body RequestBody body);

    @FormUrlEncoded
    @POST("mco/v2/scan/logger")
    Observable<Response<JsonObject>> scanLogger(@FieldMap Map<String, Object> meta);

}
