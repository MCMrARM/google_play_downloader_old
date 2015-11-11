package com.github.mcmrarm.playdl;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import com.github.mcmrarm.playdl.proto.GooglePlay;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class App {

    LoginManager login;
    String pkgName;
    public GooglePlay.DetailsResponse resp;
    public int offerType;
    public boolean checkoutRequired;
    public int versionCode;

    App(LoginManager login, String pkgName) throws IOException {
        this.login = login;
        this.pkgName = pkgName;
        ArrayList<NameValuePair> opt = new ArrayList<NameValuePair>();
        opt.add(new BasicNameValuePair("doc", pkgName));
        GooglePlay.ResponseWrapper responseWrapper = execRequest(login, "https://android.clients.google.com/fdfe/details", opt, null, false);

        resp = responseWrapper.getPayload().getDetailsResponse();
        versionCode = resp.getDocV2().getDetails().getAppDetails().getVersionCode();
        if (!resp.getDocV2().getDetails().getAppDetails().hasVersionCode())
            versionCode = 1;

        System.out.println("Version code: " + versionCode);
        offerType = resp.getDocV2().getOffer(0).getOfferType();
        checkoutRequired = resp.getDocV2().getOffer(0).getCheckoutFlowRequired();
        System.out.println("Offer type: " + offerType);
        System.out.println("Checkout required: " + checkoutRequired);
    }

    public static GooglePlay.ResponseWrapper execRequest(LoginManager login, String url, ArrayList<NameValuePair> data, byte[] protobufData, boolean post) throws IOException {
        ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("Accept-Language", DeviceInfo.lang));
        args.add(new BasicNameValuePair("Authorization", "GoogleLogin auth=" + login.getAuth()));
        args.add(new BasicNameValuePair("X-DFE-Enabled-Experiments", "cl:billing.select_add_instrument_by_default"));
        args.add(new BasicNameValuePair("X-DFE-Unsupported-Experiments", "nocache:billing.use_charging_poller,market_emails,buyer_currency,prod_baseline,checkin.set_asset_paid_app_field,shekel_test,content_ratings,buyer_currency_in_app,nocache:encrypted_apk,recent_changes"));
        args.add(new BasicNameValuePair("X-DFE-Device-Id", DeviceInfo.androidId));
        args.add(new BasicNameValuePair("X-DFE-Client-Id", "am-android-google"));
        args.add(new BasicNameValuePair("User-Agent", "Android-Finsky/3.10.14 (api=3,versionCode=8016014,sdk="+DeviceInfo.SDK_VERSION+",device="+DeviceInfo.DEVICE+",hardware="+DeviceInfo.PRODUCT+",product="+DeviceInfo.PRODUCT+")"));
        args.add(new BasicNameValuePair("X-DFE-SmallestScreenWidthDp", "320"));
        args.add(new BasicNameValuePair("X-DFE-Filter-Level", "3"));
        args.add(new BasicNameValuePair("Host", "android.clients.google.com"));
        args.add(new BasicNameValuePair("Content-Type", (protobufData == null ? "application/x-www-form-urlencoded; charset=UTF-8" : "application/x-protobuf")));

        HttpResponse response;
        if (post) {
            HttpPost request = new HttpPost(url);
            for (NameValuePair p : args) {
                request.addHeader(p.getName(), p.getValue());
            }
            if (protobufData != null) {
                request.setEntity(new ByteArrayEntity(protobufData));
            } else {
                request.setEntity(new UrlEncodedFormEntity(data, Consts.UTF_8));
            }

            response = new DefaultHttpClient().execute(request);
        } else {
            HttpGet request = new HttpGet(url + "?" + URLEncodedUtils.format(data, "UTF-8"));
            for (NameValuePair p : args) {
                request.addHeader(p.getName(), p.getValue());
            }

            response = new DefaultHttpClient().execute(request);
        }
        HttpEntity entity = response.getEntity();
        if (entity == null)
            throw new IOException(response.getStatusLine().toString());
        return GooglePlay.ResponseWrapper.parseFrom(entity.getContent());
    }

    GooglePlay.AndroidAppDeliveryData deliver() throws IOException {
        ArrayList<NameValuePair> opt = new ArrayList<NameValuePair>();
        opt.add(new BasicNameValuePair("ot", String.valueOf(offerType)));
        opt.add(new BasicNameValuePair("doc", pkgName));
        opt.add(new BasicNameValuePair("vc", String.valueOf(versionCode)));
        GooglePlay.ResponseWrapper responseWrapper = execRequest(login, checkoutRequired ? "https://android.clients.google.com/fdfe/delivery" : "https://android.clients.google.com/fdfe/purchase", opt, null, checkoutRequired ? false : true);
        GooglePlay.AndroidAppDeliveryData resp = (checkoutRequired ? responseWrapper.getPayload().getDeliveryResponse().getData() : responseWrapper.getPayload().getBuyResponse().getPurchaseStatusResponse().getAppDeliveryData());

        return resp;
    }

    void download(GooglePlay.AndroidAppDeliveryData data, String outFile) throws IOException {
        StringBuilder cookies = new StringBuilder();
        for (GooglePlay.HttpCookie cookie : data.getDownloadAuthCookieList()) {
            if (cookies.length() > 0)
                cookies.append("; ");
            cookies.append(cookie.getName());
            cookies.append("=");
            cookies.append(cookie.getValue());
        }

        HttpGet request = new HttpGet(data.getDownloadUrl());
        request.setHeader("Cookie", cookies.toString());
        request.setHeader("User-Agent", "AndroidDownloadManager/" + DeviceInfo.VERSION + " (Linux; U; Android " + DeviceInfo.VERSION + "; " + DeviceInfo.MODEL + " Build/" + DeviceInfo.BUILD + ")");

        System.out.print("Downloading " + data.getDownloadUrl() + " to " + outFile);
        HttpResponse response = new DefaultHttpClient().execute(request);
        ReadableByteChannel inChannel = Channels.newChannel(response.getEntity().getContent());
        FileChannel outChannel = new FileOutputStream(outFile).getChannel();
        outChannel.transferFrom(inChannel, 0, Long.MAX_VALUE);
    }

}
