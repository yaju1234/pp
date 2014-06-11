/**
 * @author Ratul Ghosh
 * 08-Apr-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbee.pairpost.R;

public class CameraTest2 extends Activity implements Callback, OnClickListener
{

	private SurfaceView svTest;
	private SurfaceHolder holder;
	private Button btRecord;
	private ProgressBar pbBar;
	private Timer progressBarAdvancer;
	private TextView tvTime;

	private Camera camera;
	private MediaRecorder mediaRecorder;

	private PictureCallback pictureCallback1;

	private boolean isRecording;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_test2);
		svTest = (SurfaceView) findViewById(R.id.svTest2);
		btRecord = (Button) findViewById(R.id.btRecord);
		pbBar = (ProgressBar) findViewById(R.id.pbTimeLine);
		tvTime = (TextView) findViewById(R.id.tvTime);
		pbBar.setMax(30);
		progressBarAdvancer = new Timer();
		holder = svTest.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		pictureCallback1 = new PictureCallback()
		{

			@Override
			public void onPictureTaken(byte[] data, Camera camera)
			{
				FileOutputStream outStream = null;
				try
				{
					outStream = new FileOutputStream(String.format(
							"/sdcard/%d.jpg", System.currentTimeMillis()));
					outStream.write(data);
					outStream.close();
					Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
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

		btRecord.setOnClickListener(this);

	}

	private void startCamera()
	{
		if (camera == null)
		{
			camera = Camera.open();
			camera.setDisplayOrientation(90);
		}

	}

	@Override
	protected void onPause()
	{

		if (isRecording) stopRecording();
		stopCamera();

		super.onPause();

	}

	private void stopCamera()
	{
		if (camera != null)
		{
			camera.stopPreview();
			camera.release();
		}
		camera = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{

	}

	private void createNewMediaRecorder(SurfaceHolder holder)
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
		mediaRecorder.setMaxDuration(30 * 1000);
		/*			mediaRecorder.setProfile(CamcorderProfile
							.get(CamcorderProfile.QUALITY_HIGH));*/
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		// Step 4: Set output file

		mediaRecorder.setOutputFile(String.format("/sdcard/Test2-%d.mp4",
				System.currentTimeMillis()));
		mediaRecorder.setOnInfoListener(new OnInfoListener()
		{

			@Override
			public void onInfo(MediaRecorder mr, int what, int extra)
			{
				if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED)
				{
					Toast.makeText(CameraTest2.this, "30 sec is over",
							Toast.LENGTH_SHORT).show();

					stopRecording();
					isRecording = false;
					progressBarAdvancer.cancel();
					btRecord.setText("Record");
				}

			}
		});

		// Step 5: Set the preview output
		mediaRecorder.setPreviewDisplay(holder.getSurface());

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
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btRecord:
			if (btRecord.getText().toString().equals("Record"))
			{
				startCamera();
				createNewMediaRecorder(holder);
				isRecording = true;
				pbBar.setProgress(0);
				tvTime.setText("00:00:00");
				mediaRecorder.start();
				btRecord.setText("Stop");
				progressBarAdvancer = new Timer();
				progressBarAdvancer.scheduleAtFixedRate(new TimerTask()
				{
					public void run()
					{
						pbBar.setProgress(pbBar.getProgress() + 1);
						tvTime.post(new Runnable()
						{

							@Override
							public void run()
							{
								tvTime.setText("00:00:"
										+ String.format("%02d",
												pbBar.getProgress()));
							}
						});

					}
				}, 0, 1000);

			}
			else
			{

				stopRecording();
				progressBarAdvancer.cancel();
				isRecording = false;
				btRecord.setText("Record");
			}
			break;
		default:
			break;
		}
	}

	private void stopRecording()
	{
		mediaRecorder.stop();
		mediaRecorder.release();
		camera.lock();
	}
}
