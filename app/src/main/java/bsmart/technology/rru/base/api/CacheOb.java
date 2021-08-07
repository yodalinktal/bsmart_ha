package bsmart.technology.rru.base.api;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class CacheOb<T> {

  private static final String TAG = "CacheOb";

  private CacheOb(Observable<T> ob) {
    this.mOrigin = ob;
  }

  private Observable<T> mOrigin;

  private Observable<T> mRefresh;

  private boolean gotCache = true;

  public static <T> CacheOb<T> create(Observable<T> cacheOb) {
    CacheOb<T> cOb = new CacheOb<>(cacheOb);
    return cOb;
  }

  public CacheOb<T> refreshWith(Observable<T> refreshOb) {
    this.mRefresh = refreshOb;
    return this;
  }

  public <R> CacheOb<R> compose(Observable.Transformer<? super T, R> transformer) {
    return new CacheOb<>(mOrigin.compose(transformer)).refreshWith(mRefresh.compose(transformer));
  }

  public Subscription subscribe(Subscriber<T> subscriber) {
    return mOrigin.onErrorResumeNext(throwable -> {
      Log.d(TAG, "Cache Error : " + throwable.getMessage());
      gotCache = false;
      return Observable.empty();
    }).concatWith(mRefresh.onErrorResumeNext(throwable -> {
      Log.d(TAG, "Net Error : " + throwable.getMessage());
      if (gotCache) {
        return Observable.empty();
      } else {
        return Observable.error(throwable);
      }
    })).subscribe(subscriber);
  }
}
