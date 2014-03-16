package com.https.app.SSL;

import android.content.Context;

import com.https.app.R;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class SslHttpClient extends DefaultHttpClient {
    private Context context;
    private int httpsPort;

    public SslHttpClient(Context context, int httpsPort) {
        this.context = context;
        this.httpsPort = httpsPort;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        try {
            String defaultType = KeyStore.getDefaultType();
            KeyStore trustedStore = KeyStore.getInstance(defaultType);
            // your certification key file
            InputStream certificateStream = context.getResources().openRawResource(R.raw.certification);
            // insert your password at below
            trustedStore.load(certificateStream, "YOUR_PASS".toCharArray());
            certificateStream.close();

            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(trustedStore);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // You have to set port number for https and http
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sslSocketFactory, httpsPort));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return new SingleClientConnManager(getParams(), registry);
    }
}

