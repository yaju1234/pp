/**
 * @author Ratul Ghosh
 * 27-Feb-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.util.LogManager;
import com.appsbee.pairpost.util.Util;

public class ForgotPasswordActivity extends BaseActivity implements
		OnClickListener
{

	private EditText etUserName, etEmail;
	private Button btSubmit;
	private ProgressBar pbLoading;

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_forgot_password);
		findViewsById();
		btSubmit.setOnClickListener(this);
	}

	private void findViewsById()
	{
		etUserName = (EditText) findViewById(R.id.etUserName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		btSubmit = (Button) findViewById(R.id.btSubmit);
		pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
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
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v)
	{
		if (validateFormData())
		{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("username", etUserName.getText().toString().trim());
			params.put("email", etEmail.getText().toString().trim());
			new ForgetPasswordAsyncTask().execute(params);

		}
	}

	private class ForgetPasswordAsyncTask extends
			AsyncTask<HashMap<String, String>, Void, String>
	{

		private boolean isExceptionOccured;

		@Override
		protected void onPreExecute()
		{

			super.onPreExecute();
			super.onPreExecute();
			if (!application.isNetWorkAvailable())
			{
				Toast.makeText(ForgotPasswordActivity.this,
						"No Internet Connection Found", Toast.LENGTH_LONG)
						.show();
				return;
			}
			pbLoading.setVisibility(View.VISIBLE);

		}

		@Override
		protected String doInBackground(HashMap<String, String>... params)
		{
			try
			{
				return httpClient.sendHttpPostWithJson(
						Constant.Url.FORGOT_PASSWORD_URL, params[0]);
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
			pbLoading.setVisibility(View.INVISIBLE);
			if (isExceptionOccured)
			{
				Toast.makeText(ForgotPasswordActivity.this,
						"Something Went Wrong :(.\n Please Try Again Later. ",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Util.isJsonResponseOK(ForgotPasswordActivity.this, result))
			{
				Toast.makeText(ForgotPasswordActivity.this,
						"Password has been reset.\n Please check your email.",
						Toast.LENGTH_LONG).show();
			}
		}

	}

}
