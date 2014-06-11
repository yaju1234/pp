/**
 * @author Ratul Ghosh
 * 25-Mar-2014
 * 
 */
package com.appsbee.pairpost.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.adapter.ExpandableListSettingsAdapter;

public class SettingsActivity extends BaseActivity implements
		OnChildClickListener, OnGroupClickListener
{

	private ExpandableListView expandableListView;
	private ExpandableListSettingsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		expandableListView = (ExpandableListView) findViewById(R.id.eplvSettings);
		adapter = new ExpandableListSettingsAdapter();
		expandableListView.setAdapter(adapter);
		expandableListView.setOnChildClickListener(this);
		expandableListView.setOnGroupClickListener(this);
		expandableListView.expandGroup(0);
		expandableListView.expandGroup(1);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id)
	{
		String action = (String) adapter.getChild(groupPosition, childPosition);

		if (action != "Logout")
		{
			action = action.replace(" ", "");
			try
			{
				startActivity(new Intent(this,
						Class.forName(("com.appsbee.pairpost.activity."
								+ action + "Activity"))));
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}

		}else{
		    application.getAppInfo().setSession(false);
		    Intent intent=new Intent();  
		    intent.putExtra("result","100");         
		    setResult(100,intent);		          
		    finish();
		}
		return false;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id)
	{
		expandableListView.invalidate();
		return false;
	}
	
	@Override
	public void onBackPressed() {
	super.onBackPressed();
	Intent intent=new Intent();  
        intent.putExtra("result","101");         
        setResult(100,intent);          
        finish();
	}

}
