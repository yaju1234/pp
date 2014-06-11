/**
 * @author Ratul Ghosh
 * 26-Mar-2014
 * 
 */
package com.appsbee.pairpost.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.OtherUserProfile;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.pojo.pUserSearchResult;
import com.appsbee.pairpost.util.ImageLoader;

public class ListUserSearchResultAdapter extends BaseAdapter
{
	private ArrayList<pUserSearchResult> data;
	private Context mContext;
	private ImageLoader imageLoader;
	private ListUserSearchResultAdapterCallBack callback;

	public interface ListUserSearchResultAdapterCallBack
	{
		public void follow(String userId, boolean isFollowing);
	}

	public ListUserSearchResultAdapter(Context context,
			ArrayList<pUserSearchResult> data,
			ListUserSearchResultAdapterCallBack callback)
	{
		this.data = data;
		mContext = context;
		imageLoader = new ImageLoader(mContext);
		this.callback = callback;
	}

	@Override
	public int getCount()
	{
		return data.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return data.get(arg0);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.row_user_search_result, parent, false);
		}
		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(mContext, OtherUserProfile.class);
				intent.putExtra(OtherUserProfile.PUT_EXTRA_USER_ID,
						data.get(position).getMemberId());
				intent.putExtra(OtherUserProfile.PUT_EXTRA_IS_FOLLOWING, data
						.get(position).isFollowing());
				mContext.startActivity(intent);
			}
		});
		TextView tvUserName = (TextView) convertView
				.findViewById(R.id.tvUserName);
		ImageView ivUserPic = (ImageView) convertView
				.findViewById(R.id.ivUserPic);
		Button btFollowFollowing = (Button) convertView
				.findViewById(R.id.btFollowFollowing);
		tvUserName.setText(data.get(position).getMemberName());
		if (data.get(position).getMemberImageUrl() != null)
		{
			imageLoader.displayImage(
					Constant.Url.ASSET_DIRECTORY
							+ data.get(position).getMemberImageUrl(),
					R.drawable.profile_img, ivUserPic);
		}
		else
		{
			ivUserPic.setImageResource(R.drawable.profile_img);
		}
		if (data.get(position).isFollowing())
		{
			btFollowFollowing.setText("Following");
			btFollowFollowing.setBackgroundResource(R.drawable.bt_following_bg);
			btFollowFollowing.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					callback.follow(data.get(position).getMemberId(),
							data.get(position).isFollowing());
				}
			});
		}
		else
		{
			btFollowFollowing.setText("+Follow");
			btFollowFollowing.setBackgroundResource(R.drawable.bt_follow_bg);
			btFollowFollowing.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					callback.follow(data.get(position).getMemberId(),
							data.get(position).isFollowing());
				}
			});
		}

		return convertView;
	}
}
