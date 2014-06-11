/**
 * @author Ratul Ghosh
 * 27-Feb-2014
 * 
 */
package com.appsbee.pairpost.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.appsbee.pairpost.util.LogManager;

public class HttpClient
{
	private DefaultHttpClient httpClient;
	private final String TAG;

	public HttpClient(Context context)
	{
		TAG = getClass().getSimpleName();
		httpClient = new DefaultHttpClient();
		httpClient.setCookieStore(new PersistentCookieStore(context));
	}

	public String sendHttpPostWithJson(String url,
			HashMap<String, String> params) throws Exception
	{

		try
		{

			HttpPost httpPostRequest = new HttpPost(url);
			httpPostRequest.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Accept-Encoding", "gzip");

			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
					params.size());
			Iterator<String> iterator = params.keySet().iterator();
			while (iterator.hasNext())
			{
				String key = iterator.next();
				String value = params.get(key);
				nameValuePair.add(new BasicNameValuePair(key, value));
			}

			try
			{
				httpPostRequest.setEntity(new UrlEncodedFormEntity(
						nameValuePair));
			}
			catch (UnsupportedEncodingException e)
			{
				LogManager.e(TAG, e.getMessage());
				throw e;
			}

			LogManager.v(TAG,
					"Sending to " + url + ":\n" + nameValuePair.toString());

			Header[] headers = httpPostRequest.getAllHeaders();
			LogManager.v(TAG, "Printing Headers [" + headers.length + "]\n");
			for (int i = 0; i < headers.length; i++)
			{
				LogManager.v(TAG,
						headers[i].getName() + ":" + headers[i].getValue()
								+ "\n");
			}
			LogManager.v(TAG, "Printing Entity");
			LogManager.v(TAG, inputStreamToString(httpPostRequest.getEntity()
					.getContent()));

			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) httpClient
					.execute(httpPostRequest);
			LogManager.v(TAG,
					"HTTPResponse received in ["
							+ (System.currentTimeMillis() - t) + "ms] from "
							+ url);

			HttpEntity entity = response.getEntity();

			if (entity != null)
			{
				InputStream instream = entity.getContent();
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip"))
				{
					instream = new GZIPInputStream(instream);
				}

				String resultString = inputStreamToString(instream);
				instream.close();
				resultString = resultString.substring(0,
						resultString.length() - 1);

				LogManager.v(TAG, "Received Data :");
				LogManager.v(TAG, resultString);

				return resultString;
			}
			else
			{
				LogManager.v(TAG, "No response data received from " + url);
				return null;
			}

		}
		catch (Exception e)
		{

			LogManager.e(TAG, e.getMessage());
			throw e;
		}

	}

	public String sendHttpPostWithJson(String url,
			HashMap<String, String> params, File file) throws IOException
	{
		HttpPost httpPost = new HttpPost(url);

		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		Iterator<String> iterator = params.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = iterator.next();
			String value = params.get(key);
			entity.addPart(key, new StringBody(value));
		}

		entity.addPart("file", new FileBody(file));

		httpPost.setEntity(entity);

		LogManager.v(TAG, "Sending to " + url + ":\n" + params.toString());
		long t = System.currentTimeMillis();
		HttpResponse response = httpClient.execute(httpPost);
		LogManager.v(TAG,
				"HTTPResponse received in [" + (System.currentTimeMillis() - t)
						+ "ms] from " + url);
		HttpEntity resEntity = response.getEntity();
		final String responseData = EntityUtils.toString(resEntity);
		LogManager.v(TAG, "Received Data :");
		LogManager.v(TAG, responseData);

		return responseData;

	}

	private String inputStreamToString(InputStream is) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");

		}
		catch (IOException e)
		{
			LogManager.e(TAG, e.getMessage());
			throw e;
		}
		finally
		{
			try
			{
				is.close();
				reader.close();
			}
			catch (IOException e)
			{
				LogManager.e(TAG, e.getMessage());
				throw e;
			}
		}
		return sb.toString();
	}

}
