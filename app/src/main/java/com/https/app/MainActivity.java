package com.https.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.https.app.SSL.SslHttpClient;
import com.https.app.SSL.SslHttpStack;

public class MainActivity extends Activity {

    private final String DEBUG_TAG = "SSL_TEST_TAG";
    private final String REQUEST_URL = "https://tp.bolyartech.com:44400/https_test.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClicked(View view) {
        createHttpRequestUsedToVolley();
    }

    private void createHttpRequestUsedToVolley() {
        VolleyListener listener = new VolleyListener();
        RequestQueue queue = Volley.newRequestQueue(this, new SslHttpStack(new SslHttpClient(getBaseContext(), 44400)));
        StringRequest request = new StringRequest(
                Request.Method.GET,
                REQUEST_URL,
                listener,
                listener);
        queue.add(request);
    }

    public class VolleyListener implements Response.Listener<String>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            ((TextView)findViewById(R.id.textview)).setText(error.getMessage());
            Log.d(DEBUG_TAG, error.getMessage());
        }

        @Override
        public void onResponse(String response) {
            ((TextView)findViewById(R.id.textview)).setText(response);
            Log.d(DEBUG_TAG, response);
        }
    }
}
