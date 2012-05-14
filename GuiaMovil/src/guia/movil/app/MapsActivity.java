package guia.movil.app;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapsActivity extends MapActivity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        
        AutoCompleteTextView search = (AutoCompleteTextView) this.findViewById(R.id.autoCompleteTextMaps);
        search.requestFocus();
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.blue_dot);
        PointersActivity itemizedoverlay = new PointersActivity(drawable, this);
        
        GeoPoint point = new GeoPoint(-34983330,-71233330);
        OverlayItem overlayitem = new OverlayItem(point, "Hola mundo!", "I'm in Curicó City!");
        
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
