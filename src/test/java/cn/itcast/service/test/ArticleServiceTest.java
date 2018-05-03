package cn.itcast.service.test;

import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.domain.Article;
import cn.itcast.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ArticleServiceTest {
	@Autowired
	private ArticleService articleService;

	@Autowired
	private Client client; // ����ԭ��API

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Test
	public void createIndex() {
		elasticsearchTemplate.createIndex(Article.class);
		elasticsearchTemplate.putMapping(Article.class);
	}

	@Test
	public void testSave() {
		Article article = new Article();
		article.setId(1001);
		article.setTitle("Spring Data Elasticsearch 1.3.1 ���췢��");
		article.setContent("DATAES-171 - ���ʧЧ��ѯ�ؼ���֧�� DATAES-194 - ���Կ�������  data Ŀ¼ DATAES-179 - ֧��  Attachment �ֶ����� DATAES-94 - ���µ����°汾�� elasticsearch 1.7.3 ������");

		articleService.save(article);
	}

	@Test
	public void testDelete() {
		Article article = new Article();
		article.setId(1001);

		articleService.delete(article);
	}

	@Test
	public void testFindOne() {
		System.out.println(articleService.findOne(1001));
	}

	@Test
	public void testSaveBatch() {
		for (int i = 1; i <= 100; i++) {
			Article article = new Article();
			article.setId(i);
			article.setTitle(i + "Spring Data Elasticsearch 1.3.1 ���췢��");
			article.setContent(i
					+ "DATAES-171 - ���ʧЧ��ѯ�ؼ���֧�� DATAES-194 - ���Կ�������  data Ŀ¼ DATAES-179 - ֧��  Attachment �ֶ����� DATAES-94 - ���µ����°汾�� elasticsearch 1.7.3 ������");

			articleService.save(article);
		}
	}

	@Test
	// �����ҳ��ѯ
	public void testSortAndPaging() {
		Iterable<Article> articles = articleService.findAll();
		for (Article article : articles) {
			System.out.println(article);
		}

		Pageable pageable = new PageRequest(0, 10);
		Page<Article> pageData = articleService.findAll(pageable);
		for (Article article : pageData.getContent()) {
			System.out.println(article);
		}
	}

	@Test
	// ������ѯ
	public void testConditionQuery() {
		// ��ѯ �����к��� �����족
		// List<Article> articles = articleService.findByTitle("����");
		// for (Article article : articles) {
		// System.out.println(article);
		// }

		// ��ѯ �����к��� �����족 1-10��
		Pageable pageable = new PageRequest(0, 10);
		Page<Article> pageData = articleService.findByTitle("����", pageable);
		System.out.println("�ܼ�¼����" + pageData.getTotalElements());
		for (Article article : pageData.getContent()) {
			System.out.println(article);
		}
	}

}
