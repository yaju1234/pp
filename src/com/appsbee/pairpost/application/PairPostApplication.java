/**
 * @author Ratul Ghosh
 * 26-Feb-2014
 * 
 */
package com.appsbee.pairpost.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.appsbee.pairpost.R;
import com.appsbee.pairpost.http.HttpClient;


public class PairPostApplication extends Application
{

	private static HttpClient httpClient;
	private SharedPreferences sharedPreferences;
	private String loggedUserId, loggedUserName, loggedUserImageUri;
	private long userInfoLastUpdatedTime;
	public AppInfo mAppUserInfo;

	@Override
	public void onCreate()
	{
		super.onCreate();
		httpClient = new HttpClient(this);
		sharedPreferences = getSharedPreferences(getString(R.string.app_name),
				MODE_PRIVATE);

	}
	
	public AppInfo getAppInfo() {
		return mAppUserInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.mAppUserInfo = appInfo;
	}

	public static HttpClient getHttpClient()
	{
		return httpClient;
	}

	public SharedPreferences getSharedPreferences()
	{
		return sharedPreferences;
	}

	public boolean isNetWorkAvailable()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo)
		{
			if (ni.isConnected()) return true;
		}
		return false;
	}

	public String getLoggedUserId()
	{
		return loggedUserId;
	}

	public void setLoggedUserId(String loggedUserId)
	{
		this.loggedUserId = loggedUserId;
	}

	public String getLoggedUserName()
	{
		return loggedUserName;
	}

	public void setLoggedUserName(String loggedUserName)
	{
		this.loggedUserName = loggedUserName;
	}

	public String getLoggedUserImageUri()
	{
		return loggedUserImageUri;
	}

	public void setLoggedUserImageUri(String loggedUserImageUri)
	{
		this.loggedUserImageUri = loggedUserImageUri;
	}

	public long getUserInfoLastUpdatedTime()
	{
		return userInfoLastUpdatedTime;
	}

	public void setUserInfoLastUpdatedTime(long userInfoLastUpdatedTime)
	{
		this.userInfoLastUpdatedTime = userInfoLastUpdatedTime;
	}

}
