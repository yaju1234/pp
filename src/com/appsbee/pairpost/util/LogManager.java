package com.appsbee.pairpost.util;

import android.util.Log;

public class LogManager
{

	public static enum DeploymentModes
	{
		Production, Testing, Development
	};

	public static DeploymentModes presentMode = DeploymentModes.Development;

	public static void i(String tag, String message)
	{
		if (presentMode != DeploymentModes.Production) Log.i(tag, message);

	}

	public static void d(String tag, String message)
	{
		if (presentMode != DeploymentModes.Production) Log.d(tag, message);

	}

	public static void e(String tag, String message)
	{
		Log.e(tag, message);
	}

	public static void v(String tag, String message)
	{
		if (presentMode != DeploymentModes.Production) Log.v(tag, message);

	}

	public static void wtf(String tag, String message)
	{
		Log.wtf(tag, message);

	}

}
