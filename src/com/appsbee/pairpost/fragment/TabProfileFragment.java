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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.BaseActivity;
import com.appsbee.pairpost.activity.LoginActivity;
import com.appsbee.pairpost.activity.OtherUserProfile;
import com.appsbee.pairpost.activity.SettingsActivity;
import com.appsbee.pairpost.adapter.ViewPagerAdapter;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.fragment.InnerTabFollowers.InnerTabFollowersListener;
import com.appsbee.pairpost.fragment.InnerTabFollowing.InnerTabFollowingListener;
import com.appsbee.pairpost.fragment.InnerTabPairPost.InnerTabPairPostFragmentListener;
import com.appsbee.pairpost.pojo.Discoder;
import com.appsbee.pairpost.pojo.pPairPostUser;
import com.appsbee.pairpost.util.ImageLoader;
import com.appsbee.pairpost.util.Util;

@SuppressWarnings("unused")
public class TabProfileFragment extends Fragment implements OnPageChangeListener, OnClickListener,InnerTabFollowingListener,InnerTabFollowersListener,InnerTabPairPostFragmentListener{

	
    public interface TabProfileFragmentListener {
	    public void onTabProfileFragmentListener(String id);
	}
    private ViewPager viewPager;
	private int tabSelectedColor;
	private int tabUnSelectedColor;
	private ImageLoader imageLoader;
	private Button btTabSearch, btTabBio, btTabPairPost, btTabFollwers,btTabFollowing;
	private TextView tvUserName;
	private ImageView ivUserProfilePic;
	private ImageView ivGrid, ivList;
	private ImageView ivSetting;
	private ProgressBar pbLoading;
	BaseActivity activity;
	private String userBio;
	private ArrayList<pPairPostUser> following, followers;
	private InnerTabPairPost innerTabPairPost;
	private boolean flag = false;
	private String userId;
	private String fname;
	private String email;
	private String photo;
	private Button btOtherUserProfileFollowFollowing;
	//private LinearLayout llOtherUserTotalPairPost;
	//private TextView lbl_other_user_total_pair_post;
	public TabProfileFragmentListener mCallback;
	boolean check =false;
	private ArrayList<Discoder>  Arr = new ArrayList<Discoder>();
	

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.activity = (BaseActivity) activity;
		try {
	            mCallback = (TabProfileFragmentListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement TabProfileFragmentListener");
	        }
	}

