package com.example.pushnotificationapp;

import android.util.Log;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * This class is used to send HTTP Post request to the server
 */

public class NetworkService {

  public void RegisterDevice(String deviceToken) throws IOException {
      OkHttpClient client = new OkHttpClient();

      MediaType mediaType = MediaType.parse("application/json");
      RequestBody body = RequestBody.create(mediaType, "\r\n{\r\n  \"DeviceToken\":" + "\"" + deviceToken + "\"" + "\r\n}");
      Request request = new Request.Builder()
              .url("http://192.168.1.34:5566/api/Device/RegisterDevice")
              .post(body)
              .build();

      try {
          Response response = client.newCall(request).execute();
      }catch (Exception e){
          Log.d(TAG, "RegisterDevice: " + e.getMessage());
      }
  }
}
