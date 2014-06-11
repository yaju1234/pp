/**
 * @author Ratul Ghosh
 * 14-Mar-2014
 * 
 */
package com.appsbee.pairpost.adapter;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.CustomVideoView;
import com.appsbee.pairpost.activity.ImageFullScreennView;
import com.appsbee.pairpost.activity.CustomVideoView.PlayPauseListener;
import com.appsbee.pairpost.adapter.ListPairFeedAdapter.CommentListAsyncTask;
import com.appsbee.pairpost.adapter.ListPairFeedAdapter.DeletePairPostAsyncTask;
import com.appsbee.pairpost.adapter.ListPairFeedAdapter.InputCommentAsyncTask;
import com.appsbee.pairpost.adapter.ListPairFeedAdapter.LikeAsyncTask;
import com.appsbee.pairpost.adapter.ListPairFeedAdapter.LikeListAsyncTask;
import com.appsbee.pairpost.adapter.ListPairFeedAdapter.ViewHolder;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.fragment.InnerTabPairPost;
import com.appsbee.pairpost.fragment.TabPairFeedFragment;
import com.appsbee.pairpost.http.KlHttpClient;
import com.appsbee.pairpost.pojo.Comments;
import com.appsbee.pairpost.pojo.Discoder;
import com.appsbee.pairpost.pojo.Like;
import com.appsbee.pairpost.util.ImageLoader;
import com.appsbee.pairpost.util.LogManager;

public class InnerListPairFeedAdapter  extends ArrayAdapter<Discoder> {

    private ArrayList<Discoder> mItems = new ArrayList<Discoder>();
    private ViewHolder mHolder;
    private Activity activity;
    private ImageLoader imageLoader;
    private boolean likeflag = true;
    private boolean commentflag = true;
    private ArrayList<Comments> arr = new ArrayList<Comments>();
    String post1Type;
    String post2Type;
    String post1Url;
    String post2Url;
    private ArrayList<Like> mLikeArr = new ArrayList<Like>();
    private ArrayList<Comments> mCommentArr = new ArrayList<Comments>();
    private ListView list_comments;
    private int pos;
    private TextView lbl_total_comments;
    private InnerTabPairPost innerTabPairPost;
    private  Dialog dlg_comments;
    private  Dialog dlg_list;

