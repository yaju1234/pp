/**
 * @author Ratul Ghosh
 * 26-Mar-2014
 * 
 */
package com.appsbee.pairpost.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.adapter.ViewPagerAdapter;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.fragment.BaseInnerFragment;
import com.appsbee.pairpost.fragment.InnerTabBio;
import com.appsbee.pairpost.fragment.InnerTabFollowers;
import com.appsbee.pairpost.fragment.InnerTabFollowing;
import com.appsbee.pairpost.fragment.InnerTabPairPost;
import com.appsbee.pairpost.pojo.pPairPostUser;
import com.appsbee.pairpost.util.ImageLoader;
import com.appsbee.pairpost.util.Util;

public class OtherUserProfile extends BaseActivity implements OnClickListener,
		OnPageChangeListener
{
	private ViewPager viewPager;

	private int tabSelectedColor;
	private int tabUnSelectedColor;

	private Button btTabBio, btTabPairPost, btTabFollwers, btTabFollowing;

	private Button btFollowFollowing;
	private ProgressBar pbLoading;

	private boolean isFollowing = false;;

	private String userBio;
	private ArrayList<pPairPostUser> following, followers;

	public static final String PUT_EXTRA_USER_ID = "user_id";
	public static final String PUT_EXTRA_IS_FOLLOWING = "is_following";

	private String memberId;

	private ImageView ivGrid, ivList;
	private InnerTabPairPost innerTabPairPost;

	private TextView tvOtherUserProfileName;
	private ImageView ivOtherUserPic;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_other_user_profile);
		imageLoader = new ImageLoader(this);

		memberId = getIntent().getExtras().getString(PUT_EXTRA_USER_ID);
		isFollowing = getIntent().getExtras()
				.getBoolean(PUT_EXTRA_IS_FOLLOWING);

		followers = new ArrayList<pPairPostUser>();
		following = new ArrayList<pPairPostUser>();

		tabUnSelectedColor = Color.parseColor("#142840");
		tabSelectedColor = Color.parseColor("#4A647D");

		findViewsById();
		btTabBio.setOnClickListener(this);
		btTabPairPost.setOnClickListener(this);
		btTabFollwers.setOnClickListener(this);
		btTabFollowing.setOnClickListener(this);

		if (isFollowing)
		{
			btFollowFollowing.setText("Following");
			btFollowFollowing.setBackgroundResource(R.drawable.bt_following_bg);
			btFollowFollowing.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					new UnFollowAsyncTask().execute(memberId);

				}
			});
		}
		else
		{
			btFollowFollowing.setText("+ Follow");
			btFollowFollowing.setBackgroundResource(R.drawable.bt_follow_bg);
			btFollowFollowing.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					new FollowAsyncTask().execute(memberId);

				}
			});
		}
		innerTabPairPost = new InnerTabPairPost();
		ArrayList<Fragment> innerTabs = new ArrayList<Fragment>();
		InnerTabBio innerTabBio = new InnerTabBio();
		Bundle arg = new Bundle();
		arg.putBoolean(InnerTabBio.ARG_IS_ANOTHER_USER_PROFILE, true);
		innerTabBio.setArguments(arg);
		innerTabs.add(innerTabBio);
		innerTabs.add(innerTabPairPost);
		innerTabs.add(new InnerTabFollowers());
		innerTabs.add(new InnerTabFollowing());

		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),
				innerTabs));
		viewPager.setOnPageChangeListener(this);
		onPageSelected(0);
		new GetUserDataAsyncTask().execute();
	}

	private void findViewsById()
	{
		viewPager = (ViewPager) findViewById(R.id.vpOtherUserProfileTabConatiner);

		btTabBio = (Button) findViewById(R.id.btInnertabBio);
		btTabPairPost = (Button) findViewById(R.id.btInnertabPairPost);
		btTabFollwers = (Button) findViewById(R.id.btInnertabFollwers);
		btTabFollowing = (Button) findViewById(R.id.btInnertabFollowing);
		btFollowFollowing = (Button) findViewById(R.id.btOtherUserProfileFollowFollowing);
		pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

		ivGrid = (ImageView) findViewById(R.id.ivOtherUserProfileGridIcon);
		ivList = (ImageView) findViewById(R.id.ivOtherUserProfileListIcon);
		ivGrid.setOnClickListener(this);
		ivList.setOnClickListener(this);

		tvOtherUserProfileName = (TextView) findViewById(R.id.tvOtherUserProfileName);
		ivOtherUserPic = (ImageView) findViewById(R.id.ivOtherUserProfilePic);

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
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btInnertabBio:
				viewPager.setCurrentItem(0, true);
				break;
			case R.id.btInnertabPairPost:
				viewPager.setCurrentItem(1, true);
				break;
			case R.id.btInnertabFollwers:
				viewPager.setCurrentItem(2, true);
				break;
			case R.id.btInnertabFollowing:
				viewPager.setCurrentItem(3, true);
				break;
			case R.id.ivOtherUserProfileGridIcon:
				innerTabPairPost.showInGrid();
				break;
			case R.id.ivOtherUserProfileListIcon:
				innerTabPairPost.showInList();
				break;
			default:
				break;
		}
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

		btTabBio.setLayoutParams(llParamUnSelected);
		btTabPairPost.setLayoutParams(llParamUnSelected);
		btTabFollwers.setLayoutParams(llParamUnSelected);
		btTabFollowing.setLayoutParams(llParamUnSelected);

		btTabBio.setBackgroundColor(tabUnSelectedColor);
		btTabPairPost.setBackgroundColor(tabUnSelectedColor);
		btTabFollwers.setBackgroundColor(tabUnSelectedColor);
		btTabFollowing.setBackgroundColor(tabUnSelectedColor);

		switch (pos)
		{
			case 0:
				btTabBio.setLayoutParams(llParamSelected);
				btTabBio.setBackgroundColor(tabSelectedColor);
				break;
			case 1:
				btTabPairPost.setLayoutParams(llParamUnSelected);
				btTabPairPost.setBackgroundColor(tabSelectedColor);

				break;
			case 2:
				btTabFollwers.setLayoutParams(llParamSelected);
				btTabFollwers.setBackgroundColor(tabSelectedColor);
				break;
			case 3:
				btTabFollowing.setLayoutParams(llParamSelected);
				btTabFollowing.setBackgroundColor(tabSelectedColor);
				break;
			default:
				break;
		}
	}

	private int dpToPx(int dp)
	{
		float density = getApplicationContext().getResources()
				.getDisplayMetrics().density;
		return Math.round((float) dp * density);
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
				param.put("member_id", memberId);
				return httpClient.sendHttpPostWithJson(
						Constant.Url.GET_ONE_USER_INFO, param);
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
			if (Util.isJsonResponseOK(OtherUserProfile.this, result))
			{
				try
				{
					JSONObject jUserDetails = new JSONObject(result)
							.getJSONObject("data")
							.getJSONObject("user_details");
					tvOtherUserProfileName.setText(jUserDetails
							.getString("user_name"));
					String userPic = jUserDetails.getString("user_image_url");
					if (userPic.trim().length() != 0)
					{
						imageLoader.displayImage(Constant.Url.ASSET_DIRECTORY
								+ userPic, R.drawable.profile_img,
								ivOtherUserPic);
					}
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

	public boolean isFollowing(pPairPostUser user)
	{
		for (pPairPostUser follwing : following)
		{
			if (follwing.equals(user)) return true;
		}

		return false;
	}

	public String getUserBio()
	{
		return userBio;
	}

	public ArrayList<pPairPostUser> getFollowing()
	{
		return following;
	}

	public ArrayList<pPairPostUser> getFollowers()
	{
		return followers;
	}

	private class FollowAsyncTask extends AsyncTask<String, Void, String>{
		private ProgressDialog pd;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			pd = new ProgressDialog(OtherUserProfile.this);
			pd.setTitle("Following");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			String followerId = params[0];

			try
			{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", application.getLoggedUserId());
				param.put("follower_id", followerId);

				return httpClient.sendHttpPostWithJson(
						Constant.Url.FOLLOW_USER, param);
			}
			catch (Exception e)
			{

				e.printStackTrace();
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) pd.dismiss();

			if (Util.isJsonResponseOK(OtherUserProfile.this, result))
			{
				Toast.makeText(OtherUserProfile.this, "Succesfully followed",
						Toast.LENGTH_LONG).show();
				btFollowFollowing.setText("Following");
				btFollowFollowing
						.setBackgroundResource(R.drawable.bt_following_bg);
				btFollowFollowing.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						new UnFollowAsyncTask().execute(memberId);

					}
				});
			}

		}
	}

	private class UnFollowAsyncTask extends AsyncTask<String, Void, String>
	{
		private ProgressDialog pd;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			pd = new ProgressDialog(OtherUserProfile.this);
			pd.setTitle("Unfollowing");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			String followerId = params[0];

			try
			{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", application.getLoggedUserId());
				param.put("follower_id", followerId);

				return httpClient.sendHttpPostWithJson(
						Constant.Url.UNFOLLOW_USER, param);
			}
			catch (Exception e)
			{

				e.printStackTrace();
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) pd.dismiss();

			if (Util.isJsonResponseOK(OtherUserProfile.this, result))
			{
				Toast.makeText(OtherUserProfile.this, "Succesfully Unfollowed",
						Toast.LENGTH_LONG).show();
				btFollowFollowing.setText("Follow+");
				btFollowFollowing
						.setBackgroundResource(R.drawable.bt_follow_bg);

				btFollowFollowing.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						new FollowAsyncTask().execute(memberId);

					}
				});
			}

		}
	}
}
