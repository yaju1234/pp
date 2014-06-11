package com.appsbee.pairpost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.appsbee.pairpost.R;

public class ListLikeAdapter extends BaseAdapter{

	private int size = 20;
	private Context mContext;
	

	public ListLikeAdapter(Context context)
	{
		mContext = context;
		
	}

	@Override
	public int getCount()
	{

		return size;
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.row_like, parent, false);
		}

		

		return convertView;
	}
}
