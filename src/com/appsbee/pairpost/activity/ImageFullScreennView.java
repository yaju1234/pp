package com.appsbee.pairpost.activity;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.util.ImageLoader;

import android.os.Bundle;
import android.widget.ImageView;

public class ImageFullScreennView extends BaseActivity {
    private ImageView imageView;
    private String url;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	setContentView(R.layout.activity_image_fullscreen);
	imageView = (ImageView)findViewById(R.id.imageView1);
	imageLoader = new ImageLoader(ImageFullScreennView.this);
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
	    url = bundle.getString("url");
	}
	imageLoader.displayImage(url, R.drawable.pp_logo, imageView);
    }

}
