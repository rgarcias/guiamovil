package guia.movil.app;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GuiaMovilActivity extends ListActivity {
    /** Called when the activity is first created. */
	 private ArrayAdapter<String> adapter;
	 private boolean english = true;
	 private String[] categories;
	 private String[] attractions;
	 private String[] interest;
	 private String[] recreation;
	 private String[] services;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        selectLanguage();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        setListAdapter(adapter);
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);

		if(item.compareTo(categories[0]) == 0){
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, attractions);
			setListAdapter(adapter);
		}
		if(item.compareTo(categories[1]) == 0){
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, interest);
			setListAdapter(adapter);
		}
		if(item.compareTo(categories[2]) == 0){
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recreation);
			setListAdapter(adapter);
		}
		if(item.compareTo(categories[3]) == 0){
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, services);
			setListAdapter(adapter);
		}
	}
    
    private void selectLanguage(){
    	if(!english){
    		categories = getResources().getStringArray(R.array.categoriesESP);
    		attractions = getResources().getStringArray(R.array.touristAttractionsESP);
    		interest = getResources().getStringArray(R.array.siteOfInterestESP);
    		recreation = getResources().getStringArray(R.array.recreationESP);
    		services = getResources().getStringArray(R.array.servicesESP);
    	}
    	else{
    		categories = getResources().getStringArray(R.array.categoriesING);
    		attractions = getResources().getStringArray(R.array.touristAttractionsING);
    		interest = getResources().getStringArray(R.array.siteOfInterestING);
    		recreation = getResources().getStringArray(R.array.recreationING);
    		services = getResources().getStringArray(R.array.servicesING);
    	}
    }
}