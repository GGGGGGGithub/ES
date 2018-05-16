package com.taozhiye.controller;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taozhiye.entity.User;
import com.taozhiye.service.UserService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RestfulAction {  
    @Autowired
    private UserService userService;
    // 修改
    @RequestMapping(value = "put/{param}", method = RequestMethod.PUT)
    public @ResponseBody String put(@PathVariable String param) {
        return "put:" + param;
    }
    // 新增
    @RequestMapping(value = "post/{param}", method = RequestMethod.POST)
    public @ResponseBody User post(@PathVariable String param,String id,String name) {
        User u = new User();
        System.out.println(id);
        System.out.println(name);
        u.setName(id);
        u.setPassword(name);
        u.setEmail(id);
        u.setUsername(name);
        return u;
    }    
    // 删除
    @RequestMapping(value = "delete/{param}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable String param) {
        return "delete:" + param;
    }
    // 查找
    @RequestMapping(value = "get/{param}", method = RequestMethod.GET)
    public @ResponseBody User get(@PathVariable String param) {
        User u = new User();
        u.setName(param);
        u.setPassword(param);
        u.setEmail(param);
        u.setUsername("爱爱啊");
        return u;
    }
    @RequestMapping(value = "dealCon2/{param}")
    public @ResponseBody User dealCon2(@PathVariable String param) {
        User user = null;
        try {
            HttpClient client = HttpClients.createDefault();
            if("get".equals(param)){
                HttpGet request = new HttpGet("http://localhost:8080/tao-manager-web/get/"
                        +"啊啊啊");
                request.setHeader("Accept", "application/json");
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                ObjectMapper mapper = new ObjectMapper();
                user = mapper.readValue(entity.getContent(), User.class);
            }else if("post".equals(param)){
                HttpPost request2 = new HttpPost("http://localhost:8080/tao-manager-web/post/xxx");
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
                nvps.add(new BasicNameValuePair("id", "啊啊啊"));  
                nvps.add(new BasicNameValuePair("name", "secret"));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, "GBK");
                request2.setEntity(formEntity);
                HttpResponse response2 = client.execute(request2);
                HttpEntity entity = response2.getEntity();
                ObjectMapper mapper = new ObjectMapper();
                user = mapper.readValue(entity.getContent(), User.class);
            }else if("delete".equals(param)){

            }else if("put".equals(param)){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    } 
}