package com.example.campmoa.components;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class SmsComponent {
    private static final String ACCESS_KEY =
//    sns 접속 키 :: ncp 등록시 생성됨.
    private static final String SECRET_KEY = 
//     ncp 등록시 생성됨.
    private static final String SERVICE_ID = 
    //     ncp 등록시 생성됨.
    private static final String CALLER = 
    public int send(String to, String content)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        long timestamp = System.currentTimeMillis();
        String signature = String.format("POST /sms/v2/services/%s/messages\n%d\n%s",
                SmsComponent.SERVICE_ID,
                timestamp,
                SmsComponent.ACCESS_KEY
        );
        SecretKeySpec secretKeySpec =
                new SecretKeySpec(SmsComponent.SECRET_KEY.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(signature.getBytes(StandardCharsets.UTF_8));
        signature = Base64.encodeBase64String(rawHmac);

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("type", "SMS");
        bodyJson.put("contentType", "COMM");
        bodyJson.put("countryCode", "82");
        bodyJson.put("from", SmsComponent.CALLER);
        bodyJson.put("content", content);

        JSONArray messagesJson = new JSONArray();
        JSONObject messageJson = new JSONObject();
        messageJson.put("to", to);
        messagesJson.put(messageJson);
        bodyJson.put("messages", messagesJson);

        HttpURLConnection connection = (HttpURLConnection) new URL(String.format("https://sens.apigw.ntruss.com/sms/v2/services/%s/messages", SmsComponent.SERVICE_ID)).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("x-ncp-apigw-timestamp", Long.toString(timestamp));
        connection.setRequestProperty("x-ncp-iam-access-key", SmsComponent.ACCESS_KEY);
        connection.setRequestProperty("x-ncp-apigw-signature-v2", signature);
        connection.setRequestMethod("POST");


        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(bodyJson.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
        return connection.getResponseCode();
    }
}
