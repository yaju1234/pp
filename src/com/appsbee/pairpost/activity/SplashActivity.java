/**
 * @author Ratul Ghosh
 * 25-Feb-2014
 * 
 */
package com.appsbee.pairpost.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.appsbee.pairpost.R;

public class SplashActivity extends BaseActivity
{

	private ImageView ivPair, ivPost, ivBgOrangeCircle,
			ivPairPosttextContainer;
	private int[] displayDimension;
	private boolean isPairAnimationCompleted, isPostAnimationCompleted;
	private Button btRegister, btLogin;
	private RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
		setContentView(R.layout.activity_splash);

		ivPair = (ImageView) findViewById(R.id.ivPair);
		ivPost = (ImageView) findViewById(R.id.ivPost);
		ivBgOrangeCircle = (ImageView) findViewById(R.id.ivBgOrangeCircle);
		ivPairPosttextContainer = (ImageView) findViewById(R.id.ivPairPostTextContainer);
		rlRoot = (RelativeLayout) findViewById(R.id.rlRoot);

		displayDimension = computeScreenDimension(this);

		int loc[] = new int[2];
		ivPair.getLocationInWindow(loc);
		Log.d(getClass().getSimpleName(), "screen dimension:"
				+ displayDimension[0] + "," + displayDimension[1]);
		ivPair.getLocationOnScreen(loc);
		Log.d(getClass().getSimpleName(),
				"before animation ivPair Co-Ordinate :" + loc[0] + "," + loc[1]);
		ivPost.getLocationOnScreen(loc);
		Log.d(getClass().getSimpleName(),
				"before animation ivPost Co-Ordinate :" + loc[0] + "," + loc[1]);
		ivBgOrangeCircle.getLocationOnScreen(loc);
		Log.d(getClass().getSimpleName(),
				"before animation ivBgOrange Co-Ordinate :" + loc[0] + ","
						+ loc[1]);
		ivPair.post(new Runnable()
		{

			@Override
			public void run()
			{
				AnimationSet ivPairAnimationSet = new AnimationSet(true);
				ivPairAnimationSet.setFillAfter(true);
				final int xy[] = new int[2];
				ivPair.getLocationOnScreen(xy);

				int bottomCoY = (displayDimension[1] - 334);
				TranslateAnimation animationTopToDown = new TranslateAnimation(
						Animation.ABSOLUTE, 0, Animation.ABSOLUTE,
						((int) (displayDimension[0] * .55f))
								- (xy[0] + ivPair.getWidth()),
						Animation.ABSOLUTE,
						0 - (xy[1] + ivPair.getHeight() + 50),
						Animation.ABSOLUTE, (displayDimension[1] - 334) - xy[1]);
				Log.d(getClass().getSimpleName(), ivPair.getHeight() + "");
				animationTopToDown.setDuration(700);
				animationTopToDown.setStartOffset(400);
				animationTopToDown
						.setInterpolator(new AccelerateInterpolator());
				animationTopToDown.setAnimationListener(new AnimationListener()
				{

					@Override
					public void onAnimationStart(Animation animation)
					{
					}

					@Override
					public void onAnimationRepeat(Animation animation)
					{
					}

					@Override
					public void onAnimationEnd(Animation animation)
					{
					}
				});

				RotateAnimation animationClockWiseRotation = new RotateAnimation(
						0f, 15f, Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, .5f);

				animationClockWiseRotation.setDuration(200);
				animationClockWiseRotation.setStartOffset(animationTopToDown
						.getDuration() + animationTopToDown.getStartOffset());

				ScaleAnimation animationScaleAnimSquashSrink = new ScaleAnimation(
						1f, 1f, 1f, .8f, .5f, .5f);
				animationScaleAnimSquashSrink.setDuration(200);
				animationScaleAnimSquashSrink.setStartOffset(animationClockWiseRotation
						.getStartOffset()
						+ (animationClockWiseRotation.getDuration() / 2));

				RotateAnimation animationAntiClockWiseRotation = new RotateAnimation(
						0f, -5f, Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, .5f);
				animationAntiClockWiseRotation.setDuration(200);
				animationAntiClockWiseRotation
						.setStartOffset(animationScaleAnimSquashSrink
								.getStartOffset()
								+ animationScaleAnimSquashSrink.getDuration());
				animationAntiClockWiseRotation.setFillAfter(true);

				ScaleAnimation animationScaleAnimSquashExpand = new ScaleAnimation(
						1f, 1f, .8f, 1f, .5f, .5f);
				animationScaleAnimSquashExpand.setDuration(300);
				animationScaleAnimSquashExpand
						.setStartOffset(animationAntiClockWiseRotation
								.getStartOffset()
								+ animationAntiClockWiseRotation.getDuration());

				TranslateAnimation animationBottomToTopRight = new TranslateAnimation(
						Animation.ABSOLUTE,
						0,
						Animation.ABSOLUTE,
						((displayDimension[0] / 2) - (xy[0] + ivPair.getWidth())),
						Animation.ABSOLUTE,
						0,
						Animation.ABSOLUTE,
						(((int) (displayDimension[1] * .50)) - (bottomCoY + ivPair
								.getHeight())));

				animationBottomToTopRight.setDuration(500);
				animationBottomToTopRight
						.setStartOffset(animationScaleAnimSquashSrink
								.getStartOffset()
								+ animationScaleAnimSquashSrink.getDuration());
				animationBottomToTopRight
						.setAnimationListener(new AnimationListener()
						{

							@Override
							public void onAnimationStart(Animation animation)
							{
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animation animation)
							{
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation animation)
							{
								animationFinished("pair");
							}
						});
				/*	ScaleAnimation animationIvPairZoomInZoomOut = new ScaleAnimation(
							1f, 1.2f, 1f, 1.1f, Animation.RELATIVE_TO_SELF, 1f,
							Animation.RELATIVE_TO_SELF, .5f);
					animationIvPairZoomInZoomOut.setDuration(50);
					//animationIvPairZoomInZoomOut.setRepeatCount(1);
					//animationIvPairZoomInZoomOut.setRepeatMode(Animation.REVERSE);
					animationIvPairZoomInZoomOut
							.setStartOffset(animationBottomToTopRight
									.getStartOffset());*/

				ivPairAnimationSet.addAnimation(animationTopToDown);
				ivPairAnimationSet.addAnimation(animationClockWiseRotation);
				ivPairAnimationSet.addAnimation(animationScaleAnimSquashSrink);
				ivPairAnimationSet.addAnimation(animationAntiClockWiseRotation);
				ivPairAnimationSet.addAnimation(animationScaleAnimSquashExpand);
				ivPairAnimationSet.addAnimation(animationBottomToTopRight);
				//ivPairAnimationSet.addAnimation(animationIvPairZoomInZoomOut);
				ivPair.startAnimation(ivPairAnimationSet);

			}
		});

