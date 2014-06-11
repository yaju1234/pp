/**
 * @author Ratul Ghosh
 * 12-Mar-2014
 * 
 */
package com.appsbee.pairpost.fragment;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.adapter.ListNotificationAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TabNotificationFragment extends Fragment
{

	private ListView lvNotification;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_tab_notification,
				container, false);
		lvNotification = (ListView) v.findViewById(R.id.lvNotification);
		lvNotification.setAdapter(new ListNotificationAdapter());
		return v;
	}
}
