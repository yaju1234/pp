package com.appsbee.pairpost.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.pojo.Comments;
import com.appsbee.pairpost.util.ImageLoader;

public class CommentAdapter extends ArrayAdapter<Comments>{
	
	private ArrayList<Comments> mItems = new ArrayList<Comments>();
	private ViewHolder mHolder;
	private Activity activity;
	private ImageLoader imageLoader;
	private String userId;
	private String postId;
	public CommentAdapter(String user_id,String post_id,Activity activity, int textViewResourceId,	ArrayList<Comments> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity =activity;
		imageLoader = new ImageLoader(activity);
		this.postId = post_id;
		this.userId = user_id;
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
			v = vi.inflate(R.layout.row_comments, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.iv_user_image = (ImageView)v.findViewById(R.id.iv_user_image);
			mHolder.lbl_comment_name = (TextView)v.findViewById(R.id.lbl_comment_name);
			mHolder.lbl_comment_text = (TextView)v.findViewById(R.id.lbl_comment_text);
			mHolder.lbl_comments_time = (TextView)v.findViewById(R.id.lbl_comments_time);
					
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.iv_user_image.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			
			
		    }
		});
			
		final Comments bean = mItems.get(position);
	if (bean != null) {
	    mHolder.lbl_comment_name.setText(bean.getFirstName());
	    mHolder.lbl_comment_text.setText(bean.getComment());
	    mHolder.lbl_comments_time.setText(bean.getTime());
	    imageLoader.displayImage(Constant.Url.PROFILE_IMAGE_URL+bean.getImage(), R.drawable.pp_logo, mHolder.iv_user_image);
	}		
		return v;
	}
	class ViewHolder {
	        public ImageView iv_user_image;
		public TextView lbl_comment_name;
		public TextView lbl_comment_text;
		public TextView lbl_comments_time;
				
	}

	
}
