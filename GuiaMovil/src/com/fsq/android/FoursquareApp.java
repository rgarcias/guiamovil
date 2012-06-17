package com.fsq.android;

import com.fsq.android.FoursquareDialog.FsqDialogListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import android.app.ProgressDialog;

import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;

public class FoursquareApp {
	private FoursquareSession mSession;
	private FoursquareDialog mDialog;
	private FsqAuthListener mListener;
	private ProgressDialog mProgress;
	private String mTokenUrl;
	private String mAccessToken;
	private ArrayList<FsqVenue> venueList;
	
	public static final String CALLBACK_URL = "myapp://connect";
	private static final String AUTH_URL = "https://foursquare.com/oauth2/authenticate?response_type=code";
	private static final String TOKEN_URL = "https://foursquare.com/oauth2/access_token?grant_type=authorization_code";	
	private static final String API_URL = "https://api.foursquare.com/v2";
	private static final String CHECKIN_URL = "https://api.foursquare.com/v2/checkins/add";
	
	private static final String TAG = "FoursquareApi";
	
	public FoursquareApp(Context context, String clientId, String clientSecret) {
		mSession		= new FoursquareSession(context);
		mAccessToken	= mSession.getAccessToken();
		mTokenUrl		= TOKEN_URL + "&client_id=" + clientId + "&client_secret=" + clientSecret
						+ "&redirect_uri=" + CALLBACK_URL;
		String url		= AUTH_URL + "&client_id=" + clientId + "&redirect_uri=" + CALLBACK_URL;
		
		FsqDialogListener listener = new FsqDialogListener() {
			@Override
			public void onComplete(String code) {
				getAccessToken(code);
			}
			
			@Override
			public void onError(String error) {
				mListener.onFail("Authorization failed");
			}
		};
		
		mDialog			= new FoursquareDialog(context, url, listener);
		mProgress		= new ProgressDialog(context);
		mProgress.setCancelable(false);
	}
	
	private void getAccessToken(final String code) {
		mProgress.setMessage("Getting access token ...");
		mProgress.show();
		
		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Getting access token");		
				int what = 0;
				try {
					URL url = new URL(mTokenUrl + "&code=" + code);
					Log.i(TAG, "Opening URL " + url.toString());		
					HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();	
					urlConnection.setRequestMethod("GET");		
					urlConnection.connect();
					JSONObject jsonObj  = (JSONObject) new JSONTokener(streamToString(urlConnection.getInputStream())).nextValue();
		        	mAccessToken 		= jsonObj.getString("access_token"); 	
		        	Log.i(TAG, "Got access token: " + mAccessToken);
				} catch (Exception ex) {
					what = 1;	
					ex.printStackTrace();
				}		
				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
			}
		}.start();
	}
	
	private void fetchUserName() {
		mProgress.setMessage("Finalizing ...");
		
		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user name");
				int what = 0;
		
				try {
					URL url = new URL(API_URL + "/users/self?oauth_token=" + mAccessToken);	
					Log.d(TAG, "Opening URL " + url.toString());			
					HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();			
					urlConnection.setRequestMethod("GET");		
					urlConnection.connect();		
					String response		= streamToString(urlConnection.getInputStream());
					JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();
					JSONObject resp		= (JSONObject) jsonObj.get("response");
					JSONObject user		= (JSONObject) resp.get("user");
					
					String firstName 	= user.getString("firstName");
		        	String lastName		= user.getString("lastName");
		        
		        	Log.i(TAG, "Got user name: " + firstName + " " + lastName);  	
		        	mSession.storeAccessToken(mAccessToken, firstName + " " + lastName);
				} catch (Exception ex) {
					what = 1;		
					ex.printStackTrace();
				}	
				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				if (msg.what == 0) {
					fetchUserName();
				} else {
					mProgress.dismiss();	
					mListener.onFail("Failed to get access token");
				}
			} else {
				mProgress.dismiss();	
				mListener.onSuccess();
			}
		}
	};
	
	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}
	
	public void setListener(FsqAuthListener listener) {
		mListener = listener;
	}
	
	public String getUserName() {
		return mSession.getUsername();
	}
	
	public void authorize() {
		mDialog.show();
	}
	
	public ArrayList<FsqVenue> getNearby(double latitude, double longitude) throws Exception {
		 venueList = new ArrayList<FsqVenue>();
		String response = "";
		
		try {
			String ll 	= String.valueOf(latitude) + "," + String.valueOf(longitude);
			URL url 	= new URL(API_URL + "/venues/search?ll=" + ll + "&oauth_token=" + mAccessToken + "&v=20120614");	
			Log.e(TAG, "Opening URL " + url.toString());	
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();	
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			response		= streamToString(urlConnection.getInputStream());
			JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();
			
			JSONArray venues = (JSONArray) jsonObj.getJSONObject("response").getJSONArray("venues");
			
			int length= venues.length();
			
			if (length > 0) {
				for (int i = 0; i < length; i++) {
						JSONObject item = (JSONObject) venues.get(i);
						
						FsqVenue venue = new FsqVenue();
						venue.id = item.getString("id");
						venue.name = item.getString("name");
						JSONObject location = (JSONObject) item.getJSONObject("location");
						
						Location loc 	= new Location(LocationManager.GPS_PROVIDER);
						
						loc.setLatitude(Double.valueOf(location.getString("lat")));
						loc.setLongitude(Double.valueOf(location.getString("lng")));
						
						venue.location	= loc;
						venue.distance	= item.getJSONObject("location").getInt("distance");
						venue.herenow	= item.getJSONObject("hereNow").getInt("count");
						
						venueList.add(venue);
					}
				}
		} catch (Exception ex) {
			throw ex;
		}
		return venueList;
	}
	
	private String streamToString(InputStream is) throws IOException {
		String str  = "";
		
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			
			try {
				BufferedReader reader 	= new BufferedReader(new InputStreamReader(is));
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}		
				reader.close();
			} finally {
				is.close();
			}	
			str = sb.toString();
		}
		
		return str;
	}
	
	public interface FsqAuthListener {
		public abstract void onSuccess();
		public abstract void onFail(String error);
	}
	
	public void checkin(String id){
		try {
			URL url 	= new URL(API_URL + "/checkins/add?venueId=" + id + "&oauth_token=" + mAccessToken);	
			Log.e(TAG, "Opening URL " + url.toString());	
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();	
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			
		} catch (Exception ex) {

		}
	}
}