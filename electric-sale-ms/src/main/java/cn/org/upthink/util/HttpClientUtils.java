package cn.org.upthink.util;

import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjh on 2017/11/1.
 */
public enum HttpClientUtils {
    INSTANCE;

    public final static String XML = "xml";
    public final static String JSON = "json";

    /**
     * get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String sendGet(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responseBody = null;
        try {
            HttpGet httpget = new HttpGet(url);
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            responseBody = httpclient.execute(httpget, responseHandler);
        } finally {
            httpclient.close();
        }
        return responseBody;
    }

    /**
     * post请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String sendPost(String url, String data, String type) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> parameters = new ArrayList<>();
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        if(type.equals(XML)){
            httpPost.addHeader("Content-Type", "text/xml");
        }else{
            httpPost.addHeader("Content-Type", "application/json");
        }
        httpPost.setEntity(postEntity);
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        String responseBody = null;

        try {
            HttpEntity entity = response2.getEntity();
            responseBody = EntityUtils.toString(entity, "UTF-8");
        } finally {
            response2.close();
        }
        return responseBody;
    }

}
