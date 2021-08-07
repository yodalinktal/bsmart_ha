package bsmart.technology.rru.base.api;

import java.io.IOException;

public class HttpException extends IOException {

  private int mStatusCode;

  public HttpException(String message, int code) {
    super(message);
    this.mStatusCode = code;
  }

  public int statusCode() {
    return mStatusCode;
  }
}
