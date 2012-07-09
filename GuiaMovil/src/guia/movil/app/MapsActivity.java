package guia.movil.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapsActivity extends MapActivity implements OnClickListener{
	MapView mapView;
    private Road mRoad;
    GeoPoint point;
    Location userLocation =null ;
    LocationManager mlocManager;
    String descripcion ="";
	private String latitude;
	private String longitude;
	private Dialog exitDialog;
	String[] arreglo = {""};
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.blue_dot);
        PointersActivity itemizedoverlay = new PointersActivity(drawable, this);
        
        if(isOnline()){
        	
        	String methodname = "getLatitude";
            String soap = "http://turismo/" + methodname;
            latitude = Services.getLatitude(methodname, soap, "place", CategoryActivity.PLACE);
            
            String methodname2 = "getLongitude";
            String soap2 = "http://turismo/" + methodname;
            longitude = Services.getLongitude(methodname2, soap2, "place", CategoryActivity.PLACE);
            
            point = new GeoPoint(Integer.parseInt(latitude),Integer.parseInt(longitude));
            
            MapController control = mapView.getController();
            control.setCenter(point);
            control.setZoom(13);
            OverlayItem overlayitem = new OverlayItem(point, CategoryActivity.PLACE,Integer.parseInt(latitude)/1e6 +" "+Integer.parseInt(longitude)/1e6);
            
            itemizedoverlay.addOverlay(overlayitem);
            mapOverlays.add(itemizedoverlay);
        }
	}
	
	public void showDriveIndicationRoute(final double lat2,final double lon2){
		cProgress = new ProgressDialog(this);
		if(PresentationActivity.english)
			cProgress.setMessage("Searching route...");
		else
			cProgress.setMessage("Buscando ruta...");

		cProgress.show();
		cProgress.setCancelable(false);	
	
		new Thread() {
            @Override
            public void run() {
				 double fromLat = userLocation.getLatitude(), fromLon = userLocation.getLongitude(), toLat = lat2, toLon = lon2;
		         mlocManager.removeUpdates(locListener);
		         String url = RoadProvider.getUrlR(fromLat, fromLon, toLat, toLon);
		         Log.d("url",url);
		         InputStream is = getConnection(url);
		         mRoad = RoadProvider.getRoute(is);
		         cHandler.sendEmptyMessage(0);
            } 
        }.start();
	}
	
	private Handler cHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            cProgress.dismiss();
            arreglo= new String[mRoad.getmPoints().length];
    		ArrayList<String> arreglin = new ArrayList<String>();
    		for (int i=0; (mRoad.getmPoints().length)>i; i++ ){
    			if(mRoad.getmPoints()[i].getmDescription()!=null&&mRoad.getmPoints()[i].getmName()!=null)
    				arreglin.add(mRoad.getmPoints()[i].getmName()+"\n"+mRoad.getmPoints()[i].getmDescription());
    			else if (mRoad.getmPoints()[i].getmName()!=null)
    				arreglin.add(mRoad.getmPoints()[i].getmName());	
    		}	
    		arreglo=new String[arreglin.size()];
    		arreglin.toArray(arreglo);
            final Dialog routDialog = new Dialog(MapsActivity.this, R.style.FullHeightDialog);
    		routDialog.setContentView(R.layout.showroute);
    		ImageButton back = (ImageButton)routDialog.findViewById(R.id.routBack);
    		TextView title = (TextView)routDialog.findViewById(R.id.routName);	        
    	    title.setText("Cómo llegar"); 
    	    if(PresentationActivity.english)
    	       	title.setText("Getting to"); 

    		back.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    		    routDialog.dismiss();
    		}});
    		ListView list = (ListView) routDialog.findViewById(R.id.listaRuta);
    			
    		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(routDialog.getContext(), android.R.layout.simple_list_item_1, arreglo);
    		
    		list.setAdapter(listAdapter);
    		routDialog.setCancelable(true);
    			
    		if (mRoad.getmName()!=null)
    			routDialog.show();
    		else{
    			if(PresentationActivity.english)
         			Toast.makeText(getApplicationContext(), "This place has not route", Toast.LENGTH_SHORT).show();
         		else
         			Toast.makeText(getApplicationContext(), "Este lugar no posee ruta", Toast.LENGTH_SHORT).show();
    		}
            return;
            }
    };

	LocationListener locListener = new LocationListener() {
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
		public void onProviderEnabled(String provider) {
		}
		
		public void onProviderDisabled(String provider) {
		}
		
		public void onLocationChanged(Location location) {
		}
	};
	private ProgressDialog cProgress;
	public void localizar(){
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        userLocation =mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(userLocation==null){
        	userLocation =mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        	mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
        }
        else{
        	mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }
	}

	public void traceRoad(final double lat2, final double lon2){
		cProgress = new ProgressDialog(this);
		if(PresentationActivity.english)
			cProgress.setMessage("Tracing route...");
		else
			cProgress.setMessage("Trazando ruta...");

		cProgress.show();
		cProgress.setCancelable(false);
		new Thread() {
            @Override
            public void run() {
                double fromLat = userLocation.getLatitude(), fromLon = userLocation.getLongitude(), toLat = lat2, toLon = lon2;
                mlocManager.removeUpdates(locListener);
                String url = RoadProvider.getUrl(fromLat, fromLon, toLat, toLon);
                Log.d("url",url);
                InputStream is = getConnection(url);
                mRoad = RoadProvider.getRoute(is);
                mHandler.sendEmptyMessage(0);
            }
    }.start();
	}
	
	Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	if (mRoad.mName==null){
        		if(PresentationActivity.english)
        			Toast.makeText(getApplicationContext(), "Route not found", Toast.LENGTH_SHORT).show();
        		else
        			Toast.makeText(getApplicationContext(), "No hay ruta ", Toast.LENGTH_SHORT).show();
            }
        	else{
                MapOverlay mapOverlay = new MapOverlay(mRoad, mapView);
                List<Overlay> listOfOverlays = mapView.getOverlays();
                listOfOverlays.clear();
                listOfOverlays.add(mapOverlay);
                Drawable drawable = MapsActivity.this.getResources().getDrawable(R.drawable.blue_dot);
                PointersActivity itemizedoverlay = new PointersActivity(drawable, MapsActivity.this);
                OverlayItem overlayitem = new OverlayItem(point, CategoryActivity.PLACE,Integer.parseInt(latitude)/1e6 +" "+Integer.parseInt(longitude)/1e6);         
                itemizedoverlay.addOverlay(overlayitem);
                listOfOverlays.add(itemizedoverlay);
                int latitude=(int) (mRoad.getmPoints()[0].getmLatitude()*1e6);
                int longitude =(int) (mRoad.getmPoints()[0].getmLongitude()*1e6);
                GeoPoint point2 = new GeoPoint(latitude,longitude);
                if(PresentationActivity.english)
                	overlayitem = new OverlayItem(point2, "Start point", mRoad.getmPoints()[0].getmLatitude() +" "+mRoad.getmPoints()[0].getmLongitude());
                else
                	overlayitem = new OverlayItem(point2, "Punto de inicio", mRoad.getmPoints()[0].getmLatitude() +" "+mRoad.getmPoints()[0].getmLongitude());
                itemizedoverlay.addOverlay(overlayitem);
                listOfOverlays.add(itemizedoverlay);
                mapView.invalidate();
        	}
        	cProgress.dismiss();
        };
	};

	private InputStream getConnection(String url) {
        InputStream is = null;
        try {
                URLConnection conn = new URL(url).openConnection();
                is = conn.getInputStream();
        } catch (MalformedURLException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return is;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
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
	    exitDialog = new Dialog(MapsActivity.this, R.style.FullHeightDialog);
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

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.cancelButton){
			exitDialog.dismiss();
		}
		if(v.getId()==R.id.acceptButton){
			Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
			settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			MapsActivity.this.startActivityForResult(settingsIntent, 0);
			exitDialog.dismiss();
		}
	}
	
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	          MenuInflater inflater = getMenuInflater();
	          inflater.inflate(R.layout.mapsmenu, menu);
	          if(PresentationActivity.english){
  				menu.getItem(1).setTitle("Getting there");
  				menu.getItem(2).setTitle("Description");
  				menu.getItem(0).setTitle("Trace route");
	          }
	          return true;
	      }
	public boolean haveRouteConnector(){
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&!mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			final Dialog rankDialog = new Dialog(MapsActivity.this, R.style.FullHeightDialog);
	        rankDialog.setContentView(R.layout.connectdialog);
	        rankDialog.setCancelable(true);
	        ImageButton aceptar = (ImageButton) rankDialog.findViewById(R.id.connectAcept);
	        ImageButton cancelar = (ImageButton) rankDialog.findViewById(R.id.conectCancel);
	        if(PresentationActivity.english){
	        	aceptar.setImageResource(R.drawable.accept_button2);
	        	cancelar.setImageResource(R.drawable.cancel_button2);
	        }
	        else{
	        	aceptar.setImageResource(R.drawable.accept_button);
	        	cancelar.setImageResource(R.drawable.cancel_button);
	        }
	        aceptar.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    			settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	    			MapsActivity.this.startActivityForResult(settingsIntent, 0);
					 rankDialog.dismiss();
	            }
	        });
	        cancelar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					 rankDialog.dismiss();
				}
			});
	        rankDialog.show();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   switch (item.getItemId()) {
	        case R.id.btLlegar:
	        	if(haveRouteConnector()){
        			localizar();
        			double lat2 =  point.getLatitudeE6() / 1e6;
        			double lon2 = point.getLongitudeE6() / 1e6;
        			try{
        			showDriveIndicationRoute(lat2,lon2);
        			}
        			catch (Exception e){
        			}
        			
        		}
	           return true;
	        case R.id.btTrazar:
	        		if(haveRouteConnector()){
	        			localizar();
	        			double latt2 =  point.getLatitudeE6() / 1e6;
	        			double lont2 = point.getLongitudeE6() / 1e6;
	        			try{
	        				traceRoad(latt2,lont2);
	        			}
	        			catch (Exception e){
	        			}
	        		}
	           return true;
	        case R.id.btDescripcion:
	        	if(haveRouteConnector()){
	        		localizar();
	        		double latt2 =  point.getLatitudeE6() / 1e6;
        			double lont2 = point.getLongitudeE6() / 1e6;
		        	try{
		        		getDescription(latt2,lont2);
		        	}
		        	catch (Exception e){
		        		if(PresentationActivity.english)
	        				Toast.makeText(getApplicationContext(), "First trace the route or see indications", Toast.LENGTH_SHORT).show();
	        			else
	        				Toast.makeText(getApplicationContext(), "Primero trazar la ruta o ver indicaciones", Toast.LENGTH_SHORT).show();
		        	}
        		}
	           return true;
	        default:
	           return super.onOptionsItemSelected(item);
	   }
	}

	private void getDescription(final double lat2, final double lon2) {
		cProgress = new ProgressDialog(this);
		if(PresentationActivity.english)
			cProgress.setMessage("Getting description...");
		else
			cProgress.setMessage("Obteniendo descripción...");
		cProgress.show();
		cProgress.setCancelable(false);
		new Thread() {
	        @Override
	        public void run() {
	        	double fromLat = userLocation.getLatitude(), fromLon = userLocation.getLongitude(), toLat = lat2, toLon = lon2;
		        mlocManager.removeUpdates(locListener);
		        String url = RoadProvider.getUrlR(fromLat, fromLon, toLat, toLon);
		        Log.d("url",url);
		        InputStream is = getConnection(url);
		        mRoad = RoadProvider.getRoute(is);
			    zHandler.sendEmptyMessage(0);
	        	}
	     }.start();    
	}

	Handler zHandler = new Handler() {
	    public void handleMessage(android.os.Message msg) {
	        if (mRoad.getmName()!=null){
	        	final Dialog commentView = new Dialog(MapsActivity.this, R.style.FullHeightDialog);
		    	commentView.setContentView(R.layout.commentview);
		    	commentView.setCancelable(true);
		        TextView nick = (TextView) commentView.findViewById(R.id.nickName);
		        TextView comment = (TextView) commentView.findViewById(R.id.commentSpace);
		        if(PresentationActivity.english){
		        	nick.setText("Description");
		        	comment.setText(mRoad.mName + " " + mRoad.mDescription);
		        }
		        else{
		        	nick.setText("Descripción");
		        	comment.setText(mRoad.getmPoints()[mRoad.getmPoints().length-1].getmName()+" "+mRoad.getmName() +" "+ 
		        	mRoad.getmPoints()[mRoad.getmPoints().length-1].getmDescription());
		        }
		        ImageButton back = (ImageButton)commentView.findViewById(R.id.commentBack);
		        back.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		               commentView.dismiss();
		            }
		        });
		        commentView.show();
	        }	
	        else{
	        	if(PresentationActivity.english)
	    			Toast.makeText(getApplicationContext(), "This place has not description", Toast.LENGTH_SHORT).show();
	    		else
	    			Toast.makeText(getApplicationContext(), "Este lugar no posee descripción", Toast.LENGTH_SHORT).show();
	        }
	    	cProgress.dismiss(); 
	    }
	};

