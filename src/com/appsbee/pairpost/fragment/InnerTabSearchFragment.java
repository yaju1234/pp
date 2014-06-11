/**
 * @author Ratul Ghosh
 * 24-Mar-2014
 * 
 */
package com.appsbee.pairpost.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.BaseActivity;
import com.appsbee.pairpost.activity.OtherUserProfile;
import com.appsbee.pairpost.adapter.ListUserSearchResultAdapter;
import com.appsbee.pairpost.adapter.ListUserSearchResultAdapter.ListUserSearchResultAdapterCallBack;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.pojo.pUserSearchResult;
import com.appsbee.pairpost.util.Util;

public class InnerTabSearchFragment extends Fragment implements	OnItemClickListener, ListUserSearchResultAdapterCallBack{

	private ListView lvSearchResult;
	private EditText etSearchUser;
	private String lastSearchString;
	private ProgressBar pbLoading;
	private BaseActivity activity;
	private ArrayList<pUserSearchResult> searchResult;
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		searchResult = new ArrayList<pUserSearchResult>();
	}

	@Override
	public void onAttach(Activity activity)	{
		super.onAttach(activity);
		this.activity = (BaseActivity) activity;
	}

	@Override
	public void onDetach()	{
		activity = null;
		super.onDetach();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_inner_tab_search,	container, false);
		findViewsById(v);

		etSearchUser.setOnEditorActionListener(new TextView.OnEditorActionListener(){
					@Override
					public boolean onEditorAction(TextView v, int actionId,	KeyEvent event)
					{
						if (actionId == EditorInfo.IME_ACTION_SEARCH)
						{
							if (lastSearchString != etSearchUser.getText()	.toString().trim())
								lastSearchString = etSearchUser.getText().toString().trim();
							new SearchUserAsyncTask().execute(lastSearchString);
							InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

							return true;
						}
						return false;
					}
				});
		lvSearchResult.setAdapter(new ListUserSearchResultAdapter(activity,searchResult, this));
		lvSearchResult.setOnItemClickListener(this);
		return v;
	}

	private void findViewsById(View v)
	{
		etSearchUser = (EditText) v.findViewById(R.id.etUserSearch);
		lvSearchResult = (ListView) v.findViewById(R.id.lvSearchResult);
		pbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);
	}

	private void hideProgressLoading()
	{
		if (pbLoading.getVisibility() == View.VISIBLE)
			pbLoading.setVisibility(View.INVISIBLE);
		if (lvSearchResult.getVisibility() == View.INVISIBLE)
			lvSearchResult.setVisibility(View.VISIBLE);
	}

	private void showProgressLoading()
	{
		if (pbLoading.getVisibility() == View.INVISIBLE)
			pbLoading.setVisibility(View.VISIBLE);
		if (lvSearchResult.getVisibility() == View.VISIBLE)
			lvSearchResult.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id)
	{
		Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
		getActivity().startActivity(new Intent(getActivity(), OtherUserProfile.class));
	}

	private class SearchUserAsyncTask extends AsyncTask<String, Void, String>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			showProgressLoading();
		}

		@Override
		protected String doInBackground(String... params){
			String searchKey = params[0];
			try{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("searchkey", searchKey);
				param.put("member_id", activity.application.getLoggedUserId());
				return activity.httpClient.sendHttpPostWithJson(Constant.Url.SEARCH_USER, param);
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			if (Util.isJsonResponseOK(activity, result)){
				try{
					JSONArray jUsers = new JSONObject(result).getJSONObject("data").getJSONArray("users");
					searchResult.clear();
					for (int i = 0; i < jUsers.length(); i++){
						pUserSearchResult user = new pUserSearchResult();
						user.setMemberId(jUsers.getJSONObject(i).getString("member_id"));
						user.setMemberName(jUsers.getJSONObject(i).getString("member_name"));
						user.setMemberEmail(jUsers.getJSONObject(i).getString("member_email"));
						user.setMemberImageUrl(jUsers.getJSONObject(i).getString("member_image_url"));
						user.setFollowing(jUsers.getJSONObject(i).getBoolean("is_following"));
						searchResult.add(user);
					}
					lvSearchResult.setAdapter(new ListUserSearchResultAdapter(activity, searchResult,InnerTabSearchFragment.this));
				}
				catch (JSONException e)	{
					e.printStackTrace();
				}
			}
			hideProgressLoading();
		}

	}

	@Override
	public void follow(String userId, boolean isFollowing){
		if (!isFollowing)
			new FollowAsyncTask().execute(userId);
		else
			new UnFollowAsyncTask().execute(userId);

	}

	private class FollowAsyncTask extends AsyncTask<String, Void, String>{
		private ProgressDialog pd;

		@Override
		protected void onPreExecute()	{
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setTitle("Following");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params){
			String followerId = params[0];
			try{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", activity.application.getLoggedUserId());
				param.put("follower_id", followerId);
				return activity.httpClient.sendHttpPostWithJson(Constant.Url.FOLLOW_USER, param);
			}
			catch (Exception e){
				e.printStackTrace();
				return "";
			}	}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) pd.dismiss();

			if (Util.isJsonResponseOK(getActivity(), result)){
				new SearchUserAsyncTask().execute(etSearchUser.getText().toString().trim());

			}

		}
	}

	private class UnFollowAsyncTask extends AsyncTask<String, Void, String>	{
		private ProgressDialog pd;

		@Override
		protected void onPreExecute()	{
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setTitle("Unfollowing");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params){
			String followerId = params[0];
			try{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", activity.application.getLoggedUserId());
				param.put("follower_id", followerId);
				return activity.httpClient.sendHttpPostWithJson(Constant.Url.UNFOLLOW_USER, param);
			}
			catch (Exception e){
				e.printStackTrace();
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) pd.dismiss();

			if (Util.isJsonResponseOK(getActivity(), result)){
				/*Toast.makeText(getActivity(), "Succesfully Unfollowed",
						Toast.LENGTH_LONG).show();*/
				new SearchUserAsyncTask().execute(etSearchUser.getText().toString().trim());
			}
		}
	}
}
