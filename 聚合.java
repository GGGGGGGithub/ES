 public void aggsearch() {  
        init();  
        SearchResponse response = null;  
  
        SearchRequestBuilder responsebuilder = client.prepareSearch("iktest")  
                .setTypes("iktest").setFrom(0).setSize(250);  
        AggregationBuilder aggregation = AggregationBuilders  
                .terms("agg")  
                .field("category_id")  
                .subAggregation(  
                        AggregationBuilders.topHits("top").setFrom(0)  
                                .setSize(10)).size(100);  
        response = responsebuilder.setQuery(QueryBuilders.boolQuery()  
  
        .must(QueryBuilders.matchPhraseQuery("name", "中学历史")))  
                .addSort("category_id", SortOrder.ASC)  
                .addAggregation(aggregation)// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)  
                .setExplain(true).execute().actionGet();  
  
        SearchHits hits = response.getHits();  
  
        Terms agg = response.getAggregations().get("agg");  
        System.out.println(agg.getBuckets().size());  
        for (Terms.Bucket entry : agg.getBuckets()) {  
            String key = (String) entry.getKey(); // bucket key  
            long docCount = entry.getDocCount(); // Doc count  
            System.out.println("key " + key + " doc_count " + docCount);  
  
            // We ask for top_hits for each bucket  
            TopHits topHits = entry.getAggregations().get("top");  
            for (SearchHit hit : topHits.getHits().getHits()) {  
                System.out.println(" -> id " + hit.getId() + " _source [{}]"  
                        + hit.getSource().get("category_name"));  
                ;  
            }  
        }  
        System.out.println(hits.getTotalHits());  
        int temp = 0;  
        for (int i = 0; i < hits.getHits().length; i++) {  
            // System.out.println(hits.getHits()[i].getSourceAsString());  
            System.out.print(hits.getHits()[i].getSource().get("product_id"));  
            // if(orderfield!=null&&(!orderfield.isEmpty()))  
            // System.out.print("\t"+hits.getHits()[i].getSource().get(orderfield));  
            System.out.print("\t"  
                    + hits.getHits()[i].getSource().get("category_id"));  
            System.out.print("\t"  
                    + hits.getHits()[i].getSource().get("category_name"));  
            System.out.println("\t"  
                    + hits.getHits()[i].getSource().get("name"));  
        }  
    }  
}  