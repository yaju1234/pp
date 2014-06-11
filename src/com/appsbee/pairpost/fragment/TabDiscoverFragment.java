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

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.BaseActivity;
import com.appsbee.pairpost.adapter.GridDiscoverAdapter1;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.http.KlHttpClient;
import com.appsbee.pairpost.pojo.Discoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class TabDiscoverFragment extends Fragment
{
	private GridView gvDiscover;
	private ArrayList<Discoder>  Arr = new ArrayList<Discoder>();
	public ProgressDialog prsDlg;
	BaseActivity activity;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.activity = (BaseActivity) activity;
	}

	@Override
	public void onDetach(){
		super.onDetach();
		activity = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_tab_discover, container,false);
		gvDiscover = (GridView) v.findViewById(R.id.gvDiscover);		
		new DiscoverWeb().execute();
		/*gvDiscover.setAdapter(new GridDiscoverAdapter(getActivity()));*/
		return v;
	}
	
	
	public class DiscoverWeb extends AsyncTask<String, String, ArrayList<Discoder>>{

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
			   JSONObject response  = KlHttpClient.SendHttpPost(Constant.Url.DISCOVER_POST, req);
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
		     gvDiscover.setAdapter(new GridDiscoverAdapter1(Constant.userId,getActivity(), R.layout.grid_item_discover1, result)); 
		 }
	    }
	    
	}
}
