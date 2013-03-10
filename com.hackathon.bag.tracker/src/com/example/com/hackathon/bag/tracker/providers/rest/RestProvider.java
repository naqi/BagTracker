package com.example.com.hackathon.bag.tracker.providers.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.net.Uri;

import com.example.com.hackathon.bag.tracker.providers.IRestProvider;
import com.google.gson.Gson;

public class RestProvider implements IRestProvider {

	@Override
	public HttpResponse getRequest(Uri uri) {
		HttpGet httpGet = new HttpGet(uri.toString());
		HttpClient httpClient = this.getHttpClient(uri, httpGet);
		
		try {
			return httpClient.execute(httpGet);
		} catch (ClientProtocolException exception) {
			return null;
		} catch (IOException exception) {
			return null;
		}
	}

	@Override
	public <BodyType> HttpResponse postRequest(Uri uri, BodyType body) {
		HttpPost httpPost = new HttpPost(uri.toString());
		
		HttpClient httpClient = this.getHttpClient(uri, httpPost);
		if(httpClient == null) {
			return null;
		}
		
		Gson gson = new Gson();
		String jsonBody = gson.toJson(body);
		
		try {
			StringEntity entity = new StringEntity(jsonBody, "UTF-8");
			entity.setContentType("application/json");
			
			httpPost.setEntity(entity);
			return httpClient.execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException exception) {
			return null;
		} catch (IOException exception) {
			return null;
		}
	}
	
	@Override
	public HttpResponse postRequest(Uri uri) {
		HttpPost httpPost = new HttpPost(uri.toString());
		HttpClient httpClient = this.getHttpClient(uri, httpPost);
		
		if(httpClient != null) {
			try {
				return httpClient.execute(httpPost);
			} catch (ClientProtocolException exception) {
				return null;
			} catch (IOException exception) {
				return null;
			}
		} else {
			return null;
		} 
	}

	/**
	 * Code jacked from http://janis.peisenieks.lv/en/76/english-making-an-ssl-connection-via-android/
	 * @param uri
	 * @param request
	 * @return
	 */
	private HttpClient getHttpClient(Uri uri, HttpRequestBase request) {
		HttpClient client = new DefaultHttpClient();
		
		if(uri.getScheme().equals("https")) {
			try {
				X509TrustManager tm = new X509TrustManager() {
		            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		            }
	 
		            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		            }
	 
		            public X509Certificate[] getAcceptedIssuers() {
		                return null;
		            }
		        };
		        
		        SSLContext ctx = SSLContext.getInstance("TLS");
		        ctx.init(null, new TrustManager[] { tm }, null);
		        
		        SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
		        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		       
		        ClientConnectionManager ccm = client.getConnectionManager();
		        SchemeRegistry sr = ccm.getSchemeRegistry();
		        sr.register(new Scheme("https", ssf, uri.getPort()));
		        
		        return client;
			} catch (Exception exception) {
				return null;
			}
		} else {
			return client;
		}
	}
}
