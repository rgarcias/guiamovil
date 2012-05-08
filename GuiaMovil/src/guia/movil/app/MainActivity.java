package guia.movil.app;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        //Resources res = getResources();
        
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
         
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_tab_categories);
        
        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.ic_tab_location);
        Intent intent = new Intent().setClass(this, CategoryActivity.class);

        TabHost.TabSpec spec=tabs.newTabSpec("tabInfo");
        spec.setContent(intent);

        spec.setIndicator(iv);
        tabs.addTab(spec);
         
        spec=tabs.newTabSpec("tabMapa");
        spec.setContent(R.id.tab2);

        spec.setIndicator(iv2);
        tabs.addTab(spec);
         
        tabs.setCurrentTab(0);
    }
}
