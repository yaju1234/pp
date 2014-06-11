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
import com.appsbee.pairpost.adapter.ListFollowingAdapter;

public class InnerTabFollowing extends BaseInnerFragment {
    
    public interface InnerTabFollowingListener {
	    public void onInnerTabFollowingListener(String id);
	}

    private ListView lvFollowing;

    private BaseActivity activity;
    private TabProfileFragment profileFragment;
    private OtherUserProfile otherUserProfileActivity;
    private TextView tvInnerTabFollwerCount;
    public InnerTabFollowingListener mCallback;

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	this.activity = (BaseActivity) activity;
	if (getParentFragment() != null)
	    profileFragment = (TabProfileFragment) getParentFragment();
	try {
            mCallback = (InnerTabFollowingListener) profileFragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.fragment_inner_tab_following,container, false);
	lvFollowing = (ListView) v.findViewById(R.id.lvFollowing);
	tvInnerTabFollwerCount = (TextView) v.findViewById(R.id.tvInnerTabFollwerCount);
	//tvInnerTabFollwerCount.setText(""+ profileFragment.getFollowing().size() + " Following");
	updateFollowers();

	return v;
    }

    private void updateFollowers() {

	//lvFollowing.setAdapter(new ListFollowingAdapter(InnerTabFollowing.this,activity,R.layout.row_follower_following,profileFragment.getFollowing()));
	if (profileFragment != null)
	    lvFollowing.setAdapter(new ListFollowingAdapter(InnerTabFollowing.this,activity,R.layout.row_follower_following,profileFragment.getFollowing()));	else
	{
		otherUserProfileActivity = (OtherUserProfile) activity;
		if (otherUserProfileActivity != null)
			lvFollowing.setAdapter(new ListFollowingAdapter(InnerTabFollowing.this,activity,R.layout.row_follower_following,profileFragment.getFollowing()));
	}
    }

    @Override
    public void dataUpdated() {
	updateFollowers();

    }
    public void doCallback(String id){
	    mCallback.onInnerTabFollowingListener(id);
	}
}
