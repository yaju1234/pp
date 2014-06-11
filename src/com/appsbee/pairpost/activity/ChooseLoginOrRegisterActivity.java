/**
 * @author Ratul Ghosh
 * 26-Feb-2014
 * 
 */
package com.appsbee.pairpost.activity;

import com.appsbee.pairpost.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseLoginOrRegisterActivity extends BaseActivity implements
		OnClickListener
{
	private Button btLogin, btRegister;

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_chooze_login_or_register);
		btLogin = (Button) findViewById(R.id.btLogin);
		btRegister = (Button) findViewById(R.id.btRegister);

		btLogin.setOnClickListener(this);
		btRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btLogin:
				startActivity(new Intent(this,LoginActivity.class));
				break;
			case R.id.btRegister:
				startActivity(new Intent(this,RegistrationActivity.class));
				break;

			default:
				break;
		}
	}

}
