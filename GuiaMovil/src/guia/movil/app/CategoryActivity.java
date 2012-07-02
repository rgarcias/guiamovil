package guia.movil.app;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends ListActivity implements OnClickListener {
	protected static final int DIALOG_BACK_ID = 0;
	protected static String PLACE = "";
    /* Variables*/
    private int depth;
    private ArrayList<String> itemes;
    
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
    private String[] dialogItems;
    private Dialog exitDialog;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState); 
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.category);
       Bundle bundle = getIntent().getExtras();
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(depth >= 2){
        	if(isOnline()){
	        	Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
	        	String place = (String) lv.getAdapter().getItem(position);
	        	this.PLACE = place;
	        	this.startActivity(intent);
        	}
        }
        else{
        	this.depth += 1;
        	if(!(itemes.get(itemes.size()-1).compareTo((String) lv.getAdapter().getItem(position))==0)){
        		itemes.add((String) lv.getAdapter().getItem(position));
        	}
        	ib.setVisibility(View.VISIBLE);
        	ib2.setVisibility(View.VISIBLE);
        	stv.setText(this.arrayListtoString());
        	stv.setVisibility(View.VISIBLE);
        	tv.setText(itemes.get(itemes.size()-1));
            String[] aux = selectArray(itemes.get(itemes.size()-1));
            listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aux);
        	lv.setAdapter(listAdapter);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.imageButton1){
			this.onBackPressed();
		}
		
		if(v.getId() == R.id.imageButton2){
			this.home();
		}
		if(v.getId() == R.id.exitButton){
			this.onBackPressed();
			exitDialog.dismiss();
			Intent temp = new Intent(Intent.ACTION_MAIN);
			temp.addCategory(Intent.CATEGORY_HOME);
			startActivity(temp);
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
        exitDialog.setCancelable(false);
		if(PresentationActivity.english){
	        ImageButton exit = (ImageButton) exitDialog.findViewById(R.id.exitButton);
	        exit.setImageResource(R.drawable.quit_button2);
	        
	        TextView exitText = (TextView) exitDialog.findViewById(R.id.exitText);
	        exitText.setText(R.string.exitDialogING);
	        exit.setOnClickListener(this);
		}
		else{
			ImageButton exit = (ImageButton) exitDialog.findViewById(R.id.exitButton);
	        exit.setImageResource(R.drawable.quit_button);
	        
	        TextView exitText = (TextView) exitDialog.findViewById(R.id.exitText);
	        exitText.setText(R.string.exitDialogESP);
	        exit.setOnClickListener(this);
		}
		exitDialog.show(); 
	    return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.ctxmenu, menu);
	    if(PresentationActivity.english){
	        menu.getItem(0).setTitle("Search");
			menu.getItem(1).setTitle("Sort");
			menu.getItem(2).setTitle("Language");
			menu.getItem(3).setTitle("About");
	    }
	    return true;
	}
}
