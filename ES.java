/*
**@brief:遍历Json，批量插入ES
**@param:空
**@return:空
*/
 private static void insertBulkIndex() throws Exception {
//Json文件的存储
final String JSONFILEINPUT = ESConfig.es_json_path;
logger.info("path = " + JSONFILEINPUT);
LinkedList<String> curJsonList = FileProcess.getJsonFilePath(JSONFILEINPUT);
logger.info("size = " + curJsonList.size());

for (int i = 0; i < curJsonList.size(); ++i){
//System.out.println(" i = " + i + " " + curJsonList.get(i));
String curJsonPath = curJsonList.get(i);
ImageInfo curImageInfo = JsonParse.GetImageJson(curJsonPath);
//JsonParse.printImageJson(curImageInfo);
if (curImageInfo == null){
continue;
}
//遍历插入操作
InsertIndex (curImageInfo);
}
}

/*
**@brief:单条Json插入ES（借助了Jest封装后的API）
**@param:空
**@return:空
*/
private static void InsertIndex(AgeInfo ageInfo) throws Exception {
JestClient jestClient = JestExa.getJestClient();
JsonParse.PrintImageJson( ageInfo );

Bulk bulk = new Bulk.Builder()
.defaultIndex("age_index")
.defaultType("age_type")
.addAction(Arrays.asList(
new Index.Builder( ageInfo ).build()
)).build();
  JestResult result = jestClient.execute(bulk);
  if (result.isSucceeded()){
  System.out.println("insert success!");
  }else{
  System.out.println("insert failed");
  }

}