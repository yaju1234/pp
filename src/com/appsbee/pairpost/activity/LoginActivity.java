/**
 * @author Ratul Ghosh
 * 26-Feb-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.util.LogManager;
import com.appsbee.pairpost.util.Util;

public class LoginActivity extends BaseActivity implements OnClickListener
{
	private EditText etUserName, etPassword;
	private Button btLogin;
	private ProgressBar pbLoading;
	private TextView tvForgotPassword;
	private LinearLayout llLoginFormConatiner;

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		findViewsById();
		btLogin.setOnClickListener(this);
		tvForgotPassword.setOnClickListener(this);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		//scaleContents(llLoginFormConatiner, findViewById(android.R.id.content));
	}

	private void findViewsById()
	{
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btLogin = (Button) findViewById(R.id.btLogin);
		pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
		tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
		llLoginFormConatiner = (LinearLayout) findViewById(R.id.ll1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btLogin:
				if (validateFormData())
				{
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("user_id", etUserName.getText().toString()
							.trim());
					params.put("password", etPassword.getText().toString()
							.trim());
					new LoginAsyncTask().execute(params);
				}
				break;
			case R.id.tvForgotPassword:
				startActivity(new Intent(LoginActivity.this,
						ForgotPasswordActivity.class));
				break;
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

		if (etPassword.getText().toString().trim().length() == 0)
		{
			etPassword.setError("Password can't be empty");
			flag = false;
		}

		return flag;
	}

	private class LoginAsyncTask extends
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
				Toast.makeText(LoginActivity.this,
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
				return httpClient.sendHttpPostWithJson(Constant.Url.LOG_IN_URL,
						params[0]);
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
				Toast.makeText(LoginActivity.this,
						"Something Went Wrong :(.\n Please Try Again Later. ",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Util.isJsonResponseOK(LoginActivity.this, result))
			{

				try
				{
					JSONObject jUser = new JSONObject(result).getJSONObject(
							"data").getJSONObject("user");
					/*application.setLoggedUserId(jUser.getString("member_id"));*/
					Constant.userId = jUser.getString("member_id");
					/*application.setLoggedUserName(jUser.getString("member_name"));*/
					Constant.userName = jUser.getString("member_name");
					/*application.setLoggedUserImageUri(jUser	.optString("member_image_url"));*/
					Constant.userImage = jUser.getString("member_image_url");
					
					application.setLoggedUserId(jUser.getString("member_id"));
					application.setLoggedUserName(jUser.getString("member_name"));
					application.setLoggedUserImageUri(jUser	.optString("member_image_url"));
					application.getAppInfo().setUserInfo(jUser.getString("member_name"), jUser.getString("member_id"), jUser.optString("member_image_url"), true);
					startActivity(new Intent(LoginActivity.this,
							MainActivity.class));
					finish();

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}

			}
		}

	}
}
