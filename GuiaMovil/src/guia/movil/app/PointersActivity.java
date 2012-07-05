package guia.movil.app;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class PointersActivity extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> pointers = new ArrayList<OverlayItem>();
	Context mContext;

	public PointersActivity(Drawable icon_dot, Context context) {
		super(boundCenterBottom(icon_dot));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return pointers.get(i);
	}

	@Override
	public int size() {
		return pointers.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    pointers.add(overlay);
	    populate();
	}
	
	protected boolean onTap(int index){
		OverlayItem item = pointers.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
}
