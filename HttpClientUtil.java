public class HttpClientUtil {

//apiURL就是访问第三方的URL
    private static String apiURL = "https://a1.easemob.com/{org_name}/{app_name}/token";
//创建应用的时候会对应生成
    private static String client_id = "";

    private static String client_secret = "";

    public static void main(String[] args) throws Exception {
        System.out.println("=========1获取token=========");
        String accessToken = getToken(apiURL, client_id, client_secret);// 获取token
        if (accessToken != null)
        System.out.println(accessToken);

    }

    public static String getToken(String url, String appid, String secret)
            throws Exception {
        String resultStr = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpPost post = new HttpPost(url);
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
        // 接收参数json列表
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("grant_type", "client_credentials");
        jsonParam.put("client_id", client_id);
        jsonParam.put("client_secret", client_secret);
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        post.setEntity(entity);

        // 请求结束，返回结果
        try {
            HttpResponse res = httpClient.execute(post);
            // 如果服务器成功地返回响应
            String responseContent = null; // 响应内容
            HttpEntity httpEntity = res.getEntity();
            responseContent = EntityUtils.toString(httpEntity, "UTF-8");
            JsonObject json = jsonparer.parse(responseContent)
                    .getAsJsonObject();
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (json.get("errcode") != null) 
                    //resultStr = json.get("errcode").getAsString();
                } else {// 正常情况下
                    resultStr = json.get("access_token").getAsString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接 ,释放资源
            httpClient.getConnectionManager().shutdown();
            return resultStr;
        }
    }

}