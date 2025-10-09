package au.com.cuscal.cards.services.rest;

import static org.springframework.http.HttpStatus.OK;

import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.cards.commons.JsonMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Map;
import java.util.UUID;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service(value = Constants.REST_SERVICE)
public class RestService {

	public static final String ACCEPT = "Accept";

	public static final String APPLICATION_JSON_VALUE = "application/json";

	public static final String CONTENT_TYPE = "Content-type";

	public static final String TRACE_ID = "traceId";

	public URI buildURI(String path, Map<String, String> params)
		throws URISyntaxException {

		URIBuilder uriBuilder = new URIBuilder(path);

		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue());
			}
		}

		return uriBuilder.build();
	}

	public <T> T doGet(
			String url, Map<String, String> queryParams, Class<T> responseType)
		throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(buildURI(url, queryParams));

		setBasicHeaders(httpGet);
		log.info(
			"{} - Connecting to rest service [{}] using GET",
			getTraceId(httpGet), url);
		CloseableHttpResponse response = client.execute(httpGet);

		if (OK.value() != response.getStatusLine(
			).getStatusCode()) {

			throw new Exception(
				"HTTP Status Code: " +
					response.getStatusLine(
					).getStatusCode() + " Error: " +
						EntityUtils.toString(response.getEntity()));
		}

		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				response.getEntity(
				).getContent()));

		String responseBody = reader.readLine();
		reader.close();
		client.close();
		log.info("{} - Response received from [{}]", getTraceId(httpGet), url);

		return JsonMapper.read(responseBody, responseType);
	}

	public String doGet(
			String url, Map<String, String> queryParams,
			Map<String, String> headers)
		throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(buildURI(url, queryParams));

		setHeaders(headers, httpGet);
		log.info(
			"{} - Connecting to rest service [{}] using GET",
			getTraceId(httpGet), url);
		CloseableHttpResponse response = client.execute(httpGet);

		if (OK.value() != response.getStatusLine(
			).getStatusCode()) {

			throw new Exception(
				"HTTP Status Code: " +
					response.getStatusLine(
					).getStatusCode() + " Error: " +
						EntityUtils.toString(response.getEntity()));
		}

		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				response.getEntity(
				).getContent()));

		String responseBody = reader.readLine();
		reader.close();
		client.close();
		log.info("{} - Response received from [{}]", getTraceId(httpGet), url);

		return responseBody;
	}

	public <T> T doGet(
			String url, Map<String, String> queryParams,
			Map<String, String> headers, Class<T> responseType)
		throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(buildURI(url, queryParams));

		setHeaders(headers, httpGet);
		log.info(
			"{} - Connecting to rest service [{}] using GET",
			getTraceId(httpGet), url);
		CloseableHttpResponse response = client.execute(httpGet);

		if (OK.value() != response.getStatusLine(
			).getStatusCode()) {

			throw new Exception(
				"HTTP Status Code: " +
					response.getStatusLine(
					).getStatusCode() + " Error: " +
						EntityUtils.toString(response.getEntity()));
		}

		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				response.getEntity(
				).getContent()));

		String responseBody = reader.readLine();
		reader.close();
		client.close();
		log.info("{} - Response received from [{}]", getTraceId(httpGet), url);

		return JsonMapper.read(responseBody, responseType);
	}

	public String doPost(
			String url, Map<String, String> queryParams,
			Map<String, String> headers, String body)
		throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(buildURI(url, queryParams));

		setHeaders(headers, httpPost);
		log.info(
			"{} - Connecting to rest service [{}] using POST",
			getTraceId(httpPost), url);
		StringEntity entity = new StringEntity(body);

		httpPost.setEntity(entity);
		CloseableHttpResponse response = client.execute(httpPost);

		if (OK.value() != response.getStatusLine(
			).getStatusCode()) {

			throw new Exception(
				"HTTP Status Code: " +
					response.getStatusLine(
					).getStatusCode() + " Error: " +
						EntityUtils.toString(response.getEntity()));
		}

		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				response.getEntity(
				).getContent()));

		String responseBody = reader.readLine();
		reader.close();
		client.close();
		log.info("{} - Response received from [{}]", getTraceId(httpPost), url);

		return responseBody;
	}

	public <T> T doPost(
			String url, Map<String, String> queryParams,
			Map<String, String> headers, String body, Class<T> responseType)
		throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(buildURI(url, queryParams));

		setHeaders(headers, httpPost);
		log.info(
			"{} - Connecting to rest service [{}] using POST",
			getTraceId(httpPost), url);
		StringEntity entity = new StringEntity(body);

		httpPost.setEntity(entity);
		CloseableHttpResponse response = client.execute(httpPost);

		if (OK.value() != response.getStatusLine(
			).getStatusCode()) {

			throw new Exception(
				"HTTP Status Code: " +
					response.getStatusLine(
					).getStatusCode() + " Error: " +
						EntityUtils.toString(response.getEntity()));
		}

		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				response.getEntity(
				).getContent()));

		String responseBody = reader.readLine();
		reader.close();
		client.close();
		log.info("{} - Response received from [{}]", getTraceId(httpPost), url);

		return JsonMapper.read(responseBody, responseType);
	}

	private String getTraceId(HttpRequestBase httpRequest) {
		return httpRequest.getLastHeader(
			TRACE_ID
		).getName() + ":" +
			httpRequest.getLastHeader(
				TRACE_ID
			).getValue();
	}

	private void setBasicHeaders(HttpRequestBase httpRequest) {
		httpRequest.setHeader(
			TRACE_ID,
			UUID.randomUUID(
			).toString());
	}

	private void setHeaders(
		Map<String, String> additionalHeaders, HttpRequestBase httpRequest) {

		httpRequest.setHeader(ACCEPT, APPLICATION_JSON_VALUE);
		httpRequest.setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);
		setBasicHeaders(httpRequest);

		for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
			httpRequest.setHeader(entry.getKey(), entry.getValue());
		}
	}

	private static Logger log = LoggerFactory.getLogger(RestService.class);

}