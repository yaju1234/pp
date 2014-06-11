/**
 * @author Ratul Ghosh
 * 24-Mar-2014
 * 
 */
package com.appsbee.pairpost.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.BaseActivity;
import com.appsbee.pairpost.activity.OtherUserProfile;
import com.appsbee.pairpost.adapter.ListFollowerAdapter;

public class InnerTabFollowers extends BaseInnerFragment /*implements	OnItemClickListener*/ {
    
    public interface InnerTabFollowersListener {
	    public void onInnerTabFollowersListener(String id);
	}
    private ListView lvFollowers;
    private BaseActivity activity;
    private OtherUserProfile otherUserProfileActivity;
    private TabProfileFragment profileFragment;
    private TextView tvInnerTabFollwerCount;
    public InnerTabFollowersListener mCallback;

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	this.activity = (BaseActivity) activity;
	if (getParentFragment() != null)
	    profileFragment = (TabProfileFragment) getParentFragment();
	try {
            mCallback = (InnerTabFollowersListener) profileFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTabPairPostListener");
        }
    }

    @Override
    public void onDetach() {
	super.onDetach();
	this.activity = null;
	profileFragment = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,    Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.fragment_inner_tab_followers,container, false);
	lvFollowers = (ListView) v.findViewById(R.id.lvFollowers);
	tvInnerTabFollwerCount = (TextView)v.findViewById(R.id.tvInnerTabFollwerCount);
	//tvInnerTabFollwerCount.setText(""+profileFragment.getFollowers().size()+" Followers");
	//lvFollowers.setOnItemClickListener(this);
	updateFollowers();
	return v;
    }

    private void updateFollowers() {
	// lvFollowers.setAdapter(new ListFollowerAdapter(InnerTabFollowers.this,activity,R.layout.row_follower_following,   profileFragment.getFollowers()));
	if (profileFragment != null)
	    lvFollowers.setAdapter(new ListFollowerAdapter(InnerTabFollowers.this,activity,R.layout.row_follower_following,   profileFragment.getFollowers()));
	else {
	    otherUserProfileActivity = (OtherUserProfile) activity;
	    if (otherUserProfileActivity != null)
		lvFollowers.setAdapter(new ListFollowerAdapter(InnerTabFollowers.this,activity,R.layout.row_follower_following,   profileFragment.getFollowers()));
	}
    }

    @Override
    public void dataUpdated() {
	updateFollowers();

    }
    
    public void doCallback(String id){
	    mCallback.onInnerTabFollowersListener(id);
	}

   /* @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	Intent intent = new Intent(getActivity(), OtherUserProfile.class);

	ListFollowerAdapter adapter = (ListFollowerAdapter) lvFollowers.getAdapter();
	pPairPostUser user = (pPairPostUser) adapter.getItem(arg2);

	boolean isFollowing = false;
	if (profileFragment != null)
	    isFollowing = profileFragment.isFollowing(user);
	else
	    isFollowing = otherUserProfileActivity.isFollowing(user);

	intent.putExtra(OtherUserProfile.PUT_EXTRA_USER_ID, user.getMemberId());
	intent.putExtra(OtherUserProfile.PUT_EXTRA_IS_FOLLOWING, isFollowing);
	getActivity().startActivity(intent);

    }*/
}
