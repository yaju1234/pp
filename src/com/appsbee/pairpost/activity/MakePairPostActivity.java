/**
 * @author Ratul Ghosh
 * 10-Apr-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.CustomVideoView.PlayPauseListener;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.pojo.YoutubeUploadRequest;
import com.appsbee.pairpost.pojo.YoutubeUploader;
import com.appsbee.pairpost.pojo.YoutubeUploader.ProgressListner;

public class MakePairPostActivity extends BaseActivity implements OnClickListener {
    
    private RelativeLayout rlViewPanelTop;
    private RelativeLayout rlControlPanelTop;
    private ImageView ivSnapshotImageTop;
    private ImageView ivPlayButtonTop;
    private CustomVideoView vvRecordedVideoTop;
    private ImageView  ivGalleryTop;
    private ImageView ivCameraTop;
    private ImageView ivRecorderTop;
    
    
    private RelativeLayout rlViewPanelBottom;
    private RelativeLayout rlControlPanelBottom;
    private ImageView ivSnapshotImageBottom;
    private ImageView ivPlayButtonBottom;
    private CustomVideoView vvRecordedVideoBottom;
    private ImageView  ivGalleryBottom;
    private ImageView ivCameraBottom;
    private ImageView ivRecorderBottom;
    
    private LinearLayout llSettingsPanel;
    private Button btn_cancel_post,btn_caption_post,btn_share_post,btn_submit_post;
    private LinearLayout ll_post_tag;
    private TextView lbl_post_tag;
    
    public static final int REQUEST_CODE_TAKE_PICTURE_TOP = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE_BOTTOM = 0x2;
    public static final int VIDEO_CAPTURE_TOP = 0x3;
    public static final int VIDEO_CAPTURE_BOTTOM = 0x4;
    
    public File mediaFileTop;
    public File mediaFileBottom;
    
    public boolean PostStatusTop = false;
    public boolean PostStatusBottom = false;
    public boolean isPostVideoTop = false;
    public boolean isPostVideoBottom = false;
    
    private String postPathTop;
    private String postPathBottom;
    private String postTypeTop; // Type video(Youtube video pair_post_id) OR Image
    private String postTypeBottom;
   // private Uri uriTop;
   // private Uri uriBottom;
    private File fileTop;
    private File fileBottom;
    
    private static final String TYPE_VIDEO = "video";
    private static final String TYPE_IMAGE = "image";
    private  String uriPostTop;
    private  String uriPostBottom;
    public HttpEntity resEntity;
    private ProgressBar progressBar;
    private Bitmap bitmapTop;
    private Bitmap bitmapBottom;
    private String postTag = "";
    
    @Override
    protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	setContentView(R.layout.activity_make_pair_post1);
	
	rlViewPanelTop = (RelativeLayout)findViewById(R.id.rlViewPanelTop);
	rlControlPanelTop = (RelativeLayout)findViewById(R.id.rlControlPanelTop);
	ivSnapshotImageTop = (ImageView)findViewById(R.id.ivSnapshotImageTop);
	ivPlayButtonTop = (ImageView)findViewById(R.id.ivPlayButtonTop);
	vvRecordedVideoTop = (CustomVideoView)findViewById(R.id.vvRecordedVideoTop);
	ivGalleryTop = (ImageView)findViewById(R.id.ivGalleryTop);
	ivCameraTop = (ImageView)findViewById(R.id.ivCameraTop);
	ivRecorderTop = (ImageView)findViewById(R.id.ivRecorderTop);	
	
	
	rlViewPanelBottom = (RelativeLayout)findViewById(R.id.rlViewPanelBottom);
	rlControlPanelBottom = (RelativeLayout)findViewById(R.id.rlControlPanelBottom);
	ivSnapshotImageBottom = (ImageView)findViewById(R.id.ivSnapshotImageBottom);
	ivPlayButtonBottom = (ImageView)findViewById(R.id.ivPlayButtonBottom);
	vvRecordedVideoBottom = (CustomVideoView)findViewById(R.id.vvRecordedVideoBottom);
	ivGalleryBottom = (ImageView)findViewById(R.id.ivGalleryBottom);
	ivCameraBottom = (ImageView)findViewById(R.id.ivCameraBottom);
	ivRecorderBottom = (ImageView)findViewById(R.id.ivRecorderBottom);
	
	llSettingsPanel = (LinearLayout)findViewById(R.id.llSettingsPanel);
	btn_cancel_post = (Button)findViewById(R.id.btn_cancel_post);
	btn_caption_post = (Button)findViewById(R.id.btn_caption_post);
	btn_share_post = (Button)findViewById(R.id.btn_share_post);
	btn_submit_post = (Button)findViewById(R.id.btn_submit_post);
	
	progressBar = (ProgressBar)findViewById(R.id.progressBar1);
	
	ll_post_tag = (LinearLayout)findViewById(R.id.ll_post_tag);
	lbl_post_tag = (TextView)findViewById(R.id.lbl_post_tag);
	
	ivGalleryTop.setOnClickListener(this);
	ivCameraTop.setOnClickListener(this);
	ivRecorderTop.setOnClickListener(this);
	ivPlayButtonTop.setOnClickListener(this);
	
	
	ivGalleryBottom.setOnClickListener(this);
	ivCameraBottom.setOnClickListener(this);
	ivRecorderBottom.setOnClickListener(this);
	ivPlayButtonBottom.setOnClickListener(this);
	
	btn_submit_post.setOnClickListener(this);
	btn_share_post.setOnClickListener(this);
	btn_caption_post.setOnClickListener(this);
	btn_cancel_post.setOnClickListener(this);
	
	createFolder();//Create folder "pp_videos" into SD card if not exist
	
	vvRecordedVideoTop.setPlayPauseListener(new PlayPauseListener() {
	    
	    @Override
	    public void onPlay() {
		ivPlayButtonTop.setVisibility(View.INVISIBLE);
		
	    }
	    
	    @Override
	    public void onPause() {
		ivPlayButtonTop.setVisibility(View.VISIBLE);
		
	    }
	    
	    
	});
	
	vvRecordedVideoTop.setOnCompletionListener(new OnCompletionListener() {
	    
	    @Override
	    public void onCompletion(MediaPlayer mp) {
		
		ivPlayButtonTop.setVisibility(View.VISIBLE);
	    }
	});
	vvRecordedVideoBottom.setPlayPauseListener(new PlayPauseListener() {
	    
	    @Override
	    public void onPlay() {
		ivPlayButtonBottom.setVisibility(View.INVISIBLE);
		
	    }
	    
	    @Override
	    public void onPause() {
		ivPlayButtonBottom.setVisibility(View.VISIBLE);
		
	    }

	});
	vvRecordedVideoBottom.setOnCompletionListener(new OnCompletionListener() {
	    
	    @Override
	    public void onCompletion(MediaPlayer mp) {
		
		ivPlayButtonBottom.setVisibility(View.VISIBLE);
	    }
	});
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	case R.id.ivGalleryTop:

	    break;
	case R.id.ivCameraTop:
	    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i , REQUEST_CODE_TAKE_PICTURE_TOP);
	    break;
	case R.id.ivRecorderTop:
	    openCameraRecordModeTop(VIDEO_CAPTURE_TOP,"top");
	    break;
	case R.id.ivPlayButtonTop:
	     vvRecordedVideoTop.start();
	     ivPlayButtonTop.setVisibility(View.INVISIBLE);
	    break;
	   
	    
	    
	    
	case R.id.ivPlayButtonBottom:
	    vvRecordedVideoBottom.start();
	     ivPlayButtonBottom.setVisibility(View.INVISIBLE);
	    break;
	case R.id.ivGalleryBottom:

	    break;
	case R.id.ivCameraBottom:
	    Intent i1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i1 , REQUEST_CODE_TAKE_PICTURE_BOTTOM);
	    break;
	case R.id.ivRecorderBottom:
	    openCameraRecordModeBottom(VIDEO_CAPTURE_BOTTOM,"bottom");
	    break;
	case R.id.btn_submit_post:
	    submitPost();
	    break;
	    
	case R.id.btn_caption_post:
	    showCaptionDlg();
	    break;
	    
	case R.id.btn_cancel_post:
	    rlViewPanelTop.setVisibility(View.INVISIBLE);
	    rlControlPanelTop.setVisibility(View.VISIBLE);
	    llSettingsPanel.setVisibility(View.GONE); 
	    rlViewPanelBottom.setVisibility(View.INVISIBLE);
	    rlControlPanelBottom.setVisibility(View.VISIBLE);
	    PostStatusTop = false;
	    PostStatusBottom = false;
	    isPostVideoTop = false;
	    isPostVideoBottom = false;
	    break;

	default:
	    break;
	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == REQUEST_CODE_TAKE_PICTURE_TOP && resultCode == RESULT_OK	&& null != data) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    @SuppressWarnings("deprecation")
	    Cursor cursor = managedQuery(  MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,   null, null, null);
	    int column_index_data = cursor .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToLast();
	    postPathTop = cursor.getString(column_index_data);
	    /*System.out.println("!-- path  " );
	    System.out.println("!-- path  "+postPathTop );
	    Log.e("Path", postPathTop);*/
	   /* File imgFile = new  File(imagePath);
	    if(imgFile.exists())
	    {
		ivSnapshotImageTop.setImageResource(R.drawable.test);

	    }*/
	    Bitmap bitmap = BitmapFactory.decodeFile(postPathTop);
	    ivSnapshotImageTop.setImageBitmap(bitmap);
	    
	    rlViewPanelTop.setVisibility(View.VISIBLE);
	    rlControlPanelTop.setVisibility(View.GONE);
	    ivSnapshotImageTop.setVisibility(View.VISIBLE);
	    ivPlayButtonTop.setVisibility(View.GONE);
	    vvRecordedVideoTop.setVisibility(View.GONE);
	    PostStatusTop = true;
	    isPostVideoTop = false;
	    
	    if(PostStatusBottom){
		if(isPostVideoBottom){
		    rlViewPanelBottom.setVisibility(View.VISIBLE);
			rlControlPanelBottom.setVisibility(View.GONE);
			ivSnapshotImageBottom.setVisibility(View.GONE);
			ivPlayButtonBottom.setVisibility(View.VISIBLE);
			vvRecordedVideoBottom.setVisibility(View.VISIBLE);
			
			File f = new File(mediaFileBottom.getAbsolutePath());
			vvRecordedVideoBottom.setMediaController(new MediaController(this));
			vvRecordedVideoBottom.setVideoPath(Uri.fromFile(f).toString());
			vvRecordedVideoBottom.seekTo(100); 
		    
		}else{
		    Bitmap bitmap1 = BitmapFactory.decodeFile(postPathBottom);
		    ivSnapshotImageBottom.setImageBitmap(bitmap1);
		    
		    rlViewPanelBottom.setVisibility(View.VISIBLE);
		    rlControlPanelBottom.setVisibility(View.GONE);
		    ivSnapshotImageBottom.setVisibility(View.VISIBLE);
		    ivPlayButtonBottom.setVisibility(View.GONE);
		    vvRecordedVideoBottom.setVisibility(View.GONE); 
		}
	    }
	    
	}
	
	
	if (requestCode == VIDEO_CAPTURE_TOP ) {
	    if (resultCode == RESULT_OK) {
		//uriTop = data.getData();
		postPathTop = mediaFileTop.getAbsolutePath();
		
		
		rlViewPanelTop.setVisibility(View.VISIBLE);
		rlControlPanelTop.setVisibility(View.GONE);
		ivSnapshotImageTop.setVisibility(View.GONE);
		ivPlayButtonTop.setVisibility(View.VISIBLE);
		vvRecordedVideoTop.setVisibility(View.VISIBLE);
		
		fileTop = new File(mediaFileTop.getAbsolutePath());
		vvRecordedVideoTop.setMediaController(new MediaController(this));
		vvRecordedVideoTop.setVideoPath(Uri.fromFile(fileTop).toString());
		vvRecordedVideoTop.seekTo(100);
		isPostVideoTop = true;
		PostStatusTop = true;
		
	    } else if (resultCode == RESULT_CANCELED) {
		Toast.makeText(this, "Video recording cancelled.",Toast.LENGTH_LONG).show();		
	    } else {

	    }
	    
	}
	if (requestCode == REQUEST_CODE_TAKE_PICTURE_BOTTOM && resultCode == RESULT_OK	&& null != data) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    @SuppressWarnings("deprecation")
	    Cursor cursor = managedQuery(  MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,   null, null, null);
	    int column_index_data = cursor .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToLast();
	    postPathBottom = cursor.getString(column_index_data);
	    Log.e("Path", postPathBottom);
	    Bitmap bitmap = BitmapFactory.decodeFile(postPathBottom);
	    ivSnapshotImageBottom.setImageBitmap(bitmap);
	    
	    rlViewPanelBottom.setVisibility(View.VISIBLE);
	    rlControlPanelBottom.setVisibility(View.GONE);
	    ivSnapshotImageBottom.setVisibility(View.VISIBLE);
	    ivPlayButtonBottom.setVisibility(View.GONE);
	    vvRecordedVideoBottom.setVisibility(View.GONE);
	    PostStatusBottom = true;
	    isPostVideoBottom = false;
	    if(PostStatusTop){
		if(isPostVideoTop){
		    rlViewPanelTop.setVisibility(View.VISIBLE);
			rlControlPanelTop.setVisibility(View.GONE);
			ivSnapshotImageTop.setVisibility(View.GONE);
			ivPlayButtonTop.setVisibility(View.VISIBLE);
			vvRecordedVideoTop.setVisibility(View.VISIBLE);
			
			fileBottom = new File(mediaFileTop.getAbsolutePath());
			vvRecordedVideoTop.setMediaController(new MediaController(this));
			vvRecordedVideoTop.setVideoPath(Uri.fromFile(fileBottom).toString());
			vvRecordedVideoTop.seekTo(100);
		}else{
		    Bitmap bitmap1 = BitmapFactory.decodeFile(postPathTop);
		    ivSnapshotImageTop.setImageBitmap(bitmap1);
		    
		    rlViewPanelTop.setVisibility(View.VISIBLE);
		    rlControlPanelTop.setVisibility(View.GONE);
		    ivSnapshotImageTop.setVisibility(View.VISIBLE);
		    ivPlayButtonTop.setVisibility(View.GONE);
		    vvRecordedVideoTop.setVisibility(View.GONE); 
		}
	    }
	}
	

	
	if (requestCode == VIDEO_CAPTURE_BOTTOM ) {
	    if (resultCode == RESULT_OK) {
		//uriBottom = data.getData();
		postPathBottom = mediaFileBottom.getAbsolutePath();
		
		
		rlViewPanelBottom.setVisibility(View.VISIBLE);
		rlControlPanelBottom.setVisibility(View.GONE);
		ivSnapshotImageBottom.setVisibility(View.GONE);
		ivPlayButtonBottom.setVisibility(View.VISIBLE);
		vvRecordedVideoBottom.setVisibility(View.VISIBLE);
		
		File f = new File(mediaFileBottom.getAbsolutePath());
		vvRecordedVideoBottom.setMediaController(new MediaController(this));
		vvRecordedVideoBottom.setVideoPath(Uri.fromFile(f).toString());
		vvRecordedVideoBottom.seekTo(100);
		isPostVideoBottom = true;
		PostStatusBottom = true;
	    } else if (resultCode == RESULT_CANCELED) {
		Toast.makeText(this, "Video recording cancelled.",Toast.LENGTH_LONG).show();		
	    } 
	    if(PostStatusTop){
			if(isPostVideoTop){
			    rlViewPanelTop.setVisibility(View.VISIBLE);
				rlControlPanelTop.setVisibility(View.GONE);
				ivSnapshotImageTop.setVisibility(View.GONE);
				ivPlayButtonTop.setVisibility(View.VISIBLE);
				vvRecordedVideoTop.setVisibility(View.VISIBLE);
				
				File f = new File(mediaFileTop.getAbsolutePath());
				vvRecordedVideoTop.setMediaController(new MediaController(this));
				vvRecordedVideoTop.setVideoPath(Uri.fromFile(f).toString());
				vvRecordedVideoTop.seekTo(100);
			}else{
			    Bitmap bitmap1 = BitmapFactory.decodeFile(postPathTop);
			    ivSnapshotImageTop.setImageBitmap(bitmap1);
			    
			    rlViewPanelTop.setVisibility(View.VISIBLE);
			    rlControlPanelTop.setVisibility(View.GONE);
			    ivSnapshotImageTop.setVisibility(View.VISIBLE);
			    ivPlayButtonTop.setVisibility(View.GONE);
			    vvRecordedVideoTop.setVisibility(View.GONE); 
			}
		    }
	    
	}
	
	if(PostStatusTop && PostStatusBottom){
	    llSettingsPanel.setVisibility(View.VISIBLE);
	}
    }
    
    
    public void openCameraRecordModeTop(int CODE , String pos){
	String timestamp = "1";
	    String timestamp1 = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss aa") .format(Calendar.getInstance().getTime());
	    File filepath = Environment.getExternalStorageDirectory();
	    File dir = new File(filepath.getAbsolutePath()+ "/pp_videos/");
	    dir.mkdirs();
	    mediaFileTop = new File(Environment.getExternalStorageDirectory() .getAbsolutePath() + "/pp_videos/Video_" + pos + ".mp4");
	    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    Uri fileUri = Uri.fromFile(mediaFileTop);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	    startActivityForResult(intent, CODE);
    }
    
    public void openCameraRecordModeBottom(int CODE , String pos){
	String timestamp = "1";
	    String timestamp1 = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss aa") .format(Calendar.getInstance().getTime());
	    File filepath = Environment.getExternalStorageDirectory();
	    File dir = new File(filepath.getAbsolutePath()+ "/pp_videos/");
	    dir.mkdirs();
	    mediaFileBottom = new File(Environment.getExternalStorageDirectory() .getAbsolutePath() + "/pp_videos/Video_" + pos + ".mp4");
	    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    Uri fileUri = Uri.fromFile(mediaFileBottom);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	    startActivityForResult(intent, CODE);
    }
    
    public void createFolder() {
   	File mydir = new File(Environment.getExternalStorageDirectory()	.getAbsolutePath()+ "/pp_videos/");
   	if (!mydir.exists())
   	    mydir.mkdirs();
   	else
   	    Log.d("error", "dir. already exists");
       }
    
    public void submitPost(){
	if(!(postTag.length()>0)){
	    Toast.makeText(MakePairPostActivity.this, "Please enter caption", Toast.LENGTH_LONG).show();
	    return;
	}
	if(PostStatusTop){
	    if(PostStatusBottom){
		if(postPathTop.contains(".mp4")){
		    postTypeTop = TYPE_VIDEO;
		    System.out.println("!-- path "+postPathTop);
		    System.out.println("!-- URI "+Uri.fromFile(new File(postPathTop)));
		   /* uriPostTop =  */uploadYoutube(true,postPathTop,Uri.fromFile(new File(postPathTop)));
		}else{
		    postTypeTop = TYPE_IMAGE; 
		    uriPostTop = postPathTop;
		}
		if(postPathBottom.contains(".mp4")){
		    postTypeBottom = TYPE_VIDEO;
		    /*uriPostBottom = */uploadYoutube(false,postPathBottom,Uri.fromFile(new File(postPathBottom)));
		}else{
		    postTypeBottom = TYPE_IMAGE;
		    uriPostBottom = postPathBottom;
		}
		
		Log.e("webservice call", "webservice call");
		
		new UploadTask().execute();
		
	    }else{
		Toast.makeText(MakePairPostActivity.this, "Top post is empty", Toast.LENGTH_SHORT).show();
	    }
	}else{
	    Toast.makeText(MakePairPostActivity.this, "Bottom post is empty", Toast.LENGTH_SHORT).show();
	}
    }
   private void uploadYoutube(final Boolean isTop,final String path, final Uri data) {
	
	
	 
        new AsyncTask<Void, Integer, String>() {
            
            @Override
	    protected void onPreExecute() {
		super.onPreExecute();
		 progressBar.setVisibility(View.VISIBLE);
	    }
            
            @Override
            protected String doInBackground(Void... params) {
                YoutubeUploadRequest request = new YoutubeUploadRequest();
                request.setUri(data);
                //request.setCategory(category);
                //request.setTags(tags);
                request.setTitle("PAIR POST");
                request.setDescription(postTag);

                String id = YoutubeUploader.upload(path,request, new ProgressListner() {

                    @Override
                    public void onUploadProgressUpdate(int progress) {

                        publishProgress(progress);
                    }
                }, MakePairPostActivity.this);
                if(isTop){
                    uriPostTop = id; 
                }else{
                    uriPostBottom = id;
                }
                
                System.out.println("!-- Youtube ID "+id);
                return id;
            } 

	    @Override
	    protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// progressBar.setVisibility(View.INVISIBLE);
		//youtubeId = result;
		//Toast.makeText(getApplicationContext(), "Test "+result , 10000).show();
	    }	 
        }.execute();
	
    }
    
    
    
    class UploadTask extends AsyncTask<String, Void, String> {
		@Override
	protected String doInBackground(String... param) {
	   	try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();					
				HttpPost httpPost = new HttpPost(Constant.Url.POST_UPLOAD);
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);						
				
				entity.addPart("id", new StringBody(application.getLoggedUserId()));
				Log.e("pair_post_id", ""+"2");
				entity.addPart("caption", new StringBody(postTag));
				Log.e("caption", "test caption");
				if(postTypeTop.equalsIgnoreCase(TYPE_VIDEO)){
				    entity.addPart("postTypeTop", new StringBody("video")); 
				    Log.e("postTypeTop", "video");
				    entity.addPart("urlTop", new StringBody(uriPostTop));
				    Log.e("urlTop", uriPostTop);
				}else{
				    entity.addPart("postTypeTop", new StringBody("image")); 
				    Log.e("postTypeTop", "image");
				    entity.addPart("urlTop", new FileBody(new File(uriPostTop)));
				    Log.e("urlTop", ""+uriPostTop);
				}
				
				if(postTypeBottom.equalsIgnoreCase(TYPE_VIDEO)){
				    entity.addPart("postTypeBottom", new StringBody("video")); 
				    Log.e("postTypeBottom", "video");
				    entity.addPart("urlBottom", new StringBody(uriPostBottom));
				    Log.e("urlBottom", uriPostBottom);
				}else{
				    entity.addPart("postTypeBottom", new StringBody("image"));
				    Log.e("postTypeBottom", "image");
				    entity.addPart("urlBottom", new FileBody(new File(uriPostBottom)));
				    Log.e("urlBottom", ""+uriPostBottom);
				}
				
				httpPost.setEntity(entity);
				
				HttpResponse response;
				response = httpClient.execute(httpPost);
				resEntity = response.getEntity();
			
			
            final String response_str = EntityUtils.toString(resEntity);
            System.out.println("!--  Response "+ response_str);
            Log.e("TAG12","Response "+ response_str);
            return response_str;
		} catch (Exception e) {
		    runOnUiThread(new Runnable() {
		        
		        @Override
		        public void run() {
		            progressBar.setVisibility(View.GONE);
		    	
		        }
		    });
		   
			return null;
		}
	}

	@Override
	protected void onProgressUpdate(Void... unsued) {

	}
	
	

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPostExecute(String sResponse) {	
	    progressBar.setVisibility(View.GONE);
        	if(sResponse!=null){
        	    try {
			JSONObject json = new JSONObject(sResponse);
			if(json.getBoolean("status")){
			    Toast.makeText(MakePairPostActivity.this, "Successfully Upload", Toast.LENGTH_LONG).show();
	        	    rlViewPanelTop.setVisibility(View.INVISIBLE);
	        	    rlControlPanelTop.setVisibility(View.VISIBLE);
	        	    llSettingsPanel.setVisibility(View.GONE); 
	        	    rlViewPanelBottom.setVisibility(View.INVISIBLE);
	        	    rlControlPanelBottom.setVisibility(View.VISIBLE);
	        	    PostStatusTop = false;
	        	    PostStatusBottom = false;
	        	    isPostVideoTop = false;
	        	    isPostVideoBottom = false;
			}
		    } catch (JSONException e) {
			e.printStackTrace();
		    }
        	   
        	}
    }
}
    
    public void showCaptionDlg(){
	final Dialog dlg = new Dialog(this);
	dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dlg.setContentView(R.layout.dialog_caption);
	dlg.setCancelable(true);
	final EditText lbl_caption = (EditText)dlg.findViewById(R.id.lbl_caption);
	final TextView lbl_text_count = (TextView)dlg.findViewById(R.id.lbl_text_count);
	Button btnOK = (Button)dlg.findViewById(R.id.btnOK);
	
	btnOK.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		dlg.dismiss();

		postTag = lbl_caption.getText().toString().trim();
		if(postTag.length()>0){
		    ll_post_tag.setVisibility(View.VISIBLE);
		    lbl_post_tag.setText(postTag);
		}else{
		    ll_post_tag.setVisibility(View.GONE);
		}
		
	    
	    }
	});
	dlg.setOnDismissListener(new OnDismissListener() {
	    
	    @Override
	    public void onDismiss(DialogInterface dialog) {
		postTag = lbl_caption.getText().toString().trim();
		if(postTag.length()>0){
		    ll_post_tag.setVisibility(View.VISIBLE);
		    lbl_post_tag.setText(postTag);
		}else{
		    ll_post_tag.setVisibility(View.GONE);
		}
		
	    }
	});
	lbl_caption.addTextChangedListener(new TextWatcher() {
	    
	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
		lbl_text_count.setText(""+lbl_caption.getText().toString().trim().length()+"/140");
		
	    }
	    
	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		
		
	    }
	    
	    @Override
	    public void afterTextChanged(Editable s) {
		
		
	    }
	});
	dlg.show();
    }
}