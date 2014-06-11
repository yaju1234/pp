package com.appsbee.pairpost.view;
//http://img.youtube.com/vi/NYtB6mlu7vA/default.jpg

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewCircle extends ImageView
{
	private int borderColor;
	private int borderWidth = 5;

	public ImageViewCircle(Context context)
	{
		super(context);
		borderColor = Color.parseColor("#00000000");

	}

	public ImageViewCircle(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		borderColor = Color.parseColor("#00000000");
	}

	public ImageViewCircle(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		borderColor = Color.parseColor("#00000000");
	}

	public void setColor(String hexColorCode)
	{
		borderColor = Color.parseColor(hexColorCode);
		invalidate();
	}

	public void setBorderWidth(int borderWidth)
	{
		this.borderWidth = borderWidth;
		invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas)
	{

		BitmapDrawable drawable = (BitmapDrawable) getDrawable();

		if (drawable == null)
		{
			return;
		}

		if (getWidth() == 0 || getHeight() == 0)
		{
			return;
		}

		Bitmap originalBitmap = drawable.getBitmap();

		BitmapShader shader;
		shader = new BitmapShader(originalBitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		Paint paint = new Paint();

		float circleCenterX = originalBitmap.getWidth() * 0.5f;
		float circleCenterY = originalBitmap.getHeight() * 0.5f;

		float radius = circleCenterX <= circleCenterY ? circleCenterX
				: circleCenterY;

		paint.setColor(borderColor);
		paint.setAntiAlias(true);
		canvas.drawCircle(circleCenterX, circleCenterY, radius, paint);

		paint.reset();
		paint.setAntiAlias(true);
		paint.setShader(shader);
		canvas.drawCircle(circleCenterX, circleCenterY, radius - borderWidth,
				paint);
	}

}
