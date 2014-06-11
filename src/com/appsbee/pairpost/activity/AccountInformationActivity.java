/**
 * @author Ratul Ghosh
 * 25-Mar-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.util.ImageLoader;
import com.appsbee.pairpost.util.Util;

public class AccountInformationActivity extends BaseActivity implements
		OnClickListener
{

	private EditText etUserName, etPassword, etEmailId;
	private ToggleButton tbDeactivateAccount;
	private ImageView ivChangeUserImage;
	private ImageLoader imageLoader;
	private Button btSave;

	private Uri imageFileUri;
	private final int PICK_IMAGE = 1;
	private final int CAPTURE_IMAGE_FROM_CAMERA = 2;
	private String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_information);
		imageLoader = new ImageLoader(this);
		findViewsById();
		tbDeactivateAccount.setChecked(false);
		etUserName.setText(application.getLoggedUserName());

		btSave.setOnClickListener(this);
		ivChangeUserImage.setOnClickListener(this);
		if (application.getLoggedUserImageUri() != null)
		{
			imageLoader.displayImage(
					Constant.Url.ASSET_DIRECTORY
							+ application.getLoggedUserImageUri(),
					R.drawable.change_image, ivChangeUserImage);
		}
	}

	private void findViewsById()
	{
		btSave = (Button) findViewById(R.id.btSave);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etEmailId = (EditText) findViewById(R.id.etEmail);
		tbDeactivateAccount = (ToggleButton) findViewById(R.id.tbDeactivateAcoount);
		ivChangeUserImage = (ImageView) findViewById(R.id.ivChangeProfilePicture);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.ivChangeProfilePicture:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pick the photo");
				builder.setPositiveButton("Gallery",
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{

								uploadFromPhone();
							}
						});
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								dialog.dismiss();

							}
						});
				builder.setNeutralButton("Camera",
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								takePhoto();

							}
						});
				builder.show();
				break;
			case R.id.btSave:
				String userName = etUserName.getText().toString().trim();
				String password = etPassword.getText().toString().trim();
				String emailId = etEmailId.getText().toString().trim();
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", application.getLoggedUserId());
				param.put("deactivate_account",
						tbDeactivateAccount.isChecked() ? "true" : "false");
				if (userName.length() != 0
						&& !userName.equals(application.getLoggedUserName()))
				{

					param.put("username", userName);
				}
				if (password.length() != 0)
				{
					param.put("password", password);
				}
				if (emailId.length() != 0)
				{
					param.put("email", emailId);
				}
				new UpdateUserAccountInfoAsyncTask().execute(param);
				break;
			default:
				break;
		}
	}

	private class UpdateUserAccountInfoAsyncTask extends
			AsyncTask<HashMap<String, String>, Void, String>
	{
		private ProgressDialog pd;

		@Override
		protected void onPreExecute()
		{

			super.onPreExecute();
			pd = new ProgressDialog(AccountInformationActivity.this);
			pd.setTitle("Uploading");
			pd.setMessage("Please Wait");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(HashMap<String, String>... params)
		{
			HashMap<String, String> param = params[0];
			if (filePath != null)
			{
				try
				{
					return httpClient.sendHttpPostWithJson(
							Constant.Url.UPDATE_PROFILE, param, new File(
									filePath));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					if (Util.isJsonResponseOK(AccountInformationActivity.this,
							httpClient.sendHttpPostWithJson(
									Constant.Url.UPDATE_PROFILE, param)))
					{
						param = new HashMap<String, String>();
						param.put("member_id", application.getLoggedUserId());
						String result = httpClient.sendHttpPostWithJson(
								Constant.Url.GET_ONE_USER_INFO, param);
						if (Util.isJsonResponseOK(
								AccountInformationActivity.this, result))
						{
							JSONObject jUser = new JSONObject(result)
									.getJSONObject("data")
									.getJSONObject("user");
							application.setLoggedUserName(jUser
									.getString("user_name"));
							application.setLoggedUserImageUri(jUser
									.getString("user_image_url"));
							application.setUserInfoLastUpdatedTime(System
									.currentTimeMillis());
						}
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) pd.dismiss();
			if (Util.isJsonResponseOK(AccountInformationActivity.this, result))
			{
				Toast.makeText(AccountInformationActivity.this,
						"Updated Succesfully", Toast.LENGTH_LONG).show();
				finish();
			}
		}

	}

	private void takePhoto()
	{
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED))
		{
			imageFileUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new ContentValues());
			Intent i = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
			startActivityForResult(i, CAPTURE_IMAGE_FROM_CAMERA);
		}
		else
		{
			new AlertDialog.Builder(this)
					.setMessage(
							"External Storage (SD Card) is required.\n\nCurrent state: "
									+ storageState).setCancelable(true)
					.create().show();
		}
	}

	private void uploadFromPhone()
	{
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, PICK_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		switch (requestCode)
		{
			case PICK_IMAGE:
				if (resultCode == Activity.RESULT_OK)
				{
					Uri selectedImage = data.getData();
					try
					{
						Bitmap bitmap = decodeUri(selectedImage);
						filePath = saveImage(bitmap, this).getAbsolutePath();
						ivChangeUserImage.setImageBitmap(BitmapFactory
								.decodeFile(filePath));

					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}

				}
				break;
			case CAPTURE_IMAGE_FROM_CAMERA:

				if (resultCode == Activity.RESULT_OK)
				{
					int dh = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 400, getResources()
									.getDisplayMetrics());
					int dw = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 400, getResources()
									.getDisplayMetrics());

					try
					{
						BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
						bmpFactoryOptions.inJustDecodeBounds = true;
						Bitmap bmp = BitmapFactory.decodeStream(
								getContentResolver().openInputStream(
										imageFileUri), null, bmpFactoryOptions);

						int heightRatio = (int) Math
								.ceil(bmpFactoryOptions.outHeight / (float) dh);
						int widthRatio = (int) Math
								.ceil(bmpFactoryOptions.outWidth / (float) dw);
						if (heightRatio > 1 && widthRatio > 1)
						{
							if (heightRatio > widthRatio)
							{
								bmpFactoryOptions.inSampleSize = heightRatio;
							}
							else
							{
								bmpFactoryOptions.inSampleSize = widthRatio;
							}
						}
						bmpFactoryOptions.inJustDecodeBounds = false;
						bmp = BitmapFactory.decodeStream(getContentResolver()
								.openInputStream(imageFileUri), null,
								bmpFactoryOptions);

						int rotation = getCameraPhotoOrientation(this,
								imageFileUri);
						if (rotation != 0)
						{
							Matrix matrix = new Matrix();
							matrix.preRotate(rotation);
							bmp = Bitmap.createBitmap(bmp, 0, 0,
									bmp.getWidth(), bmp.getHeight(), matrix,
									true);
						}
						filePath = saveImage(bmp, this).getAbsolutePath();
						ivChangeUserImage.setImageBitmap(BitmapFactory
								.decodeFile(filePath));

					}
					catch (FileNotFoundException e)
					{
					}
				}
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException
	{
		int rotation = 0;
		// float rotationInDegres = 0f;

		rotation = getOrientation(this, selectedImage);
		// rotation = getCameraPhotoOrientation(this, selectedImage);

		// DebugLog.log("rotation: " + rotation);

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 140;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true)
		{
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
			{
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;

		Bitmap bmp = BitmapFactory.decodeStream(getContentResolver()
				.openInputStream(selectedImage), null, o2);

		if (rotation != 0)
		{
			Matrix matrix = new Matrix();
			matrix.preRotate(rotation);
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), matrix, true);
		}

		return bmp;

	}

	private int getOrientation(Context context, Uri photoUri)
	{
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		try
		{
			if (cursor.moveToFirst())
			{
				return cursor.getInt(0);
			}
			else
			{
				return -1;
			}
		}
		finally
		{
			cursor.close();
		}
	}

	private File saveImage(Bitmap myBitmap, Context context)
	{
		File myDir = new File(Environment.getExternalStorageDirectory(),
				context.getPackageName());
		if (!myDir.exists())
		{
			myDir.mkdir();
		}

		File file = new File(myDir, "" + new Date().getTime());
		if (file.exists()) file.delete();
		try
		{
			FileOutputStream out = new FileOutputStream(file);
			myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return file;
	}

	public static int getCameraPhotoOrientation(Context context, Uri imageUri)
	{
		int rotate = 0;
		try
		{
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imageUri.getPath());
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return rotate;
	}

}