    public InnerListPairFeedAdapter(InnerTabPairPost innerTabPairPost,Activity activity, int textViewResourceId) {
	super(activity, textViewResourceId);
	this.mItems = Constant.innerfeedArr;
	this.activity =activity;
	this.innerTabPairPost = innerTabPairPost;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
	View v = convertView;
	if (v == null) {
		LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.gallery_item_pair_feed_2, null);
		mHolder = new ViewHolder();
		v.setTag(mHolder);
		
		mHolder.post1Image = (ImageView)v.findViewById(R.id.post1_image);
		mHolder.post2Image = (ImageView)v.findViewById(R.id.post2_image);
		mHolder.post1VideoPreview = (ImageView)v.findViewById(R.id.post1_video_preview);
		mHolder.post2VideoPreview = (ImageView)v.findViewById(R.id.post2_video_preview);
		mHolder.post1VideoPlayIcon = (ImageView)v.findViewById(R.id.iv_post1_video_play_icon);
		mHolder.post2VideoPlayIcon = (ImageView)v.findViewById(R.id.iv_post2_video_play_icon);		      
		
		mHolder.llLikeCounter = (LinearLayout) v.findViewById(R.id.ll_like_counter);
		mHolder.llLike = (LinearLayout) v.findViewById(R.id.ll_like);
		mHolder.llCommentCounter = (LinearLayout) v.findViewById(R.id.ll_comment_counter);
		mHolder.llComment = (LinearLayout) v.findViewById(R.id.ll_comment);
		mHolder.lbl_like_counter = (TextView)v.findViewById(R.id.lbl_like_counter);
		mHolder.lbl_comment_counter = (TextView)v.findViewById(R.id.lbl_comment_counter);
		mHolder.iv_like = (ImageView)v.findViewById(R.id.iv_like);
		mHolder.progressBar = (ProgressBar)v.findViewById(R.id.progressBar1);
		mHolder.ivUserPic = (ImageView)v.findViewById(R.id.ivUserPic);
		mHolder.tvUserName = (TextView)v.findViewById(R.id.tvUserName);
		mHolder.tvPairFeedStatusText = (TextView)v.findViewById(R.id.tvPairFeedStatusText);
		mHolder.ivDelete = (ImageView)v.findViewById(R.id.ivDelete);
		mHolder.ivFlag = (ImageView)v.findViewById(R.id.ivFlag);
		mHolder.tvpairFeedTime = (TextView)v.findViewById(R.id.tvpairFeedTime);
			
	}
	else {
		mHolder =  (ViewHolder) v.getTag();
	}
	
	
	mHolder.post1Image.setOnLongClickListener(new OnLongClickListener() {
	    
	    @Override
	    public boolean onLongClick(View v) {
		Intent i = new Intent(activity, ImageFullScreennView.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("url", Constant.Url.IMAGE_URL+mItems.get(position).getPost1Url());
		activity.startActivity(i);
		return false;
		
	    }
	});

	mHolder.post2Image.setOnLongClickListener(new OnLongClickListener() {
    
	 @Override
	 public boolean onLongClick(View v) {
	     Intent i = new Intent(activity, ImageFullScreennView.class);
	     i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     i.putExtra("url", Constant.Url.IMAGE_URL+mItems.get(position).getPost2Url());
	     activity.startActivity(i);
	     return false;
    }
		});
	
	mHolder.ivUserPic.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		innerTabPairPost.doCallback(mItems.get(position).getId());
	    }
	});
	
	mHolder.tvUserName.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		innerTabPairPost.doCallback(mItems.get(position).getId());
	    }
	});
	
	mHolder.ivDelete.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		//new DeletePairPostAsyncTask().execute(mItems.get(position).getPairpostId());
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(activity);
		alertDialog2.setTitle("Delete Post");
		alertDialog2.setMessage("Are you sure you want to delete this Pair Post?");
		alertDialog2.setIcon(R.drawable.pp_logo);
		alertDialog2.setPositiveButton("YES",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		        	dialog.cancel();
		        	new DeletePairPostAsyncTask().execute(mItems.get(position).getPairpostId());
		            }
		        });
		alertDialog2.setNegativeButton("NO",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.cancel();
		            }
		        });

		alertDialog2.show();
		
	    
		
	    }
	});
	
        
	
       
	

	mHolder.llLikeCounter.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		//Toast.makeText(mContext, "Test Like Toast", 3000).show();
		
		if(likeflag){
		    likeflag = false;
		    new LikeListAsyncTask().execute(mItems.get(position).getPairpostId()); 
		}
		   
		
	    }
	});
	mHolder.llLike.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		pos = position;
		doLike(mItems.get(position).getPairpostId(),mItems.get(position).isLike());		
	    }
	   
	});
	
	mHolder.llComment.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {		
		pos = position;
		if(commentflag){
		    mCommentArr.clear();
		    commentflag = false;
		    new CommentListAsyncTask().execute(mItems.get(position).getPairpostId(),mItems.get(position).getTotalComments()); 
		}		
		
	    }
	});
	mHolder.llCommentCounter.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		pos = position;
		//Toast.makeText(mContext, "Test Comment Toast", 3000).show();
		if(commentflag){
		    mCommentArr.clear();
		    commentflag = false;
		    new CommentListAsyncTask().execute(mItems.get(position).getPairpostId(),mItems.get(position).getTotalComments()); 
		}
		
	    }
	});
	final Discoder bean = mItems.get(position);
	if(bean!=null){
	    if(bean.getId().equalsIgnoreCase(Constant.userId)){
		mHolder.ivDelete.setVisibility(View.VISIBLE);
	    }else{
		mHolder.ivDelete.setVisibility(View.INVISIBLE);
	    }
	    
	    if(bean.getId().equalsIgnoreCase(Constant.userId)){
		mHolder.ivFlag.setVisibility(View.INVISIBLE);
	    }else{
		mHolder.ivFlag.setVisibility(View.VISIBLE);
	    }
	    
	    
	    //mHolder.ivFlag
	    mHolder.tvpairFeedTime.setText(bean.getCreated());
	    mHolder.tvPairFeedStatusText.setText(bean.getCaption());
	    mHolder.tvUserName.setText(bean.getFirstName());
	    imageLoader.displayImage(Constant.Url.PROFILE_IMAGE_URL+bean.getUserIamge(), R.drawable.pp_logo,  mHolder.ivUserPic);
	    mHolder.lbl_like_counter.setText(bean.getTotalLike());
	    mHolder.lbl_comment_counter.setText(bean.getTotalComments());
	    setPreview(bean.getPost1Type(), bean.getPost2Type(), bean.getPost1Url(), bean.getPost2Url());
	    if(bean.isLike()){
		mHolder.iv_like.setImageResource(R.drawable.like);
	        }else{
	            mHolder.iv_like.setImageResource(R.drawable.dislike);
	        }
	}
	
	return v;
    }

    public void likeDlg(String totallike) {

   	dlg_list = new Dialog(activity);
   	dlg_list.requestWindowFeature(Window.FEATURE_NO_TITLE);
   	dlg_list.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
   	dlg_list.setContentView(R.layout.dialog_like);
   	dlg_list.setCancelable(true);

   	ImageView iv_cross = (ImageView) dlg_list.findViewById(R.id.iv_cross);
   	TextView lbl_no_of_count = (TextView)dlg_list.findViewById(R.id.lbl_no_of_count);
   	lbl_no_of_count.setText(totallike);
   	ListView list_comments = (ListView)dlg_list.findViewById(R.id.list_comments);
   	InnerTabLikeAdapter listLikeAdapter = new InnerTabLikeAdapter(InnerListPairFeedAdapter.this,activity,R.layout.row_like,mLikeArr);
   	list_comments.setAdapter(listLikeAdapter);
   	iv_cross.setOnClickListener(new OnClickListener() {

   	    @Override
   	    public void onClick(View v) {

   		dlg_list.dismiss();
   	    }
   	});
   	dlg_list.setOnDismissListener(new OnDismissListener() {
   	    
   	    @Override
   	    public void onDismiss(DialogInterface dialog) {
   		likeflag = true;
   		InnerListPairFeedAdapter.this.notifyDataSetChanged();
   		
   	    }
   	});

   	dlg_list.show();

       }
    
    public void commentDlg( final String post_id,final String totalcomment) {

  	dlg_comments = new Dialog(activity);
  	dlg_comments.requestWindowFeature(Window.FEATURE_NO_TITLE);
  	dlg_comments.getWindow().setBackgroundDrawable(	new ColorDrawable(android.graphics.Color.TRANSPARENT));
  	dlg_comments.setContentView(R.layout.dialog_comment);
  	dlg_comments.setCancelable(true);

  	ImageView iv_cross = (ImageView) dlg_comments.findViewById(R.id.iv_cross);
  	ImageView btn_comments = (ImageView) dlg_comments.findViewById(R.id.btn_comment);
  	lbl_total_comments = (TextView)dlg_comments.findViewById(R.id.lbl_total_comments);
  	lbl_total_comments.setText(totalcomment);
  	list_comments = (ListView)dlg_comments.findViewById(R.id.list_comments);
  	InnerTabCommentAdapter listCommentAdapter = new InnerTabCommentAdapter(pos,lbl_total_comments,InnerListPairFeedAdapter.this,Constant.userId,post_id,activity,R.layout.row_comments,mCommentArr);
  	list_comments.setAdapter(listCommentAdapter);
  	final EditText input_comments = (EditText) dlg_comments.findViewById(R.id.et_input_comment);
  	iv_cross.setOnClickListener(new OnClickListener() {

  	    @Override
  	    public void onClick(View v) {

  		dlg_comments.dismiss();
  	    }
  	});

  	btn_comments.setOnClickListener(new OnClickListener() {

  	    @Override
  	    public void onClick(View v) {

  		if (input_comments.getText().toString().trim().length() > 0) {
  		    new InputCommentAsyncTask().execute(input_comments.getText().toString().trim(),post_id,totalcomment);
  		    input_comments.setText("");
  		    int no = Integer.parseInt(totalcomment)+1;
  		    lbl_total_comments.setText(""+no);
  		   
  		}
  	    }
  	});
  	
  	dlg_comments.setOnDismissListener(new OnDismissListener() {
  	    
  	    @Override
  	    public void onDismiss(DialogInterface dialog) {
  		
  		commentflag = true;
  		InnerListPairFeedAdapter.this.notifyDataSetChanged();
  	    }
  	});

  	dlg_comments.show();

      }
    
    class ViewHolder {
	public ImageView post1Image;
	public ImageView post2Image;
	public ImageView post1VideoPreview;
	public ImageView post2VideoPreview;
	public ImageView post1VideoPlayIcon;
	public ImageView post2VideoPlayIcon;
	public VideoView videoView;
	
	public LinearLayout llLikeCounter;
	public LinearLayout llLike;
	public LinearLayout llCommentCounter;
	public LinearLayout llComment;
	public TextView lbl_like_counter;
	public TextView lbl_comment_counter;
	public ImageView iv_like;
	private ProgressBar progressBar;
	private ImageView ivUserPic;
	private TextView tvUserName;
	private TextView tvPairFeedStatusText;
	private ImageView ivDelete;
	private ImageView ivFlag; 
	private TextView tvpairFeedTime;
}
    
    public void setPreview(String type1, String type2,String post1Url,String post2Url){
   	if(type1.equalsIgnoreCase("video")){
   	 mHolder.post1Image.setVisibility(View.INVISIBLE);
   	Log.e("type2 video", "http://img.youtube.com/vi/"+post1Url+"/default.jpg");
   	    imageLoader.displayImage("http://img.youtube.com/vi/"+post1Url+"/default.jpg", R.drawable.youtube,  mHolder.post1VideoPreview);
   	 mHolder.post1VideoPlayIcon.setVisibility(View.VISIBLE);
   	   
   	}else{
   	 mHolder.post1Image.setVisibility(View.VISIBLE);
   	mHolder.post1VideoPlayIcon.setVisibility(View.INVISIBLE);
   	mHolder.post1VideoPreview.setVisibility(View.INVISIBLE);
   	    System.out.println("!-- Image path "+Constant.Url.IMAGE_URL+post1Url);
   	    imageLoader.displayImage(Constant.Url.IMAGE_URL+post1Url, R.drawable.pp_logo,  mHolder.post1Image);
   	}
   	
   	if(type2.equalsIgnoreCase("video")){
   	    Log.e("type2 video", "http://img.youtube.com/vi/"+post2Url+"/default.jpg");
   	 mHolder.post2Image.setVisibility(View.INVISIBLE);
   	    imageLoader.displayImage("http://img.youtube.com/vi/"+post2Url+"/default.jpg", R.drawable.youtube,  mHolder.post2VideoPreview);
   	 mHolder.post2VideoPlayIcon.setVisibility(View.VISIBLE);
   	}else{
   	 mHolder.post2Image.setVisibility(View.VISIBLE);
   	mHolder.post2VideoPlayIcon.setVisibility(View.INVISIBLE);
   	mHolder.post2VideoPreview.setVisibility(View.INVISIBLE);
   	    System.out.println("!-- Image path "+Constant.Url.IMAGE_URL+post2Url);
   	    imageLoader.displayImage(Constant.Url.IMAGE_URL+post2Url, R.drawable.pp_logo,  mHolder.post2Image);
   	}
       }
    

