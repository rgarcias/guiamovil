package guia.movil.app;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.fsq.android.FoursquareApp;
import com.fsq.android.FoursquareApp.FsqAuthListener;
import com.fsq.android.FsqVenue;
import com.fsq.android.NearbyAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FoursquareActivity extends ListActivity implements OnItemClickListener, OnClickListener {
	public static final String CLIENT_ID = "US20XZDXCS11UWUIVBFPZT3PN2JIQUJX0WU4CRBJ30DKXBFX";
	public static final String CLIENT_SECRET = "35XSC3TDZINLK0R4IAJ4HCGCVMVPOZBQ4KUDPEAMF5YMAO24";
	
	private com.fsq.android.FoursquareApp mFsqApp;
	private ListView mListView;
	private com.fsq.android.NearbyAdapter mAdapter;
	private ArrayList<com.fsq.android.FsqVenue> mNearbyList;
	private ProgressDialog mProgress;
	private FsqVenue selectedVenue;
	private Dialog checkinDialog;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.foursquare);
		
        mListView = this.getListView();
        mListView.setOnItemClickListener(this);
        mFsqApp = new FoursquareApp(this, CLIENT_ID, CLIENT_SECRET);
        mAdapter = new NearbyAdapter(this);
        mNearbyList = new ArrayList<FsqVenue>();
        mProgress = new ProgressDialog(this);
        
        mProgress.setMessage("Loading data ...");
        
        String methodname = "getLatitude";
        String soap = "http://turismo/" + methodname;
        String latitude = Services.getLatitude(methodname, soap, "place", CategoryActivity.PLACE);
        
        String methodname2 = "getLongitude";
        String soap2 = "http://turismo/" + methodname;
        String longitude = Services.getLongitude(methodname2, soap2, "place", CategoryActivity.PLACE);
                
        Integer lat  = Integer.valueOf(latitude);
        Integer lon  = Integer.valueOf(longitude);
        
        double lat2 = lat / 1e6;
        double lon2 = lon / 1e6;
        
        Log.e("Location", "latitude: " + lat2 + ", longitude: " + lon2);

        if(mFsqApp.hasAccessToken()){
        	loadNearbyPlaces(lat2, lon2);
        }
        else{
        	mFsqApp.authorize();
            FsqAuthListener listener = new FsqAuthListener() {
                @Override
                public void onSuccess() {
                	if(PresentationActivity.english){
                		 Toast.makeText(FoursquareActivity.this, "Connected as " + mFsqApp.getUserName(), Toast.LENGTH_SHORT).show();
                	}
                	else{
                		 Toast.makeText(FoursquareActivity.this, "Conectado como " + mFsqApp.getUserName(), Toast.LENGTH_SHORT).show();
                	}
                }
     
                @Override
                public void onFail(String error) {
                    Toast.makeText(FoursquareActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            };   
            mFsqApp.setListener(listener);
            
            if(mFsqApp.hasAccessToken()){
            	loadNearbyPlaces(lat, lon);
            }
        }
    }
 
    private void loadNearbyPlaces(final double latitude, final double longitude) {
        mProgress.show();
 
        new Thread() {
            @Override
            public void run() {
                int what = 0;
 
                try {
 
                    mNearbyList = mFsqApp.getNearby(latitude, longitude);
                } catch (Exception e) {
                    what = 1;
                    e.printStackTrace();
                }
 
                mHandler.sendMessage(mHandler.obtainMessage(what));
            }
        }.start();
    }
 
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgress.dismiss();
 
            if (msg.what == 0) {
                if (mNearbyList.size() == 0) {
                	if(PresentationActivity.english){
                		Toast.makeText(FoursquareActivity.this, "No nearby places available", Toast.LENGTH_SHORT).show();
                	}
                	else{
                		Toast.makeText(FoursquareActivity.this, "No hay lugares cercanos disponibles", Toast.LENGTH_SHORT).show();
                	}
                    return;
                }
 
                mAdapter.setData(mNearbyList);
                mListView.setAdapter(mAdapter);
            } else {
            	if(PresentationActivity.english){
            		Toast.makeText(FoursquareActivity.this, "Failed to load nearby places", Toast.LENGTH_SHORT).show();
            	}
            	else{
            		Toast.makeText(FoursquareActivity.this, "Fall� en cargar lugares cercanos", Toast.LENGTH_SHORT).show();
            	}
            }
        }
    };

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		checkinDialog = new Dialog(FoursquareActivity.this, R.style.FullHeightDialog);
        checkinDialog.setContentView(R.layout.checkindialog);    
        TextView checkinText = (TextView) checkinDialog.findViewById(R.id.checkintext);
        selectedVenue = (FsqVenue)mListView.getAdapter().getItem(arg2);
        if(PresentationActivity.english){
	        checkinText.setText(R.string.checkinING);
		}	
		else{
			checkinText.setText(R.string.checkinESP);
		}
		Button checkin = (Button) checkinDialog.findViewById(R.id.checkinbutton);
        checkin.setOnClickListener(this);
		checkinDialog.show();	
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		mFsqApp.checkin(selectedVenue.id);
		checkinDialog.dismiss();
	}
}
