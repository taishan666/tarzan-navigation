package com.tarzan.navigation.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author tarzan
 */
public class HttpsUrlValidator {

    static HostnameVerifier hv = (urlHostName, session) -> {
        System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                + session.getPeerHost());
        return true;
    };

    public static String retrieveResponseFromServer(final String url) {
        HttpURLConnection connection = null;

        try {
            URL validationUrl = new URL(url);
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            connection = (HttpURLConnection) validationUrl.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            String line;
            final StringBuffer stringBuffer = new StringBuffer(255);

            synchronized (stringBuffer) {
                while ((line = in.readLine()) != null) {
                    stringBuffer.append(line);
                    stringBuffer.append("\n");
                }
                return stringBuffer.toString();
            }

        } catch (final IOException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (final Exception e1){
            System.out.println(e1.getMessage());
            return null;
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new Mitm();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class Mitm implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
        }
    }

}