package com.appsbee.pairpost.activity;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.CustomVideoView.PlayPauseListener;
import com.appsbee.pairpost.adapter.DisCoverCommentAdapter;
import com.appsbee.pairpost.adapter.DiscoverLikeAdapter;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.http.HttpClient;
import com.appsbee.pairpost.http.KlHttpClient;
import com.appsbee.pairpost.pojo.Comments;
import com.appsbee.pairpost.pojo.Like;
import com.appsbee.pairpost.util.ImageLoader;
import com.appsbee.pairpost.util.LogManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.google.android.youtube.player.YouTubeBaseActivity;

public class DiscoverPairPostDetail extends YouTubeBaseActivity implements OnClickListener, OnTouchListener{
    
    private ImageView post1Image;
    private ImageView post2Image;
    private ImageView post1VideoPreview;
    private ImageView post2VideoPreview;
    private ImageView post1VideoPlayIcon;
    private ImageView post2VideoPlayIcon;
    private CustomVideoView  videoPlayer1, videoPlayer2;
    private VideoView videoView;
    private TextView lbl_created;
    private TextView lbl_caption;
    
    private TextView lbl_user_name;
    private ImageView iv_userImage;
    private ImageView iv_image_like;
    private ImageView iv_image_comment;
    private LinearLayout ll_comment;
    private TextView lbl_like_counter;
    private TextView lbl_comment_counter;
    private Button btn_done;
    String post1Type;
    String post2Type;
    String post1Url;
    String post2Url;
    String caption;
    String created;
    String userImage;
    String userName;
    String totallike;
    String totalcomment;
    boolean isLike;
    String pair_post_id;
    String loggedUserId;
    String post_owner_id;
    int count;
    private ImageLoader imageLoader;
    public HttpClient httpClient;
    public static final String API_KEY = "AIzaSyA2sVmbZqNLup9qBdozSlOGcpQSVx6fXYg";
    private  String videoUrl;
    private ArrayList<Like> mLikeArr = new ArrayList<Like>();
    private ArrayList<Comments> mCommentArr = new ArrayList<Comments>();
    private boolean likeFlag = true;
    private boolean commentFlag = true;
    private ListView list_comments;
    private ProgressBar progressBar;
    private  Dialog dlg_comments;
    private  Dialog dlg_list;
    private LinearLayout ll_bottom_arrow_up;
    private LinearLayout ll_bottom_arrow_down;
    private ImageView iv_arrow_up;
    private ImageView iv_arrow_down;
    private TextView lbl_total_comments;
    private int total_comments;
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_discover_pair_post_detail);
        post1Image = (ImageView)findViewById(R.id.post1_image);
        post2Image = (ImageView)findViewById(R.id.post2_image);
        post1VideoPreview = (ImageView)findViewById(R.id.post1_video_preview);
        post2VideoPreview = (ImageView)findViewById(R.id.post2_video_preview);
        post1VideoPlayIcon = (ImageView)findViewById(R.id.iv_post1_video_play_icon);
        post2VideoPlayIcon = (ImageView)findViewById(R.id.iv_post2_video_play_icon);
        videoPlayer1 = (CustomVideoView) findViewById(R.id.youtube_player1);
        videoPlayer2 = (CustomVideoView) findViewById(R.id.youtube_player2);
        lbl_user_name = (TextView)findViewById(R.id.lbl_user_name);
        iv_userImage = (ImageView)findViewById(R.id.iv_user_image);
        lbl_caption= (TextView)findViewById(R.id.lbl_caption);
        lbl_created = (TextView)findViewById(R.id.lbl_created);
        iv_image_like = (ImageView)findViewById(R.id.iv_like);
        iv_image_comment = (ImageView)findViewById(R.id.iv_comment);
        ll_comment = (LinearLayout)findViewById(R.id.ll_comment);
        lbl_like_counter = (TextView)findViewById(R.id.lbl_like_counter);
        lbl_comment_counter = (TextView)findViewById(R.id.lbl_comment_counter);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        btn_done = (Button) findViewById(R.id.btn_done);
        ll_bottom_arrow_up = (LinearLayout)findViewById(R.id.ll_bottom_arrow_up);
        ll_bottom_arrow_down = (LinearLayout)findViewById(R.id.ll_bottom_arrow_down);
        iv_arrow_up = (ImageView)findViewById(R.id.iv_arrow_up);
        iv_arrow_down = (ImageView)findViewById(R.id.iv_arrow_down);
        imageLoader = new ImageLoader(DiscoverPairPostDetail.this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            post1Type = bundle.getString("post1Type") ;
            post2Type = bundle.getString("post2Type") ;
            post1Url = bundle.getString("post1Url") ;
            post2Url = bundle.getString("post2Url") ;
            caption = bundle.getString("caption");
            created = bundle.getString("created");
            userName = bundle.getString("username");
            userImage = bundle.getString("image");
            totallike = bundle.getString("totallike");
            totalcomment = bundle.getString("totalcomment");
            count = Integer.parseInt(totalcomment);
            isLike = bundle.getBoolean("isLike");
            pair_post_id = bundle.getString("pair_post_id");
            loggedUserId = bundle.getString("user_id");
            post_owner_id = bundle.getString("pair_post_owner_id");
            
            setPreview(post1Type, post2Type);
        }
        post1VideoPlayIcon.setOnClickListener(this);
        post2VideoPlayIcon.setOnClickListener(this);
        iv_image_like.setOnClickListener(this);
        ll_comment.setOnClickListener(this);
        lbl_like_counter.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        iv_userImage.setOnClickListener(this);
        lbl_user_name.setOnClickListener(this);
        iv_arrow_up.setOnClickListener(this);
        iv_arrow_down.setOnClickListener(this);
        lbl_created.setText(created);
        lbl_caption.setText(caption);
        lbl_user_name.setText(userName);
        lbl_like_counter.setText(totallike);
        lbl_comment_counter.setText(totalcomment);
        if(isLike){
            iv_image_like.setImageResource(R.drawable.like);
        }else{
            iv_image_like.setImageResource(R.drawable.dislike);
        }
        imageLoader.displayImage(Constant.Url.PROFILE_IMAGE_URL+userImage, R.drawable.pp_logo, iv_userImage);
        videoPlayer1.setPlayPauseListener(new PlayPauseListener() {
	    
	    @Override
	    public void onPlay() {
		post1VideoPlayIcon.setVisibility(View.INVISIBLE);
		
	    }
	    
	    @Override
	    public void onPause() {
		post1VideoPlayIcon.setVisibility(View.VISIBLE);
		
	    }
	});
        
        videoPlayer1.setOnCompletionListener(new OnCompletionListener() {
	    
	    @Override
	    public void onCompletion(MediaPlayer mp) {
		post1VideoPlayIcon.setVisibility(View.VISIBLE);
		
	    }
	});
        
        videoPlayer2.setPlayPauseListener(new PlayPauseListener() {
	    
	    @Override
	    public void onPlay() {
		post2VideoPlayIcon.setVisibility(View.INVISIBLE);
		
	    }
	    
	    @Override
	    public void onPause() {
		post2VideoPlayIcon.setVisibility(View.VISIBLE);
		
	    }
	});
        videoPlayer2.setOnCompletionListener(new OnCompletionListener() {
	    
	    @Override
	    public void onCompletion(MediaPlayer mp) {
		post2VideoPlayIcon.setVisibility(View.VISIBLE);
		
	    }
	});
        
        post1Image.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(DiscoverPairPostDetail.this, ImageFullScreennView.class);
		i.putExtra("url", Constant.Url.IMAGE_URL+post1Url);
		startActivity(i);
		
	    }
	});
        
        post2Image.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(DiscoverPairPostDetail.this, ImageFullScreennView.class);
		i.putExtra("url", Constant.Url.IMAGE_URL+post2Url);
		startActivity(i);
		
	    }
	});
       
    }
    
    public void setPreview(String type1, String type2){
	if(type1.equalsIgnoreCase("video")){
	    post1Image.setVisibility(View.INVISIBLE);
	    imageLoader.displayImage("http://img.youtube.com/vi/"+post1Url+"/default.jpg", R.drawable.youtube, post1VideoPreview);
	    post1VideoPlayIcon.setVisibility(View.VISIBLE);
	   
	}else{
	    post1Image.setVisibility(View.VISIBLE);
	    post1VideoPlayIcon.setVisibility(View.INVISIBLE);
	    post1VideoPreview.setVisibility(View.INVISIBLE);
	    System.out.println("!-- Image path "+Constant.Url.IMAGE_URL+post1Url);
	    imageLoader.displayImage(Constant.Url.IMAGE_URL+post1Url, R.drawable.pp_logo, post1Image);
	}
	
	if(type2.equalsIgnoreCase("video")){
	    post2Image.setVisibility(View.INVISIBLE);
	    imageLoader.displayImage("http://img.youtube.com/vi/"+post2Url+"/default.jpg", R.drawable.youtube, post2VideoPreview);
	    post2VideoPlayIcon.setVisibility(View.VISIBLE);
	}else{
	    post2Image.setVisibility(View.VISIBLE);
	    post2VideoPlayIcon.setVisibility(View.INVISIBLE);
	    post2VideoPreview.setVisibility(View.INVISIBLE);
	    System.out.println("!-- Image path "+Constant.Url.IMAGE_URL+post2Url);
	    imageLoader.displayImage(Constant.Url.IMAGE_URL+post2Url, R.drawable.pp_logo, post2Image);
	}
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.iv_post1_video_play_icon:
	    post1VideoPlayIcon.setVisibility(View.INVISIBLE);
	    videoPlayer1.setVisibility(View.VISIBLE);
	    videoView = videoPlayer1;
	    post1VideoPreview.setVisibility(View.INVISIBLE);
	    new YourAsyncTask().execute(post1Url);
	    break;
	case R.id.iv_post2_video_play_icon:
	    post2VideoPlayIcon.setVisibility(View.INVISIBLE);
	    videoPlayer2.setVisibility(View.VISIBLE);
	    post2VideoPreview.setVisibility(View.INVISIBLE);
	    videoView = videoPlayer2;
	    new YourAsyncTask().execute(post2Url);
	    break;
	case R.id.iv_like:
	   
	   doLike();		
	    break;
	
	case R.id.lbl_like_counter:
	    if(likeFlag)
	    new LikeListAsyncTask().execute();
	    break;
	    
	    
    	case R.id.ll_comment:
    	doComment();
    	break;
    	case R.id.btn_done:
    	onBackPressed();
    	    break;
    	    
    	case R.id.iv_user_image:
    	doCallBackProfileIconClick();
    	    break;
    	case R.id.lbl_user_name:
    	doCallBackProfileIconClick();
    	    break;
    	case R.id.iv_arrow_up:
    	ll_bottom_arrow_up.setVisibility(View.INVISIBLE);
    	ll_bottom_arrow_down.setVisibility(View.VISIBLE);
    	    break;
    	case R.id.iv_arrow_down:
    	ll_bottom_arrow_up.setVisibility(View.VISIBLE);
    	ll_bottom_arrow_down.setVisibility(View.INVISIBLE);
    	    break;
	default:
	    break;
	}
	
    }
    
    private void doComment() {
	if(commentFlag)
	new CommentListAsyncTask().execute(pair_post_id);
    }

    @SuppressWarnings("unchecked")
    private void doLike() {
	
	    if(isLike){
		new LikeAsyncTask().execute(loggedUserId,"dislike",pair_post_id);
		
	    }else{
		new LikeAsyncTask().execute(loggedUserId,"like",pair_post_id);
	    }
	   
	    	
    }

    private class YourAsyncTask extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()   {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DiscoverPairPostDetail.this, "", "Loading Video wait...", true);
        }

        @Override
        protected Void doInBackground(String... params)      {
            try
            {
                String url = "http://www.youtube.com/watch?v="+params[0];
                videoUrl = getUrlVideoRTSP(url);
                Log.e("Video url for playing=========>>>>>", videoUrl);
            }
            catch (Exception e)
            {
                Log.e("Login Soap Calling in Exception", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();        
            videoView.setVideoURI(Uri.parse(videoUrl));
            MediaController mc = new MediaController(DiscoverPairPostDetail.this);
            videoView.setMediaController(mc);
            videoView.requestFocus();
            videoView.start();          
            mc.show();
        }

    }


public static String getUrlVideoRTSP(String urlYoutube)
{
    try
    {
        String gdy = "http://gdata.youtube.com/feeds/api/videos/";
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        String id = extractYoutubeId(urlYoutube);
        URL url = new URL(gdy + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Document doc = documentBuilder.parse(connection.getInputStream());
        Element el = doc.getDocumentElement();
        NodeList list = el.getElementsByTagName("media:content");///media:content
        String cursor = urlYoutube;
        for (int i = 0; i < list.getLength(); i++)
        {
            Node node = list.item(i);
            if (node != null)
            {
                NamedNodeMap nodeMap = node.getAttributes();
                HashMap<String, String> maps = new HashMap<String, String>();
                for (int j = 0; j < nodeMap.getLength(); j++)
                {
                    Attr att = (Attr) nodeMap.item(j);
                    maps.put(att.getName(), att.getValue());
                }
                if (maps.containsKey("yt:format"))
                {
                    String f = maps.get("yt:format");
                    if (maps.containsKey("url"))
                    {
                        cursor = maps.get("url");
                    }
                    if (f.equals("1"))
                        return cursor;
                }
            }
        }
        return cursor;
    }
    catch (Exception ex)
    {
        Log.e("Get Url Video RTSP Exception======>>", ex.toString());
    }
    return urlYoutube;

}

protected static String extractYoutubeId(String url) throws MalformedURLException    {       
   String id = null;
try
{
    String query = new URL(url).getQuery();
    if (query != null)
    {
        String[] param = query.split("&");
        for (String row : param)
        {
            String[] param1 = row.split("=");
            if (param1[0].equals("v"))
            {
                id = param1[1];
            }
        }
    }
    else
    {
        if (url.contains("embed"))
        {
            id = url.substring(url.lastIndexOf("/") + 1);
        }
    }
}
catch (Exception ex)
{
    Log.e("Exception", ex.toString());
}
return id;
}

@Override
public boolean onTouch(View v, MotionEvent event) {
    boolean firstTouch = false;
    long time = 0;
    switch (v.getId()) {
    case R.id.youtube_player1:
	if(event.getAction() == MotionEvent.ACTION_DOWN){
	   // Toast.makeText(getApplicationContext(), "Test", 1000).show();
            if(firstTouch && (System.currentTimeMillis() - time) <= 300) {
                //do stuff here for double tap
                Log.e("** DOUBLE TAP**"," second tap ");
                firstTouch = false;
                videoView.pause();
                Intent i = new Intent(DiscoverPairPostDetail.this, ActivityVideoView.class);
                i.putExtra("videoid", post1Url);
                startActivity(i);

            } else {
                firstTouch = true;
                time = System.currentTimeMillis();
                Log.e("** SINGLE  TAP**"," First Tap time  "+time);
               // return false;
            }
        }
	break;
    case R.id.youtube_player2:
	if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(firstTouch && (System.currentTimeMillis() - time) <= 300) {
                //do stuff here for double tap
                Log.e("** DOUBLE TAP**"," second tap ");
                firstTouch = false;
                videoView.pause();
                Intent i = new Intent(DiscoverPairPostDetail.this, ActivityVideoView.class);
                i.putExtra("videoid", post2Url);
                startActivity(i);

            } else {
                firstTouch = true;
                time = System.currentTimeMillis();
                Log.e("** SINGLE  TAP**"," First Tap time  "+time);
               // return false;
            }
        }
	break;
    default:
	break;
    }
    return true;
}


public class CommentListAsyncTask extends    AsyncTask<String, Void, Boolean> {
	String totalcomment;
	@Override
	protected Boolean doInBackground(String... params) {
	    boolean flag = false;
	    try {
		JSONObject req = new JSONObject();
		req.put("pairpost_id", pair_post_id);
		System.out.println("!-- req "+req.toString());
		JSONObject response  =  KlHttpClient.SendHttpPost(Constant.Url.COMMENTS_LIST,	req);
		if(response!=null){
		     flag = response.getBoolean("status");
		     totalcomment = ""+response.getInt("total_comments");
		     JSONArray jArr = response.getJSONArray("comments");
		     mCommentArr.clear();
		     if(jArr.length()>0){
			
			 for (int i=0; i< jArr.length(); i++){
			     JSONObject c = jArr.getJSONObject(i);
			     String comment_id = c.getString("comment_id");
			     String id = c.getString("id");
			     String image = c.getString("photo");
			     String name = c.getString("first_name");
			     String comment = c.getString("comment");
			     String time = c.getString("time");
			     mCommentArr.add(new Comments(comment_id, id,image, name,"", comment, time));
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
	protected void onPreExecute() {
	    super.onPreExecute();
	    progressBar.setVisibility(View.VISIBLE);
	}


	@Override
	protected void onPostExecute(Boolean result) {
	    super.onPostExecute(result);
	    progressBar.setVisibility(View.GONE);
	  // if(result){
	       commentDlg(loggedUserId,pair_post_id,totalcomment);
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
		req.put("pairpost_id", pair_post_id);
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
	    progressBar.setVisibility(View.GONE);
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
	    progressBar.setVisibility(View.VISIBLE);
	}
	
	

}

    public class LikeAsyncTask extends    AsyncTask<String, Void, Boolean> {
	String totallike;
	@Override
	protected Boolean doInBackground(String... params) {
	    boolean flag = false;
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
	         lbl_like_counter.setText(totallike);  
	         isLike = !isLike;
	       if(isLike){
	            iv_image_like.setImageResource(R.drawable.like);
	        }else{
	            iv_image_like.setImageResource(R.drawable.dislike);
	        }
	      
	   }

	}

    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constant.isOnResumeCall = true;
    }
    
    public void likeDlg(String totallike) {

   	dlg_list = new Dialog(DiscoverPairPostDetail.this);
   	dlg_list.requestWindowFeature(Window.FEATURE_NO_TITLE);
   	dlg_list.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
   	dlg_list.setContentView(R.layout.dialog_like);
   	dlg_list.setCancelable(true);

   	ImageView iv_cross = (ImageView) dlg_list.findViewById(R.id.iv_cross);
   	TextView lbl_no_of_count = (TextView)dlg_list.findViewById(R.id.lbl_no_of_count);
   	lbl_no_of_count.setText(totallike);
   	ListView list_comments = (ListView)dlg_list.findViewById(R.id.list_comments);
   	DiscoverLikeAdapter listLikeAdapter = new DiscoverLikeAdapter(DiscoverPairPostDetail.this,R.layout.row_like,mLikeArr);
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
   		likeFlag = true;
   		
   	    }
   	});

   	dlg_list.show();

       }
    
    public void commentDlg(String user_id, String post_id,final String totalcomment) {

  	dlg_comments = new Dialog(DiscoverPairPostDetail.this);
  	dlg_comments.requestWindowFeature(Window.FEATURE_NO_TITLE);
  	dlg_comments.getWindow().setBackgroundDrawable(	new ColorDrawable(android.graphics.Color.TRANSPARENT));
  	dlg_comments.setContentView(R.layout.dialog_comment);
  	dlg_comments.setCancelable(true);

  	ImageView iv_cross = (ImageView) dlg_comments
  		.findViewById(R.id.iv_cross);
  	ImageView btn_comments = (ImageView) dlg_comments
  		.findViewById(R.id.btn_comment);
  	lbl_total_comments = (TextView)dlg_comments.findViewById(R.id.lbl_total_comments);
  	lbl_total_comments.setText(totalcomment);
  	total_comments = Integer.parseInt(totalcomment);
  	list_comments = (ListView)dlg_comments.findViewById(R.id.list_comments);
  	DisCoverCommentAdapter listCommentAdapter = new DisCoverCommentAdapter(user_id,post_id,DiscoverPairPostDetail.this,R.layout.row_comments,mCommentArr);
  	list_comments.setAdapter(listCommentAdapter);
  	final EditText input_comments = (EditText) dlg_comments
  		.findViewById(R.id.et_input_comment);
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
  		    new InputCommentAsyncTask().execute(input_comments.getText().toString().trim());
  		    input_comments.setText("");
  		    int no = Integer.parseInt(totalcomment)+1;
  		    lbl_total_comments.setText(""+no);
  		    lbl_comment_counter.setText(""+no);
  		}
  	    }
  	});
  	
  	dlg_comments.setOnDismissListener(new OnDismissListener() {
  	    
  	    @Override
  	    public void onDismiss(DialogInterface dialog) {
  		
  		commentFlag = true;
  	    }
  	});

  	dlg_comments.show();

      }
    public class InputCommentAsyncTask extends    AsyncTask<String, Void, Boolean> {
	String comments="";
	String comment_id = "";
  	@Override
  	protected Boolean doInBackground(String... params) {
  	    boolean flag = false;
  	    comments = params[0];
  	    try {
  		JSONObject req = new JSONObject();
  		req.put("pairpost_id", pair_post_id);
  		req.put("user_id", loggedUserId);
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
  	      
  	     mCommentArr.add(new Comments(comment_id,Constant.userId,Constant.userImage, Constant.userName, "", comments, "Now"));
  	     DisCoverCommentAdapter listCommentAdapter = new DisCoverCommentAdapter(loggedUserId,pair_post_id,DiscoverPairPostDetail.this,R.layout.row_comments,mCommentArr);
  	     list_comments.setAdapter(listCommentAdapter);  
  	   }

  	}

      }
    
    public void doCallBackProfileIconClick(){
	
   	
   	   Constant.CallBackuserId = post_owner_id;
   	    Constant.isCallBackFromDiscoder = true;
   	    DiscoverPairPostDetail.this.finish();
   	
       }
    
    public void doCallBackLike(String id){
	
	if(dlg_list.isShowing()){
	    dlg_list.dismiss();
	}
	   Constant.CallBackuserId = id;
	    Constant.isCallBackFromDiscoder = true;
	    DiscoverPairPostDetail.this.finish();
	
    }
    
    public void doCallBackComment(String id){
	if(dlg_comments.isShowing()){
	    dlg_comments.dismiss();
	}
	    Constant.CallBackuserId = id;
	    Constant.isCallBackFromDiscoder = true;
	    DiscoverPairPostDetail.this.finish();
	
    }
    
    public void setTotalComments(){
	total_comments = total_comments-1;
	lbl_total_comments.setText(""+total_comments);
	lbl_comment_counter.setText(""+total_comments);
    }
    
}
