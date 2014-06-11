/**
 * @author Ratul Ghosh
 * 08-Apr-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appsbee.pairpost.R;

public class CameraTest extends Activity implements Callback, OnClickListener
{

	private SurfaceView svPreview1, svPreview2;
	private SurfaceHolder holder1, holder2;
	private Button btCapture1, btCapture2, btCaptureImage1, btCaptureImage2,
			btCaptureVideo1, btCaptureVideo2;
	private Camera camera;
	private MediaRecorder mediaRecorder;

	private PictureCallback pictureCallback1;

	private boolean isPlayingOnTopView = true;

	private boolean isImageCaptureOnSurface1 = true;
	private boolean isImageCaptureOnSurface2 = true;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_test);
		svPreview1 = (SurfaceView) findViewById(R.id.svCameraPreview1);
		svPreview2 = (SurfaceView) findViewById(R.id.svCameraPreview2);

		holder1 = svPreview1.getHolder();
		holder1.addCallback(this);
		holder1.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		holder2 = svPreview2.getHolder();
		holder2.addCallback(this);
		holder2.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		btCapture1 = (Button) findViewById(R.id.btCaptureCamera1);

		btCapture2 = (Button) findViewById(R.id.btCaptureCamera2);

		btCaptureImage1 = (Button) findViewById(R.id.btImageCapture1);
		btCaptureImage2 = (Button) findViewById(R.id.btImageCapture2);

		btCaptureVideo1 = (Button) findViewById(R.id.btVideoCapture1);
		btCaptureVideo2 = (Button) findViewById(R.id.btVideoCapture2);

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
		btCapture1.setOnClickListener(this);
		btCapture2.setOnClickListener(this);

		btCaptureImage1.setOnClickListener(this);
		btCaptureImage2.setOnClickListener(this);
		btCaptureVideo1.setOnClickListener(this);
		btCaptureVideo2.setOnClickListener(this);

		svPreview1.setOnClickListener(this);
		svPreview2.setOnClickListener(this);

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

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.svCameraPreview1:
				if (!isPlayingOnTopView)
				{
					try
					{
						stopCamera();
						startCamera();
						camera.setPreviewDisplay(holder1);
						camera.startPreview();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					isPlayingOnTopView = true;

				}
				break;
			case R.id.svCameraPreview2:
				if (isPlayingOnTopView)
				{
					try
					{
						/*						stopCamera();
												startCamera();
												
						*/
						if (camera != null)
						{
							camera.stopPreview();
							camera.setPreviewDisplay(holder2);
							camera.startPreview();
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					isPlayingOnTopView = false;
				}

				break;
			case R.id.btCaptureCamera1:
				if (isImageCaptureOnSurface1)
					camera.takePicture(null, null, pictureCallback1);
				break;
			case R.id.btCaptureCamera2:
				if (isImageCaptureOnSurface2)
					camera.takePicture(null, null, pictureCallback1);
				else
				{
					stopCamera();
					startCamera();

					mediaRecorder = new MediaRecorder();

					// Step 1: Unlock and set camera to MediaRecorder
					camera.unlock();
					mediaRecorder.setCamera(camera);

					// Step 2: Set sources
					mediaRecorder
							.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
					mediaRecorder
							.setVideoSource(MediaRecorder.VideoSource.CAMERA);

					// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
					/*mediaRecorder
							.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					mediaRecorder.setMaxDuration(30 * 1000);*/
					mediaRecorder.setProfile(CamcorderProfile
							.get(CamcorderProfile.QUALITY_HIGH));
					/*				mediaRecorder
											.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
									mediaRecorder
											.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);*/
					// Step 4: Set output file
					File f = new File(String.format("/sdcard/%d.mp4",
							System.currentTimeMillis()));
					try
					{
						f.createNewFile();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					mediaRecorder.setOutputFile(String.format("/sdcard/%d.mp4",
							System.currentTimeMillis()));

					// Step 5: Set the preview output
					mediaRecorder.setPreviewDisplay(holder2.getSurface());

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
								"IOException preparing MediaRecorder: "
										+ e.getMessage());
						camera.lock();
						mediaRecorder.release();
					}
					mediaRecorder.start();

				}
				break;
			case R.id.btImageCapture1:
				isImageCaptureOnSurface1 = true;
				break;
			case R.id.btVideoCapture1:
				isImageCaptureOnSurface1 = false;
				break;
			case R.id.btImageCapture2:
				isImageCaptureOnSurface2 = true;
				break;
			case R.id.btVideoCapture2:
				isImageCaptureOnSurface2 = false;
				break;
			default:
				break;
		}

	}
}
