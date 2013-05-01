package com.michaelcamerongames.framework.implementation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.michaelcamerongames.framework.Audio;
import com.michaelcamerongames.framework.FileIO;
import com.michaelcamerongames.framework.Game;
import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.Input;
import com.michaelcamerongames.framework.Screen;
import com.michaelcamerongames.framework.IActivityRequestHandler;
import com.michaelcamerongames.percula.Assets;
import com.michaelcamerongames.percula.Settings;

import android.util.Log;

//ad related imports
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.widget.RelativeLayout;
//import com.google.ads.AdRequest;
//import com.google.ads.AdSize;
//import com.google.ads.AdView;

public abstract class AndroidGame extends Activity implements Game, IActivityRequestHandler
{
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	Screen previousScreen;
	WakeLock wakeLock;
	
//	private AdView adView;
//	private final static int SHOW_ADS = 1;
//	private final static int HIDE_ADS = 0;
	
//	protected Handler handler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			switch (msg.what)
//			{
//			case SHOW_ADS:
//				adView.setVisibility(View.VISIBLE);
//				break;
//			case HIDE_ADS:
//				adView.setVisibility(View.GONE);
//				break;
//			}
//		}
//	};
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Log.v("Check", "android game onCreate");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		
		// low-res
		int frameBufferWidth = isLandscape ? 480 : 320;
		int frameBufferHeight = isLandscape ? 320 : 480;
		// high-res
		//int frameBufferWidth = isLandscape ? 960 : 640;
		//int frameBufferHeight = isLandscape ? 640 : 960;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
		
		float scaleX = (float)frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float)frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
		
		renderView = new AndroidFastRenderView(this, frameBuffer);
		
		// ad stuff - move to Percula Game
//			RelativeLayout layout = new RelativeLayout(this);
//			layout.addView(renderView);
//			
//			String pubID = "a14f8c135c6d62c";
//			adView = new AdView(this, AdSize.BANNER, pubID);
//			
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//			
//			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//			//params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//			
//			layout.addView(adView, params);
//			
//			setContentView(layout);
//			
//			AdRequest request = new AdRequest();
//			request.addTestDevice("put in test device code");
//			adView.loadAd(request);
		// end ad stuff
		
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);
		
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	public void onResume()
	{
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		if (previousScreen != null)
			previousScreen.resume();
		if (Settings.SoundEnabled && Assets.MusicTrack1 != null)
			Assets.MusicTrack1.play();
		renderView.resume();
	}
	
	public void onPause()
	{
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		if (previousScreen != null)
			previousScreen.pause();
		if (Assets.MusicTrack1 != null)// && Assets.MusicTrack1.isPlaying())
			Assets.MusicTrack1.pause();
//		adView.stopLoading();
		
		if (isFinishing())
		{
			screen.dispose();
			if (previousScreen != null)
				previousScreen.dispose();
		}
	}
	
	
	public Input getInput() { return input; }
	public FileIO getFileIO() { return fileIO; }
	public Graphics getGraphics() { return graphics; }
	public Audio getAudio() { return audio; }

	public void setScreen(Screen _screen)
	{
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		
		this.screen.pause();
		this.screen.dispose();
		_screen.resume();
		_screen.update(0);
		this.screen = _screen;
		this.previousScreen = null;
	}

	public Screen getCurrentScreen() { return screen; }
	
	public boolean addSubScreen(Screen _screen)
	{
		if (previousScreen == null)
		{
			previousScreen = this.screen;
			this.screen = _screen;
			return true;
		}
		return false;
	}
	
	public boolean closeSubScreen()
	{
		if (previousScreen != null)
		{
			setScreen(previousScreen);
			return true;
		}
		return false;
	}
	
	public void onBackPressed()
	{
		if (!closeSubScreen())
			super.onBackPressed();
	}
	
	
	public void showAds(boolean show)
	{
		//handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS );
	}

}
