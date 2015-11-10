package com.github.mcmrarm.playdl;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginManager {

    static final String SERVICE_GOOGLE_PLAY = "androidmarket";
    static final String APP_GOOGLE_PLAY = "com.android.vending";

    String service, app;
    String email;

    String auth;

    LoginManager(String service, String app) {
        this.service = service;
        this.app = app;
    }

    void setEmail(String email) {
        this.email = email;
    }
    String getEmail() {
        return email;
    }

    void login(String password, String androidId) throws IOException, LoginException {
        if (auth != null)
            return;

        HttpPost post = new HttpPost("https://android.clients.google.com/auth");
        post.setHeader("User-Agent", DeviceInfo.USER_AGENT);
        ArrayList<NameValuePair> args = new ArrayList<>();
        args.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
        args.add(new BasicNameValuePair("Email", email));
        args.add(new BasicNameValuePair("Passwd", password));
        args.add(new BasicNameValuePair("has_permission", "1"));
        args.add(new BasicNameValuePair("service", service));
        args.add(new BasicNameValuePair("source", "android"));
        if (androidId != null)
            args.add(new BasicNameValuePair("androidId", androidId));
        args.add(new BasicNameValuePair("app", app));
        args.add(new BasicNameValuePair("client_sig", "61ed377e85d386a8dfee6b864bd85b0bfaa5af81"));
        //args.add(new BasicNameValuePair("device_country", DeviceInfo.COUNTRY));
        //args.add(new BasicNameValuePair("operatorCountry", DeviceInfo.COUNTRY));
        args.add(new BasicNameValuePair("lang", DeviceInfo.lang));
        args.add(new BasicNameValuePair("sdk_version", String.valueOf(DeviceInfo.SDK_VERSION)));
        post.setEntity(new UrlEncodedFormEntity(args, Consts.UTF_8));

        HttpResponse response = new HttpClientGzip().execute(post);
        HttpEntity entity = response.getEntity();
        if (entity == null)
            throw new LoginException(response.getStatusLine().toString());

        BufferedReader body = new BufferedReader(new InputStreamReader(entity.getContent()));
        String line;
        while ((line = body.readLine()) != null) {
            if (line.startsWith("Auth=")) {
                auth = line.substring(5).trim();
                System.out.println("Auth token: " + auth);
            }
        }
    }

    String getAuth() {
        return auth;
    }

    void setAuth(String auth) {
        this.auth = auth;
    }

    static class LoginException extends Exception {
        LoginException(String err) {
            super(err);
        }
    }
}