public class CommentListAsyncTask extends    AsyncTask<String, Void, Boolean> {
	String totalcomment;
	String id;
	@Override
	protected Boolean doInBackground(String... params) {
	    boolean flag = false;
	    id = params[0];
	    try {
		JSONObject req = new JSONObject();
		req.put("pairpost_id", params[0]);
		System.out.println("!-- req "+req.toString());
		JSONObject response  =  KlHttpClient.SendHttpPost(Constant.Url.COMMENTS_LIST,	req);
		if(response!=null){
		     flag = response.getBoolean("status");
		     totalcomment = ""+response.getInt("total_comments");
		     Log.e("totalcomment", totalcomment);
		     JSONArray jArr = response.getJSONArray("comments");
		     mCommentArr.clear();
		     Log.e("jArr ", ""+jArr.length());
		     if(jArr.length()>0){
			 Log.e("jArr here", "here");
			 for (int i=0; i< jArr.length(); i++){
			     JSONObject c = jArr.getJSONObject(i);
			     String comment_id = c.getString("comment_id");
			     String id = c.getString("id");
			     String image = c.getString("photo");
			     String name = c.getString("first_name");
			     String comment = c.getString("comment");
			     String time = c.getString("time");
			     Log.e("id", id);
			     Log.e("name", name);
			     Log.e("comment", comment);
			     mCommentArr.add(new Comments(comment_id,id,image, name,"", comment, time));
			 }
		     }
		}
	    } catch (Exception e) {
		e.printStackTrace();
		return flag;
	    }
	    return flag;
	}
	

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    mHolder.progressBar.setVisibility(View.VISIBLE);
	}


	@Override
	protected void onPostExecute(Boolean result) {
	    super.onPostExecute(result);
	    mHolder.progressBar.setVisibility(View.GONE);
	  // if(result){
	    Log.e("Size...", ""+mCommentArr.size());
	       commentDlg(id,totalcomment);
	  // }

	}

}



