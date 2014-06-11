/**
 * @author Ratul Ghosh
 * 25-Mar-2014
 * 
 */
package com.appsbee.pairpost.adapter;

import com.appsbee.pairpost.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListSettingsAdapter extends BaseExpandableListAdapter
{

	private String[] group = { "Preferences", "Support",
			"Manage Introduction Pair Post" };
	private String[] preferenceChild = { "Account Information",
			"Sharing Settings", "Notifications", "Privacy Settings", "Logout" };
	private String[] supportChild = { "Privacy Policy", "Terms Of Service",
			"About Pair Post", "Contact Us" };

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		if (groupPosition == 0)
			return preferenceChild[childPosition];
		else if (groupPosition == 1)
			return supportChild[childPosition];
		else
			return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.row_setting_child, parent, false);
		}
		TextView tvChildName = (TextView) convertView
				.findViewById(R.id.tvChildName);
		if (groupPosition == 0)
			tvChildName.setText(preferenceChild[childPosition]);
		else
			tvChildName.setText(supportChild[childPosition]);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		if (groupPosition == 0)
			return preferenceChild.length;
		else if (groupPosition == 1)
			return supportChild.length;
		else
			return 0;
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return group[groupPosition];
	}

	@Override
	public int getGroupCount()
	{

		return group.length;
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.row_setting_group, parent, false);
		}
		TextView tvGroupName = (TextView) convertView
				.findViewById(R.id.tvGroupName);
		tvGroupName.setText(group[groupPosition]);
		ImageView ivArrow = (ImageView) convertView.findViewById(R.id.ivArrow);
		if (groupPosition != 2)
		{
			ivArrow.setVisibility(View.VISIBLE);
			if (isExpanded)
			{
				ivArrow.setImageResource(R.drawable.setting_group_up_arrow);
			}
			else
			{
				ivArrow.setImageResource(R.drawable.setting_group_down_arrow);
			}
		}
		else
		{
			ivArrow.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

}
