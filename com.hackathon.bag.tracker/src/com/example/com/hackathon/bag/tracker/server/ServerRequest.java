package com.example.com.hackathon.bag.tracker.server;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

public class ServerRequest  {
	private static final String LOG_TAG = ServerRequest.class.getSimpleName();
	
	private DefaultHttpClient mHttpClient;
	private static ServerRequest sInstance;

	private ServerRequest() {
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 200);
		ConnManagerParams.setTimeout(params, 15*1000);
		params.setIntParameter(ConnManagerParams.MAX_TOTAL_CONNECTIONS, 200);
		params.setParameter(ConnManagerParams.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(100));
		
		//HttpConnectionManagerParams.setMaxTotalConnections(params, 100);
		//HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		//HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry 
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		// This connection manager must be used if more than one thread will
		// be using the HttpClient.
		ClientConnectionManager cm = (new ThreadSafeClientConnManager(params, schemeRegistry));

		mHttpClient = new DefaultHttpClient(cm, params);
	}
	
	public static synchronized ServerRequest get() {
		if (sInstance == null) {
			sInstance = new ServerRequest();
		}
		
		return sInstance;
	}

	public HttpResponse doPost(HttpPost post) throws ClientProtocolException, IOException {
		mHttpClient.getConnectionManager().closeExpiredConnections();
		HttpResponse response=null;
		try{
			response = mHttpClient.execute(post);
		} catch (HttpHostConnectException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			post.abort();
		} catch (ConnectionPoolTimeoutException e){
			Log.e(LOG_TAG, e.getMessage(), e);
			post.abort();
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			post.abort();
		}
		return response;
	}
	
//	public HttpResponse doGet(HttpGet get) throws ClientProtocolException, IOException {
//		if (!this.GetCookie()) {
//			throw new ClientProtocolException("Error getting cookie");
//		}
//		mHttpClient.getConnectionManager().closeExpiredConnections();
//		return mHttpClient.execute(get);
//	}
}
