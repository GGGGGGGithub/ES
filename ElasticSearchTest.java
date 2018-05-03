package cn.itcast.elasticsearch.test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.junit.Test;

import cn.itcast.elasticsearch.domain.Article;

import com.fasterxml.jackson.databind.ObjectMapper;

// ElasticSearch ���Գ��� 
public class ElasticSearchTest {

	@Test
	// ֱ����ElasticSearch�н����ĵ����Զ���������
	public void demo1() throws IOException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));
		// ����json ����
		/*
		 * {id:xxx, title:xxx, content:xxx}
		 */
		XContentBuilder builder = XContentFactory
				.jsonBuilder()
				.startObject()
				.field("id", 1)
				.field("title", "ElasticSearch��һ������Lucene������������")
				.field("content",
						"���ṩ��һ���ֲ�ʽ���û�������ȫ���������棬����RESTful web�ӿڡ�Elasticsearch����Java�����ģ�����ΪApache��������µĿ���Դ�뷢�����ǵ�ǰ���е���ҵ���������档��������Ƽ����У��ܹ��ﵽʵʱ�������ȶ����ɿ������٣���װʹ�÷��㡣")
				.endObject();
		// �����ĵ�����
		client.prepareIndex("blog1", "article", "1").setSource(builder).get();

		// �ر�����
		client.close();
	}

	@Test
	// ������elasticSearch�д����ĵ�����
	public void demo2() throws IOException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));
		// ��������
		// get() === execute().actionGet()
		SearchResponse searchResponse = client.prepareSearch("blog1")
				.setTypes("article").setQuery(QueryBuilders.matchAllQuery())
				.get();
		printSearchResponse(searchResponse);

		// �ر�����
		client.close();
	}

	@Test
	// ���ֲ�ѯʹ��
	public void demo3() throws IOException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));
		// ��������
		// get() === execute().actionGet()
		// SearchResponse searchResponse = client.prepareSearch("blog1")
		// .setTypes("article")
		// .setQuery(QueryBuilders.queryStringQuery("ȫ��")).get();

		// SearchResponse searchResponse = client.prepareSearch("blog1")
		// .setTypes("article")
		// .setQuery(QueryBuilders.wildcardQuery("content", "*ȫ��*")).get();

		SearchResponse searchResponse = client.prepareSearch("blog2")
				.setTypes("article")
				.setQuery(QueryBuilders.termQuery("content", "����")).get();
		printSearchResponse(searchResponse);

		// �ر�����
		client.close();
	}

	private void printSearchResponse(SearchResponse searchResponse) {
		SearchHits hits = searchResponse.getHits(); // ��ȡ���д�������ѯ����ж��ٶ���
		System.out.println("��ѯ����У�" + hits.getTotalHits() + "��");
		Iterator<SearchHit> iterator = hits.iterator();
		while (iterator.hasNext()) {
			SearchHit searchHit = iterator.next(); // ÿ����ѯ����
			System.out.println(searchHit.getSourceAsString()); // ��ȡ�ַ�����ʽ��ӡ
			System.out.println("title:" + searchHit.getSource().get("title"));
		}
	}

	@Test
	// ��������
	public void demo4() throws IOException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));

		// ��������
		client.admin().indices().prepareCreate("blog2").get();

		// ɾ������
		client.admin().indices().prepareDelete("blog2").get();

		// �ر�����
		client.close();
	}

	@Test
	// ӳ�����
	public void demo5() throws IOException, InterruptedException,
			ExecutionException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));

		// ���ӳ��
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
				.startObject("article").startObject("properties")
				.startObject("id").field("type", "integer")
				.field("store", "yes").endObject().startObject("title")
				.field("type", "string").field("store", "yes")
				.field("analyzer", "ik").endObject().startObject("content")
				.field("type", "string").field("store", "yes")
				.field("analyzer", "ik").endObject().endObject().endObject()
				.endObject();

		PutMappingRequest mapping = Requests.putMappingRequest("blog2")
				.type("article").source(builder);
		client.admin().indices().putMapping(mapping).get();

		// �ر�����
		client.close();
	}

	@Test
	// �ĵ���ز���
	public void demo6() throws IOException, InterruptedException,
			ExecutionException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));
		// ����json ����
		/*
		 * {id:xxx, title:xxx, content:xxx}
		 */
		Article article = new Article();
		article.setId(2);
		article.setTitle("����������ʵ�ܿ���");
		article.setContent("����ϣ�����ǵ������������Ҫ�죬����ϣ����һ�������ú�һ����ȫ��ѵ�����ģʽ������ϣ���ܹ��򵥵�ʹ��JSONͨ��HTTP���������ݣ�����ϣ�����ǵ�����������ʼ�տ��ã�����ϣ���ܹ�һ̨��ʼ����չ�����٣�����Ҫʵʱ����������Ҫ�򵥵Ķ��⻧������ϣ������һ���ƵĽ��������Elasticsearchּ�ڽ��������Щ����͸�������⡣");

		ObjectMapper objectMapper = new ObjectMapper();

		// �����ĵ�
		// client.prepareIndex("blog2", "article", article.getId().toString())
		// .setSource(objectMapper.writeValueAsString(article)).get();

		// �޸��ĵ�
		// client.prepareUpdate("blog2", "article", article.getId().toString())
		// .setDoc(objectMapper.writeValueAsString(article)).get();

		// �޸��ĵ�
		// client.update(
		// new UpdateRequest("blog2", "article", article.getId()
		// .toString()).doc(objectMapper
		// .writeValueAsString(article))).get();

		// ɾ���ĵ�
		// client.prepareDelete("blog2", "article", article.getId().toString())
		// .get();

		// ɾ���ĵ�
		client.delete(
				new DeleteRequest("blog2", "article", article.getId()
						.toString())).get();

		// �ر�����
		client.close();
	}

	@Test
	// ������ѯ100����¼
	public void demo7() throws IOException, InterruptedException,
			ExecutionException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));

		ObjectMapper objectMapper = new ObjectMapper();

		for (int i = 1; i <= 100; i++) {
			// ����json ����
			Article article = new Article();
			article.setId(i);
			article.setTitle(i + "����������ʵ�ܿ���");
			article.setContent(i
					+ "����ϣ�����ǵ������������Ҫ�죬����ϣ����һ�������ú�һ����ȫ��ѵ�����ģʽ������ϣ���ܹ��򵥵�ʹ��JSONͨ��HTTP���������ݣ�����ϣ�����ǵ�����������ʼ�տ��ã�����ϣ���ܹ�һ̨��ʼ����չ�����٣�����Ҫʵʱ����������Ҫ�򵥵Ķ��⻧������ϣ������һ���ƵĽ��������Elasticsearchּ�ڽ��������Щ����͸�������⡣");

			// �����ĵ�
			client.prepareIndex("blog2", "article", article.getId().toString())
					.setSource(objectMapper.writeValueAsString(article)).get();
		}
		// �ر�����
		client.close();
	}

	@Test
	// ��ҳ����
	public void demo8() throws IOException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));
		// ��������
		// get() === execute().actionGet()
		SearchRequestBuilder searchRequestBuilder = client
				.prepareSearch("blog2").setTypes("article")
				.setQuery(QueryBuilders.matchAllQuery());

		// ��ѯ��2ҳ���ݣ�ÿҳ20��
		searchRequestBuilder.setFrom(20).setSize(20);

		SearchResponse searchResponse = searchRequestBuilder.get();
		printSearchResponse(searchResponse);

		// �ر�����
		client.close();
	}

	@Test
	// ������ѯ��� ���� ����
	public void demo9() throws IOException {
		// ����������������������
		Client client = TransportClient
				.builder()
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("127.0.0.1"), 9300));

		ObjectMapper objectMapper = new ObjectMapper();

		// ��������
		SearchRequestBuilder searchRequestBuilder = client
				.prepareSearch("blog2").setTypes("article")
				.setQuery(QueryBuilders.termQuery("title", "����"));

		// ��������
		searchRequestBuilder.addHighlightedField("title"); // ��title�ֶν��и���
		searchRequestBuilder.setHighlighterPreTags("<em>"); // ǰ��Ԫ��
		searchRequestBuilder.setHighlighterPostTags("</em>");// ����Ԫ��

		SearchResponse searchResponse = searchRequestBuilder.get();

		SearchHits hits = searchResponse.getHits(); // ��ȡ���д�������ѯ����ж��ٶ���
		System.out.println("��ѯ����У�" + hits.getTotalHits() + "��");
		Iterator<SearchHit> iterator = hits.iterator();
		while (iterator.hasNext()) {
			SearchHit searchHit = iterator.next(); // ÿ����ѯ����

			// ��������������ݣ��滻ԭ������ ��ԭ�����ݣ����ܻ������ʾ��ȫ ��
			Map<String, HighlightField> highlightFields = searchHit
					.getHighlightFields();
			HighlightField titleField = highlightFields.get("title");

			// ��ȡ��ԭ�������� ÿ��������ʾ ����λ�� fragment ���Ǹ���Ƭ��
			Text[] fragments = titleField.fragments();
			String title = "";
			for (Text text : fragments) {
				title += text;
			}
			// ����ѯ���ת��Ϊ����
			Article article = objectMapper.readValue(
					searchHit.getSourceAsString(), Article.class);

			// �ø��������ݣ��滻ԭ������
			article.setTitle(title);

			System.out.println(article);
		}

		// �ر�����
		client.close();
	}
}
