package guia.movil.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public abstract class FBConnectionActivity extends Activity {
    public static final String TAG = "FACEBOOK";
    private Facebook mFacebook;
    public static final String APP_ID = "292867444131607";
    private AsyncFacebookRunner mAsyncRunner;
    private static final String[] PERMS = new String[] { "read_stream", "publish_stream" };
    private SharedPreferences sharedPrefs;
    private Context mContext;

    

    public void setConnection() {
            mContext = this;
            mFacebook = new Facebook(APP_ID);
            mAsyncRunner = new AsyncFacebookRunner(mFacebook);
    }

    public void getID() {
           
            if (isSession()) {
                    Log.d(TAG, "sessionValid");
                    mAsyncRunner.request("me", new IDRequestListener());
            } else {
                    // no logged in, so relogin
                    Log.d(TAG, "sessionNOTValid, relogin");
                    mFacebook.authorize(this, PERMS, new LoginDialogListener());
            }
    }

    public boolean isSession() {
    	
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String access_token = sharedPrefs.getString("access_token", "x");
            Long expires = sharedPrefs.getLong("access_expires", -1);
            Log.d(TAG, access_token);

            if (access_token != null && expires != -1) {
                    mFacebook.setAccessToken(access_token);
                    mFacebook.setAccessExpires(expires);
            }
            return mFacebook.isSessionValid();
    }
    
    public void conect()
    {
    	setConnection();
		getID();
    }
    public boolean isLogged()
    {
    	mContext = this;
    	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    	String access_token = sharedPrefs.getString("access_token", "x");
         Long expires = sharedPrefs.getLong("access_expires", -1);
    	if(access_token != null && expires != -1)
    		return true;
    	return false;
    }

    private class LoginDialogListener implements DialogListener {

            public void onComplete(Bundle values) {
                    Log.d(TAG, "LoginONComplete");
                    String token = mFacebook.getAccessToken();
                    long token_expires = mFacebook.getAccessExpires();
                    Log.d(TAG, "AccessToken: " + token);
                    Log.d(TAG, "AccessExpires: " + token_expires);
                    sharedPrefs = PreferenceManager
                                    .getDefaultSharedPreferences(mContext);
                    sharedPrefs.edit().putLong("access_expires", token_expires)
                                    .commit();
                    sharedPrefs.edit().putString("access_token", token).commit();
                    mAsyncRunner.request("me", new IDRequestListener());
            }

            public void onFacebookError(FacebookError e) {
                    Log.d(TAG, "FacebookError: " + e.getMessage());
            }

            public void onError(DialogError e) {
                    Log.d(TAG, "Error: " + e.getMessage());
            }

            public void onCancel() {
                    Log.d(TAG, "OnCancel");
            }
    }

    private class IDRequestListener implements RequestListener {

            public void onComplete(String response, Object state) {
                    try {
                            Log.d(TAG, "IDRequestONComplete");
                            Log.d(TAG, "Response: " + response.toString());
                            JSONObject json = Util.parseJson(response);

                         
                            FBConnectionActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                            

                                    }
                            });
                    } catch (JSONException e) {
                            Log.d(TAG, "JSONException: " + e.getMessage());
                    } catch (FacebookError e) {
                            Log.d(TAG, "FacebookError: " + e.getMessage());
                    }
            }

            public void onIOException(IOException e, Object state) {
                    Log.d(TAG, "IOException: " + e.getMessage());
            }

            public void onFileNotFoundException(FileNotFoundException e,
                            Object state) {
                    Log.d(TAG, "FileNotFoundException: " + e.getMessage());
            }

            public void onMalformedURLException(MalformedURLException e,
                            Object state) {
                    Log.d(TAG, "MalformedURLException: " + e.getMessage());
            }

            public void onFacebookError(FacebookError e, Object state) {
                    Log.d(TAG, "FacebookError: " + e.getMessage());
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    public String postOnWall(String title,String msg) {
        Log.d("Tests", "Testing graph API wall post");
         try {
                String response = mFacebook.request("me");
                Bundle parameters = new Bundle();
                //parameters.putString("name", "Iloca");
                //parameters.putString("link", "http://www.curico.cl");
                parameters.putString("message", title+"\n"+msg);
                parameters.putString("description", "");
                response = mFacebook.request("me/feed", parameters, 
                        "POST");
                Log.d("Tests", "got response: " + response);
                if (response == null || response.equals("") || 
                        response.equals("false")) {
                   Log.v("Error", "Blank response");
                   
               }
                return response;
         } catch(Exception e) {
             return e.getMessage();
         }
    }
    
    
    
    
    
    
    
    
    
}