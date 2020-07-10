package com.example.pushnotificationapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private  Button btnRegisterDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegisterDevice = findViewById(R.id.btnRegisterDevice);

        /**
         * onCreate -> check if our app has the latest version of Google Play Services
         */
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        RegisterDeviceListener();

    }

    /**
     * onResume -> check if our app has the latest version of Google Play Services
     */
    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
    }

    /**
     * Our App is retrieving its unique token from Firebase and send it to the server
     */
    private void RegisterDeviceListener(){
        btnRegisterDevice.setOnClickListener(v -> FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "getInstanceId failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    try {
                        FirebaseInstanceId.getInstance().getToken(token, FirebaseMessaging.INSTANCE_ID_SCOPE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Register Device by passing its unique Token
                    RegisterDeviceAsyncTask registerTask = new RegisterDeviceAsyncTask();
                    registerTask.execute(token);

                    Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                }));
    }

    /**
     * AsyncTask for registering our device by sending POST request,containing our unique device token
     */
    private static class RegisterDeviceAsyncTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            NetworkService service = new NetworkService();
            try {
                service.RegisterDevice(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
