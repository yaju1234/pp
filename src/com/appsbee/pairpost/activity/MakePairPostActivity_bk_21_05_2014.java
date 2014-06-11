/**
 * @author Ratul Ghosh
 * 10-Apr-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.appsbee.pairpost.R;

public class MakePairPostActivity_bk_21_05_2014 extends BaseActivity implements
		OnClickListener
{
	private static final int EX_FILE_PICKER_RESULT = 100;
	

	private SurfaceView svCameraPreviewTop, svCameraPreviewBottom;
	private SurfaceHolder holderTop, holderBottom;
	private RelativeLayout rlControlPanelTop, rlControlPanelBottom,
			rlCameraPreviewTop, rlCameraPreviewBottom;

	private ImageView ivCamTop,/* ivGalleryTop,*/ ivRecorderTop, ivCamBottom,
			/*ivGalleryBottom,*/ ivRecorderBottom;
	private ProgressBar pbTimeLineTop, pbTimeLineBottom;
	private TextView tvTimeSpanTop, tvTimeSpanBottom;

	private Button /*btGalleryTop,*/ btTakePhotoTop, btRecordTop,
			btRotateCameraTop, btFlashCameraTop, /*btGalleryBottom,*/
			btTakePhotoBottom, btRecordBottom, btRotateCameraBottom,
			btFlashCameraBottom;
	
	private VideoView vv_top,vv_bottom;
	private ImageView iv_topScreenshot, iv_bottomScreenshot;
	private ImageView iv_topVideoPlayBtn,iv_bottomVideoPlayBtn;

	private Camera camera;
	private int noOfCameras;

	private int presentTopCameraID = CameraInfo.CAMERA_FACING_BACK;
	private int presentBottomCameraID = CameraInfo.CAMERA_FACING_BACK;
	private int currentCameraId = CameraInfo.CAMERA_FACING_BACK;

	private boolean isDisplayingCameraOnTop;
	private boolean isFlashPresent;
	private boolean isFlashOn;
	private boolean isTopPaused;
	private boolean isBottomPaused;
	private boolean isRecordingTop, isRecordingBottom;
	private boolean isGalleryTop;

	private MediaRecorder mediaRecorder;
	private PictureCallback pictureCallback;
	private Handler uiThreadHandler;

	private File topPictureFile, bottomPictureFile, topVideoFile,
			bottomVideoFile;

	private Timer topProgressBarAdvancer, bottomProgressBarAdvancer;
	private String topGalleryFilePath, bottomGalleryFilePath;
	private String  video_file_name;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_make_pair_post);

		findViewsById();
		setOnClickListeners();

		holderTop = svCameraPreviewTop.getHolder();
		holderBottom = svCameraPreviewBottom.getHolder();

		holderTop.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holderBottom.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		uiThreadHandler = new Handler();
		pictureCallback = new PictureCallback()
		{

			@Override
			public void onPictureTaken(byte[] data, Camera camera)
			{
				FileOutputStream outStream = null;
				try
				{
					String filePath = String.format("/sdcard/%d.jpg",
							System.currentTimeMillis());

					outStream = new FileOutputStream(filePath);

					outStream.write(data);
					outStream.close();

					if (isDisplayingCameraOnTop){
						topPictureFile = new File(filePath);
						Bitmap bitmap = BitmapFactory.decodeFile(filePath);
						rlControlPanelTop.setVisibility(View.GONE);
						rlCameraPreviewTop.setVisibility(View.GONE);
						iv_topVideoPlayBtn.setVisibility(View.GONE);
						vv_top.setVisibility(View.GONE);
						iv_topScreenshot.setVisibility(View.VISIBLE);
						iv_topScreenshot.setImageBitmap(bitmap);
					}else{
						bottomPictureFile = new File(filePath);
						Bitmap bitmap = BitmapFactory.decodeFile(filePath);
						rlControlPanelBottom.setVisibility(View.GONE);
						rlCameraPreviewBottom.setVisibility(View.GONE);
						iv_bottomVideoPlayBtn.setVisibility(View.GONE);
						vv_bottom.setVisibility(View.GONE);
						iv_bottomScreenshot.setVisibility(View.VISIBLE);
						iv_bottomScreenshot.setImageBitmap(bitmap);
					}
					Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
					/*uiThreadHandler.post(new Runnable()
					{

						@Override
						public void run()
						{
							if (isTopPaused)
							{
								if (isDisplayingCameraOnTop)
								{

									startTopPreview();
								}

								isTopPaused = false;
							}
							if (isBottomPaused)
							{
								if (!isDisplayingCameraOnTop)
								{
									startBottomPreview();
								}
							}
						}
					});*/
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{

				}
				Log.d("Log", "onPictureTaken - jpeg");
			}
		};

		isFlashPresent = getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH);
		noOfCameras = Camera.getNumberOfCameras();

		if (noOfCameras != 2)
		{
			btRotateCameraBottom.setVisibility(View.INVISIBLE);
			btRotateCameraTop.setVisibility(View.INVISIBLE);
		}

		if (!isFlashPresent)
		{
			btFlashCameraBottom.setVisibility(View.INVISIBLE);
			btFlashCameraTop.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (isDisplayingCameraOnTop)
			startCamera(presentTopCameraID);
		else
			startCamera(presentBottomCameraID);
	}

	@Override
	protected void onPause()
	{
		stopCamera();
		super.onPause();

	}

	private void startCamera(int cameraId)
	{
		if (camera == null || currentCameraId != cameraId)
		{
			if (currentCameraId != cameraId) stopCamera();

			camera = Camera.open(cameraId);
			camera.setDisplayOrientation(90);
			currentCameraId = cameraId;
		}
	}

	private void stopCamera()
	{
		if (camera != null)
		{
		    camera.stopPreview();
		    camera.setPreviewCallback(null);
		    camera.unlock();
		    camera.release();
		    camera = null;
			/*camera.stopPreview();
			camera.release();
			camera = null;*/
		}
	}

	private void restartCamera()
	{
		stopCamera();
		if (isDisplayingCameraOnTop)
			startCamera(presentTopCameraID);
		else
			startCamera(presentBottomCameraID);
	}

	private void toggleFlash()
	{
		if (camera != null)
		{
			Parameters parameters = camera.getParameters();
			if (isFlashOn)
			{
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				isFlashOn = false;
			}
			else
			{
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				isFlashOn = true;
			}
			camera.setParameters(parameters);
		}
		else
		{
			Toast.makeText(this, "Camera is turned off", Toast.LENGTH_LONG)
					.show();
		}

	}

	private void setOnClickListeners()
	{
		btFlashCameraBottom.setOnClickListener(this);
		//btGalleryBottom.setOnClickListener(this);
		btRecordBottom.setOnClickListener(this);
		btRotateCameraBottom.setOnClickListener(this);
		btTakePhotoBottom.setOnClickListener(this);

		btFlashCameraTop.setOnClickListener(this);
		//btGalleryTop.setOnClickListener(this);
		btRecordTop.setOnClickListener(this);
		btRotateCameraTop.setOnClickListener(this);
		btTakePhotoTop.setOnClickListener(this);

		ivCamTop.setOnClickListener(this);
		//ivGalleryTop.setOnClickListener(this);
		ivRecorderTop.setOnClickListener(this);

		ivCamBottom.setOnClickListener(this);
		//ivGalleryBottom.setOnClickListener(this);
		ivRecorderBottom.setOnClickListener(this);

		svCameraPreviewTop.setOnClickListener(this);
		svCameraPreviewBottom.setOnClickListener(this);
	}

	private void enableTopPreviewPanel()
	{
		btFlashCameraTop.setEnabled(true);
		//btGalleryTop.setEnabled(true);
		btRecordTop.setEnabled(true);
		btRotateCameraTop.setEnabled(true);
		btTakePhotoTop.setEnabled(true);
		svCameraPreviewTop.setEnabled(true);
	}

	private void disableTopPreviewPanel()
	{
		btFlashCameraTop.setEnabled(false);
		//btGalleryTop.setEnabled(false);
		btRecordTop.setEnabled(false);
		btRotateCameraTop.setEnabled(false);
		btTakePhotoTop.setEnabled(false);
	}

	private void findViewsById()
	{
		svCameraPreviewTop = (SurfaceView) findViewById(R.id.svCameraPreviewTop);
		svCameraPreviewBottom = (SurfaceView) findViewById(R.id.svCameraPreviewBottom);

		rlControlPanelTop = (RelativeLayout) findViewById(R.id.rlControlPanelTop);
		rlControlPanelBottom = (RelativeLayout) findViewById(R.id.rlControlPanelBottom);

		rlCameraPreviewTop = (RelativeLayout) findViewById(R.id.rlCameraPreviewPanelTop);
		rlCameraPreviewBottom = (RelativeLayout) findViewById(R.id.rlCameraPreviewPanelBottom);

		ivCamTop = (ImageView) findViewById(R.id.ivCameraTop);
		ivCamBottom = (ImageView) findViewById(R.id.ivCameraBottom);

		//ivGalleryTop = (ImageView) findViewById(R.id.ivGalleryTop);
		//ivGalleryBottom = (ImageView) findViewById(R.id.ivGalleryBottom);

		ivRecorderTop = (ImageView) findViewById(R.id.ivRecorderTop);
		ivRecorderBottom = (ImageView) findViewById(R.id.ivRecorderBottom);

		pbTimeLineTop = (ProgressBar) findViewById(R.id.pbTopPreviewTimeLine);
		pbTimeLineBottom = (ProgressBar) findViewById(R.id.pbBottomPreviewTimeLine);

		tvTimeSpanTop = (TextView) findViewById(R.id.tvTopPreviewTime);
		tvTimeSpanBottom = (TextView) findViewById(R.id.tvBottomPreviewTime);

		//btGalleryTop = (Button) findViewById(R.id.btGalleryTop);
		//btGalleryBottom = (Button) findViewById(R.id.btGalleryBottom);

		btTakePhotoTop = (Button) findViewById(R.id.btTakePhotoTop);
		btTakePhotoBottom = (Button) findViewById(R.id.btTakePhotoBottom);

		btRecordTop = (Button) findViewById(R.id.btTakeVideoTop);
		btRecordBottom = (Button) findViewById(R.id.btTakeVideoBottom);

		btFlashCameraTop = (Button) findViewById(R.id.btFlashCameraTop);
		btFlashCameraBottom = (Button) findViewById(R.id.btFlashCameraBottom);

		btRotateCameraTop = (Button) findViewById(R.id.btRotateCameraTop);
		btRotateCameraBottom = (Button) findViewById(R.id.btRotateCameraBottom);
		
		
		vv_top = (VideoView)findViewById(R.id.vvTopRecordedVideo);
		vv_bottom = (VideoView)findViewById(R.id.vvBottomRecordedVideo);
		iv_topScreenshot = (ImageView)findViewById(R.id.ivTopSnapshotImage);
		iv_bottomScreenshot = (ImageView)findViewById(R.id.ivBottomSnapshotImage);
		iv_topVideoPlayBtn = (ImageView)findViewById(R.id.iv_post1_video_play_icon);
		iv_bottomVideoPlayBtn = (ImageView)findViewById(R.id.iv_post2_video_play_icon);
	}

	public void pickFile()
	{
		Intent intent = new Intent(getApplicationContext(),
				ru.bartwell.exfilepicker.ExFilePickerActivity.class);
		intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
		intent.putExtra(ExFilePicker.SET_FILTER_LISTED, new String[] { "jpg",
				"jpeg", "png", "mp4", "3gp" });
		intent.putExtra(ExFilePicker.DISABLE_NEW_FOLDER_BUTTON, true);

		intent.putExtra(ExFilePicker.SET_CHOICE_TYPE,
				ExFilePicker.CHOICE_TYPE_FILES);

		startActivityForResult(intent, EX_FILE_PICKER_RESULT);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == EX_FILE_PICKER_RESULT)
		{
			if (data != null)
			{
				ExFilePickerParcelObject object = (ExFilePickerParcelObject) data
						.getParcelableExtra(ExFilePickerParcelObject.class
								.getCanonicalName());
				if (object.count > 0)
				{
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < object.count; i++)
					{
						buffer.append(object.names.get(i));
						if (i < object.count - 1) buffer.append(", ");
					}
					if (isGalleryTop)
					{
						topGalleryFilePath = object.path;
						Toast.makeText(this,
								"You have picked " + topGalleryFilePath,
								Toast.LENGTH_LONG).show();
					}
					else
					{
						bottomGalleryFilePath = object.path;
						Toast.makeText(this,
								"You have picked " + bottomGalleryFilePath,
								Toast.LENGTH_LONG).show();

					}

					/*
					 * ((TextView) findViewById(R.id.result)).setText("Count: "
						+ object.count + "\n" + "Path: " + object.path
						+ "\n" + "Selected: " + buffer.toString());
						
					*/

				}
			}
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{

		/*case R.id.ivGalleryTop:
		case R.id.btGalleryTop:
			isGalleryTop = true;
			pickFile();
			break;
		case R.id.ivGalleryBottom:
		case R.id.btGalleryBottom:
			isGalleryTop = false;
			pickFile();
			break;*/
			
			
		case R.id.ivCameraTop: //Snapshot for camera TOP
			rlControlPanelTop.setVisibility(View.INVISIBLE);
			rlCameraPreviewTop.setVisibility(View.VISIBLE);
			isDisplayingCameraOnTop = true;
			showinTopCameraMode();
			startCamera(presentTopCameraID);
			startTopPreview();
			break;
		case R.id.ivCameraBottom: //Snapshot for camera Bottom
			rlControlPanelBottom.setVisibility(View.INVISIBLE);
			rlCameraPreviewBottom.setVisibility(View.VISIBLE);
			isDisplayingCameraOnTop = false;
			showinBottomCameraMode();
			startCamera(presentBottomCameraID);
			startBottomPreview();
			break;
			
		case R.id.ivRecorderTop:
			rlControlPanelTop.setVisibility(View.INVISIBLE);
			rlCameraPreviewTop.setVisibility(View.VISIBLE);
			isDisplayingCameraOnTop = true;
			showinTopRecordMode();
			if (isRecordingBottom)
			{
				btRecordBottom.setText("Record");
				stopRecording();
				stopBottomRecordingProgressBar();
				isRecordingBottom = false;
			}
			else
			{
				startCamera(presentTopCameraID);
				startTopPreview();
			}
			onClick(btRecordTop);

			break;
		case R.id.btTakeVideoTop:
		    
		   /* if(btRecordTop.getText().toString().equalsIgnoreCase("stop")){
			btRecordTop.setText("Record");
			File f = new File(video_file_name);
		//	stopRecording();
			   	//
			        topProgressBarAdvancer.cancel();
			        rlControlPanelTop.setVisibility(View.GONE);
				rlCameraPreviewTop.setVisibility(View.GONE);
				iv_topVideoPlayBtn.setVisibility(View.VISIBLE);
				vv_top.setVisibility(View.VISIBLE);
				iv_topScreenshot.setVisibility(View.GONE);
				stopRecording();
				camera.stopPreview();
				stopCamera();
				vv_top.setMediaController(new MediaController(this));
				vv_top.setVideoPath(Uri.fromFile(f).toString());
				vv_top.seekTo(100);
		    }else{*/
			if (isDisplayingCameraOnTop)
			{
				if (isRecordingTop)
				{
					btRecordTop.setText("Record");
					stopRecording();
					stopTopRecordingProgressBar();
					isRecordingTop = false;
				}
				else
				{
					showTopRecordingBar();
					createNewMediaRecorder(true);
					btRecordTop.setText("Stop");
					isRecordingTop = true;
					pbTimeLineTop.setProgress(0);
					tvTimeSpanTop.setText("00:00:00");
					mediaRecorder.start();

					topProgressBarAdvancer = new Timer();
					topProgressBarAdvancer.scheduleAtFixedRate(new TimerTask()
					{
						public void run()
						{
							pbTimeLineTop.setProgress(pbTimeLineTop
									.getProgress() + 1);
							tvTimeSpanTop.post(new Runnable()
							{
								@Override
								public void run()
								{
									tvTimeSpanTop.setText("00:00:"
											+ String.format("%02d",
													pbTimeLineTop.getProgress()));
								}
							});

						}
					}, 0, 1000);
				}

			}
			else
			{
				if (isRecordingBottom)
				{
					btRecordBottom.setText("Record");
					stopRecording();
					stopBottomRecordingProgressBar();
					isRecordingBottom = false;
				}
				isDisplayingCameraOnTop = true;
				startCamera(presentTopCameraID);
				startTopPreview();
				
			}
			
		   // }
			break;	
			
			
		case R.id.ivRecorderBottom:
			rlControlPanelBottom.setVisibility(View.INVISIBLE);
			rlCameraPreviewBottom.setVisibility(View.VISIBLE);
			isDisplayingCameraOnTop = false;
			showinBottomRecordMode();
			if (isRecordingTop)
			{
				btRecordTop.setText("Record");
				stopRecording();
				stopTopRecordingProgressBar();
				isRecordingTop = false;
			}
			else
			{
				startCamera(presentBottomCameraID);
				startBottomPreview();
			}
			onClick(btRecordBottom);
			break;
			
			
			
			
			
			
			
			

		case R.id.btRotateCameraTop:
			presentTopCameraID = (presentTopCameraID == CameraInfo.CAMERA_FACING_BACK) ? CameraInfo.CAMERA_FACING_FRONT
					: CameraInfo.CAMERA_FACING_BACK;
			isDisplayingCameraOnTop = true;
			restartCamera();
			startTopPreview();
			break;
		case R.id.btRotateCameraBottom:
			presentBottomCameraID = (presentBottomCameraID == CameraInfo.CAMERA_FACING_BACK) ? CameraInfo.CAMERA_FACING_FRONT
					: CameraInfo.CAMERA_FACING_BACK;
			isDisplayingCameraOnTop = false;
			restartCamera();
			startBottomPreview();
			break;
		case R.id.btFlashCameraTop:
		case R.id.btFlashCameraBottom:
			toggleFlash();
			break;
		
		
		case R.id.svCameraPreviewTop:
			if (isDisplayingCameraOnTop) break;
			//rlControlPanelTop.setVisibility(View.INVISIBLE);
			if (isRecordingBottom)
			{
				btRecordBottom.setText("Record");
				stopRecording();
				stopBottomRecordingProgressBar();
				isRecordingBottom = false;
			}
			rlCameraPreviewTop.setVisibility(View.VISIBLE);
			isDisplayingCameraOnTop = true;
			startCamera(presentTopCameraID);
			startTopPreview();
			break;
		case R.id.svCameraPreviewBottom:
			if (!isDisplayingCameraOnTop) break;
			if (isRecordingTop)
			{
				btRecordTop.setText("Record");
				stopRecording();
				stopTopRecordingProgressBar();
				isRecordingTop = false;
			}
			//rlControlPanelBottom.setVisibility(View.INVISIBLE);
			rlCameraPreviewBottom.setVisibility(View.VISIBLE);
			isDisplayingCameraOnTop = false;
			startCamera(presentBottomCameraID);
			startBottomPreview();
			break;
		case R.id.btTakePhotoTop:
			if (isDisplayingCameraOnTop)
				camera.takePicture(null, null, pictureCallback);
			else
			{
				isDisplayingCameraOnTop = true;
				startCamera(presentTopCameraID);
				startTopPreview();
			}
			break;
		case R.id.btTakePhotoBottom:
			if (!isDisplayingCameraOnTop)
				camera.takePicture(null, null, pictureCallback);
			else
			{
				isDisplayingCameraOnTop = false;
				startCamera(presentBottomCameraID);
				startBottomPreview();
			}
			break;
		
		case R.id.btTakeVideoBottom:
			if (!isDisplayingCameraOnTop)
			{
				if (isRecordingBottom)
				{
					btRecordBottom.setText("Record");
				}
				else
				{
					showBottomRecordingBar();
					createNewMediaRecorder(false);
					btRecordBottom.setText("Stop");
					isRecordingBottom = true;
					pbTimeLineBottom.setProgress(0);
					tvTimeSpanBottom.setText("00:00:00");
					mediaRecorder.start();

					bottomProgressBarAdvancer = new Timer();
					bottomProgressBarAdvancer.scheduleAtFixedRate(
							new TimerTask()
							{
								public void run()
								{
									pbTimeLineBottom
											.setProgress(pbTimeLineBottom
													.getProgress() + 1);
									tvTimeSpanBottom.post(new Runnable()
									{

										@Override
										public void run()
										{
											tvTimeSpanBottom.setText("00:00:"
													+ String.format(
															"%02d",
															pbTimeLineBottom
																	.getProgress()));
										}
									});
								}
							}, 0, 1000);

				}
			}
			else
			{
				isDisplayingCameraOnTop = false;
				if (isRecordingTop)
				{
					btRecordTop.setText("Record");
					stopRecording();
					stopTopRecordingProgressBar();
					isRecordingTop = false;
				}
				startCamera(presentBottomCameraID);
				startBottomPreview();
			}

			break;
		default:
			break;
		}
	}

	private void createNewMediaRecorder(final boolean isTop)
	{
		mediaRecorder = new MediaRecorder();

		// Step 1: Unlock and set camera to MediaRecorder
		camera.unlock();
		mediaRecorder.setCamera(camera);

		// Step 2: Set sources
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mediaRecorder.setMaxDuration(5 * 1000);
		/*			mediaRecorder.setProfile(CamcorderProfile
							.get(CamcorderProfile.QUALITY_HIGH));*/
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		// Step 4: Set output file
		video_file_name = String.format("/sdcard/Pairpost-%d.mp4",
			System.currentTimeMillis()); 
		
		mediaRecorder.setOutputFile(video_file_name);
		mediaRecorder.setOnInfoListener(new OnInfoListener()
		{

			@Override
			public void onInfo(MediaRecorder mr, int what, int extra)
			{
				if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED)
				{
					Toast.makeText(MakePairPostActivity_bk_21_05_2014.this, "20 sec is over",
							Toast.LENGTH_SHORT).show();
					rlControlPanelTop.setVisibility(View.GONE);
					rlCameraPreviewTop.setVisibility(View.GONE);
					iv_topVideoPlayBtn.setVisibility(View.VISIBLE);
					vv_top.setVisibility(View.VISIBLE);
					iv_topScreenshot.setVisibility(View.GONE);
					File f = new File(video_file_name);
					vv_top.setMediaController(new MediaController(MakePairPostActivity_bk_21_05_2014.this));
					vv_top.setVideoPath(Uri.fromFile(f).toString());
					vv_top.seekTo(100);
					stopRecording();

					if (isTop)
						isRecordingTop = false;
					else
						isRecordingBottom = false;

					if (isTop)
						stopTopRecordingProgressBar();
					else
						stopBottomRecordingProgressBar();

					if (isTop)
						btRecordTop.setText("Record");
					else
						btRecordBottom.setText("Record");
				}
			}
			
			
		});

		// Step 5: Set the preview output
		if (isTop)
			mediaRecorder.setPreviewDisplay(svCameraPreviewTop.getHolder()
					.getSurface());
		else
			mediaRecorder.setPreviewDisplay(svCameraPreviewBottom.getHolder()
					.getSurface());

		// Step 6: Prepare configured MediaRecorder
		try
		{
			mediaRecorder.prepare();
		}
		catch (IllegalStateException e)
		{
			Log.d(getClass().getSimpleName(),
					"IllegalStateException preparing MediaRecorder: "
							+ e.getMessage());
			camera.lock();
			mediaRecorder.release();

		}
		catch (IOException e)
		{
			Log.d(getClass().getSimpleName(),
					"IOException preparing MediaRecorder: " + e.getMessage());
			camera.lock();
			mediaRecorder.release();
		}
		
		/*if(isTop){
		    File f = new File(file_name);
		    
		       rlControlPanelTop.setVisibility(View.GONE);
			rlCameraPreviewTop.setVisibility(View.GONE);
			iv_topVideoPlayBtn.setVisibility(View.VISIBLE);
			vv_top.setVisibility(View.VISIBLE);
			iv_topScreenshot.setVisibility(View.GONE);			
			vv_top.setMediaController(new MediaController(this));
			vv_top.setVideoPath(Uri.fromFile(f).toString());
			vv_top.seekTo(100);  
		}*/
		
	}

	private void stopRecording()
	{
		mediaRecorder.stop();
		mediaRecorder.release();
		camera.lock();
	}

	private void stopTopRecordingProgressBar()
	{
		topProgressBarAdvancer.cancel();

	}

	private void stopBottomRecordingProgressBar()
	{
		bottomProgressBarAdvancer.cancel();

	}

	private void hideBottomRecordingBar()
	{
		tvTimeSpanBottom.setVisibility(View.INVISIBLE);
		pbTimeLineBottom.setVisibility(View.INVISIBLE);
	}

	private void showBottomRecordingBar()
	{
		tvTimeSpanBottom.setVisibility(View.VISIBLE);
		pbTimeLineBottom.setVisibility(View.VISIBLE);
	}

	private void hideTopRecordingBar()
	{
		tvTimeSpanTop.setVisibility(View.INVISIBLE);
		pbTimeLineTop.setVisibility(View.INVISIBLE);
	}

	private void showTopRecordingBar()
	{
		tvTimeSpanTop.setVisibility(View.VISIBLE);
		pbTimeLineTop.setVisibility(View.VISIBLE);
	}

	private void startBottomPreview()
	{
		camera.stopPreview();
		try
		{
			camera.setPreviewDisplay(holderBottom);
			camera.startPreview();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		hideBottomRecordingBar();

	}

	private void startTopPreview()
	{
		camera.stopPreview();
		try
		{
			camera.setPreviewDisplay(holderTop);
			camera.startPreview();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		hideTopRecordingBar();

	}
	
	public void showinTopCameraMode(){
	    findViewById(R.id.btTakeVideoTop).setVisibility(View.GONE);
	    findViewById(R.id.tvTopPreviewTime).setVisibility(View.GONE);
	    findViewById(R.id.btTakePhotoTop).setVisibility(View.VISIBLE);
	}
	
	public void showinTopRecordMode(){
	    findViewById(R.id.btTakeVideoTop).setVisibility(View.VISIBLE);
	    findViewById(R.id.tvTopPreviewTime).setVisibility(View.VISIBLE);
	    findViewById(R.id.btTakePhotoTop).setVisibility(View.GONE);
	}
	public void showinBottomCameraMode(){
	    findViewById(R.id.btTakeVideoBottom).setVisibility(View.GONE);
	    findViewById(R.id.tvBottomPreviewTime).setVisibility(View.GONE);
	    findViewById(R.id.btTakePhotoBottom).setVisibility(View.VISIBLE);
	}
	
	public void showinBottomRecordMode(){
	    findViewById(R.id.btTakeVideoBottom).setVisibility(View.VISIBLE);
	    findViewById(R.id.tvBottomPreviewTime).setVisibility(View.VISIBLE);
	    findViewById(R.id.btTakePhotoBottom).setVisibility(View.GONE);
	}
}
