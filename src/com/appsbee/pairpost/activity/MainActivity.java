package com.appsbee.pairpost.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crittercism.app.Crittercism;
import com.appsbee.pairpost.R;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.fragment.TabDiscoverFragment;
import com.appsbee.pairpost.fragment.TabProfileFragment.TabProfileFragmentListener;
//http://javatechig.com/android/youtubeplayerview-example-in-android-using-youtube-api
//Client pair_post_id:30621592141.apps.googleusercontent.com
//email:30621592141@developer.gserviceaccount.com
//API key: AIzaSyA2sVmbZqNLup9qBdozSlOGcpQSVx6fXYg
import com.appsbee.pairpost.fragment.TabPairFeedFragment.TabPairFeedListener;

public class MainActivity extends BaseActivity implements OnClickListener,TabPairFeedListener,TabProfileFragmentListener {

    private LinearLayout llTabDiscover, llTabPairFeed, llTabPairPost,
	    llTabProfile, llTabNotification;

    private final String TAG_DISCOVER = "com.appsbee.pairpost.fragment.TabDiscoverFragment";
    private final String TAG_PAIR_FEED = "com.appsbee.pairpost.fragment.TabPairFeedFragment";
    private final String TAG_PAIR_POST = "";
    private final String TAG_PROFILE = "com.appsbee.pairpost.fragment.TabProfileFragment";
    private final String TAG_NOTFICATION = "com.appsbee.pairpost.fragment.TabNotificationFragment";

    private String currentTab;
    private int tabBgColor;
    private int tabSelectedDrawable;

    private ImageView ivTabIndicator1, ivTabIndicator2, ivTabIndicator3,
	    ivTabIndicator4, ivTabIndicator5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Crittercism.initialize(getApplicationContext(),
		"537a183d07229a3916000004");
	
	application.setLoggedUserId(application.getAppInfo().userId);
	application.setLoggedUserName(application.getAppInfo().userFirstName);
	application.setLoggedUserImageUri(application.getAppInfo().image);
	
	
	
	Constant.userId = application.getAppInfo().userId;
	Constant.userName = application.getAppInfo().userFirstName;
	Constant.userImage = application.getAppInfo().image;
	
	Constant.CallBackuserId = application.getLoggedUserId();
	
	findViewsById();

	llTabDiscover.setTag(TAG_DISCOVER);
	llTabPairFeed.setTag(TAG_PAIR_FEED);
	llTabProfile.setTag(TAG_PROFILE);
	llTabNotification.setTag(TAG_NOTFICATION);

	llTabDiscover.setOnClickListener(this);
	llTabPairFeed.setOnClickListener(this);
	llTabPairPost.setOnClickListener(this);
	llTabProfile.setOnClickListener(this);
	llTabNotification.setOnClickListener(this);
	 Log.e("Reach here", "reach here");
	getSupportFragmentManager().beginTransaction()	.add(R.id.flTabConatiner, new TabDiscoverFragment(),
			TAG_DISCOVER).commit();

