/**
 * @author Ratul Ghosh
 * 25-Mar-2014
 * 
 */
package com.appsbee.pairpost.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.appsbee.pairpost.R;

public class NotificationsActivity extends BaseActivity implements
		OnCheckedChangeListener
{

	private ToggleButton tbNotification;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		tbNotification = (ToggleButton) findViewById(R.id.tbNotification);
		tbNotification.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if (isChecked)
			tbNotification
					.setBackgroundResource(R.drawable.notification_on_off_turned_on);
		else
			tbNotification
					.setBackgroundResource(R.drawable.notification_on_off_turned_off);
	}
}
