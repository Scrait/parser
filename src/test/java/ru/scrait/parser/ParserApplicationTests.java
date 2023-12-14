package ru.scrait.parser;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ParserApplicationTests {

	private final String query = "data.domagic.dev";
	private final String query2 = "data.domagic.de";
	private final ExecutorService executorService = Executors.newFixedThreadPool(1);

	@Test
	void contextLoads() throws InterruptedException {
		final String[] ids = getIds().split(",");
		System.out.println(Arrays.toString(ids));
		for (String id : ids) {
			executorService.execute(() -> {
				System.out.println(id + " : " + parseId(id));
			});
			Thread.sleep(2000);
		}
		contextLoads();
	}

	private String getIds() {
		final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			final HttpGet request = new HttpGet("https://" + query + "/api/v1/get-product-ids-4-parsing");

//			final StringEntity params = new StringEntity(ipPort);
//			//request.addHeader("content-type", "application/json");
//			request.setEntity(params);
			final HttpResponse response = httpClient.execute(request);
			final String body = new String(response.getEntity().getContent().readAllBytes());
			try {
				httpClient.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return body;

		} catch (Exception ex) {
			return "ebal";
		}
	}

	private String parseId(String id) {
		final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			//final HttpGet request = new HttpGet("http://121.43.52.152:8080/?id=" + id);
			final HttpGet request = new HttpGet("https://" + query + "/api/v1/parse-product?product_id=" + id);

//			final StringEntity params = new StringEntity(ipPort);
//			//request.addHeader("content-type", "application/json");
//			request.setEntity(params);
			final HttpResponse response = httpClient.execute(request);
			final String body = new String(String.valueOf(response.getStatusLine().getStatusCode()));
			try {
				httpClient.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return body;

		} catch (Exception ex) {
			return "ebal";
		}
	}

}
