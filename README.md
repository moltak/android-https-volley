android https using volley
====================

self-signed server certificate를 사용하는 서버에 안드로이드에서 https 요청을 보낼 때 필요한 module입니다.


이 글을 읽어야 하는 이유
---------
안드로이드에서 https를 이용해서 통신을 하기 위해선 http 요청을 보낼때와 같은 방법으로 하시면 됩니다.
```java
URL url = new URL("https://wikipedia.org");
URLConnection urlConnection = url.openConnection();
InputStreamin = urlConnection.getInputStream();
copyInputStreamToOutputStream(in,System.out);
```

하지만 Self-signed server certificate 를 사용하는 서버에서는 아래와 같은 문제가 발생합니다.
```java
javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.        
        at org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl.startHandshake(OpenSSLSocketImpl.java:374)        
        at libcore.net.http.HttpConnection.setupSecureSocket(HttpConnection.java:209)        
        at libcore.net.http.HttpsURLConnectionImpl$HttpsEngine.makeSslConnection(HttpsURLConnectionImpl.java:478)      
        at libcore.net.http.HttpsURLConnectionImpl$HttpsEngine.connect(HttpsURLConnectionImpl.java:433)        
        at libcore.net.http.HttpEngine.sendSocketRequest(HttpEngine.java:290)        
        at libcore.net.http.HttpEngine.sendRequest(HttpEngine.java:240)        
        at libcore.net.http.HttpURLConnectionImpl.getResponse(HttpURLConnectionImpl.java:282)        
        at libcore.net.http.HttpURLConnectionImpl.getInputStream(HttpURLConnectionImpl.java:177)        
        at libcore.net.http.HttpsURLConnectionImpl.getInputStream(HttpsURLConnectionImpl.java:271)
```

이런 문제가 발생하는 이유는 크게 3가지가 있습니다.
- 인증서의 CA를 모를 때
- 서버 인증서가 CA로 부터 인증이 되지 않았고 self signed 인증일때
- 서버 설정에서 intermediate CA가 빠져 있을때


GOAL
----------
####이 글에서는 서버 인증서의 CA를 모를 때와 self signed 인증서일때 서버와 통신하는 방법을 담고 있습니다.


작업순서
----------
- 안드로이드에서 사용가능한 개인 인증서 준비
- SslHttpClient.java에 인증서에 대한 내용으로 변경
- volley request queue를 생성


준비물
---------
- https://tp.bolyartech.com:44400/https_test.html 의 인증서 갖고 오기
- 서버의 공개키로 만들어진 bouncy castle format의 개인 인증서 certification.cer 생성하기



#####개인키를 안드로이드에서 사용가능한 bouncy castle format으로 변경하기
```perl
keytool -import -alias YOUR_ALIAS -file tp.bolyartech.com.cer -keypass YOUR_PASS -keystore tp.bolyartech.com.bks -storetype BKS -storepass YOUR_PASS -providerClass org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath ./bcprov-jdk16-1.46.jar
```



SslHttpClient.java
---------
```java
String defaultType = KeyStore.getDefaultType();
KeyStore trustedStore = KeyStore.getInstance(defaultType);
// your certification key file
InputStream certificateStream = context.getResources().openRawResource(R.raw.certification);
// insert your password at below
trustedStore.load(certificateStream, "YOUR_PASS".toCharArray());
certificateStream.close();
```

how to use
---------
```java
VolleyListener listener = new VolleyListener();
RequestQueue queue = Volley.newRequestQueue(this, new SslHttpStack(new SslHttpClient(getBaseContext(), 44400)));
StringRequest request = new StringRequest(
        Request.Method.GET,
        REQUEST_URL,
        listener,
        listener);
queue.add(request)
```


License
=======

    Copyright 2014 Jung Kyungho

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
