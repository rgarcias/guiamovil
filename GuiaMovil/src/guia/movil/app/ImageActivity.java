package guia.movil.app;

import guia.movil.app.InformationActivity.ImageItem;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageActivity extends Activity {
	private static ImageItem item;

	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.image);
        
        ImageView image = (ImageView) this.findViewById(R.id.image);
        image.setImageBitmap(item.getImage());
    }

	public static void setImageItem(ImageItem imageItem) {
		// TODO Auto-generated method stub
		item = imageItem;
	}
}
