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
    public @ResponseBody String post(@PathVariable String param,String id,String name) {
        System.out.println("id:"+id);
        System.out.println("name:"+name);
        return "post:" + param;
    }
    // 删除
    @RequestMapping(value = "delete/{param}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable String param) {
        return "delete:" + param;
    }
    // 查找
    @RequestMapping(value = "get/{param}", method = RequestMethod.GET)
    public @ResponseBody String get(@PathVariable String param) {
        return "get:" + param;
    }
    // HttpURLConnection 方式调用Restful接口
    // 调用接口
    @RequestMapping(value = "dealCon/{param}")
    public @ResponseBody String dealCon(@PathVariable String param) {
        try {
            String url = "http://localhost:8080/tao-manager-web/";
            url+=(param+"/xxx");
            URL restServiceURL = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL
                    .openConnection();
            //param 输入小写，转换成 GET POST DELETE PUT 
            httpConnection.setRequestMethod(param.toUpperCase());
//            httpConnection.setRequestProperty("Accept", "application/json");
            if("post".equals(param)){
                //打开输出开关
                httpConnection.setDoOutput(true);
//                httpConnection.setDoInput(true);

                //传递参数
                String input = "&id="+ URLEncoder.encode("abc", "UTF-8");
                input+="&name="+ URLEncoder.encode("啊啊啊", "UTF-8");
                OutputStream outputStream = httpConnection.getOutputStream();
                outputStream.write(input.getBytes());
                outputStream.flush();
            }
            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException(
                        "HTTP GET Request Failed with Error code : "
                                + httpConnection.getResponseCode());
            }
            BufferedReader responseBuffer = new BufferedReader(
                    new InputStreamReader((httpConnection.getInputStream())));
            String output;
            System.out.println("Output from Server:  \n");
            while ((output = responseBuffer.readLine()) != null) {
                System.out.println(output);
            }
            httpConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}