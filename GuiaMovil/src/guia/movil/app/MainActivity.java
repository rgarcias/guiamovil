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

        spec.setIndicator(iv);
        tabs.addTab(spec);
         
        spec=tabs.newTabSpec("tabMapa");
        Intent intent2 = new Intent().setClass(this, MapsActivity.class);
        spec.setContent(intent2);
        
        spec.setIndicator(iv2);
        tabs.addTab(spec);
         
        tabs.setCurrentTab(0);
    }
}