public class LikeListAsyncTask extends    AsyncTask<String, Void, Boolean> {
	String totallike;
	@Override
	protected Boolean doInBackground(String... params) {
	    boolean flag = false;
	    try {
		JSONObject req = new JSONObject();
		req.put("pairpost_id", params[0]);
		System.out.println("!-- req "+req.toString());
		JSONObject response  =  KlHttpClient.SendHttpPost(Constant.Url.LIKE_LIST,	req);
		if(response!=null){
		     flag = response.getBoolean("status");
		     totallike = ""+response.getInt("total_likes");
		     if(flag){
		     JSONArray jArr = response.getJSONArray("data");
		     mLikeArr.clear();
		     if(jArr.length()>0){
			
			 for (int i=0; i< jArr.length(); i++){
			     JSONObject c = jArr.getJSONObject(i);
			     String id = c.getString("id");
			     String image = c.getString("photo");
			     String name = c.getString("first_name");
			     mLikeArr.add(new Like(id,image, name));
			 }
		     }
		     }
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
	    mHolder.progressBar.setVisibility(View.GONE);
	   if(result){
	         //lbl_like_counter.setText(totallike);  
	     
	      // if(mLikeArr!= null){
		   likeDlg(totallike); 
	      // }
	      
	       
	   }

	}

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    mHolder.progressBar.setVisibility(View.VISIBLE);
	}
	
	

}

