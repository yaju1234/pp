/**
 * @author Ratul Ghosh
 * 14-Mar-2014
 * 
 */
package com.appsbee.pairpost.adapter;

import java.util.ArrayList;
import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.DiscoverPairPostDetail;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.pojo.Discoder;
import com.appsbee.pairpost.util.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GridDiscoverAdapter1 extends ArrayAdapter<Discoder>{
	
	private ArrayList<Discoder> mItems = new ArrayList<Discoder>();
	private ViewHolder mHolder;
	private Activity activity;
	private ImageLoader imageLoader;
	private String userId;
	public GridDiscoverAdapter1(String userId,Activity activity, int textViewResourceId,	ArrayList<Discoder> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity =activity;
		this.userId = userId;
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
			v = vi.inflate(R.layout.grid_item_discover1, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.ll_list_row = (LinearLayout)v.findViewById(R.id.ll_list_row);
			mHolder.vv_left = (ImageView)v.findViewById(R.id.vv_left);
			mHolder.iv_left = (ImageView)v.findViewById(R.id.iv_left);
			mHolder.iv_play_left = (ImageView)v.findViewById(R.id.iv_play_icon_left);
			
			
			mHolder.vv_right = (ImageView)v.findViewById(R.id.vv_right);
			mHolder.iv_right = (ImageView)v.findViewById(R.id.iv_right);
			mHolder.iv_play_right = (ImageView)v.findViewById(R.id.iv_play_icon_right);
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.ll_list_row.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			Intent i = new Intent(activity, DiscoverPairPostDetail.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("caption", mItems.get(position).getCaption());
			i.putExtra("created", mItems.get(position).getCreated());
			i.putExtra("post1Type", mItems.get(position).getPost1Type());
			i.putExtra("post2Type", mItems.get(position).getPost2Type());
			i.putExtra("post1Url", mItems.get(position).getPost1Url());
			i.putExtra("post2Url", mItems.get(position).getPost2Url());
			i.putExtra("username", mItems.get(position).getFirstName());
			i.putExtra("image", mItems.get(position).getUserIamge());
			i.putExtra("totallike", mItems.get(position).getTotalLike());
			i.putExtra("totalcomment", mItems.get(position).getTotalComments());
			System.out.println("!-- isLike "+mItems.get(position).isLike());
			i.putExtra("isLike", mItems.get(position).isLike());
			i.putExtra("pair_post_id", mItems.get(position).getPairpostId());
			i.putExtra("user_id", userId);
			i.putExtra("pair_post_owner_id", mItems.get(position).getId());
			activity.startActivity(i);
			
		    }
		});
			
		final Discoder bean = mItems.get(position);
	if (bean != null) {
	    if (bean.getPost1Type().equalsIgnoreCase("video")) {
		imageLoader.displayImage("http://img.youtube.com/vi/"+bean.getPost1Url()+"/default.jpg", R.drawable.youtube, mHolder.vv_left);
		mHolder.vv_left.setVisibility(View.VISIBLE);
		mHolder.iv_play_left.setVisibility(View.VISIBLE);
		mHolder.iv_left.setVisibility(View.GONE);
	    } else {
		mHolder.vv_left.setVisibility(View.GONE);
		mHolder.iv_play_left.setVisibility(View.GONE);
		mHolder.iv_left.setVisibility(View.VISIBLE);
		imageLoader.displayImage(Constant.Url.IMAGE_URL+bean.getPost1Url(), R.drawable.pp_logo, mHolder.iv_left);
	    }

	    if (bean.getPost2Type().equalsIgnoreCase("video")) {
		imageLoader.displayImage("http://img.youtube.com/vi/"+bean.getPost2Url()+"/default.jpg", R.drawable.youtube, mHolder.vv_right);
		mHolder.vv_right.setVisibility(View.VISIBLE);
		mHolder.iv_play_right.setVisibility(View.VISIBLE);
		mHolder.iv_right.setVisibility(View.GONE);
	    } else {
		mHolder.vv_right.setVisibility(View.GONE);
		mHolder.iv_play_right.setVisibility(View.GONE);
		mHolder.iv_right.setVisibility(View.VISIBLE);
		imageLoader.displayImage(Constant.Url.IMAGE_URL+bean.getPost2Url(), R.drawable.pp_logo, mHolder.iv_right);
	    }
	}		
		return v;
	}
	class ViewHolder {
	        private LinearLayout ll_list_row;
		public ImageView vv_left;
		public ImageView iv_left;
		public ImageView iv_play_left;
		public ImageView vv_right;
		public ImageView iv_right;
		public ImageView iv_play_right;			
	}

	
}
