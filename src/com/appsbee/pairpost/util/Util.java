/**
 * @author Ratul Ghosh
 * 27-Feb-2014
 * 
 */
package com.appsbee.pairpost.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.appsbee.pairpost.application.Constant;

import android.content.Context;
import android.widget.Toast;

public class Util
{

	public static boolean isJsonResponseOK(Context context, String jsonData)
	{
		try
		{
			JSONObject jRootObj = new JSONObject(jsonData);
			JSONObject jObjStatus = jRootObj.getJSONObject("status");
			if (jObjStatus.getInt("status_code") != Constant.Flag.FLAG_SUCCESS)
			{
				Toast.makeText(context, jObjStatus.getString("message"),
						Toast.LENGTH_LONG).show();
				return false;
			}
		}
		catch (JSONException e)
		{

			LogManager.e("Utils", e.getMessage());
			return false;

		}
		return true;
	}

	public static void CopyStream(InputStream is, OutputStream os)
	{

		final int buffer_size = 1024;
		try
		{
			byte[] bytes = new byte[buffer_size];

			for (;;)
			{
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1) break;
				os.write(bytes, 0, count);
			}
		}
		catch (Exception ex)
		{
		}
	}
	
	
	    public static String readXMLinString(String fileName, Context c) {
		try {
			InputStream is = c.getAssets().open(fileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String text = new String(buffer);

			return text;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
