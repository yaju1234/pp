/**
 * @author Ratul Ghosh
 * 21-Mar-2014
 * 
 */
package com.appsbee.pairpost.adapter;

import java.util.Random;

import com.appsbee.pairpost.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListNotificationAdapter extends BaseAdapter
{

	Random random;

	public ListNotificationAdapter()
	{
		random = new Random();
	}

	@Override
	public int getCount()
	{
		return 10;
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.row_item_notification, parent, false);
		}
		TextView tvTime = (TextView) convertView
				.findViewById(R.id.tvNotificationTime);
		tvTime.setText((random.nextInt(20) + 2) + " minutues ago");
		return convertView;
	}

}