class MapOverlay extends com.google.android.maps.Overlay {
    Road mRoad;
    ArrayList<GeoPoint> mPoints;

    public MapOverlay(Road road, MapView mv) {
        mRoad = road;
        if (road.mRoute.length > 0) {
           mPoints = new ArrayList<GeoPoint>();
           for (int i = 0; i < road.mRoute.length; i++) {
               mPoints.add(new GeoPoint((int) (road.mRoute[i][1] * 1000000), (int) (road.mRoute[i][0] * 1000000)));
           }
           int moveToLat = (mPoints.get(0).getLatitudeE6() + (mPoints.get(mPoints.size() - 1).getLatitudeE6() - mPoints.get(0).getLatitudeE6()) / 2);
           int moveToLong = (mPoints.get(0).getLongitudeE6() + (mPoints.get(mPoints.size() - 1).getLongitudeE6() - mPoints.get(0).getLongitudeE6()) / 2);
           GeoPoint moveTo = new GeoPoint(mPoints.get(0).getLatitudeE6(), mPoints.get(0).getLongitudeE6());
           MapController mapController = mv.getController();
           mapController.animateTo(moveTo);
           mapController.setZoom(13); 
       }
    }

    @Override
    public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
        super.draw(canvas, mv, shadow);
        try{
          	drawPath(mv, canvas);
        }
        catch (Exception e){
          	return false;
        }
        return true;
    }

    public void drawPath(MapView mv, Canvas canvas) throws Exception  {
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

	    for (int i = 0; i < mPoints.size(); i++) {
	        Point point = new Point();
	        mv.getProjection().toPixels(mPoints.get(i), point);
	        x2 = point.x;
	        y2 = point.y;
	        if (i > 0) {
	        	canvas.drawLine(x1, y1, x2, y2, paint);
	        }
	        x1 = x2;
	        y1 = y2;
	    }
    }
}
}
