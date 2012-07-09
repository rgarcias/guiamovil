package guia.movil.app;




import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends ListActivity implements OnClickListener {
	protected static final int DIALOG_BACK_ID = 0;
	protected static String PLACE = "";
	protected static int changed = 0;
	public final static int INFORMATION = 1;
    /* Variables*/
    private int depth;
    private ArrayList<String> itemes;
    boolean ASC =true;
    
    /* Lista */
    private String[] categories;
    private String[] attractions;
    private String[] interest;
    private String[] recreation;
    private String[] services;
    private String main;
    private ListView lv;
    private TextView tv; 
    private TextView stv; 
    private ImageButton ib;
    private ImageButton ib2;
    private ArrayAdapter<String> listAdapter;
    private Dialog exitDialog;
    private AutoCompleteTextView searchAutoComplete;
    private Dialog searchView;
    private ListView results;
	protected String[] lugares;
	private ProgressDialog iProgress;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState); 
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.category);
       selectLanguage();
       
       itemes = new ArrayList<String>();
       itemes.add(main);
       tv = (TextView) this.findViewById(R.id.textView1);
       tv.setText(itemes.get(itemes.size()-1));
       
       ib = (ImageButton) this.findViewById(R.id.imageButton1);
       ib.setOnClickListener(this);
       ib.setVisibility(View.INVISIBLE);
       
       ib2 = (ImageButton) this.findViewById(R.id.imageButton2);
       ib2.setOnClickListener(this);
       ib2.setVisibility(View.INVISIBLE);
       
       stv = (TextView) this.findViewById(R.id.path);
       stv.setVisibility(View.INVISIBLE);
       
       this.depth = 0;
       lv = this.getListView();
       listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
       lv.setAdapter(listAdapter);
       lv.requestFocus();
       lv.requestFocusFromTouch();
   }

	@Override
	protected void onRestart() {
		super.onRestart();
		if(CategoryActivity.changed == 1){
			refresh();
			CategoryActivity.changed = 0;
		}
	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(depth >= 2){
        	if(isOnline()){
	        	final Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
	        	String place = (String) lv.getAdapter().getItem(position);
	        	this.PLACE = place;
	        	startActivityForResult(intent, CategoryActivity.INFORMATION);
        	}
        }
        else{
        	this.depth += 1;
        	if(!(itemes.get(itemes.size()-1).compareTo((String) lv.getAdapter().getItem(position))==0)){
        		itemes.add((String) lv.getAdapter().getItem(position));
        	}
        	if(this.depth==2)
        	{
        		
        		iProgress = new ProgressDialog(this);
    			iProgress.setCancelable(false);
    			iProgress.setMessage("Cargando lugares");
    			if(PresentationActivity.english){
    				iProgress.setMessage("Loading places");
    			}
    			iProgress.show();
        		
        		
        		new Thread() {
    				@Override
    	            public void run() {
    	                int what = 0;
    	                
    	                try{
    	               
    	                	lugares = procesarConsulta(Services.getPlaces("getPlaces", "http://turismo/getPlaces", "subcategory", itemes.get(itemes.size()-1)));
    	                	
    	                }
    	                catch(Exception e){
    	                	what = 1;
    	                }
    	                cHandler.sendMessage(cHandler.obtainMessage(what));
    	            }
    	        }.start();
            
        		
        	}
        	else
        	{
        		doThat();
        	}
        }
    }
	
	private Handler cHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            iProgress.dismiss();
 
            if (msg.what == 0 &&lugares.length>0) {
            	
            	ib.setVisibility(View.VISIBLE);
            	ib2.setVisibility(View.VISIBLE);
            	stv.setText(CategoryActivity.this.arrayListtoString());
            	stv.setVisibility(View.VISIBLE);
            	tv.setText(itemes.get(itemes.size()-1));
                
                listAdapter = new ArrayAdapter<String>(CategoryActivity.this, android.R.layout.simple_list_item_1, lugares);
            	lv.setAdapter(listAdapter);
            	
               
		        
                if(PresentationActivity.english){
                	Toast.makeText(CategoryActivity.this, "Successfully loading", Toast.LENGTH_SHORT).show();
                }
                else{
                	Toast.makeText(CategoryActivity.this, "Cargado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
            else if(msg.what == 0 &&lugares.length==0)
            {
            	itemes.remove(depth);
           	 depth -= 1;
           	 if(PresentationActivity.english){
           		
                	Toast.makeText(CategoryActivity.this, "No location available", Toast.LENGTH_SHORT).show();
                }
                else{
                	Toast.makeText(CategoryActivity.this, "No hay lugares disponibles ", Toast.LENGTH_SHORT).show();
                }
            }
            else{
            	itemes.remove(depth);
            	 depth -= 1;
            	 if(PresentationActivity.english){
            		
                 	Toast.makeText(CategoryActivity.this, "Unsuccessfully loading", Toast.LENGTH_SHORT).show();
                 }
                 else{
                 	Toast.makeText(CategoryActivity.this, "Cargado sin éxito", Toast.LENGTH_SHORT).show();
                 }
            }
        }
    };
	private String[] topTens;
	
	public void doThat()
	{
		ib.setVisibility(View.VISIBLE);
    	ib2.setVisibility(View.VISIBLE);
    	stv.setText(this.arrayListtoString());
    	stv.setVisibility(View.VISIBLE);
    	tv.setText(itemes.get(itemes.size()-1));
        String[] aux = selectArray(itemes.get(itemes.size()-1));
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aux);
    	lv.setAdapter(listAdapter);
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case CategoryActivity.INFORMATION:
				if(resultCode == InformationActivity.RESULT_WRONG){
					Toast.makeText(this, "Error al cargar la información", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
    
    @Override
    public void onBackPressed(){
    	itemes.remove(itemes.size()-1);
    	if(depth > 0){
    		this.depth -= 1;
	    	if(depth == 0){
	    		listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
	    		ib.setVisibility(View.INVISIBLE);
	    		ib2.setVisibility(View.INVISIBLE);
	    		stv.setVisibility(View.INVISIBLE);
	    	}
	    	else{
	    		if(depth>0){
	    			listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectArray(itemes.get(itemes.size()-1)));
	    			stv.setText(this.arrayListtoString());
	    		}
	    	}
    		tv.setText(itemes.get(itemes.size()-1));
        	lv.setAdapter(listAdapter);
    	}
    	else{
    		this.finish();
    	}
    }
	
    private void selectLanguage(){
    	if(!PresentationActivity.english){
    		main = getResources().getString(R.string.mainESP);
            categories = getResources().getStringArray(R.array.categoriesESP);
            attractions = getResources().getStringArray(R.array.touristAttractionsESP);
            interest = getResources().getStringArray(R.array.siteOfInterestESP);
            recreation = getResources().getStringArray(R.array.recreationESP);
            services = getResources().getStringArray(R.array.servicesESP);
    	}
    	else{
    		main = getResources().getString(R.string.mainING);
            categories = getResources().getStringArray(R.array.categoriesING);
            attractions = getResources().getStringArray(R.array.touristAttractionsING);
            interest = getResources().getStringArray(R.array.siteOfInterestING);
            recreation = getResources().getStringArray(R.array.recreationING);
            services = getResources().getStringArray(R.array.servicesING);
    	}
    }
    
    private String[] selectArray(String item){
    	if(depth == 1){
    		if(item.compareTo(categories[0]) == 0){
        	   	return attractions;
    		}
    		if(item.compareTo(categories[1]) == 0){
        	   	return interest;
    		}
    		if(item.compareTo(categories[2]) == 0){
        	   	return recreation;
    		}
    		if(item.compareTo(categories[3]) == 0){
        	   	return services;
    		}
    	}
    	else if(depth == 2){
    		if(isOnline()){
    			String[] aux = retrievePlaces(item);
        		return aux;
    		}
    		else{
    			String[] aux = new String[]{""};
        		return aux;
    		}
    	}
		return categories;
    }
    
    private String[] retrievePlaces(String item){
    	String[] aux = procesarConsulta(Services.getPlaces("getPlaces", "http://turismo/getPlaces", "subcategory", item));
    	return aux;
    }
	
	public String arrayListtoString(){
		String aux = "";
		for(int i = 0;i<itemes.size()-1;i++){
			aux = aux + itemes.get(i) + " - ";
		}
		return aux;
	}
	
	private String[] procesarConsulta(String consulta) {
		
		Gson gson = new Gson();
		ArrayList<String> arreglo = new ArrayList<String>();
        Type collectionType = new TypeToken<ArrayList<String>>(){}.getType();
        arreglo = gson.fromJson(consulta, collectionType);
        
        String[] stringarray; 
        stringarray=new String[arreglo.size()];
        arreglo.toArray(stringarray);
		
        return stringarray;
	}

	public void onClick(View v) {
		if(v.getId() == R.id.imageButton1){
			this.onBackPressed();
		}
		if(v.getId() == R.id.imageButton2){
			this.home();
		}
		if(v.getId()==R.id.cancelButton){
			exitDialog.dismiss();
		}
		if(v.getId()==R.id.acceptButton){
			this.onBackPressed();
			Intent settingsIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
			settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			CategoryActivity.this.startActivityForResult(settingsIntent, 0);
			exitDialog.dismiss();
		}
		if(v.getId() == R.id.acept){
			if(this.isOnline()){
	    		String keyword = String.valueOf(searchAutoComplete.getText());
	    		if(keyword.length() == 0){
	    			if(PresentationActivity.english){
	    				Toast.makeText(this, "No results for this search", Toast.LENGTH_LONG).show();
			        }
			        else{
			        	Toast.makeText(this, "No hay resultados para esta búsqueda", Toast.LENGTH_LONG).show();
			        }
	    		}
	    		else{
	    		String[] resultado = procesarConsulta(Services.getLocationsSearched("getLocationsSearched", "http://turismo/getLocationsSearched", "Name", keyword));
	    		if(resultado.length == 0){
	    			if(PresentationActivity.english){
	    				Toast.makeText(this, "No results for this search", Toast.LENGTH_LONG).show();
			        }
			        else{
			        	Toast.makeText(this, "No hay resultados para esta búsqueda", Toast.LENGTH_LONG).show();
			        }
	    		}
	    		else{
	    			final Dialog resultSearchView = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
		        	resultSearchView.setContentView(R.layout.listsearch);
		        	resultSearchView.setCancelable(true);
		        	ImageButton back = (ImageButton)resultSearchView.findViewById(R.id.sitesBack);
		        	back.setOnClickListener(new View.OnClickListener() {
			            public void onClick(View v) {
			            	resultSearchView.dismiss();
			            }});
		        	TextView titleSearch = (TextView) resultSearchView.findViewById(R.id.resultSearch);
		        	results = (ListView) resultSearchView.findViewById(R.id.searchList);
		        	ArrayAdapter<String> resultsAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultado);
    				results.setAdapter(resultsAdapter);
    				results.setOnItemClickListener(new OnItemClickListener(){

					public void onItemClick(AdapterView<?> l, View v, int position, long arg3) {
						// TODO Auto-generated method stub
						if(isOnline()){
				    		   Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
				    		   String place = (String) results.getAdapter().getItem(position);
				    		   CategoryActivity.PLACE = place;
				    		   startActivity(intent);
				    		   resultSearchView.dismiss();
				   	    	   searchView.dismiss();
				    	   }
					}});
			        if(PresentationActivity.english){
			        	titleSearch.setText("Results");
			        }
			        else{
			        	titleSearch.setText("Resultados");
			        }
			        resultSearchView.show();
	    			}
	    		}
	    	}
		}
		if(v.getId() == R.id.cancel){
			searchView.dismiss();
		}
	}
	
	public void home(){
		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
	    lv.setAdapter(listAdapter);
	    ib2.setVisibility(View.INVISIBLE);
	    ib.setVisibility(View.INVISIBLE);
	    if(depth == 2){
	    	itemes.remove(2);
	    	itemes.remove(1);
	    }
	    if(depth == 1){
	    	itemes.remove(1);
	    }
	    tv.setText(itemes.get(0));
	    stv.setVisibility(View.INVISIBLE);
	    depth = 0;
	}
	
	public boolean isOnline() {
		Context context = getApplicationContext();
	    ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    if (wifi.isConnected()) {
	        return true;
	    } else if (mobile.isConnected()) {
	        return true;
	    }
	    exitDialog = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
        exitDialog.setContentView(R.layout.exitdialog);
		if(PresentationActivity.english){
	        ImageButton accept = (ImageButton) exitDialog.findViewById(R.id.acceptButton);
	        accept.setImageResource(R.drawable.accept_button2);
	        ImageButton cancel = (ImageButton) exitDialog.findViewById(R.id.cancelButton);
	        cancel.setImageResource(R.drawable.cancel_button2);
	        
	        TextView exitText = (TextView) exitDialog.findViewById(R.id.exitText);
	        exitText.setText(R.string.exitDialogING);
	        accept.setOnClickListener(this);
	        cancel.setOnClickListener(this);
		}
		else{
			ImageButton accept = (ImageButton) exitDialog.findViewById(R.id.acceptButton);
	        accept.setImageResource(R.drawable.accept_button);
	        ImageButton cancel= (ImageButton) exitDialog.findViewById(R.id.cancelButton);
	        cancel.setImageResource(R.drawable.cancel_button);
	        TextView exitText = (TextView) exitDialog.findViewById(R.id.exitText);
	        exitText.setText(R.string.exitDialogESP);
	        accept.setOnClickListener(this);
	        cancel.setOnClickListener(this);
		}
		exitDialog.show(); 
	    return false;
	}

	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    menu.clear();
	    if(this.depth>=2){
		    inflater.inflate(R.layout.ctxmenu, menu);
		    if(PresentationActivity.english){
		        menu.getItem(0).setTitle("Search");
				menu.getItem(1).setTitle("Sort");
				menu.getItem(2).setTitle("Language");
				menu.getItem(3).setTitle("About");
		    }
		}
		else{
		    inflater.inflate(R.layout.ctxmenu3, menu);
		    if(PresentationActivity.english){
		        menu.getItem(0).setTitle("Search");
				menu.getItem(1).setTitle("Language");
				menu.getItem(2).setTitle("About");
		    }
		}
	    return true;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if(this.depth>=2){
		    inflater.inflate(R.layout.ctxmenu, menu);
		    if(PresentationActivity.english){
		        menu.getItem(0).setTitle("Search");
				menu.getItem(1).setTitle("Sort");
				menu.getItem(2).setTitle("Language");
				menu.getItem(3).setTitle("About");
		    }
		}
		else{
		    inflater.inflate(R.layout.ctxmenu3, menu);
		    if(PresentationActivity.english){
		        menu.getItem(0).setTitle("Search");
				menu.getItem(1).setTitle("Language");
				menu.getItem(2).setTitle("About");
		    }
		}
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case R.id.searchMenu:
		      	searchView = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
		        searchView.setContentView(R.layout.search);
		        searchView.setCancelable(true);
		        searchAutoComplete = (AutoCompleteTextView)searchView.findViewById(R.id.autoCompleteTextView);;
		        ImageButton aceptar = (ImageButton) searchView.findViewById(R.id.acept);
		        ImageButton cancelar = (ImageButton) searchView.findViewById(R.id.cancel);
		        
		        if(PresentationActivity.english){
		        	aceptar.setImageResource(R.drawable.accept_button2);
		        	cancelar.setImageResource(R.drawable.cancel_button2);
		        }
		        else{
		        	aceptar.setImageResource(R.drawable.accept_button);
		        	cancelar.setImageResource(R.drawable.cancel_button);
		        }
			    aceptar.setOnClickListener(this);
			    cancelar.setOnClickListener(this);  
			    searchView.show();
			    return true;
		    case R.id.sortMenu:
		       	if(ASC){
		       		String[] aux = procesarConsulta(Services.getPlaces("getPlacesSortAsc", "http://turismo/getPlaces", "name", itemes.get(itemes.size()-1)));
		       		listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aux);
		       		lv.setAdapter(listAdapter);
		       		ASC=false;
		       	}
		       	else{
		       		String[] aux = procesarConsulta(Services.getPlaces("getPlacesSortDsc", "http://turismo/getPlaces", "name", itemes.get(itemes.size()-1)));
		       		listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aux);
		       		lv.setAdapter(listAdapter);
		       		ASC=true;
		       	}
		       	return true;
		    case R.id.languageMenu:
		       	if(PresentationActivity.english){
	        		PresentationActivity.english = false;
	        	}
	        	else{
        			PresentationActivity.english = true;
        		}
        		refresh();
        		return true;
	        case R.id.aboutMenu:
	        	showAbout();
	        	return true;
	        case R.id.searchMenu3:
	        	searchView = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
		        searchView.setContentView(R.layout.search);
		        searchView.setCancelable(true);
		        searchAutoComplete = (AutoCompleteTextView)searchView.findViewById(R.id.autoCompleteTextView);;
		        aceptar = (ImageButton) searchView.findViewById(R.id.acept);
		        cancelar = (ImageButton) searchView.findViewById(R.id.cancel);
			    if(PresentationActivity.english){
			       	aceptar.setImageResource(R.drawable.accept_button2);
			       	cancelar.setImageResource(R.drawable.cancel_button2);
			    }
			    else{
			      	aceptar.setImageResource(R.drawable.accept_button);
			       	cancelar.setImageResource(R.drawable.cancel_button);
			    }
			    aceptar.setOnClickListener(this);
			    cancelar.setOnClickListener(this);
			    searchView.show();
		        return true;
		    case R.id.languageMenu3:
		    	if(PresentationActivity.english){
        			PresentationActivity.english = false;
        		}
        		else{
        			PresentationActivity.english = true;
        		}
        		refresh();
        		return true;
		    case R.id.aboutMenu3:
	        	showAbout();
			    return true;   
		    case R.id.topTenMenu3:
		    	showTop10();
		    	return true;
	        default:
	        	return super.onOptionsItemSelected(item);
		}
	}
	
	public void showAbout(){
		final Dialog commentView = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
		commentView.setContentView(R.layout.about);
		commentView.setCancelable(true);
	    TextView name = (TextView) commentView.findViewById(R.id.aboutName);
	    TextView text = (TextView) commentView.findViewById(R.id.aboutText);
	    if(PresentationActivity.english)
	    {
	    	name.setText("About");
			text.setText(R.string.abouting);
	    }
	    ImageButton back = (ImageButton)commentView.findViewById(R.id.aboutBack);
	    back.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
	           commentView.dismiss();
	        }});
	    commentView.show();
	}
	public void showTop10(){
		
		iProgress = new ProgressDialog(this);
		iProgress.setCancelable(false);
		iProgress.setMessage("Cargando Top 10");
		if(PresentationActivity.english){
			iProgress.setMessage("Loading Top 10");
		}
		iProgress.show();
    	
    	new Thread() {
			@Override
            public void run() {
                int what = 0;
                
                try{
                	 topTens = procesarConsulta(Services.getTopTen("getRaitingSort",  "http://turismo/getPlaces"));
                	
                }
                catch(Exception e){
                	what = 1;
                }
                tHandler.sendMessage(cHandler.obtainMessage(what));
            }
        }.start();
    
		
		
		
		
	   
	
	}
	
	
	 private Handler tHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            iProgress.dismiss();
	 
	            if (msg.what == 0&&topTens.length>0) {
	            	final Dialog routDialog = new Dialog(CategoryActivity.this, R.style.FullHeightDialog);
	        		routDialog.setContentView(R.layout.showroute);
	        		ImageButton back = (ImageButton)routDialog.findViewById(R.id.routBack);
	        		TextView title = (TextView)routDialog.findViewById(R.id.routName);	        
	                title.setText("Top 10"); 
	        	    back.setOnClickListener(new View.OnClickListener() {
	        	    public void onClick(View v) {
	        	           routDialog.dismiss();
	        	        }});
	        		final ListView list = (ListView) routDialog.findViewById(R.id.listaRuta);
	        		
	        		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(routDialog.getContext(), android.R.layout.simple_list_item_1, topTens);
	        	
	        	    list.setAdapter(listAdapter);
	        	    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	        					long arg3) {

	        				if(isOnline()){
	        		        	final Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
	        		        	String place = list.getItemAtPosition(arg2).toString();
	        		        	CategoryActivity.PLACE = place;
	        			        routDialog.dismiss();
	        		        	startActivity(intent);
	        	        	}
	        			}});
	        		routDialog.setCancelable(true);
	        		routDialog.show();
    
	            }
	            else if(msg.what == 0 &&topTens.length==0)
	            {

	           	 if(PresentationActivity.english){
	           		
	                	Toast.makeText(CategoryActivity.this, "Top 10 not available", Toast.LENGTH_SHORT).show();
	                }
	                else{
	                	Toast.makeText(CategoryActivity.this, "Top 10 no disponibles", Toast.LENGTH_SHORT).show();
	                }
	            }
	            else{
	            	if(PresentationActivity.english){
	                	Toast.makeText(CategoryActivity.this, "Unsuccessfully loading", Toast.LENGTH_SHORT).show();
	                }
	                else{
	                	Toast.makeText(CategoryActivity.this, "Cargado sin éxito", Toast.LENGTH_SHORT).show();
	            	
	            	finish();
	                }
	            }
	        }
	    };
	
	public void refresh(){
		Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
		this.finish();
		startActivity(intent);
	}
}
