package ru.romanov.telerandomizer.csvwork;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

@Component
public class HttpsDownloader {

    private X509TrustManager fakeTrustManager = getFakeTrustManager();

    @SneakyThrows
    public ByteArrayInputStream loadByteArrayInputStreamByURL(String URL) {
        TrustManager[] trustAllCerts = new TrustManager[]{fakeTrustManager};
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        URL url = new URL(URL);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private X509TrustManager getFakeTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }
}