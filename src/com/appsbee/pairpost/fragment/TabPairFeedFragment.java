/**
 * @author Ratul Ghosh
 * 12-Mar-2014
 * 
 */
package com.appsbee.pairpost.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.BaseActivity;
import com.appsbee.pairpost.adapter.ListPairFeedAdapter;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.http.KlHttpClient;
import com.appsbee.pairpost.pojo.Discoder;



@SuppressWarnings("deprecation")
public class TabPairFeedFragment extends Fragment
{
    
    public interface TabPairFeedListener {
	    public void onTabPairFeedListener(String id);
	}
        public ProgressDialog prsDlg;
	private Gallery galleryPairFeed;
	private BaseActivity activity;
	private ArrayList<Discoder>  Arr = new ArrayList<Discoder>();
	public TabPairFeedListener mCallback;

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.activity = (BaseActivity) activity;
		try {
	            mCallback = (TabPairFeedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnTabPairPostListener");
	        }

	}

	@Override
	public void onDetach(){
		super.onDetach();
		activity = null;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_tab_pair_feed, container,false);
		galleryPairFeed = (Gallery) v.findViewById(R.id.galleryPairFeed);
		new PairFeedWeb().execute();
/*		galleryPairFeed.setAdapter(new ListPairFeedAdapter(getActivity()));*/
		return v;
	}
	
	public class PairFeedWeb extends AsyncTask<String, String, ArrayList<Discoder>>{

	    @Override
	    protected void onPreExecute() {
		super.onPreExecute();
		prsDlg = new ProgressDialog(getActivity());
		prsDlg.setMessage("Please wait...");
		prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prsDlg.setIndeterminate(true);
		prsDlg.setCancelable(false);
		prsDlg.show();
	    }

	    @Override
	    protected ArrayList<Discoder> doInBackground(String... params) {
		try {
		  //  JSONObject response  = new JSONObject(Util.readXMLinString("log.txt", getActivity()));
		   JSONObject req = new JSONObject();
		   req.put("user_id", ""+Constant.userId);
		   Log.e("req", req.toString());
		   JSONObject response  = KlHttpClient.SendHttpPost(Constant.Url.POST_FEED, req);
		    if(response!=null){
			JSONArray jArr = response.getJSONArray("postlist");
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
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		    if (prsDlg.isShowing()) {
			prsDlg.dismiss();
		}
		    return null;
		 
		}
		return Arr;
	    }
	    

	    @Override
	    protected void onPostExecute(ArrayList<Discoder> result) {
		super.onPostExecute(result);
		 if (prsDlg.isShowing()) {
			prsDlg.dismiss();
		}
		// Toast.makeText(getActivity(), result.size(), 1000).show();
		 if(result!=null){
		     Constant.feedArr = result;
		      galleryPairFeed.setAdapter(new ListPairFeedAdapter(TabPairFeedFragment.this,getActivity(), R.layout.gallery_item_pair_feed));
		     
		    
		 }
	    }
	    
	}
	
	public void doCallback(String id){
	    mCallback.onTabPairFeedListener(id);
	}
}
