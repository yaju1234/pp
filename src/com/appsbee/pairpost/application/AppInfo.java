package com.appsbee.pairpost.application;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppInfo {
	public String userFirstName          =null;
	public String userId                 = null;
	public String image                  = null;
	public boolean session               = false;
	public SharedPreferences sharedPreferences;
	
	public AppInfo(Context ctx){
		
		sharedPreferences        = ctx.getSharedPreferences(Constant.ShredPrefKey.PREF_NAME, Context.MODE_PRIVATE);
		userFirstName            = sharedPreferences.getString(Constant.ShredPrefKey.FIRST_NAME, userFirstName);
		userId                   = sharedPreferences.getString(Constant.ShredPrefKey.USER_ID, userId);
		image                    = sharedPreferences.getString(Constant.ShredPrefKey.PHOTO, image);
		session                  = sharedPreferences.getBoolean(Constant.ShredPrefKey.SESSION, session);
		}
	
	public void setUserInfo(String fname,String id, String img, boolean session){
		this.userFirstName       = fname;
		this.userId              = id;
		this.image               = img;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.ShredPrefKey.FIRST_NAME, userFirstName);
		edit.putString(Constant.ShredPrefKey.USER_ID, userId);
		edit.putString(Constant.ShredPrefKey.PHOTO, image);
		edit.putBoolean(Constant.ShredPrefKey.SESSION, session);
		edit.commit();
	}
	
	public void setFirstname(String str){
	        userFirstName                    = str;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.ShredPrefKey.FIRST_NAME, userFirstName);
		edit.commit();
	}
	
	
	public void setImage(String img){
		image                    = img;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.ShredPrefKey.USER_ID, image);
		edit.commit();
	}
	
	public void setSession(boolean flg){
		session                  = flg;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constant.ShredPrefKey.SESSION, session);
		edit.commit();
	}	
	
}
