android-https-volley
====================

##
android https using volley

#####
apache의 DefaultHttpClient와 Volley의 HttpStack을 이용해서 만든 모듈입니다.


how to use
'''java
VolleyListener listener = new VolleyListener();
RequestQueue queue = Volley.newRequestQueue(this, new SslHttpStack(new SslHttpClient(getBaseContext(), 44400)));
StringRequest request = new StringRequest(
        Request.Method.GET,
        REQUEST_URL,
        listener,
        listener);
queue.add(request)


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