	@Override
	public void onDetach(){
		super.onDetach();
		activity = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		followers = new ArrayList<pPairPostUser>();
		following = new ArrayList<pPairPostUser>();
		imageLoader = new ImageLoader(activity);
		//Toast.makeText(activity, "onCreate call", 3000).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState){
		tabUnSelectedColor = Color.parseColor("#142840");
		tabSelectedColor = Color.parseColor("#4A647D");
		View v = inflater.inflate(R.layout.fragment_tab_profile, container,false);
		if(Constant.CallBackuserId.equalsIgnoreCase(activity.application.getLoggedUserId())){
		    flag = true;
		    userId = activity.application.getLoggedUserId();
		   // Toast.makeText(activity, ""+flag, 3000).show();
		}else{
		    flag = false;
		    userId = Constant.CallBackuserId; 
		    //Toast.makeText(activity, ""+flag, 3000).show();
		}
		findViewsById(v);
		
		
		
		/*tvUserName.setText(activity.application.getLoggedUserName());*/
		//if (activity.application.getLoggedUserImageUri() != null|| activity.application.getLoggedUserImageUri().trim().length() != 0)
		//imageLoader.displayImage(Constant.Url.ASSET_DIRECTORY+ activity.application.getLoggedUserImageUri(),R.drawable.profile_img, ivUserProfilePic);

		ivSetting.setOnClickListener(this);
		btTabSearch.setOnClickListener(this);
		btTabBio.setOnClickListener(this);
		btTabPairPost.setOnClickListener(this);
		btTabFollwers.setOnClickListener(this);
		btTabFollowing.setOnClickListener(this);
		btOtherUserProfileFollowFollowing.setOnClickListener(this);

		ivGrid.setOnClickListener(this);
		ivList.setOnClickListener(this);

		innerTabPairPost = new InnerTabPairPost();
		ArrayList<Fragment> innerTabs = new ArrayList<Fragment>();
		if(flag){
		    innerTabs.add(new InnerTabSearchFragment()); 
		}
		
		innerTabs.add(new InnerTabBio());
		innerTabs.add(innerTabPairPost);
		innerTabs.add(new InnerTabFollowers());
		innerTabs.add(new InnerTabFollowing());

		viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),innerTabs));
		viewPager.setOnPageChangeListener(this);
		onPageSelected(0);

		new GetUserDataAsyncTask().execute();
		return v;
	}

	private void findViewsById(View v){
		viewPager = (ViewPager) v.findViewById(R.id.vpMyProfileTabsTabConatiner);
		btTabSearch = (Button) v.findViewById(R.id.btInnertabSearch);
		if(flag){
		    btTabSearch.setVisibility(View.VISIBLE); 
		}else{
		    btTabSearch.setVisibility(View.GONE); 
		}
		btTabBio = (Button) v.findViewById(R.id.btInnertabBio);
		btTabPairPost = (Button) v.findViewById(R.id.btInnertabPairPost);
		btTabFollwers = (Button) v.findViewById(R.id.btInnertabFollwers);
		btTabFollowing = (Button) v.findViewById(R.id.btInnertabFollowing);
		ivSetting = (ImageView) v.findViewById(R.id.ivUserProfileTabSettings);
		pbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);
		ivGrid = (ImageView) v.findViewById(R.id.ivUserProfileTabGridIcon);
		ivList = (ImageView) v.findViewById(R.id.ivUserProfileTabListIcon);
		tvUserName = (TextView) v.findViewById(R.id.tvUserProfileTabProfileName);
		ivUserProfilePic = (ImageView) v.findViewById(R.id.ivUserProfileTabProfilePic);
		btOtherUserProfileFollowFollowing = (Button)v.findViewById(R.id.btOtherUserProfileFollowFollowing);
		//llOtherUserTotalPairPost = (LinearLayout)v.findViewById(R.id.llOtherUserTotalPairPost);
		//lbl_other_user_total_pair_post = (TextView)v.findViewById(R.id.lbl_other_user_total_pair_post);
	}

	private void showProgress(){
		if (pbLoading.getVisibility() == View.INVISIBLE)
			pbLoading.setVisibility(View.VISIBLE);
		if (viewPager.getVisibility() == View.VISIBLE)
			viewPager.setVisibility(View.INVISIBLE);
	}

	private void hideProgress(){
		if (pbLoading.getVisibility() == View.VISIBLE)
			pbLoading.setVisibility(View.INVISIBLE);
		if (viewPager.getVisibility() == View.INVISIBLE)
			viewPager.setVisibility(View.VISIBLE);

	}

	@Override
	public void onPageScrollStateChanged(int arg0){
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2){
	}

	@Override
	public void onPageSelected(int pos){
	    if(flag){

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

		switch (pos){
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

	
	    }else{

		LinearLayout.LayoutParams llParamUnSelected = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
		llParamUnSelected.setMargins(0, 0, 0, dpToPx(3));

		LinearLayout.LayoutParams llParamSelected = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
		llParamSelected.setMargins(0, 0, 0, 0);

		//btTabSearch.setLayoutParams(llParamUnSelected);
		btTabBio.setLayoutParams(llParamUnSelected);
		btTabPairPost.setLayoutParams(llParamUnSelected);
		btTabFollwers.setLayoutParams(llParamUnSelected);
		btTabFollowing.setLayoutParams(llParamUnSelected);

		//btTabSearch.setBackgroundColor(tabUnSelectedColor);
		btTabBio.setBackgroundColor(tabUnSelectedColor);
		btTabPairPost.setBackgroundColor(tabUnSelectedColor);
		btTabFollwers.setBackgroundColor(tabUnSelectedColor);
		btTabFollowing.setBackgroundColor(tabUnSelectedColor);

		switch (pos){
			/*case 0:
				btTabSearch.setLayoutParams(llParamSelected);
				btTabSearch.setBackgroundColor(tabSelectedColor);

				break;*/
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
	}

	private int dpToPx(int dp){
		float density = getActivity().getApplicationContext().getResources()
				.getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	private void notifyFragmentsAboutChange(){
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
	public void onClick(View v){
		switch (v.getId()){
			case R.id.btInnertabSearch:
			    if(flag)
				viewPager.setCurrentItem(0, true);
				break;
			case R.id.btInnertabBio:
			    if(flag)
				viewPager.setCurrentItem(1, true);
			    else
				viewPager.setCurrentItem(0, true);
				break;
			case R.id.btInnertabPairPost:
			    if(flag)
				viewPager.setCurrentItem(2, true);
			    else
				viewPager.setCurrentItem(1, true);
				break;
			case R.id.btInnertabFollwers:
			    if(flag)
				viewPager.setCurrentItem(3, true);
			    else
				viewPager.setCurrentItem(2, true);
				break;
			case R.id.btInnertabFollowing:
			    if(flag)
				viewPager.setCurrentItem(4, true);
			    else
				viewPager.setCurrentItem(3, true);
				break;
			case R.id.ivUserProfileTabSettings:
			    Intent i = new Intent(getActivity(), SettingsActivity.class);
			    startActivityForResult(i,100);
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
			case R.id.btOtherUserProfileFollowFollowing:
			    doFollowing();
			        break;
			default:
				break;
		}
	}

	

	private class GetUserDataAsyncTask extends AsyncTask<Void, Void, String>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			showProgress();
		}
		@Override
		protected String doInBackground(Void... params)	{
		    String result = null;
			try{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", userId);
				Log.e("member_id", userId);
				param.put("logged_id", activity.application.getLoggedUserId());// other user pair_post_id
				Log.e("logged_id",  activity.application.getLoggedUserId());
				//param.put("user_id", activity.application.getLoggedUserId());// logger user pair_post_id
				result =  activity.httpClient.sendHttpPostWithJson(Constant.Url.GET_USER_DATA, param);
				JSONObject jUserDetails = new JSONObject(result).getJSONObject("data").getJSONObject("user_details");
				userBio = jUserDetails.getJSONObject("bio").getString("bio_data");
				fname = jUserDetails.getJSONObject("bio").getString("first_name");
				email = jUserDetails.getJSONObject("bio").getString("email");
				
				photo = jUserDetails.getJSONObject("bio").getString("photo");
				JSONArray jFollowing = jUserDetails.getJSONArray("following");
				following.clear();
				for (int i = 0; i < jFollowing.length(); i++){
					pPairPostUser pUser = new pPairPostUser();
					pUser.setMemberId(jFollowing.getJSONObject(i).getString("member_id"));
					pUser.setMemberName(jFollowing.getJSONObject(i).getString("member_name"));
					pUser.setMemberEmailId(jFollowing.getJSONObject(i).getString("member_email"));
					pUser.setMemberImageUri(jFollowing.getJSONObject(i).getString("member_image_url"));
					following.add(pUser);
				}
				
				 	JSONArray jFollower = jUserDetails.getJSONArray("followers");
				 	followers.clear();
				 	for (int i = 0; i < jFollower.length(); i++)	{
					pPairPostUser pUser = new pPairPostUser();
					pUser.setMemberId(jFollower.getJSONObject(i).getString("member_id"));
					pUser.setMemberName(jFollower.getJSONObject(i).getString("member_name"));
					pUser.setMemberEmailId(jFollower.getJSONObject(i).getString("member_email"));
					pUser.setMemberImageUri(jFollower.getJSONObject(i).getString("member_image_url"));
					followers.add(pUser);
				}
				 	
				 	
				 	
				 	
					JSONArray jArr = jUserDetails.getJSONArray("pair_posts");
					Arr.clear();
					for(int i=0; i<jArr.length(); i++){
					    JSONObject c = jArr.getJSONObject(i);
					    String pairpostId = c.getString("pairpostid");
					    String id = c.getString("id");
					    String fname = c.getString("first_name");
					    String lname;
					    if(!c.isNull("last_name")){
						lname = c.getString("last_name");
					    }else{
						lname = "";
					    }
					    String caption = c.getString("post_caption");
					    JSONObject likeObj = c.getJSONObject("like");
					    String totalLike = likeObj.getString("totallike");
					    JSONObject isLikeObj = c.getJSONObject("islike");
					    boolean isLike ;
					    if(Integer.parseInt(isLikeObj.getString("islike"))==1){
						isLike = true;
					    }else{
						isLike = false;
					    }
					    JSONObject commentObj = c.getJSONObject("postcomment");			    
					    String totalcomment = commentObj.getString("totalcomment");			   
					    String created  = c.getString("created");
					    String user_image = c.getString("user_image");
					    JSONObject objPost1 = c.getJSONObject("post1");
					    String post1type = objPost1.getString("postTypeTop");
					    String post1Url = objPost1.getString("urlTop");
					    JSONObject objPost2 = c.getJSONObject("post2");
					    String post2type = objPost2.getString("postTypeBottom");
					    String post2Url = objPost2.getString("urlBottom");			   
					    Arr.add(new Discoder(pairpostId,id,fname, lname, user_image, caption,created, post1type, post1Url, post2type, post2Url,totalLike,totalcomment,isLike));
					
					}
				/* return activity.httpClient.sendHttpPostWithJson(Constant.Url.GET_USER_DATA, param);*/
			
			
			/*
				JSONObject jUserDetails = new JSONObject(result).getJSONObject("data").getJSONObject("user_details");
				userBio = jUserDetails.getJSONObject("bio").getString("bio_data");
				fname = jUserDetails.getJSONObject("bio").getString("first_name");
				email = jUserDetails.getJSONObject("bio").getString("email");
				tvUserName.setText(fname);
				photo = jUserDetails.getJSONObject("bio").getString("photo");
				imageLoader.displayImage(Constant.Url.ASSET_DIRECTORY+ photo,R.drawable.profile_img, ivUserProfilePic);
				JSONArray jFollowing = jUserDetails.getJSONArray("following");
				following.clear();
				for (int i = 0; i < jFollowing.length(); i++){
					pPairPostUser pUser = new pPairPostUser();
					pUser.setMemberId(jFollowing.getJSONObject(i).getString("member_id"));
					pUser.setMemberName(jFollowing.getJSONObject(i).getString("member_name"));
					pUser.setMemberEmailId(jFollowing.getJSONObject(i).getString("member_email"));
					pUser.setMemberImageUri(jFollowing.getJSONObject(i).getString("member_image_url"));
					following.add(pUser);
				}
				
				 	JSONArray jFollower = jUserDetails.getJSONArray("followers");
				 	followers.clear();
				 	for (int i = 0; i < jFollower.length(); i++)	{
					pPairPostUser pUser = new pPairPostUser();
					pUser.setMemberId(jFollower.getJSONObject(i).getString("member_id"));
					pUser.setMemberName(jFollower.getJSONObject(i).getString("member_name"));
					pUser.setMemberEmailId(jFollower.getJSONObject(i).getString("member_email"));
					pUser.setMemberImageUri(jFollower.getJSONObject(i).getString("member_image_url"));
					followers.add(pUser);
				}
				 	
				 	
				 	
				 	
					JSONArray jArr = jUserDetails.getJSONArray("pair_posts");
					Arr.clear();
					for(int i=0; i<jArr.length(); i++){
					    JSONObject c = jArr.getJSONObject(i);
					    String pairpostId = c.getString("pairpostid");
					    String id = c.getString("id");
					    String fname = c.getString("first_name");
					    String lname;
					    if(!c.isNull("last_name")){
						lname = c.getString("last_name");
					    }else{
						lname = "";
					    }
					    String caption = c.getString("post_caption");
					    JSONObject likeObj = c.getJSONObject("like");
					    String totalLike = likeObj.getString("totallike");
					    JSONObject isLikeObj = c.getJSONObject("islike");
					    boolean isLike ;
					    if(Integer.parseInt(isLikeObj.getString("islike"))==1){
						isLike = true;
					    }else{
						isLike = false;
					    }
					    JSONObject commentObj = c.getJSONObject("postcomment");			    
					    String totalcomment = commentObj.getString("totalcomment");			   
					    String created  = c.getString("created");
					    String user_image = c.getString("user_image");
					    JSONObject objPost1 = c.getJSONObject("post1");
					    String post1type = objPost1.getString("postTypeTop");
					    String post1Url = objPost1.getString("urlTop");
					    JSONObject objPost2 = c.getJSONObject("post2");
					    String post2type = objPost2.getString("postTypeBottom");
					    String post2Url = objPost2.getString("urlBottom");			   
					    Arr.add(new Discoder(pairpostId,id,fname, lname, user_image, caption,created, post1type, post1Url, post2type, post2Url,totalLike,totalcomment,isLike));
					
					}
			*/
			
			
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result){

			super.onPostExecute(result);
			if (Util.isJsonResponseOK(activity, result)){
				try{
				       // boolean is_following = new JSONObject("status").getBoolean("is_following");
				       
				        if(flag){
				           // llOtherUserTotalPairPost.setVisibility(View.GONE);
				            ivSetting.setVisibility(View.VISIBLE);
				        }else{
				            //llOtherUserTotalPairPost.setVisibility(View.VISIBLE);
				            ivSetting.setVisibility(View.GONE); 
				        }
				        tvUserName.setText(fname);
					imageLoader.displayImage(Constant.Url.ASSET_DIRECTORY+ photo,R.drawable.profile_img, ivUserProfilePic);
					
					/*JSONObject jUserDetails = new JSONObject(result).getJSONObject("data").getJSONObject("user_details");
					userBio = jUserDetails.getJSONObject("bio").getString("bio_data");
					fname = jUserDetails.getJSONObject("bio").getString("first_name");
					email = jUserDetails.getJSONObject("bio").getString("email");
					tvUserName.setText(fname);
					photo = jUserDetails.getJSONObject("bio").getString("photo");
					imageLoader.displayImage(Constant.Url.ASSET_DIRECTORY+ photo,R.drawable.profile_img, ivUserProfilePic);
					JSONArray jFollowing = jUserDetails.getJSONArray("following");
					following.clear();
					for (int i = 0; i < jFollowing.length(); i++){
						pPairPostUser pUser = new pPairPostUser();
						pUser.setMemberId(jFollowing.getJSONObject(i).getString("member_id"));
						pUser.setMemberName(jFollowing.getJSONObject(i).getString("member_name"));
						pUser.setMemberEmailId(jFollowing.getJSONObject(i).getString("member_email"));
						pUser.setMemberImageUri(jFollowing.getJSONObject(i).getString("member_image_url"));
						following.add(pUser);
					}
					
					 	JSONArray jFollower = jUserDetails.getJSONArray("followers");
					 	followers.clear();
					 	for (int i = 0; i < jFollower.length(); i++)	{
						pPairPostUser pUser = new pPairPostUser();
						pUser.setMemberId(jFollower.getJSONObject(i).getString("member_id"));
						pUser.setMemberName(jFollower.getJSONObject(i).getString("member_name"));
						pUser.setMemberEmailId(jFollower.getJSONObject(i).getString("member_email"));
						pUser.setMemberImageUri(jFollower.getJSONObject(i).getString("member_image_url"));
						followers.add(pUser);
					}
					 	
					 	
					 	
					 	
						JSONArray jArr = jUserDetails.getJSONArray("pair_posts");
						Arr.clear();
						for(int i=0; i<jArr.length(); i++){
						    JSONObject c = jArr.getJSONObject(i);
						    String pairpostId = c.getString("pairpostid");
						    String id = c.getString("id");
						    String fname = c.getString("first_name");
						    String lname;
						    if(!c.isNull("last_name")){
							lname = c.getString("last_name");
						    }else{
							lname = "";
						    }
						    String caption = c.getString("post_caption");
						    JSONObject likeObj = c.getJSONObject("like");
						    String totalLike = likeObj.getString("totallike");
						    JSONObject isLikeObj = c.getJSONObject("islike");
						    boolean isLike ;
						    if(Integer.parseInt(isLikeObj.getString("islike"))==1){
							isLike = true;
						    }else{
							isLike = false;
						    }
						    JSONObject commentObj = c.getJSONObject("postcomment");			    
						    String totalcomment = commentObj.getString("totalcomment");			   
						    String created  = c.getString("created");
						    String user_image = c.getString("user_image");
						    JSONObject objPost1 = c.getJSONObject("post1");
						    String post1type = objPost1.getString("postTypeTop");
						    String post1Url = objPost1.getString("urlTop");
						    JSONObject objPost2 = c.getJSONObject("post2");
						    String post2type = objPost2.getString("postTypeBottom");
						    String post2Url = objPost2.getString("urlBottom");			   
						    Arr.add(new Discoder(pairpostId,id,fname, lname, user_image, caption,created, post1type, post1Url, post2type, post2Url,totalLike,totalcomment,isLike));
						
						}*/
					    Constant.innerfeedArr = Arr;
					 	
					 	
						 if(flag){
						            btOtherUserProfileFollowFollowing.setVisibility(View.GONE);  
						        }else{
						            //btOtherUserProfileFollowFollowing.setVisibility(View.VISIBLE);
						            for(int i=0; i<followers.size(); i++){
						        	if(followers.get(i).getMemberId().equalsIgnoreCase(activity.application.getLoggedUserId())){
						        	    check = true; 
						        	    break;
						        	}
						            }
						            if(check){
						        	btOtherUserProfileFollowFollowing.setText("Following");
						        	btOtherUserProfileFollowFollowing.setBackgroundResource(R.drawable.bt_following_bg);
						        	btOtherUserProfileFollowFollowing.setVisibility(View.VISIBLE);
						            }else{
						        	btOtherUserProfileFollowFollowing.setText("Follow");
						        	btOtherUserProfileFollowFollowing.setBackgroundResource(R.drawable.bt_follow_bg);
						        	btOtherUserProfileFollowFollowing.setVisibility(View.VISIBLE);
						            }
						        }
						 btTabFollowing.setText(""+following.size()+"\nFollowing");
						 btTabFollwers.setText(""+followers.size()+"\nFollowers");
						 btTabPairPost.setText(""+Arr.size()+"\nPair Post");

				}
				catch (Exception e)	{
					e.printStackTrace();
				}
			}
			notifyFragmentsAboutChange();
			hideProgress();
		}
	}

	public String getUserBio(){
		return userBio;
	}

	public boolean isFollowing(pPairPostUser user){
		for (pPairPostUser follwing : following){
			if (follwing.equals(user)) return true;
		}

		return false;
	}

	public ArrayList<pPairPostUser> getFollowing(){
		return following;
	}

	public ArrayList<pPairPostUser> getFollowers(){
		return followers;
	}
	
	public ArrayList<Discoder> getPairPosts(){
		return Arr;
	}
	
	private void doFollowing() {
	    if(check){
		 new UnFollowAsyncTask().execute();
	    }else{
		 new FollowAsyncTask().execute();
	    }
	   
	    
	}
	private class FollowAsyncTask extends AsyncTask<String, Void, String>{
		private ProgressDialog pd;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			pd = new ProgressDialog(activity);
			pd.setTitle("Following");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			//String followerId = params[0];

			try
			{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", activity.application.getLoggedUserId());
				param.put("follower_id", userId);

				return activity.httpClient.sendHttpPostWithJson(
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

			if (Util.isJsonResponseOK(activity, result))	{
			        check = true;
				pPairPostUser pUser = new pPairPostUser();
				pUser.setMemberId(activity.application.getLoggedUserId());
				pUser.setMemberName(Constant.userName);
				pUser.setMemberEmailId("");
				pUser.setMemberImageUri(Constant.userImage);
				followers.add(pUser);
				btTabFollwers.setText(""+followers.size()+"\nFollowers");
				btOtherUserProfileFollowFollowing.setText("Following");
		        	btOtherUserProfileFollowFollowing.setBackgroundResource(R.drawable.bt_following_bg);
				Toast.makeText(activity, "Succesfully followed",
						Toast.LENGTH_LONG).show();
				
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
			//String followerId = params[0];
			try{
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("member_id", activity.application.getLoggedUserId());
				param.put("follower_id", userId);
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
			    check = false;
			    btOtherUserProfileFollowFollowing.setText("Follow");
		            btOtherUserProfileFollowFollowing.setBackgroundResource(R.drawable.bt_follow_bg);
		            int pos_arr = 0;
		            for(int i=0; i<followers.size(); i++){
		        	if(followers.get(i).getMemberId().equalsIgnoreCase(activity.application.getLoggedUserId())){
		        	    pos_arr = i;
		        	    break;
		        	}
		            }
		            followers.remove(pos_arr);
		            btTabFollwers.setText(""+followers.size()+"\nFollowers");
				/*Toast.makeText(getActivity(), "Succesfully Unfollowed",
						Toast.LENGTH_LONG).show();*/
				//new SearchUserAsyncTask().execute(etSearchUser.getText().toString().trim());
			}
		}
	}


	@Override
	public void onInnerTabFollowersListener(String id) {
	    doCallback(id);
	    
	}

	@Override
	public void onInnerTabFollowingListener(String id) {
	    doCallback(id);
	    
	}
	
	public void doCallback(String id){
	    mCallback.onTabProfileFragmentListener(id);
	}

	@Override
	public void onInnerTabPairPostFragmentListener(String id) {	  
	    doCallback(id);
	}

	@Override
	public void onPostDeleteListener() {
	    btTabPairPost.setText(""+Constant.innerfeedArr.size()+"\nPair Post");
	    
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == 100 && data!=null) {
	      
	            String result=data.getStringExtra("result");
	            if(result.equalsIgnoreCase("1000"));{
	        	startActivity(new Intent(activity,LoginActivity.class));
	        	activity.finish();
	          
	    	}
	    }
	      
	}
}
