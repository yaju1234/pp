/**
 * @author Ratul Ghosh
 * 24-Mar-2014
 * 
 */
package com.appsbee.pairpost.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter
{
	private FragmentManager fm;
	private ArrayList<Fragment> alFragment;

	public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments)
	{
		super(fm);
		this.fm = fm;
		alFragment = fragments;
	}

	@Override
	public Fragment getItem(int arg0)
	{
		return alFragment.get(arg0);
	}

	@Override
	public int getCount()
	{
		return alFragment.size();
	}

}
