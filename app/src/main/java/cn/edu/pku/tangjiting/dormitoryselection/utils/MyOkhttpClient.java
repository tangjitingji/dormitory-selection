package cn.edu.pku.tangjiting.dormitoryselection.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOkhttpClient {

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public static Response get(String url) throws IOException {
        try {
            OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
            mBuilder.sslSocketFactory(createSSLSocketFactory());
            mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
            OkHttpClient client = mBuilder.build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response;
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Response post(String url, RequestBody body) throws IOException {
        try {
            OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
            mBuilder.sslSocketFactory(createSSLSocketFactory());
            mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
            OkHttpClient client = mBuilder.build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response;
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
