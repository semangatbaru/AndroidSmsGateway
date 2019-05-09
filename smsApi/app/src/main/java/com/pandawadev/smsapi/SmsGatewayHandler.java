package com.pandawadev.smsapi;

import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.*;
import org.apache.http.util.*;
import org.json.*;

import android.telephony.SmsManager;
import android.util.Log;
import java.io.IOException;

public class SmsGatewayHandler implements HttpRequestHandler {
    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (httpRequest instanceof HttpEntityEnclosingRequest){
            //HTTP request harus memiliki body
            try {
                HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();

                //convert String Body menjadi JSON
                String body  = EntityUtils.toString(entity);
                JSONObject object = new JSONObject(body);
                //ambil no tujuan dari json
                String no = object.getString("no");
                //ambil pesan dari json
                String pesan = object.getString("pesan");
                //kirim sms
                SmsManager.getDefault().sendTextMessage(no,null,pesan,null,null);
                //respon
                httpResponse.setEntity(new StringEntity("SUKSES"));
            }catch (Exception ex){
                httpResponse.setEntity(new StringEntity("Gagal"));
                Log.e(SmsGatewayHandler.class.getName(), ex.getMessage(), ex);
            }
        }
    }
}
