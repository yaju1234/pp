package com.appsbee.pairpost.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCache
{

	// private Map<String, SoftReference<Bitmap>> cache=Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());

	LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(
			(int) ((Runtime.getRuntime().maxMemory() / 1024) * .4));

	public Bitmap get(String id)
	{
		if (cache.get(id) == null) return null;
		Bitmap ref = cache.get(id);
		return ref;
	}

	public void put(String id, Bitmap bitmap)
	{
		cache.put(id, bitmap);
	}

	public void clear()
	{
		cache.evictAll();
	}
}
