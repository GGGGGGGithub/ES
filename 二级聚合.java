AggregationBuilder aggregation= AggregationBuilders.terms("customer").field("province.raw");
//新增客户
BoolQueryBuilder addCustomer=QueryBuilders.boolQuery();
addCustomer.must(QueryBuilders.rangeQuery("start_time")
.from(System.currentTimeMillis()-(10*1000 * 60 * 60 * 24))
.to(Instant.now().toEpochMilli()));
FilterAggregationBuilder addAggregationBuilder
= AggregationBuilders.filter("add",addCustomer).subAggregation(aggregation);
//新增签约
BoolQueryBuilder addSignBuilder=QueryBuilders.boolQuery();
//客户
idaddSignBuilder.must(QueryBuilders.termsQuery("id.raw","36v91l0mrP_76359904-5274-4a0a-a4ac-9cffb64ef079"));
//客户创建时间
addSignBuilder.must(QueryBuilders.rangeQuery("start_time")
.from(System.currentTimeMillis()-(10*1000 * 60 * 60 * 24)).to(Instant.now().toEpochMilli()));
FilterAggregationBuilder addSignAggBuilder
= AggregationBuilders.filter("add_sign",addSignBuilder).subAggregation(aggregation);
//签约客户
BoolQueryBuilder sign=QueryBuilders.boolQuery();
//客户
idsign.must(QueryBuilders.termsQuery("id.raw","36v91l0mrP_76359904-5274-4a0a-a4ac-9cffb64ef079"));
//FilterAggregationBuilder signAggBuilder= AggregationBuilders.filter("sign",sign).subAggregation(aggregation);
SearchRequestBuilder searchRequestBuilder=EsClient.client.filterWithHeader(headers)
.prepareSearch("customer_36v91l0mrp*").setTypes("customer").setSize(5).setQuery(boolQueryBuilder)
.setFetchSource(new String[]{"customer_id"},null).addAggregation(addSignAggBuilder).addAggregation(addAggregationBuilder)
.addAggregation(signAggBuilder);
LOGGER.info("查询条件:"+searchRequestBuilder.toString());
SearchResponse searchResponse=searchRequestBuilder.execute().actionGet(); 
-----------------------------------------------------------------------------------------------------------elasticsearch过滤聚合

SearchRequestBuilder search = client.prepareSearch("index").setTypes("type");
 
TermsBuilder one=  AggregationBuilders.terms("group_name").field("name");
TermsBuilder two=  AggregationBuilders.terms("group_age").field("age");
one.subAggregation(two)
search.addAggregation(one);
 
 
	Terms terms= search.get().getAggregations().get("group_name");
		for(Terms.Bucket name_buk:terms.getBuckets()){
			//一级分组的内容
			Terms terms_age= name_buk.getAggregations().get("group_age");
			for(Terms.Bucket age_buk:terms_age.getBuckets()){
				//二级分组的内容	
				System.out.println(name_buk.getKey()+"  "+age_buk.getKey()+"  "+age_buk.getDocCount());
 
			}
}