package guia.movil.app;

import java.util.ArrayList;

import com.fsq.android.FoursquareApp;
import com.fsq.android.FoursquareApp.FsqAuthListener;
import com.fsq.android.FsqVenue;
import com.fsq.android.NearbyAdapter;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
	private ProgressDialog cProgress;
	private double lat2;
	private double lon2;
	private Dialog languageView;
    private ListView languages;
    private ListView idiomas;
	
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
        lat2 = lat / 1e6;
        lon2 = lon / 1e6;
        if(mFsqApp.hasAccessToken()){
        	loadNearbyPlaces(lat2, lon2);
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
                } 
                catch (Exception e) {
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
            } 
            else {
            	if(PresentationActivity.english){
            		Toast.makeText(FoursquareActivity.this, "Failed to load nearby places", Toast.LENGTH_SHORT).show();
            	}
            	else{
            		Toast.makeText(FoursquareActivity.this, "Fall� en cargar lugares cercanos", Toast.LENGTH_SHORT).show();
            	}
            }
        }
    };

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
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
		ImageButton checkin = (ImageButton) checkinDialog.findViewById(R.id.checkinbutton);
        checkin.setOnClickListener(this);
		checkinDialog.show();	
	}

	public void onClick(View v) {
		checkinDialog.dismiss();
		cProgress = new ProgressDialog(this);
		cProgress.setMessage("Checking you in...");
		cProgress.show();	
		new Thread() {
	           @Override
	           public void run() {
	        	   	int what = 0;
	                mFsqApp.checkin(selectedVenue.id);
	                cHandler.sendMessage(cHandler.obtainMessage(what));
	            }
	        }.start();
	}
	
	private Handler cHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            cProgress.dismiss();
            if (msg.what == 0) {
                if(PresentationActivity.english){
                	Toast.makeText(FoursquareActivity.this, "Checked in!", Toast.LENGTH_SHORT).show();
                }
                else{
                	Toast.makeText(FoursquareActivity.this, "Check-in realizado!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }};
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.ctxmenu2, menu);
	    if(PresentationActivity.english){
			menu.getItem(0).setTitle("Language");
			menu.getItem(1).setTitle("About");
	    }
	    return true;
	}
    
    public void refresh(){
		Intent intent = new Intent(FoursquareActivity.this, FoursquareActivity.class);
		this.finish();
		startActivity(intent);
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		   switch (item.getItemId()) { 		           
		        case R.id.languageMenu2:
		        	languageView = new Dialog(this, R.style.FullHeightDialog);
			        languageView.setContentView(R.layout.language);
			        languageView.setCancelable(true);
			        TextView titleLanguage = (TextView)languageView.findViewById(R.id.languageTitle);
			        ImageButton backLang = (ImageButton)languageView.findViewById(R.id.sitesBack);
		        	backLang.setOnClickListener(new View.OnClickListener() {
			            public void onClick(View v) {
			            	languageView.dismiss();
			            }});
		        	ListView idiomas = (ListView)languageView.findViewById(R.id.languagelist);
		        	ArrayList<String> idms = new ArrayList<String>();
		        	idms.add("Ingl�s");
		        	idms.add("Espa�ol");
		        	languages = (ListView)languageView.findViewById(R.id.languagelist);
		        	ArrayList<String> langs = new ArrayList<String>();
		        	langs.add("English");
		        	langs.add("Spanish");
		        	if(PresentationActivity.english){
		        		titleLanguage.setText("Language");
		        		ArrayAdapter<String> languagesAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, langs);
						languages.setAdapter(languagesAdapter);
						languages.setOnItemClickListener(new OnItemClickListener(){

						public void onItemClick(AdapterView<?> l, View v, int position, long arg3) {
							// TODO Auto-generated method stub
							if(position==0){
								if(PresentationActivity.english){
									Toast.makeText(getApplicationContext(), "This language is already selected", Toast.LENGTH_SHORT).show();
					        	}
					        	else{
				        			PresentationActivity.english = true;
				        			refresh();
				        			languageView.dismiss();
				        		}
					    	   }
							if(position==1){
								if(PresentationActivity.english){
					        		PresentationActivity.english = false;
					        		refresh();
					        		languageView.dismiss();
					        	}
					        	else{
					        		Toast.makeText(FoursquareActivity.this, "This language is already selected", Toast.LENGTH_SHORT).show();
				        		}
					    	   }
						}});
		        	}
		        	else{
		        		ArrayAdapter<String> languagesAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, idms);
						idiomas.setAdapter(languagesAdapter);
						idiomas.setOnItemClickListener(new OnItemClickListener(){

						public void onItemClick(AdapterView<?> l, View v, int position, long arg3) {
							// TODO Auto-generated method stub
							if(position==0){
								if(PresentationActivity.english){
									Toast.makeText(FoursquareActivity.this, "Este idioma ya est� aplicado", Toast.LENGTH_SHORT).show();
					        	}
					        	else{
				        			PresentationActivity.english = true;
				        			refresh();
				        			languageView.dismiss();
				        		}
					    	   }
							if(position==1){
								if(PresentationActivity.english){
					        		PresentationActivity.english = false;
					        		refresh();
					        		languageView.dismiss();
					        	}
					        	else{
					        		Toast.makeText(FoursquareActivity.this, "Este idioma ya est� aplicado", Toast.LENGTH_SHORT).show();
				        		}
					    	   }
						}});
	        		}
		        	
				    languageView.show();
			      return true;    
		        case R.id.aboutMenu2:
		        	final Dialog commentView = new Dialog(FoursquareActivity.this, R.style.FullHeightDialog);
					commentView.setContentView(R.layout.about);
					commentView.setCancelable(true);
				    TextView name = (TextView) commentView.findViewById(R.id.aboutName);
				    TextView text = (TextView) commentView.findViewById(R.id.aboutText);
    
				    if(PresentationActivity.english){
				    	name.setText("About");
	        			text.setText(R.string.abouting);
				    }
				    
				    ImageButton back = (ImageButton)commentView.findViewById(R.id.aboutBack);
				    back.setOnClickListener(new View.OnClickListener() {
				    public void onClick(View v) {
				           commentView.dismiss();
				        }});
				    
				    commentView.show();
			       return true;
		        default:
		           return super.onOptionsItemSelected(item);
		}
	}
}
