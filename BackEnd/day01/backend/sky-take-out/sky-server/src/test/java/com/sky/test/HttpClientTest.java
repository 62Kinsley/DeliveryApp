package com.sky.test;


import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HttpClientTest {


    @Test
//send a get request
    public void testGet() throws Exception{
        //create http object
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //create request object
        HttpGet httpGet = new HttpGet("http://localhost:80/user/shop/status");
        
        //send request object, and receive respond result
        CloseableHttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("server response code is:" + statusCode);
        
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println("server return data:" + body);

        response.close();
        httpClient.close();


    }


    //send a post request

    @Test
    public void testPost() throws Exception{
        //create httpclient object
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //cerate request object
        HttpPost httpPost = new HttpPost("http://localhost:80/admin/employee/login");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","admin");
        jsonObject.put("password","e10adc3949ba59abbe56e057f20f883e");

        StringEntity entity = new StringEntity(jsonObject.toString());

        entity.setContentEncoding("utt-8");

        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        //send request object
        CloseableHttpResponse response = httpClient.execute(httpPost);

        //analyse result
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("server response code is:" + statusCode);

        HttpEntity entity1 = response.getEntity();
        String body = EntityUtils.toString(entity1);
        System.out.println("server return data:" + body);

        //close
        response.close();
        httpClient.close();



    }
}
