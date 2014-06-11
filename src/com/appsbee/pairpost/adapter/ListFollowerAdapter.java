/**
 * @author Ratul Ghosh
 * 24-Mar-2014
 * 
 */
package com.appsbee.pairpost.adapter;

import java.util.ArrayList;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.adapter.CommentAdapter.ViewHolder;
import com.appsbee.pairpost.application.Constant;
import com.appsbee.pairpost.fragment.InnerTabFollowers;
import com.appsbee.pairpost.pojo.Comments;
import com.appsbee.pairpost.pojo.pPairPostUser;
import com.appsbee.pairpost.util.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListFollowerAdapter extends ArrayAdapter<pPairPostUser> {

    private Context mContext;
    private ArrayList<pPairPostUser> data;
    private ImageLoader imageLoader;
    private ViewHolder mHolder;
    private InnerTabFollowers innerTabFollowers;

    public ListFollowerAdapter(InnerTabFollowers innerTabFollowers,Context context,int textViewResourceId, ArrayList<pPairPostUser> data) {
	super(context, textViewResourceId, data);
	this.innerTabFollowers = innerTabFollowers;
	mContext = context;
	this.data = data;
	imageLoader = new ImageLoader(mContext);
    }

    @Override
	public int getCount() {
	        return data.size();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
	View v = convertView;
	if (v == null) {
	    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.row_follower_following, null);
		mHolder = new ViewHolder();
		v.setTag(mHolder);
		mHolder.ivUserPic = (ImageView) v.findViewById(R.id.ivUserPic);
		mHolder.tvName = (TextView) v.findViewById(R.id.tvUserName);
		mHolder.rl_row = (RelativeLayout)v.findViewById(R.id.rl_row);
		 
	}else{
	    mHolder =  (ViewHolder) v.getTag();
	}
	mHolder.rl_row.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		innerTabFollowers.doCallback(data.get(position).getMemberId());
	    }
	});
	final pPairPostUser bean = data.get(position);
	if(bean!=null){
	    mHolder.tvName.setText(bean.getMemberName());
	    imageLoader.displayImage(Constant.Url.ASSET_DIRECTORY  + bean.getMemberImageUri(), R.drawable.profile_img, mHolder.ivUserPic);
		   
	}
	
	return v;
    }
    class ViewHolder {
	public RelativeLayout rl_row;
        public ImageView ivUserPic;
	public TextView tvName;
	
			
}
}
