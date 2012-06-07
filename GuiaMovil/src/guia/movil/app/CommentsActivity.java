package guia.movil.app;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class CommentsActivity extends Activity implements OnClickListener {
	protected static String nick;
	protected static String comment;
	
	private TextView title;
	private ListView commentaries;
	private View tempView;
	private CommentResume[] datos= new CommentResume[]{new CommentResume("Juan", "Buen lugar"),
			new CommentResume("Pedro", "Mal lugar"),new CommentResume("Peter", "Nice place"),
			new CommentResume("James", "Is not bad"),new CommentResume("Juan", "Buen lugar"),
			new CommentResume("Pedro", "Mal lugar"),new CommentResume("Peter", "Nice place"),
			new CommentResume("James", "Is not bad"),new CommentResume("Juan", "Buen lugar"),
			new CommentResume("Pedro", "Mal lugar"),new CommentResume("Peter", "Nice place"),
			new CommentResume("James", "Is not bad")};
	private ImageButton comentar;
	protected static String placeID;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.comments);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        comentar= (ImageButton)findViewById(R.id.comment);
        title = (TextView)findViewById(R.id.comentaryTitle);
        title.setText("Comentarios");       
       
        
        commentaries = (ListView) findViewById(R.id.commentaryList);
        String methodname = "getComment";
        String soap = "http://turismo/" + methodname;
        String methodnameID = "getPlaceID";
        String soapID = "http://turismo/" + methodnameID;
        
        placeID = Services.getPlaceID(methodnameID, soapID, "place", CategoryActivity.PLACE); 
        
        String consulta =Services.getComments(methodname, soap, "placeID", placeID);
        procesarConsulta(consulta);
 
        AdaptadorTitulares adaptador = 
            	new AdaptadorTitulares(this);

        commentaries.setAdapter(adaptador);
        commentaries.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				nick=datos[arg2].getNick();
				comment=datos[arg2].getComment();
				Intent intent = new Intent(CommentsActivity.this,CommentViewActivity.class);
				
				
				CommentsActivity.this.startActivity(intent);
				
			}
		});
        
        
        comentar.setOnClickListener(this);
        
        // ToDo add your GUI initialization code here        
    }

	private void procesarConsulta(String consulta) {
		
		Gson gson = new Gson();
		HashMap<String,String> arreglo = new HashMap<String, String>();
        Type collectionType = new TypeToken<HashMap<String,String> >(){}.getType();
        arreglo = gson.fromJson(consulta, collectionType);
        
        ArrayList<CommentResume> temp= new  ArrayList<CommentResume>();
        
        Iterator iter = arreglo.entrySet().iterator();
        
        Map.Entry e;

        
        while (iter.hasNext()) {
        	e = (Map.Entry)iter.next();
        	temp.add(new CommentResume(e.getKey().toString(), e.getValue().toString()));
        	
        }
        
        datos=new CommentResume[temp.size()];
        temp.toArray(datos);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		
		Intent intent = new Intent(CommentsActivity.this,CommentWriteActivity.class);
		finish();
		this.startActivity(intent);
		
	}
	
	
	
	class AdaptadorTitulares extends ArrayAdapter<CommentResume>{
		 
	    Activity context;
		
	 
	        AdaptadorTitulares(Activity context) {
	            super(context, R.layout.commentlistitem, datos);
	            this.context = context;
	        }
	        
	 
	        public View getView(int position, View convertView, ViewGroup parent) {
	        	
	        	
	        	LayoutInflater inflater = context.getLayoutInflater();
	        	View item = inflater.inflate(R.layout.commentlistitem, null);
	 
	        	TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
	        	lblTitulo.setText(datos[position].getNick());
	 
	        	TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
	        	if(datos[position].getComment().length()>20)
	        		lblSubtitulo.setText(datos[position].getComment().substring(0,20)+"...");
	        	else
	        		lblSubtitulo.setText(datos[position].getComment());
	        	item.setTag(position);
	        	
	        	
	        	
	        	

	        	
	 
	        	return(item);
	    }

			


		
			
			
	}




	}
	
	
	
	