    public class LikeAsyncTask extends    AsyncTask<String, Void, Boolean> {
	String totallike;
	String type;
	@Override
	protected Boolean doInBackground(String... params) {
	    boolean flag = false;
	    type = params[1];
	    try {
		JSONObject req = new JSONObject();
		req.put("pairpost_id", params[2]);
		req.put("user_id", params[0]);
		req.put("disflag", params[1]);
		System.out.println("!-- req "+req.toString());
		JSONObject response  =  KlHttpClient.SendHttpPost(Constant.Url.LIKE,	req);
		if(response!=null){
		     flag = response.getBoolean("status");
		     totallike = response.getString("totallike");
		  
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
		Constant.innerfeedArr.get(pos).setTotalLike(totallike);
		if(type.equalsIgnoreCase("dislike")){
		    Constant.innerfeedArr.get(pos).setLike(false);
		    
		}else{
		    Constant.innerfeedArr.get(pos).setLike(true);
		}
		notifyDataSetChanged();
		
		
		/*
	         lbl_like_counter.setText(totallike);  
	         isLike = !isLike;
	       if(isLike){
		   mHolder.iv_like.setImageResource(R.drawable.like);
	        }else{
	            mHolder.iv_like.setImageResource(R.drawable.dislike);
	        }
	      
	   */}

	}

    }
    public void doLike(String pairpostId, boolean isLike) {
	if(isLike){
		new LikeAsyncTask().execute(Constant.userId,"dislike",pairpostId);
		
	    }else{
		new LikeAsyncTask().execute(Constant.userId,"like",pairpostId);
	    }
	
    }
    
