/**
 * @author Ratul Ghosh
 * 24-Mar-2014
 * 
 */
package com.appsbee.pairpost.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.BaseActivity;
import com.appsbee.pairpost.adapter.GridDiscoverAdapter;
import com.appsbee.pairpost.adapter.InnerListPairFeedAdapter;

public class InnerTabPairPost extends BaseInnerFragment
{
    public interface InnerTabPairPostFragmentListener {
	    public void onInnerTabPairPostFragmentListener(String id);
	    public void onPostDeleteListener();
	}
	private GridView gvPairPost;
	private ListView lvPairPost;
	private BaseActivity activity;
	private TabProfileFragment profileFragment;
	public InnerTabPairPostFragmentListener mCallback;
	  
	
	 @Override
	    public void onAttach(Activity activity) {
		super.onAttach(activity);
		  Log.e("callback onacctach", "callback onacctach");
		this.activity = (BaseActivity) activity;
		if (getParentFragment() != null)
		    profileFragment = (TabProfileFragment) getParentFragment();
		try {
	            mCallback = (InnerTabPairPostFragmentListener) profileFragment;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement TabProfileFragmentListener");
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
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_inner_tab_pair_post,
				container, false);
		gvPairPost = (GridView) v.findViewById(R.id.gvPairPost);
		lvPairPost = (ListView) v.findViewById(R.id.lvPairPost);
		 updateFollowers();
		/*gvPairPost.setAdapter(new GridDiscoverAdapter(getActivity()));
		lvPairPost.setAdapter(new InnerListPairFeedAdapter(getActivity()));
*/
		return v;
	}

	public void showInGrid()
	{
		if (gvPairPost != null && lvPairPost != null)
		{
			gvPairPost.setVisibility(View.VISIBLE);
			lvPairPost.setVisibility(View.INVISIBLE);
		}
	}

	public void showInList()
	{
		if (gvPairPost != null && lvPairPost != null)
		{

			gvPairPost.setVisibility(View.INVISIBLE);
			lvPairPost.setVisibility(View.VISIBLE);
		}
	}
	
	
	 private void updateFollowers() {

		//lvFollowing.setAdapter(new ListFollowingAdapter(InnerTabFollowing.this,activity,R.layout.row_follower_following,profileFragment.getFollowing()));
		
		    gvPairPost.setAdapter(new GridDiscoverAdapter(activity));
		    lvPairPost.setAdapter(new InnerListPairFeedAdapter(InnerTabPairPost.this,activity,R.layout.gallery_item_pair_feed_2)); 
		
		
	    }

	@Override
	public void dataUpdated() {
	   Log.e("callback", "callback");
	   if (profileFragment != null){
	       updateFollowers();
	   }
	  
	}
	
	public void doPostDelete(){
	    mCallback.onPostDeleteListener();
	}
	
	public void doCallback(String id){
	    mCallback.onInnerTabPairPostFragmentListener(id);
	}
}
