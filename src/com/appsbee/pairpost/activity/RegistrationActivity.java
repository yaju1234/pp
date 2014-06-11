/**
 * @author Ratul Ghosh
 * 26-Feb-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.io.File;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.util.LogManager;
import com.appsbee.pairpost.util.Util;

public class RegistrationActivity extends BaseActivity implements
		OnClickListener
{

	private final int PICK_IMAGE = 1;
	private final int CAPTURE_IMAGE_FROM_CAMERA = 2;

	private EditText etUserName, etPassword, etEmail;
	private Button btRegister;
	private CheckBox cbLicenseAgreement;
	private ImageView ivUserPic;
	private TextView tvUserAgreementAndPolicy;

	String userPolicyHtmlText = "Read Pair Post Term Of <a href=\"userpolicy.html\">Use Agreement</a> And <a href=\"privacy.html\">Privacy Policy</a> ";

	private RelativeLayout rlRootView;

	//File tempImageFile;

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_registration);
		findViewsById();
		btRegister.setOnClickListener(this);
		ivUserPic.setOnClickListener(this);

		setTextViewHTML(tvUserAgreementAndPolicy, userPolicyHtmlText);

	}

	private void findViewsById()
	{

		rlRootView = (RelativeLayout) findViewById(R.id.rlRootView);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etEmail = (EditText) findViewById(R.id.etEmail);
		btRegister = (Button) findViewById(R.id.btRegister);
		cbLicenseAgreement = (CheckBox) findViewById(R.id.cbLicenseAgreement);
		ivUserPic = (ImageView) findViewById(R.id.ivUserPic);
		tvUserAgreementAndPolicy = (TextView) findViewById(R.id.tvUserAgreementAndPolicy);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		scaleContents(rlRootView, findViewById(android.R.id.content));

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v)
	{
		//Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
		switch (v.getId())
		{
			case R.id.btRegister:
				if (!cbLicenseAgreement.isChecked())
				{
					Animation blink = new AlphaAnimation(0.0f, 1.0f);
					blink.setDuration(200);
					blink.setRepeatCount(5);
					blink.setRepeatMode(Animation.REVERSE);
					cbLicenseAgreement.startAnimation(blink);
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("You need to check the box in order for you to register.");
					builder.setPositiveButton("Ok",
							new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
				else
				{

					if (validateFormData())
					{

						HashMap<String, String> params = new HashMap<String, String>();
						params.put("username", etUserName.getText().toString()
								.trim());
						params.put("email", etEmail.getText().toString().trim());
						params.put("password", etPassword.getText().toString()
								.trim());
						if (ivUserPic.getTag() != null)
							params.put("file", (String) ivUserPic.getTag());
						new RegistrationAsyncTask().execute(params);
					}

				}
				break;
			case R.id.ivUserPic:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Your Photo :-)");
				builder.setPositiveButton("Gallery",
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{

								Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(Intent.createChooser(
										intent, "Select Picture"), PICK_IMAGE);
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
								Intent intent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(intent,
										CAPTURE_IMAGE_FROM_CAMERA);

							}
						});
				builder.show();

			default:
				break;
		}
	}

	private boolean validateFormData()
	{
		boolean flag = true;
		if (etUserName.getText().toString().trim().length() == 0)
		{
			etUserName.setError("Username can't be empty");
			flag = false;
		}
		if (etEmail.getText().toString().trim().length() == 0)
		{
			etEmail.setError("Email can't be empty");
			flag = false;
		}
		if (etPassword.getText().toString().trim().length() == 0)
		{
			etPassword.setError("Password can't be empty");
			flag = false;
		}

		return flag;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case PICK_IMAGE:
				if (resultCode == RESULT_OK)
				{
					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String imagePath = cursor.getString(columnIndex);
					cursor.close();
					ivUserPic.setTag(imagePath);
					ivUserPic.setImageBitmap(BitmapFactory
							.decodeFile(imagePath));

				}
				break;
			case CAPTURE_IMAGE_FROM_CAMERA:
				if (resultCode == RESULT_OK && data != null)
				{

					Bundle extras = data.getExtras();
					Bitmap imageBitmap = (Bitmap) extras.get("data");
					ivUserPic.setImageBitmap(imageBitmap);

					String[] projection = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							projection, null, null, null);
					int column_index_data = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToLast();

					String imagePath = cursor.getString(column_index_data);
					ivUserPic.setTag(imagePath);

				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class RegistrationAsyncTask extends
			AsyncTask<HashMap<String, String>, Void, String>
	{
		private ProgressDialog pd;
		private boolean isExceptionOccured;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			if (!application.isNetWorkAvailable())
			{
				Toast.makeText(RegistrationActivity.this,
						"No Internet Connection Found", Toast.LENGTH_LONG)
						.show();
				return;
			}
			pd = new ProgressDialog(RegistrationActivity.this);
			pd.setTitle("Registering");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected String doInBackground(HashMap<String, String>... params)
		{
			HashMap<String, String> param = params[0];
			try
			{
				if (param.containsKey("file"))
				{
					String filePath = param.get("file");
					param.remove("file");
					File file = new File(filePath);

					return httpClient.sendHttpPostWithJson(
							Constant.Url.SIGN_UP_URL, param, file);

				}
				else
				{
					return httpClient.sendHttpPostWithJson(
							Constant.Url.SIGN_UP_URL, param);
				}
			}

			catch (Exception e)
			{
				isExceptionOccured = true;
				LogManager.e(getClass().getCanonicalName(), e.getMessage());
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) pd.dismiss();
			if (isExceptionOccured)
			{
				Toast.makeText(RegistrationActivity.this,
						"Something Went Wrong :(.\n Please Try Again Later. ",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Util.isJsonResponseOK(RegistrationActivity.this, result))
			{
				Toast.makeText(
						RegistrationActivity.this,
						"Your account has been created.\nWelcome to PairPost :).",
						Toast.LENGTH_LONG).show();

			}

		}

	}

	protected void makeLinkClickable(SpannableStringBuilder strBuilder,
			final URLSpan span)
	{
		int start = strBuilder.getSpanStart(span);
		int end = strBuilder.getSpanEnd(span);
		int flags = strBuilder.getSpanFlags(span);
		ClickableSpan clickable = new ClickableSpan()
		{
			public void onClick(View view)
			{
				// Do something with span.getURL() to handle the link click...
				Log.d(getClass().getSimpleName(), span.getURL().toString());
				if (span.getURL().toString().trim().contains("userpolicy.html"))
				{
					startActivity(new Intent(RegistrationActivity.this,
							UserAgreementActivity.class));
				}
				else
				{
					startActivity(new Intent(RegistrationActivity.this,
							PrivacyPolicyActivity.class));
				}

			}
		};
		strBuilder.setSpan(clickable, start, end, flags);
		strBuilder.removeSpan(span);
	}

	protected void setTextViewHTML(TextView text, String html)
	{
		CharSequence sequence = Html.fromHtml(html);
		SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
		URLSpan[] urls = strBuilder.getSpans(0, sequence.length(),
				URLSpan.class);
		for (URLSpan span : urls)
		{
			makeLinkClickable(strBuilder, span);
		}
		text.setText(strBuilder);
		text.setLinksClickable(true);
		text.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