    public class InputCommentAsyncTask extends    AsyncTask<String, Void, Boolean> {
	String comments="";
	String post_id;
	String totalcomment;
	String comment_id = "";
  	@Override
  	protected Boolean doInBackground(String... params) {
  	    boolean flag = false;
  	  comments = params[0];
  	  post_id = params[1];
  	  totalcomment = params[2];
  	    try {
  		JSONObject req = new JSONObject();
  		req.put("pairpost_id", params[1]);
  		req.put("user_id", Constant.userId);
  		req.put("comment", params[0]);
  		System.out.println("!-- req "+req.toString());
  		JSONObject response  =  KlHttpClient.SendHttpPost(Constant.Url.INPUT_COMMENTS,	req);
  		if(response!=null){
  		     flag = response.getBoolean("status");
  		   if(flag){
			comment_id = response.getString("comment_id"); 
		     }
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
  	      
  	     mCommentArr.add(new Comments(comment_id,Constant.userId,Constant.userImage, Constant.userName, "", comments, ""));
  	     Constant.innerfeedArr.get(pos).setTotalComments(""+mCommentArr.size());
  	     lbl_total_comments.setText(""+mCommentArr.size());
  	      InnerTabCommentAdapter listCommentAdapter = new InnerTabCommentAdapter(pos,lbl_total_comments,InnerListPairFeedAdapter.this,Constant.userId,post_id,activity,R.layout.row_comments,mCommentArr);
  	     list_comments.setAdapter(listCommentAdapter);
  	   InnerListPairFeedAdapter.this.notifyDataSetChanged();
  	   }

  	}

      }
    
    public void doCallBackLike(String id){
	
	if(dlg_list.isShowing()){
	    dlg_list.dismiss();
	}
	
	innerTabPairPost.doCallback(id);
    }
    public void doCallBackComment(String id){
	
  	if(dlg_comments.isShowing()){
  	    dlg_comments.dismiss();
  	}
  	innerTabPairPost.doCallback(id);
      }
    
    public class DeletePairPostAsyncTask extends    AsyncTask<String, Void, Boolean> {
	String postid_id="";
	@Override
  	protected Boolean doInBackground(String... params) {
  	    boolean flag = false;
  	    postid_id = params[0];
  	    try {
  		JSONObject req = new JSONObject();
  		req.put("pairpost_id", params[0]);
  		System.out.println("!-- req "+req.toString());
  		JSONObject response  =  KlHttpClient.SendHttpPost(Constant.Url.DELETE_PAIR_POST,	req);
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
  	       Toast.makeText(activity, "Post delete successfully", Toast.LENGTH_LONG).show();
  	       boolean del_comment_flag = false;
  	       int del_pos = 0;
  	       for(int i=0; i<Constant.innerfeedArr.size(); i++){
  		   if(Constant.innerfeedArr.get(i).getPairpostId().equalsIgnoreCase(postid_id)){
  		     del_comment_flag = true;
  		     del_pos = i;
  		     break;
  		   }
  	       }
  	       if(del_comment_flag){
  		 Constant.innerfeedArr.remove(del_pos) ;  		
  	       }
  	       notifyDataSetChanged();
  	     innerTabPairPost.doPostDelete();
  	   }

  	}

      } 
    
}
