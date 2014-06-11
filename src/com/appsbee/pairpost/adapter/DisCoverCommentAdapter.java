package com.appsbee.pairpost.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.DiscoverPairPostDetail;
import com.appsbee.pairpost.adapter.PairFeedCommentAdapter.DeleteCommentAsyncTask;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.http.KlHttpClient;
import com.appsbee.pairpost.pojo.Comments;
import com.appsbee.pairpost.util.ImageLoader;
import com.appsbee.pairpost.util.LogManager;

public class DisCoverCommentAdapter extends ArrayAdapter<Comments>{
	
	private ArrayList<Comments> mItems = new ArrayList<Comments>();
	private ViewHolder mHolder;
	private DiscoverPairPostDetail activity;
	private ImageLoader imageLoader;
	private String userId;
	private String postId;
	public DisCoverCommentAdapter(String user_id,String post_id,DiscoverPairPostDetail activity, int textViewResourceId,	ArrayList<Comments> items) {
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
			mHolder.ll_row = (RelativeLayout)v.findViewById(R.id.ll_row);
					
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.ll_row.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			activity.doCallBackComment(mItems.get(position).getId());
			
		    }
		});
		
		mHolder.ll_row.setOnLongClickListener(new OnLongClickListener() {
		    
		    @Override
		    public boolean onLongClick(View v) {
			if((mItems.get(position).getId().equalsIgnoreCase(Constant.userId))){
			    final String comment_id = mItems.get(position).getCommentID();
				//new DeleteCommentAsyncTask().execute(mItems.get(position).getCommentID());
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
				alertDialogBuilder.setTitle("Delete Comment"); 
				alertDialogBuilder
					.setMessage("Click yes to delete comment")
					.setCancelable(false)
					.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					    
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						new DeleteCommentAsyncTask().execute(comment_id);
					    }
					})
					.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					    
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
						
						dialog.cancel();
					    }
					});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show(); 
			}
			
			return false;
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
	        private RelativeLayout ll_row; 
	        public ImageView iv_user_image;
		public TextView lbl_comment_name;
		public TextView lbl_comment_text;
		public TextView lbl_comments_time;
				
	}
	
	    public class DeleteCommentAsyncTask extends    AsyncTask<String, Void, Boolean> {
		String comments_id="";
		@Override
	  	protected Boolean doInBackground(String... params) {
	  	    boolean flag = false;
	  	  comments_id = params[0];
	  	    try {
	  		JSONObject req = new JSONObject();
	  		req.put("comment_id", params[0]);
	  		System.out.println("!-- req "+req.toString());
	  		JSONObject response  =  KlHttpClient.SendHttpPost(Constant.Url.DELETE_COMMENT,	req);
	  		if(response!=null){
	  		     flag = response.getBoolean("status");
	  		 
	  		}
	  	    } catch (Exception e) {
	  		LogManager.e(getClass().getCanonicalName(), e.getMessage());
	  		return flag;
	  	    }
	  	    return flag;
	  	}

	  	@Override
	  	protected void onPostExecute(Boolean result) {
	  	    super.onPostExecute(result);
	  	   
	  	   if(result){
	  	       boolean del_comment_flag = false;
	  	       int del_pos = 0;
	  	       for(int i=0; i<mItems.size(); i++){
	  		   if(mItems.get(i).getCommentID().equalsIgnoreCase(comments_id)){
	  		     del_comment_flag = true;
	  		     del_pos = i;
	  		     break;
	  		   }
	  	       }
	  	       if(del_comment_flag){
	  		 mItems.remove(del_pos) ;
	  		
	  	       }
	  	       activity.setTotalComments();
	  	       notifyDataSetChanged();
	  	       
	  	   }

	  	}

	      }

	
}
