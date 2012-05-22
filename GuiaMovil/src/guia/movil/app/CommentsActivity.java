package guia.movil.app;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class CommentsActivity extends Activity implements OnClickListener, OnItemClickListener {
	
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
	private Button comentar;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.comments);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        comentar= (Button)findViewById(R.id.commentarySubmit);
        comentar.setText("Comentar");
        title = (TextView)findViewById(R.id.comentaryTitle);
        title.setText("Comentarios");       
       
        
        commentaries = (ListView) findViewById(R.id.commentaryList);
 
        AdaptadorTitulares adaptador = 
            	new AdaptadorTitulares(this);

        commentaries.setAdapter(adaptador);
        
       
        
        
        comentar.setOnClickListener(this);
        
        // ToDo add your GUI initialization code here        
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		if(tempView != null){
	        //If row is already clicked then reset its color to default row color
	        tempView.setBackgroundColor(Color.TRANSPARENT);

	    }
		Intent intent = new Intent(CommentsActivity.this,CommentWriteActivity.class);
		finish();
		this.startActivity(intent);
		
	}
	
	
	
	class AdaptadorTitulares extends ArrayAdapter<CommentResume> implements OnClickListener, OnItemClickListener{
		 
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
	        	lblSubtitulo.setText(datos[position].getComment());
	        	item.setTag(position);
	        	item.setOnClickListener(this);
	        	commentaries.setOnItemClickListener(this);
	        	
	        	

	        	
	 
	        	return(item);
	    }

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(CommentsActivity.this,CommentViewActivity.class);
				
				CommentsActivity.this.startActivity(intent);
				
				
				 
				
			}


			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				
				v.setFocusable(true);
				v.setSelected(true);
				v.setBackgroundColor(Color.YELLOW);
				if(tempView != null){
			        //If row is already clicked then reset its color to default row color
			        tempView.setBackgroundColor(Color.TRANSPARENT);

			    }
			    tempView = v;
				
				
			}
			
			
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		v.setFocusable(true);
		v.setSelected(true);
		v.setBackgroundColor(Color.YELLOW);
		if(tempView != null){
	        //If row is already clicked then reset its color to default row color
	        tempView.setBackgroundColor(Color.TRANSPARENT);

	    }
	    tempView = v;
		
		
	}
	}
	
	
	
	
