/**
 * @author Ratul Ghosh
 * 14-Mar-2014
 * 
 */
package com.appsbee.pairpost.adapter;

import java.util.Random;

import com.appsbee.pairpost.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridDiscoverAdapter extends BaseAdapter
{

	private int size = 20;
	private Context mContext;
	Random random;

	public GridDiscoverAdapter(Context context)
	{
		mContext = context;
		random = new Random();
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
					R.layout.grid_item_discover, parent, false);
		}

		ImageView ivDiscoverImageSet1 = (ImageView) convertView
				.findViewById(R.id.ivGridItemDiscoverPhotoSet1);

		ImageView ivDiscoverImageSet2 = (ImageView) convertView
				.findViewById(R.id.ivGridItemDiscoverPhotoSet2);
		ImageView ivPlaySet1 = (ImageView) convertView
				.findViewById(R.id.ivGridItemPlaySet1);
		ImageView ivPlaySet2 = (ImageView) convertView
				.findViewById(R.id.ivGridItemPlaySet2);

		if (random.nextBoolean())
			ivPlaySet1.setVisibility(View.VISIBLE);
		else
			ivPlaySet1.setVisibility(View.INVISIBLE);

		if (random.nextBoolean())
			ivPlaySet2.setVisibility(View.VISIBLE);
		else
			ivPlaySet2.setVisibility(View.INVISIBLE);

		if (position % 2 == 0)
		{
			ivDiscoverImageSet1
					.setImageResource(R.drawable.grid_item_discover_image_1);
			ivDiscoverImageSet2
					.setImageResource(R.drawable.grid_item_discover_image_2);
		}
		else
		{
			ivDiscoverImageSet1
					.setImageResource(R.drawable.grid_item_discover_image_2);
			ivDiscoverImageSet2
					.setImageResource(R.drawable.grid_item_discover_image_1);
		}
		return convertView;
	}
}
