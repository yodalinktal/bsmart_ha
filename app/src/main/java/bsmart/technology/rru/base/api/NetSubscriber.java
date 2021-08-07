package bsmart.technology.rru.base.api;

import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;
import java.net.UnknownHostException;

import bsmart.technology.rru.base.utils.ExceptionUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class NetSubscriber<T> extends Subscriber<T> {

  private static final String TAG = "NetSubscriber";

  private NetSubOnNext mSubOnNext;

  private NetSubOnError mSubOnError;

  private NetSubOnComplete mSubOnComplete;

  public interface NetSubOnComplete {
    void onComplete();
  }

  public interface NetSubOnNext<T> {
    void onNext(T bean);
  }

  public interface NetSubOnError {
    void onError(Throwable e);
  }

  public NetSubscriber() {
  }

  public NetSubscriber(NetSubOnComplete subOnComplete) {
    this.mSubOnComplete = subOnComplete;
  }

  public NetSubscriber(NetSubOnNext<T> subOnNext) {
    this.mSubOnNext = subOnNext;
  }

  public NetSubscriber(NetSubOnNext<T> subOnNext, NetSubOnError subOnError) {
    this.mSubOnNext = subOnNext;
    this.mSubOnError = subOnError;
  }

  public NetSubscriber(NetSubOnNext<T> subOnNext,
                       NetSubOnError subOnError,
                       NetSubOnComplete subOnComplete) {
    this.mSubOnNext = subOnNext;
    this.mSubOnError = subOnError;
    this.mSubOnComplete = subOnComplete;
  }

  @Override
  public final void onCompleted() {
    if (mSubOnComplete != null) mSubOnComplete.onComplete();
  }

  @Override
  public final void onError(Throwable e) {
    e.printStackTrace();
    String toastMsg = null;

    if (e instanceof ServerException) {
      ToastUtils.showShort(e.getMessage());
      toastMsg = e.getMessage();

    } else if (e instanceof HttpException) {

      HttpException httpException = (HttpException) e;
      toastMsg = httpException.getMessage();

      ExceptionUtils.getINSTANCE().catchError(toastMsg);

    } else if (e instanceof GsonException) {

    } else if (e instanceof UnknownHostException || e instanceof IOException) {
      toastMsg = "No Network connectivity.";
      ExceptionUtils.getINSTANCE().catchError(toastMsg);

    } else {
      toastMsg = "Can not connect to server.";
      ExceptionUtils.getINSTANCE().catchError(toastMsg);
    }

    if (toastMsg != null) {
      Observable.just(toastMsg)
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe(s -> {
            // ToastUtils.showShort(s.trim());
          });
    }

    if (mSubOnError != null) mSubOnError.onError(e);
  }

  @Override
  public final void onNext(T bean) {
    if (mSubOnNext != null) {
      try {
        mSubOnNext.onNext(bean);
      } catch (Exception e) {
        Log.d("NetSubscriber", "Error in subscriber");
        e.printStackTrace();
      }
    }
  }

}