		ivPost.post(new Runnable()
		{

			@Override
			public void run()
			{
				final int xy[] = new int[2];
				ivPost.getLocationOnScreen(xy);
				int bottomCoY = (displayDimension[1] - 334);

				AnimationSet ivPostAnimationSet = new AnimationSet(true);
				ivPostAnimationSet.setFillAfter(true);
				TranslateAnimation animationLeftToRight = new TranslateAnimation(
						Animation.ABSOLUTE, displayDimension[0]
								+ ivPost.getWidth(), Animation.ABSOLUTE,
						(displayDimension[0] * .45f) - (xy[0]),
						Animation.ABSOLUTE,
						(displayDimension[1] - 384) - xy[1],
						Animation.ABSOLUTE, (displayDimension[1] - 384) - xy[1]);
				Log.d("ivPost", xy[1] + "");
				animationLeftToRight.setDuration(700);
				animationLeftToRight.setStartOffset(400);

				RotateAnimation animationAntiClockWiseRotation = new RotateAnimation(
						0f, -10f, Animation.RELATIVE_TO_SELF, 1f,
						Animation.RELATIVE_TO_SELF, .5f);

				animationAntiClockWiseRotation.setDuration(200);
				animationAntiClockWiseRotation
						.setStartOffset(animationLeftToRight.getDuration()
								+ animationLeftToRight.getStartOffset());

				ScaleAnimation animationScaleAnimSquashSrink = new ScaleAnimation(
						1f, 1f, 1f, .8f, .5f, .5f);
				animationScaleAnimSquashSrink.setDuration(200);
				animationScaleAnimSquashSrink.setStartOffset(animationAntiClockWiseRotation
						.getStartOffset()
						+ (animationAntiClockWiseRotation.getDuration() / 2));

				RotateAnimation animationClockWiseRotation = new RotateAnimation(
						0f, 5f, Animation.RELATIVE_TO_SELF, 1f,
						Animation.RELATIVE_TO_SELF, .5f);
				animationClockWiseRotation.setDuration(200);
				animationClockWiseRotation
						.setStartOffset(animationScaleAnimSquashSrink
								.getStartOffset()
								+ animationScaleAnimSquashSrink.getDuration());
				animationClockWiseRotation.setFillAfter(true);

				ScaleAnimation animationScaleAnimSquashExpand = new ScaleAnimation(
						1f, 1f, .8f, 1f, .5f, .5f);
				animationScaleAnimSquashExpand.setDuration(300);
				animationScaleAnimSquashExpand
						.setStartOffset(animationClockWiseRotation
								.getStartOffset()
								+ animationClockWiseRotation.getDuration());

				TranslateAnimation animationBottomToTopLeft = new TranslateAnimation(
						Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
						Animation.ABSOLUTE, 0, Animation.ABSOLUTE,
						(((int) (displayDimension[1] * .50)) - (bottomCoY
								+ ivPost.getHeight() - 50)));

				animationBottomToTopLeft.setDuration(500);
				animationBottomToTopLeft
						.setStartOffset(animationScaleAnimSquashSrink
								.getStartOffset()
								+ animationScaleAnimSquashSrink.getDuration());

				animationBottomToTopLeft
						.setAnimationListener(new AnimationListener()
						{
							@Override
							public void onAnimationStart(Animation animation)
							{
							}

							@Override
							public void onAnimationRepeat(Animation animation)
							{
							}

							@Override
							public void onAnimationEnd(Animation animation)
							{
								animationFinished("post");

							}
						});

				/*ScaleAnimation animationIvPostZoomIn = new ScaleAnimation(
						1f, 1.1f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, .5f);
				animationIvPostZoomIn.setDuration(100);
				animationIvPostZoomIn.setFillAfter(false);
				//animationIvPostZoomInZoomOut.setRepeatCount(1);
				//animationIvPostZoomInZoomOut.setRepeatMode(Animation.REVERSE);
				animationIvPostZoomIn
						.setStartOffset(animationBottomToTopLeft
								.getStartOffset()+animationBottomToTopLeft.getDuration());

				ScaleAnimation animationIvPostZoomOut = new ScaleAnimation(
						1.1f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, .5f);
				animationIvPostZoomOut.setDuration(100);
				//animationIvPostZoomInZoomOut.setRepeatCount(1);
				//animationIvPostZoomInZoomOut.setRepeatMode(Animation.REVERSE);
				animationIvPostZoomOut
						.setStartOffset(animationIvPostZoomIn
								.getStartOffset()+animationIvPostZoomIn.getDuration());
				animationIvPostZoomOut.setFillAfter(false);*/

				ivPostAnimationSet.addAnimation(animationLeftToRight);
				ivPostAnimationSet.addAnimation(animationAntiClockWiseRotation);
				ivPostAnimationSet.addAnimation(animationScaleAnimSquashSrink);
				ivPostAnimationSet.addAnimation(animationClockWiseRotation);
				ivPostAnimationSet.addAnimation(animationScaleAnimSquashExpand);
				ivPostAnimationSet.addAnimation(animationBottomToTopLeft);
				//ivPostAnimationSet.addAnimation(animationIvPostZoomIn);
				//ivPostAnimationSet.addAnimation(animationIvPostZoomOut);
				ivPost.startAnimation(ivPostAnimationSet);

			}
		});
	}
	
	

	@Override
	protected void onResume() {
	    super.onResume();
	    if(application.getAppInfo().session){
		startActivity(new Intent(SplashActivity.this,MainActivity.class));
		SplashActivity.this.finish();
	    }
	}



	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public int[] computeScreenDimension(Context context)
	{
		int display_width, display_height;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		if (android.os.Build.VERSION.SDK_INT > 13)
		{
			display.getSize(size);
			display_width = size.x;
			display_height = size.y;
		}
		else
		{
			display_width = display.getWidth(); // deprecated
			display_height = display.getHeight();
		}
		return new int[] { display_width, display_height };
	}

	private synchronized void animationFinished(String animationName)
	{
		if (animationName == "pair")
			isPairAnimationCompleted = true;
		else
			isPostAnimationCompleted = true;
		if (isPairAnimationCompleted && isPostAnimationCompleted)
		{

			int loc[] = new int[2];
			ivPair.getLocationOnScreen(loc);
			Log.d(getClass().getSimpleName(),
					"after animation ivPair Co-Ordinate :" + loc[0] + ","
							+ loc[1]);
			ivPost.getLocationOnScreen(loc);
			Log.d(getClass().getSimpleName(),
					"after animation ivPost Co-Ordinate :" + loc[0] + ","
							+ loc[1]);

			ivBgOrangeCircle.getLocationOnScreen(loc);
			Log.d(getClass().getSimpleName(),
					"before animation ivBgOrange Co-Ordinate :" + loc[0] + ","
							+ loc[1]);

			int locPost[] = new int[2];
			ivPost.getLocationOnScreen(locPost);

			int locPair[] = new int[2];
			ivPair.getLocationOnScreen(locPair);

			final int bgOrangeColorRadius = (locPost[0] + ivPost.getWidth())
					- locPair[0];

			RelativeLayout.LayoutParams rlParam = new RelativeLayout.LayoutParams(
					bgOrangeColorRadius, bgOrangeColorRadius);
			int bgCircleXDelta = (locPair[0]) - 10;
			int bgCircleTopPadding = ((int) ((bgOrangeColorRadius * 2) - ivPair
					.getHeight()) / 2);
			int bgCircleYDelta = (locPair[1]) - 10;
			int bottomCoY = (displayDimension[1] - 334);
			rlParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
			final int topMargin = (((int) (displayDimension[1] * .60)) - (bgOrangeColorRadius)) > 0 ? (((int) (displayDimension[1] * .60)) - (bgOrangeColorRadius))
					: 10;
			rlParam.topMargin = topMargin;

			ivBgOrangeCircle.setLayoutParams(rlParam);

			ivBgOrangeCircle.setVisibility(View.VISIBLE);
			ScaleAnimation animationScaleAnimation = new ScaleAnimation(.5f,
					1f, .5f, 1f, Animation.RELATIVE_TO_SELF, .5f,
					Animation.RELATIVE_TO_SELF, .5f);
			animationScaleAnimation.setDuration(200);
			/*			ScaleAnimation animationColorChange = new ScaleAnimation(1f, 1f,
								1f, 1f, .5f, .5f);					
			*/
			AlphaAnimation animationColorChange = new AlphaAnimation(1f, 1f);
			animationColorChange.setDuration(200);
			animationColorChange.setRepeatCount(10);
			animationColorChange.setRepeatMode(Animation.REVERSE);
			animationColorChange.setStartOffset(animationScaleAnimation
					.getDuration());
			final String[] colors = { "#313330", "#00FE01", "#5B5A59",
					"#4E81BA" };

			animationColorChange.setAnimationListener(new AnimationListener()
			{
				int i = 0;

				@Override
				public void onAnimationStart(Animation animation)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation)
				{

					GradientDrawable circle = (GradientDrawable) ivBgOrangeCircle
							.getDrawable().mutate();
					circle.setColor(Color.parseColor(colors[i % colors.length]));
					i++;
				}

				@Override
				public void onAnimationEnd(Animation animation)
				{
					RelativeLayout.LayoutParams rlParam2 = (LayoutParams) ivPairPosttextContainer
							.getLayoutParams();
					rlParam2.topMargin = topMargin + 200 + bgOrangeColorRadius;
					ivPairPosttextContainer.setLayoutParams(rlParam2);

					TranslateAnimation textTranslateAnimation = new TranslateAnimation(
							Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f,
							Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, -250f);
					textTranslateAnimation.setDuration(1000);
					textTranslateAnimation.setFillAfter(true);
					ivBgOrangeCircle.setVisibility(View.INVISIBLE);
					textTranslateAnimation
							.setAnimationListener(new AnimationListener()
							{

								@Override
								public void onAnimationStart(Animation animation)
								{
									ivPairPosttextContainer.postDelayed(
											new Runnable()
											{

												@Override
												public void run()
												{
													ivPairPosttextContainer
															.setVisibility(View.VISIBLE);

												}
											}, 100);

								}

								@Override
								public void onAnimationRepeat(
										Animation animation)
								{

								}

								@Override
								public void onAnimationEnd(Animation animation)
								{
									new Handler().postDelayed(new Runnable()
									{

										@Override
										public void run()
										{

											btLogin = new Button(
													SplashActivity.this);
											btLogin.setId(Math.abs("btLogin"
													.hashCode()));
											RelativeLayout.LayoutParams rlParam3 = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											rlParam3.addRule(RelativeLayout.CENTER_HORIZONTAL);
											rlParam3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
											rlParam3.bottomMargin = 25;
											btLogin.setText("LOG IN");
											btLogin.setTextColor(Color
													.parseColor("#00FF01"));
											btLogin.setBackgroundResource(R.drawable.login_button_bg);
											btLogin.setOnClickListener(new OnClickListener()
											{

												@Override
												public void onClick(View v)
												{
													startActivity(new Intent(
															SplashActivity.this,
															LoginActivity.class));
													SplashActivity.this.finish();

												}
											});
											btLogin.setLayoutParams(rlParam3);
											rlRoot.addView(btLogin);
											btRegister = new Button(
													SplashActivity.this);
											RelativeLayout.LayoutParams rlParam4 = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											rlParam4.addRule(RelativeLayout.CENTER_HORIZONTAL);
											rlParam4.addRule(
													RelativeLayout.ABOVE,
													btLogin.getId());
											rlParam4.bottomMargin = 25;
											btRegister
													.setLayoutParams(rlParam4);
											btRegister.setText("Register");
											btRegister.setTextColor(Color
													.parseColor("black"));
											btRegister
													.setBackgroundResource(R.drawable.register_button);
											btRegister
													.setOnClickListener(new OnClickListener()
													{

														@Override
														public void onClick(
																View v)
														{
															startActivity(new Intent(
																	SplashActivity.this,
																	RegistrationActivity.class));
															SplashActivity.this.finish();

														}
													});
											rlRoot.addView(btRegister);

										}
									}, 200);
								}
							});
					ivPairPosttextContainer
							.startAnimation(textTranslateAnimation);

				}
			});
			AnimationSet bgAnimationSet = new AnimationSet(true);
			bgAnimationSet.addAnimation(animationScaleAnimation);
			bgAnimationSet.addAnimation(animationColorChange);
			ivBgOrangeCircle.startAnimation(bgAnimationSet);

		}
	}
}
