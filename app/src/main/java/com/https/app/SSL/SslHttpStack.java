package com.https.app.SSL;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by moltak on 3/17/14.
 */
public class SslHttpStack implements HttpStack {
    private HttpClient httpClient;

    public SslHttpStack(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        HttpUriRequest uriRequest = createHttpRequest(request, additionalHeaders);
        addHeaders(uriRequest, additionalHeaders);
        addHeaders(uriRequest, request.getHeaders());
        HttpResponse response = httpClient.execute(uriRequest);
        return response;
    }

    private HttpUriRequest createHttpRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws AuthFailureError, UnsupportedEncodingException {
        switch (request.getMethod()) {
            case Request.Method.GET:
                return new HttpGet(request.getUrl());
            case Request.Method.POST:
                HttpPost post = new HttpPost(request.getUrl());
                post.setEntity(new UrlEncodedFormEntity(getParams(request), HTTP.UTF_8));
                return post;
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }

    private List<NameValuePair> getParams(Request<?> request) throws AuthFailureError {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        if(request != null && request instanceof PostRequest) {
            Map<String, String> map = ((PostRequest) request).getParams();
            Iterator iterator = map.keySet().iterator();
            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                params.add(new BasicNameValuePair(key, map.get(key)));
            }
        }

        return params;
    }

    private void addHeaders(HttpUriRequest uriRequest, Map<String, String> headers) {
        Iterator iterator = headers.keySet().iterator();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            uriRequest.setHeader(key, headers.get(key));
        }
    }
}
