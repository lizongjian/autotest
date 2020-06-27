package com.autotest.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.autotest.beans.Evn;
import com.autotest.beans.ResponseResult;
import com.autotest.common.Parse;
import com.autotest.beans.InterTestCase;
import com.autotest.beans.InterTestCaseLog;



/**
 * Description: httpClient工具类
 * 
 */
public class HttpClientUtils {

	// 编码格式。发送编码格式统一用UTF-8
	private static final String ENCODING = "UTF-8";
	
	// 设置连接超时时间，单位毫秒。
	private static final int CONNECT_TIMEOUT = 6000;
	
	// 请求获取数据的超时时间(即响应时间)，单位毫秒。
	private static final int SOCKET_TIMEOUT = 6000;

	/**
	 * 发送get请求；不带请求头和请求参数
	 * 
	 * @param url 请求地址
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doGet(String url) throws Exception {
		return doGet(url, null, null);
	}
	
	/**
	 * 发送get请求；带请求参数
	 * 
	 * @param url 请求地址
	 * @param params 请求参数集合
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doGet(String url, Map<String, String> params) throws Exception {
		return doGet(url, null, params);
	}

	/**
	 * 发送get请求；带请求头和请求参数
	 * 
	 * @param url 请求地址
	 * @param headers 请求头集合
	 * @param params 请求参数集合
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doGet(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		// 创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建访问的地址
		URIBuilder uriBuilder = new URIBuilder(url);
		if (params != null) {
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue());
			}
		}

		// 创建http对象
		HttpGet httpGet = new HttpGet(uriBuilder.build());
		/**
		 * setConnectTimeout：设置连接超时时间，单位毫秒。
		 * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
		 * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
		 * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
		 */
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpGet.setConfig(requestConfig);
		
		// 设置请求头
		packageHeader(headers, httpGet);

		// 创建httpResponse对象
		CloseableHttpResponse httpResponse = null;

		try {
			// 执行请求并获得响应结果
			return getResponseResult(httpResponse, httpClient, httpGet);
		} finally {
			// 释放资源
			release(httpResponse, httpClient);
		}
	}

	/**
	 * 发送post请求；不带请求头和请求参数
	 * 
	 * @param url 请求地址
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doPost(String url) throws Exception {
		return doPost(url, null, null);
	}
	
	/**
	 * 发送post请求；带请求参数
	 * 
	 * @param url 请求地址
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doPost(String url, Map<String, String> params) throws Exception {
		return doPost(url, null, params);
	}

	/**
	 * 发送post请求；带请求头和请求参数
	 * 
	 * @param url 请求地址
	 * @param headers 请求头集合
	 * @param params 请求参数集合
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doPost(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
		// 创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建http对象
		HttpPost httpPost = new HttpPost(url);
		/**
		 * setConnectTimeout：设置连接超时时间，单位毫秒。
		 * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
		 * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
		 * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
		 */
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpPost.setConfig(requestConfig);
		// 设置请求头
		/*httpPost.setHeader("Cookie", "");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");*/
		packageHeader(headers, httpPost);
		
		// 封装请求参数
		packageParam(params, httpPost);

		// 创建httpResponse对象
		CloseableHttpResponse httpResponse = null;

		try {
			// 执行请求并获得响应结果
			return getResponseResult(httpResponse, httpClient, httpPost);
		} finally {
			// 释放资源
			release(httpResponse, httpClient);
		}
	}

	/**
	 * 发送put请求；不带请求参数
	 * 
	 * @param url 请求地址
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doPut(String url) throws Exception {
		return doPut(url);
	}

	/**
	 * 发送put请求；带请求参数
	 * 
	 * @param url 请求地址
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doPut(String url, Map<String, String> params) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpPut.setConfig(requestConfig);
		
		packageParam(params, httpPut);

		CloseableHttpResponse httpResponse = null;

		try {
			return getResponseResult(httpResponse, httpClient, httpPut);
		} finally {
			release(httpResponse, httpClient);
		}
	}

	/**
	 * 发送delete请求；不带请求参数
	 * 
	 * @param url 请求地址
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doDelete(String url) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpDelete.setConfig(requestConfig);

		CloseableHttpResponse httpResponse = null;
		try {
			return getResponseResult(httpResponse, httpClient, httpDelete);
		} finally {
			release(httpResponse, httpClient);
		}
	}

	/**
	 * 发送delete请求；带请求参数
	 * 
	 * @param url 请求地址
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult doDelete(String url, Map<String, String> params) throws Exception {
		if (params == null) {
			params = new HashMap<String, String>();
		}

		params.put("_method", "delete");
		return doPost(url, params);
	}
	
	/**
	 * Description: 封装请求头
	 * @param params
	 * @param httpMethod
	 */
	public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
		// 封装请求头
		if (params != null) {
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				// 设置到请求头到HttpRequestBase对象中
				httpMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Description: 封装请求参数
	 * 
	 * @param params
	 * @param httpMethod
	 * @throws UnsupportedEncodingException
	 */
	public static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
			throws UnsupportedEncodingException {
		// 封装请求参数
		if (params != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			// 设置到请求的http对象中
			httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
		}
	}

	/**
	 * Description: 获得响应结果
	 * 
	 * @param httpResponse
	 * @param httpClient
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */
	public static ResponseResult getResponseResult(CloseableHttpResponse httpResponse,
			CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
		// 执行请求
		httpResponse = httpClient.execute(httpMethod);

		// 获取返回结果
		if (httpResponse != null && httpResponse.getStatusLine() != null) {
			String content = "";
			if (httpResponse.getEntity() != null) {
				content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
			}
			return new ResponseResult(httpResponse.getStatusLine().getStatusCode(), content);
		}
		return new ResponseResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}

	/**
	 * Description: 释放资源
	 * 
	 * @param httpResponse
	 * @param httpClient
	 * @throws IOException
	 */
	public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
		// 释放资源
		if (httpResponse != null) {
			httpResponse.close();
		}
		if (httpClient != null) {
			httpClient.close();
		}
	}
	
	
	//统一发送方法，返回日志，用来生成报告
	public static ResponseResult sendRequest(InterTestCase t,Evn evn,Map<String,String> context,InterTestCaseLog testCaseLog) {
		ResponseResult result = new ResponseResult();
		String reqMethod = t.getRequestMethod();
		
		//1.解析json字符串  把json中的$<>变量替换  最后解析成map
		Map<String,String> request_header = t.toMap(Parse.parse(t.getRequestHeader(), context).get("result"));
		Map<String,String> default_parame = t.toMap(Parse.parse(t.getDefaultParam(), context).get("result"));
		//入参替换 保留 在测试平台的时候 因为接口和用例是分开的
		//Map<String,String> default_parame = t.toMap(t.parse(t.getDefaultParame(), context));
		if(request_header!=null) {
			testCaseLog.setRequestHeader(request_header.toString());
		}
		if(default_parame!=null) {
			testCaseLog.setDefaultParame(default_parame.toString());
		}
		testCaseLog.setRequestPath(evn.getIp()+""+t.getRequestPath());
		
		switch (reqMethod) {
		case "get":
			try {
				result = doGet(evn.getIp()+""+t.getRequestPath(),default_parame,request_header);
			} catch (Exception e) {
				e.printStackTrace();
				result = new ResponseResult(404, e.toString());
			}
			break;
		case "post":
			
			break;
		default:
			break;
		}
		
		return result;
	}
	

	
	public static void main(String[] args) {
		try {
			ResponseResult h = HttpClientUtils.doGet("http://www.baidu.com");
			System.out.println(h);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}