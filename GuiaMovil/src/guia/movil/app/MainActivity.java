package guia.movil.app;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

public class MainActivity extends TabActivity {
	private TabHost tabs;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
        
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_tab_list);
        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.ic_tab_map);
        Intent intent = new Intent().setClass(this, InformationActivity.class);
        TabHost.TabSpec spec=tabs.newTabSpec("tabInfo");
        spec.setContent(intent);

        TabHost.TabSpec spec2=tabs.newTabSpec("tabMapa");
      
        Intent intent2 = new Intent().setClass(this, MapsActivity.class);
        spec2.setContent(intent2);

        if(PresentationActivity.english){
        	spec.setIndicator("Information", getResources().getDrawable(R.drawable.ic_tab_list));
        	spec2.setIndicator("Map", getResources().getDrawable(R.drawable.ic_tab_map));
        }
        else{
        	spec.setIndicator("Información", getResources().getDrawable(R.drawable.ic_tab_list));
        	spec2.setIndicator("Mapa", getResources().getDrawable(R.drawable.ic_tab_map));
        }
        
        tabs.addTab(spec);
        tabs.addTab(spec2);
        tabs.setCurrentTab(0); 
        this.initTabsAppearance(tabs.getTabWidget());
    }
	
	private void initTabsAppearance(TabWidget tab_widget) 
	{
	    // Change background
	    for(int i=0; i<tab_widget.getChildCount(); i++)
	        tab_widget.getChildAt(i).setBackgroundResource(R.drawable.tab_selector);
	}
}
