package guia.movil.app;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryActivity extends ListActivity implements OnClickListener, android.content.DialogInterface.OnClickListener {
	protected static final int DIALOG_BACK_ID = 0;
	protected static String PLACE = "";
	protected static boolean english = false;
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
    private ArrayAdapter<String> listAdapter;
    private String[] dialogItems;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState); 
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.category);
       Bundle bundle = getIntent().getExtras();
       CategoryActivity.english = bundle.getBoolean("english");
       selectLanguage();
       
       itemes = new ArrayList<String>();
       itemes.add(main);
       tv = (TextView) this.findViewById(R.id.textView1);
       tv.setText(itemes.get(itemes.size()-1));
       
       ib = (ImageButton) this.findViewById(R.id.imageButton1);
       ib.setOnClickListener(this);
       ib.setVisibility(View.INVISIBLE);
       
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
        	Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
        	String place = (String) lv.getAdapter().getItem(position);
        	this.PLACE = place;
        	this.startActivity(intent);
        }
        else{
        	this.depth += 1;
        	if(!(itemes.get(itemes.size()-1).compareTo((String) lv.getAdapter().getItem(position))==0)){
        		itemes.add((String) lv.getAdapter().getItem(position));
        	}
        	ib.setVisibility(View.VISIBLE);
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
    	if(!CategoryActivity.english){
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
    		String[] aux = retrievePlaces(item);
    		return aux;
    	}
		return categories;
    }
    
    private String[] retrievePlaces(String item){
    	String[] aux = new String[]{"Radal Siete Tazas", "Iloca"};
    	
    	return aux;
    }

	@Override
	public void onClick(View arg0) {
		if(itemes.size() > 2){
			this.onCreateDialog(DIALOG_BACK_ID);
			this.showDialog(DIALOG_BACK_ID);
		}
		else{
			this.onBackPressed();
		}
	}
	
	public Dialog onCreateDialog(int integer){
		dialogItems = itemes.toArray(new String[itemes.size()]);
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
	    builder.setItems(dialogItems, this);
	      
	    return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(dialogItems[which].compareTo(itemes.get(0)) == 0){
			itemes.remove(2);
			itemes.remove(1);
			depth = 0;
			listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
    		ib.setVisibility(View.INVISIBLE);
    		stv.setVisibility(View.INVISIBLE);
		}
		else if(dialogItems[which].compareTo(itemes.get(1)) == 0){
			depth = 1;
			itemes.remove(2);
			ib.setVisibility(View.VISIBLE);
			stv.setText(this.arrayListtoString());
			stv.setVisibility(View.VISIBLE);
			listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectArray(dialogItems[which]));
		}
		else{
			depth = 2;
			ib.setVisibility(View.VISIBLE);
			stv.setText(this.arrayListtoString());
			stv.setVisibility(View.VISIBLE);
			listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, retrievePlaces(dialogItems[which]));
		}
		lv.setAdapter(listAdapter);
		tv.setText(itemes.get(itemes.size()-1));
		this.removeDialog(DIALOG_BACK_ID);
	}
	
	public String arrayListtoString(){
		String aux = "";
		for(int i = 0;i<itemes.size()-1;i++){
			aux = aux + itemes.get(i) + " - ";
		}
		return aux;
	}
}
