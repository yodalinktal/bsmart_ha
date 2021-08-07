package bsmart.technology.rru.base.api;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * For HA API URL
 * HOST: https://app.recdts.eac.int
 */
public interface AppRECDTS_HAHVDVService {

    @FormUrlEncoded
    @POST("mco/da/health/update")
    Observable<Response<JsonObject>> updateHealth(@FieldMap Map<String, Object> meta);


}