	currentTab = TAG_DISCOVER;
	tabBgColor = Color.parseColor("#333333");
	tabSelectedDrawable = Color.parseColor("#0080CB");
	setAllTabColorUnselected();
	llTabDiscover.setBackgroundColor(tabSelectedDrawable);
	changeTabTextColor(llTabDiscover, Color.parseColor("#01FF00"));

    }

    private void findViewsById() {
	llTabDiscover = (LinearLayout) findViewById(R.id.llTabDisocver);
	llTabPairFeed = (LinearLayout) findViewById(R.id.llTabPairFeed);
	llTabPairPost = (LinearLayout) findViewById(R.id.llTabPairPost);
	llTabProfile = (LinearLayout) findViewById(R.id.llTabProfile);
	llTabNotification = (LinearLayout) findViewById(R.id.llTabNotification);

	ivTabIndicator1 = (ImageView) findViewById(R.id.tab1selector);
	ivTabIndicator2 = (ImageView) findViewById(R.id.tab2selector);
	ivTabIndicator3 = (ImageView) findViewById(R.id.tab3selector);
	ivTabIndicator4 = (ImageView) findViewById(R.id.tab4selector);
	ivTabIndicator5 = (ImageView) findViewById(R.id.tab5selector);
    }

    @Override
    public void onClick(View v) {
	if (v instanceof LinearLayout && v.getId() != R.id.llTabPairPost) {
	    // tab button clicked
	    String tag = (String) v.getTag();
	    System.out.println("!--  TAG = " + tag);
	    if(tag != TAG_PROFILE){
		 if (tag == currentTab)
			return;
	    }
	   

	    FragmentTransaction ft = getSupportFragmentManager()
		    .beginTransaction();
	    ft.hide(getSupportFragmentManager().findFragmentByTag(currentTab));

	    Fragment fragment = getSupportFragmentManager().findFragmentByTag(
		    tag);
	    if (fragment == null) {

		fragment = Fragment.instantiate(this, tag);
		ft.add(R.id.flTabConatiner, fragment, tag);

	    } else{
		if(tag.equalsIgnoreCase(TAG_PROFILE)){
		    fragment = Fragment.instantiate(this, TAG_PROFILE);
		    ft.add(R.id.flTabConatiner, fragment, TAG_PROFILE);
		    Constant.CallBackuserId = application.getLoggedUserId();
		}else if(tag.equalsIgnoreCase(TAG_DISCOVER)){
		    fragment = Fragment.instantiate(this, TAG_DISCOVER);
		    ft.add(R.id.flTabConatiner, fragment, TAG_DISCOVER);
		}else if(tag.equalsIgnoreCase(TAG_PAIR_FEED)){
		    fragment = Fragment.instantiate(this, TAG_PAIR_FEED);
		    ft.add(R.id.flTabConatiner, fragment, TAG_PAIR_FEED);
		}else{
		    ft.show(fragment); 
		}
		
	    }

	    ft.commit();
	    currentTab = tag;
	    setAllTabColorUnselected();
	    v.setBackgroundColor(tabSelectedDrawable);
	    changeTabTextColor(v, Color.parseColor("#01FF00"));
	} else {
	    Intent intent = new Intent(MainActivity.this,
		    MakePairPostActivity.class);
	    startActivity(intent);
	}
    }

    private void changeTabTextColor(View v, int color) {
	TextView tvTabText = (TextView) v.findViewById(R.id.tvTabText);
	tvTabText.setTextColor(color);
	String tag = (String) v.getTag();
	if (tag == TAG_DISCOVER)
	    ivTabIndicator1.setImageResource(R.drawable.tab_indicator);
	if (tag == TAG_PAIR_FEED)
	    ivTabIndicator2.setImageResource(R.drawable.tab_indicator);
	if (tag == TAG_PAIR_POST)
	    ivTabIndicator3.setImageResource(R.drawable.tab_indicator);
	if (tag == TAG_PROFILE)
	    ivTabIndicator4.setImageResource(R.drawable.tab_indicator);
	if (tag == TAG_NOTFICATION)
	    ivTabIndicator5.setImageResource(R.drawable.tab_indicator);

    }

    private void setAllTabColorUnselected() {
	llTabDiscover.setBackgroundColor(tabBgColor);
	changeTabTextColor(llTabDiscover, Color.parseColor("white"));
	llTabPairFeed.setBackgroundColor(tabBgColor);
	changeTabTextColor(llTabPairFeed, Color.parseColor("white"));
	llTabProfile.setBackgroundColor(tabBgColor);
	changeTabTextColor(llTabProfile, Color.parseColor("white"));
	llTabPairPost.setBackgroundColor(tabBgColor);

	llTabNotification.setBackgroundColor(tabBgColor);
	changeTabTextColor(llTabNotification, Color.parseColor("white"));

	ivTabIndicator1.setImageDrawable(null);
	ivTabIndicator2.setImageDrawable(null);
	ivTabIndicator3.setImageDrawable(null);
	ivTabIndicator4.setImageDrawable(null);
	ivTabIndicator5.setImageDrawable(null);
    }

    @Override
    protected void onResume() {
	super.onResume();
	// Toast.makeText(getApplicationContext(), "onResume call",
	// 1000).show();
	if (Constant.isOnResumeCall && !Constant.isCallBackFromDiscoder) {
	    Constant.isOnResumeCall = false;
	    getSupportFragmentManager()
		    .beginTransaction()
		    .replace(R.id.flTabConatiner, new TabDiscoverFragment(),
			    TAG_DISCOVER).commit();

	    currentTab = TAG_DISCOVER;
	    tabBgColor = Color.parseColor("#333333");
	    tabSelectedDrawable = Color.parseColor("#0080CB");
	    setAllTabColorUnselected();
	    llTabDiscover.setBackgroundColor(tabSelectedDrawable);
	    changeTabTextColor(llTabDiscover, Color.parseColor("#01FF00"));
	}

	if (Constant.isCallBackFromDiscoder) {
	    Constant.isCallBackFromDiscoder = false;
	    FragmentTransaction ft = getSupportFragmentManager()
		    .beginTransaction();
	    ft.hide(getSupportFragmentManager().findFragmentByTag(currentTab));
	    Fragment fragment = getSupportFragmentManager().findFragmentByTag(
		    TAG_PROFILE);
	    if (fragment == null) {

		fragment = Fragment.instantiate(this, TAG_PROFILE);
		ft.add(R.id.flTabConatiner, fragment, TAG_PROFILE);

	    } else{
		fragment = null;
		fragment = Fragment.instantiate(this, TAG_PROFILE);
		ft.add(R.id.flTabConatiner, fragment, TAG_PROFILE);
	    }
	    ft.commit();
	    currentTab = TAG_PROFILE;
	    setAllTabColorUnselected();
	    if(Constant.CallBackuserId.equalsIgnoreCase(application.getLoggedUserId())){
		 llTabProfile.setBackgroundColor(tabSelectedDrawable);
		    changeTabTextColor(llTabProfile, Color.parseColor("#01FF00"));
	    }
	}
    }

    @Override
    public void onTabPairFeedListener(String id) {
	Constant.CallBackuserId = id;
	FragmentTransaction ft = getSupportFragmentManager()
		    .beginTransaction();
	    ft.hide(getSupportFragmentManager().findFragmentByTag(currentTab));
	    Fragment fragment = getSupportFragmentManager().findFragmentByTag(
		    TAG_PROFILE);
	    if (fragment == null) {

		fragment = Fragment.instantiate(this, TAG_PROFILE);
		ft.add(R.id.flTabConatiner, fragment, TAG_PROFILE);

	    } else{
		fragment = null;
		fragment = Fragment.instantiate(this, TAG_PROFILE);
		ft.add(R.id.flTabConatiner, fragment, TAG_PROFILE);
		
	    }

	    ft.commit();
	    currentTab = TAG_PROFILE;
	    setAllTabColorUnselected();
	    if(Constant.CallBackuserId.equalsIgnoreCase(application.getLoggedUserId())){
		 llTabProfile.setBackgroundColor(tabSelectedDrawable);
		    changeTabTextColor(llTabProfile, Color.parseColor("#01FF00"));
	    }
	
    }

    @Override
    public void onTabProfileFragmentListener(String id) {
	//Toast.makeText(getApplicationContext(), "ID "+pair_post_id, 3000).show();
	Constant.CallBackuserId = id;
	FragmentTransaction ft = getSupportFragmentManager()
		    .beginTransaction();
	    ft.hide(getSupportFragmentManager().findFragmentByTag(currentTab));
	    Fragment fragment = getSupportFragmentManager().findFragmentByTag(
		    TAG_PROFILE);
	    if (fragment == null) {

		fragment = Fragment.instantiate(this, TAG_PROFILE);
		ft.add(R.id.flTabConatiner, fragment, TAG_PROFILE);

	    } else{
		fragment = null;
		fragment = Fragment.instantiate(this, TAG_PROFILE);
		ft.add(R.id.flTabConatiner, fragment, TAG_PROFILE);
		
	    }

	    ft.commit();
	    currentTab = TAG_PROFILE;
	    setAllTabColorUnselected();
	    if(Constant.CallBackuserId.equalsIgnoreCase(application.getLoggedUserId())){
		 llTabProfile.setBackgroundColor(tabSelectedDrawable);
		    changeTabTextColor(llTabProfile, Color.parseColor("#01FF00"));
	    }
	   
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Log.e("Reach here", "reach here");*/
        //getFragmentManager().popBackStack();
       MainActivity.this.finish();
    }

}
