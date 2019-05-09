package com.pandawadev.smsapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpException;

import java.io.IOException;

public class MainActivity extends Activity {
    private SmsServerGateway server;
    public MainActivity(){
        //membuat server
        server = new SmsServerGateway(8989);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //menjalankan server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    server.start();
                }catch (IOException e){
                    Log.e(MainActivity.class.getName(), e.getMessage(),e);
                }catch (HttpException e){
                    Log.e(MainActivity.class.getName(), e.getMessage(), e);
                }
            }
        }).start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //menghentikan server
        try{
            server.stop();
        }catch (IOException e){
            Log.e(MainActivity.class.getName(), e.getMessage(), e);
        }
    }
}
