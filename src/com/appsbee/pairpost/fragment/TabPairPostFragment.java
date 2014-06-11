/**
 * @author Ratul Ghosh
 * 12-Mar-2014
 * 
 */
package com.appsbee.pairpost.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.BaseActivity;
import com.appsbee.pairpost.activity.SettingsActivity;
import com.appsbee.pairpost.adapter.ViewPagerAdapter;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.pojo.pPairPostUser;
import com.appsbee.pairpost.util.ImageLoader;
import com.appsbee.pairpost.util.Util;

public class TabPairPostFragment extends Fragment implements
		OnPageChangeListener, OnClickListener
{

	private ViewPager viewPager;

	private int tabSelectedColor;
	private int tabUnSelectedColor;

	private ImageLoader imageLoader;

	private Button btTabSearch, btTabBio, btTabPairPost, btTabFollwers,
			btTabFollowing;

	private TextView tvUserName;
	private ImageView ivUserProfilePic;

	private ImageView ivGrid, ivList;

	private ImageView ivSetting;
	private ProgressBar pbLoading;
	BaseActivity activity;

	private String userBio;
	private ArrayList<pPairPostUser> following, followers;
	private InnerTabPairPost innerTabPairPost;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		this.activity = (BaseActivity) activity;
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		activity = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		followers = new ArrayList<pPairPostUser>();
		following = new ArrayList<pPairPostUser>();
		imageLoader = new ImageLoader(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		tabUnSelectedColor = Color.parseColor("#142840");
		tabSelectedColor = Color.parseColor("#4A647D");
		View v = inflater.inflate(R.layout.fragment_tab_profile, container,
				false);
		findViewsById(v);

		tvUserName.setText(activity.application.getLoggedUserName());
		if (activity.application.getLoggedUserImageUri() != null
				|| activity.application.getLoggedUserImageUri().trim().length() != 0)
			imageLoader.displayImage(Constant.Url.ASSET_DIRECTORY
					+ activity.application.getLoggedUserImageUri(),
					R.drawable.profile_img, ivUserProfilePic);

		ivSetting.setOnClickListener(this);
		btTabSearch.setOnClickListener(this);
		btTabBio.setOnClickListener(this);
		btTabPairPost.setOnClickListener(this);
		btTabFollwers.setOnClickListener(this);
		btTabFollowing.setOnClickListener(this);

		ivGrid.setOnClickListener(this);
		ivList.setOnClickListener(this);

		innerTabPairPost = new InnerTabPairPost();
		ArrayList<Fragment> innerTabs = new ArrayList<Fragment>();
		innerTabs.add(new InnerTabSearchFragment());
		innerTabs.add(new InnerTabBio());
		innerTabs.add(innerTabPairPost);
		innerTabs.add(new InnerTabFollowers());
		innerTabs.add(new InnerTabFollowing());

		viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),
				innerTabs));
		viewPager.setOnPageChangeListener(this);
		onPageSelected(0);

		new GetUserDataAsyncTask().execute();
		return v;
	}

	private void findViewsById(View v)
	{
		viewPager = (ViewPager) v
				.findViewById(R.id.vpMyProfileTabsTabConatiner);

		btTabSearch = (Button) v.findViewById(R.id.btInnertabSearch);
		btTabBio = (Button) v.findViewById(R.id.btInnertabBio);
		btTabPairPost = (Button) v.findViewById(R.id.btInnertabPairPost);
		btTabFollwers = (Button) v.findViewById(R.id.btInnertabFollwers);
		btTabFollowing = (Button) v.findViewById(R.id.btInnertabFollowing);
		ivSetting = (ImageView) v.findViewById(R.id.ivUserProfileTabSettings);
		pbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);

		ivGrid = (ImageView) v.findViewById(R.id.ivUserProfileTabGridIcon);
		ivList = (ImageView) v.findViewById(R.id.ivUserProfileTabListIcon);

		tvUserName = (TextView) v
				.findViewById(R.id.tvUserProfileTabProfileName);
		ivUserProfilePic = (ImageView) v
				.findViewById(R.id.ivUserProfileTabProfilePic);
	}

	private void showProgress()
	{
		if (pbLoading.getVisibility() == View.INVISIBLE)
			pbLoading.setVisibility(View.VISIBLE);
		if (viewPager.getVisibility() == View.VISIBLE)
			viewPager.setVisibility(View.INVISIBLE);
	}

	private void hideProgress()
	{
		if (pbLoading.getVisibility() == View.VISIBLE)
			pbLoading.setVisibility(View.INVISIBLE);
		if (viewPager.getVisibility() == View.INVISIBLE)
			viewPager.setVisibility(View.VISIBLE);

	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
	}

	@Override
	public void onPageSelected(int pos)
	{
		LinearLayout.LayoutParams llParamUnSelected = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
		llParamUnSelected.setMargins(0, 0, 0, dpToPx(3));

		LinearLayout.LayoutParams llParamSelected = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
		llParamSelected.setMargins(0, 0, 0, 0);

		btTabSearch.setLayoutParams(llParamUnSelected);
		btTabBio.setLayoutParams(llParamUnSelected);
		btTabPairPost.setLayoutParams(llParamUnSelected);
		btTabFollwers.setLayoutParams(llParamUnSelected);
		btTabFollowing.setLayoutParams(llParamUnSelected);

		btTabSearch.setBackgroundColor(tabUnSelectedColor);
		btTabBio.setBackgroundColor(tabUnSelectedColor);
		btTabPairPost.setBackgroundColor(tabUnSelectedColor);
		btTabFollwers.setBackgroundColor(tabUnSelectedColor);
		btTabFollowing.setBackgroundColor(tabUnSelectedColor);

		switch (pos)
		{
			case 0:
				btTabSearch.setLayoutParams(llParamSelected);
				btTabSearch.setBackgroundColor(tabSelectedColor);

				break;
			case 1:
				btTabBio.setLayoutParams(llParamSelected);
				btTabBio.setBackgroundColor(tabSelectedColor);

				break;
			case 2:
				btTabPairPost.setLayoutParams(llParamUnSelected);
				btTabPairPost.setBackgroundColor(tabSelectedColor);

				break;
			case 3:
				btTabFollwers.setLayoutParams(llParamSelected);
				btTabFollwers.setBackgroundColor(tabSelectedColor);

				break;
			case 4:
				btTabFollowing.setLayoutParams(llParamSelected);
				btTabFollowing.setBackgroundColor(tabSelectedColor);

				break;
			default:
				break;
		}

	}

	private int dpToPx(int dp)
	{
		float density = getActivity().getApplicationContext().getResources()
				.getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	private void notifyFragmentsAboutChange()
	{
		int numOfFragment = viewPager.getAdapter().getCount();
		for (int j = 0; j < numOfFragment; j++)
		{
			Fragment fragment = ((ViewPagerAdapter) (viewPager.getAdapter()))
					.getItem(j);
			if (fragment instanceof BaseInnerFragment)
				((BaseInnerFragment) fragment).dataUpdated();
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btInnertabSearch:
				viewPager.setCurrentItem(0, true);
				break;
			case R.id.btInnertabBio:
				viewPager.setCurrentItem(1, true);
				break;
			case R.id.btInnertabPairPost:
				viewPager.setCurrentItem(2, true);
				break;
			case R.id.btInnertabFollwers:
				viewPager.setCurrentItem(3, true);
				break;
			case R.id.btInnertabFollowing:
				viewPager.setCurrentItem(4, true);
				break;
			case R.id.ivUserProfileTabSettings:
				startActivity(new Intent(getActivity(), SettingsActivity.class));
				break;
			case R.id.ivUserProfileTabGridIcon:
				innerTabPairPost.showInGrid();
				ivGrid.setImageResource(R.drawable.ic_grid_selected);
				ivList.setImageResource(R.drawable.ic_list_unselected);
				break;
			case R.id.ivUserProfileTabListIcon:
				innerTabPairPost.showInList();
				ivGrid.setImageResource(R.drawable.ic_grid_unselected);
				ivList.setImageResource(R.drawable.ic_list_selected);

				break;
			default:
				break;
		}
	}

	private class GetUserDataAsyncTask extends AsyncTask<Void, Void, String>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			showProgress();
		}

		@Override
		protected String doInBackground(Void... params)
		{
			try
			{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", activity.application.getLoggedUserId());
				return activity.httpClient.sendHttpPostWithJson(
						Constant.Url.GET_USER_DATA, param);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{

			super.onPostExecute(result);
			if (Util.isJsonResponseOK(activity, result))
			{
				try
				{
					JSONObject jUserDetails = new JSONObject(result)
							.getJSONObject("data")
							.getJSONObject("user_details");
					userBio = jUserDetails.getJSONObject("bio").getString(
							"bio_data");
					JSONArray jFollowing = jUserDetails
							.getJSONArray("following");
					following.clear();
					for (int i = 0; i < jFollowing.length(); i++)
					{
						pPairPostUser pUser = new pPairPostUser();
						pUser.setMemberId(jFollowing.getJSONObject(i)
								.getString("member_id"));
						pUser.setMemberName(jFollowing.getJSONObject(i)
								.getString("member_name"));
						pUser.setMemberEmailId(jFollowing.getJSONObject(i)
								.getString("member_email"));
						pUser.setMemberImageUri(jFollowing.getJSONObject(i)
								.getString("member_image_url"));
						following.add(pUser);
					}
					JSONArray jFollower = jUserDetails
							.getJSONArray("followers");
					followers.clear();
					for (int i = 0; i < jFollower.length(); i++)
					{
						pPairPostUser pUser = new pPairPostUser();
						pUser.setMemberId(jFollower.getJSONObject(i).getString(
								"member_id"));
						pUser.setMemberName(jFollower.getJSONObject(i)
								.getString("member_name"));
						pUser.setMemberEmailId(jFollower.getJSONObject(i)
								.getString("member_email"));
						pUser.setMemberImageUri(jFollower.getJSONObject(i)
								.getString("member_image_url"));
						followers.add(pUser);
					}

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			notifyFragmentsAboutChange();
			hideProgress();
		}
	}

	public String getUserBio()
	{
		return userBio;
	}

	public boolean isFollowing(pPairPostUser user)
	{
		for (pPairPostUser follwing : following)
		{
			if (follwing.equals(user)) return true;
		}

		return false;
	}

	public ArrayList<pPairPostUser> getFollowing()
	{
		return following;
	}

	public ArrayList<pPairPostUser> getFollowers()
	{
		return followers;
	}

}
