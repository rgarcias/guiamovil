package guia.movil.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CommentViewActivity extends Activity  {
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.commentview);
        TextView nick = (TextView) this.findViewById(R.id.nickName);
        TextView comment = (TextView) this.findViewById(R.id.commentSpace);
        nick.setText(CommentsActivity.nick);
        comment.setText(CommentsActivity.comment);
        
	}
	
	
}
