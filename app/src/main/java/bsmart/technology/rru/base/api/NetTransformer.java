package bsmart.technology.rru.base.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;

import bsmart.technology.rru.base.App;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class NetTransformer<T> implements Observable.Transformer<Response<JsonObject>, T> {

    private Class<T> mClazz;

    private Type mType;

    private String mContentKey;

    private boolean mNoContentKey = false;

    public NetTransformer() {
        this.mNoContentKey = true;
    }

    public NetTransformer(Class<T> mClazz) {
        this.mNoContentKey = true;
        this.mClazz = mClazz;
    }

    public NetTransformer(String contentKey, Class<T> mClazz) {
        this.mContentKey = contentKey;
        this.mClazz = mClazz;
    }

    public NetTransformer(String contentKey, Type type) {
        this.mContentKey = contentKey;
        this.mType = type;
    }

    @Override
    public Observable<T> call(Observable<Response<JsonObject>> responseObservable) {


        return responseObservable
                .flatMap(new Func1<Response<JsonObject>, Observable<T>>() {
                    @Override
                    public Observable<T> call(Response<JsonObject> jsonObjectResponse) {
                        if (jsonObjectResponse.code() >= 400) {
                            try {
                                String errorBody = jsonObjectResponse.errorBody().string();
                                JsonObject bodyObj = new JsonParser().parse(errorBody).getAsJsonObject();

                                if (bodyObj.has("error")) {
                                    String errMsg = "";
                                    if (bodyObj.get("error").isJsonObject()) {
                                        JsonObject errorObj = bodyObj.get("error").getAsJsonObject();
                                        // check the new app version.
                                        if (422 == jsonObjectResponse.code() && errorObj.has("app")) {
                                            JsonArray appInfo = errorObj.getAsJsonArray("app");
                                            UpgradeHttpException e = new UpgradeHttpException(
                                                    appInfo.get(0).getAsString(),
                                                    appInfo.get(1).getAsString(),
                                                    appInfo.get(2).getAsString()
                                            );
                                            return Observable.error(e);
                                        }
                                        for (String key : errorObj.keySet()) {
                                            JsonArray array = errorObj.get(key).getAsJsonArray();
                                            for (int i = 0; i < array.size(); i++) {
                                                String s = array.get(i).getAsString();
                                                errMsg = errMsg + s + "\n";
                                            }
                                        }
                                        HttpException httpException = new HttpException(errMsg, jsonObjectResponse.code());
                                        return Observable.error(httpException);

                                    } else {

                                        String msg = bodyObj != null ? bodyObj.get("error").getAsString() : "";
                                        return Observable.error(new HttpException(msg, jsonObjectResponse.code()));
                                    }
                                }

                                return Observable.empty();

                            } catch (IOException e) {
                                e.printStackTrace();
                                return Observable.error(new HttpException("Some error happened.", jsonObjectResponse.code()));
                            }
                        }

                        JsonObject bodyObj = jsonObjectResponse.body();
                        try {

                            if (mNoContentKey) {
                                return mClazz == null ? Observable.empty() : Observable.just(App.gson.fromJson(bodyObj, mClazz));
                            }
                            T bean = App.gson.fromJson(bodyObj.get(mContentKey), mClazz == null ? mType : mClazz);
                            if (bean == null) {
                                return Observable.error(new GsonException("can't getMessenger " + mClazz + " with key \"" + mContentKey + "\""));
                            } else {
                                return Observable.just(bean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Observable.error(new HttpException(bodyObj.get("error").getAsString(), jsonObjectResponse.code()));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}