/**
 * @author Ratul Ghosh
 * 24-Mar-2014
 * 
 */
package com.appsbee.pairpost.fragment;

import java.util.HashMap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.activity.BaseActivity;
import com.appsbee.pairpost.activity.OtherUserProfile;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.util.Util;

public class InnerTabBio extends BaseInnerFragment implements OnClickListener {
    private LinearLayout llBioDetails, llBioEdit;
    private ImageView ivEditBio;
    private Button btSubmit, btReset;
    private EditText etBioEdit;
    private RelativeLayout rlLoading;
    private TextView tvBioDetails;
    private BaseActivity activity;
    private OtherUserProfile otherUserProfileActivity;
    private TabProfileFragment profileFragment;
    private boolean isOtherUserProfile;
    public static final String ARG_IS_ANOTHER_USER_PROFILE = "is_another_user_profile";

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	this.activity = (BaseActivity) activity;
	if (getParentFragment() != null)
	    profileFragment = (TabProfileFragment) getParentFragment();

    }

    @Override
    public void onDetach() {
	super.onDetach();
	this.activity = null;
	profileFragment = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,    Bundle savedInstanceState) {

	View v = inflater.inflate(R.layout.fragment_inner_tab_bio, container,	false);
	if (getArguments() != null) {
	    isOtherUserProfile = getArguments().getBoolean(   ARG_IS_ANOTHER_USER_PROFILE, false);
	}

	findViewsById(v);
	if (isOtherUserProfile)
	    ivEditBio.setVisibility(View.INVISIBLE);

	updateBioText();
	ivEditBio.setOnClickListener(this);
	btReset.setOnClickListener(this);
	btSubmit.setOnClickListener(this);
	return v;
    }

    private void updateBioText() {
	if (profileFragment != null)
	    tvBioDetails.setText(profileFragment.getUserBio());
	else {
	    otherUserProfileActivity = (OtherUserProfile) activity;
	    if (otherUserProfileActivity != null)
		tvBioDetails.setText(otherUserProfileActivity.getUserBio());

	}

    }

    private void findViewsById(View v) {
	llBioDetails = (LinearLayout) v.findViewById(R.id.llBioDetailContainer);
	llBioEdit = (LinearLayout) v.findViewById(R.id.llEditBioContainer);
	ivEditBio = (ImageView) v.findViewById(R.id.ivEditBio);
	if(Constant.CallBackuserId.equalsIgnoreCase(activity.application.getLoggedUserId())){
	    ivEditBio.setVisibility(View.VISIBLE); 
	}else{
	    ivEditBio.setVisibility(View.INVISIBLE);
	}
	btSubmit = (Button) v.findViewById(R.id.btSubmit);
	btReset = (Button) v.findViewById(R.id.btReset);
	etBioEdit = (EditText) v.findViewById(R.id.etBioEdit);
	rlLoading = (RelativeLayout) v.findViewById(R.id.rlLoading);
	tvBioDetails = (TextView) v.findViewById(R.id.tvBioDetails);
    }

    private void showBio() {
	llBioDetails.setVisibility(View.VISIBLE);
	llBioEdit.setVisibility(View.GONE);
    }

    private void showEdit() {
	llBioDetails.setVisibility(View.GONE);
	llBioEdit.setVisibility(View.VISIBLE);
	etBioEdit.setText("");
	etBioEdit.append(tvBioDetails.getText().toString());

    }

    private void showLoading() {
	if (rlLoading.getVisibility() == View.GONE)
	    rlLoading.setVisibility(View.VISIBLE);
	if (llBioDetails.getVisibility() == View.VISIBLE)
	    llBioDetails.setVisibility(View.GONE);
	if (llBioEdit.getVisibility() == View.VISIBLE)
	    llBioEdit.setVisibility(View.GONE);
    }

    private void hideLoading() {
	if (rlLoading.getVisibility() == View.VISIBLE)
	    rlLoading.setVisibility(View.GONE);
	if (llBioDetails.getVisibility() == View.GONE)
	    llBioDetails.setVisibility(View.VISIBLE);
	if (llBioEdit.getVisibility() == View.VISIBLE)
	    llBioEdit.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.ivEditBio:
	    showEdit();
	    break;
	case R.id.btReset:
	    showBio();
	    break;
	case R.id.btSubmit:
	    new UpdateBioAsyncTask().execute(etBioEdit.getText().toString()
		    .trim());
	    break;
	default:
	    break;
	}
    }

    private class UpdateBioAsyncTask extends AsyncTask<String, Void, String> {

	private String bioText;

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    showLoading();
	}

	@Override
	protected String doInBackground(String... params) {

	    bioText = params[0];
	    try {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("bio_data", bioText);
		param.put("member_id", activity.application.getLoggedUserId());
		return activity.httpClient.sendHttpPostWithJson(
			Constant.Url.UPDATE_BIO, param);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return null;
	}

	@Override
	protected void onPostExecute(String result) {
	    super.onPostExecute(result);
	    hideLoading();
	    if (Util.isJsonResponseOK(activity, result)) {
		tvBioDetails.setText(bioText);
	    }
	}

    }

    @Override
    public void dataUpdated() {
	updateBioText();

    }
}
