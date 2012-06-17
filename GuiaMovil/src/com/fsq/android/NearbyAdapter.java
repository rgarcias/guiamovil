package com.fsq.android;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NearbyAdapter extends BaseAdapter {
	private ArrayList<FsqVenue> mVenueList;
	private LayoutInflater mInflater;

	public NearbyAdapter(Context c) {
        mInflater 			= LayoutInflater.from(c);
    }

	public void setData(ArrayList<FsqVenue> poolList) {
		mVenueList = poolList;
	}
	
	public int getCount() {
		return mVenueList.size();
	}

	public Object getItem(int position) {
		return mVenueList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView	=  mInflater.inflate(guia.movil.app.R.layout.nearby_list, null);
			
			holder = new ViewHolder();
			
			holder.mNameTxt 		= (TextView) convertView.findViewById(guia.movil.app.R.id.tv_name);;
			holder.mHereNowTxt 		= (TextView) convertView.findViewById(guia.movil.app.R.id.tv_here_now);
			holder.mDistanceTxt 	= (TextView) convertView.findViewById(guia.movil.app.R.id.tv_distance);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FsqVenue venue 	= mVenueList.get(position);
	
		holder.mNameTxt.setText(venue.name);
		if(guia.movil.app.PresentationActivity.english){
			holder.mHereNowTxt.setText("(" + venue.herenow + " people here)");
		}
		else{
			if(venue.herenow == 1){
				holder.mHereNowTxt.setText("(" + venue.herenow + " persona aquí)");
			}
			else{
				holder.mHereNowTxt.setText("(" + venue.herenow + " personas aquí)");
			}
		}
		
		holder.mDistanceTxt.setText(formatDistance(venue.distance));
		
        return convertView;
	}
	
	private String formatDistance(double distance) {
		String result = "";
		
		DecimalFormat dF = new DecimalFormat("00");
		
		dF.applyPattern("0.#");
		
		if (distance < 1000)
			result = dF.format(distance) + " m";
		else {
			distance = distance / 1000.0;
			result   = dF.format(distance) + " km";
		}
		
		return result;
	}
	static class ViewHolder {
		TextView mNameTxt;
		TextView mHereNowTxt;
		TextView mDistanceTxt;
	}
}