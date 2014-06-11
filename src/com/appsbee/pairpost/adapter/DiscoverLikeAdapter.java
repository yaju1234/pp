package com.appsbee.pairpost.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.DiscoverPairPostDetail;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.pojo.Like;
import com.appsbee.pairpost.util.ImageLoader;

public class DiscoverLikeAdapter extends ArrayAdapter<Like>{
	
	private ArrayList<Like> mItems = new ArrayList<Like>();
	private ViewHolder mHolder;
	private DiscoverPairPostDetail activity;
	private ImageLoader imageLoader;
	public DiscoverLikeAdapter(DiscoverPairPostDetail activity, int textViewResourceId,	ArrayList<Like> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity =activity;
		imageLoader = new ImageLoader(activity);
	}		  
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_like, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.lbl_first_name = (TextView)v.findViewById(R.id.lbl_first_name);
			mHolder.iv_user_image = (ImageView)v.findViewById(R.id.iv_user_image);
			mHolder.ll_row = (RelativeLayout)v.findViewById(R.id.ll_row);
					
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.ll_row.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			activity.doCallBackLike(mItems.get(position).getId());
			
		    }
		});
			
		final Like bean = mItems.get(position);
	if (bean != null) {
	    System.out.println("!--      Name "+bean.getName());
	    mHolder.lbl_first_name.setText(bean.getName());
	    imageLoader.displayImage(Constant.Url.PROFILE_IMAGE_URL+bean.getImage(), R.drawable.pp_logo, mHolder.iv_user_image);
	}		
		return v;
	}
	class ViewHolder {
	    	public RelativeLayout ll_row;
	        public TextView lbl_first_name;
		public ImageView iv_user_image;
				
	}

	
}
