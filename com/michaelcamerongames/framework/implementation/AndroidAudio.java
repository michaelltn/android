package com.michaelcamerongames.framework.implementation;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.michaelcamerongames.framework.Audio;
import com.michaelcamerongames.framework.Music;
import com.michaelcamerongames.framework.Sound;

public class AndroidAudio implements Audio
{
	AssetManager assetManager;
	SoundPool soundPool;
	
	public AndroidAudio(Activity activity)
	{
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assetManager = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	public Music newMusic(String fileName)
	{
		try
		{
			AssetFileDescriptor assetDescriptor = assetManager.openFd(fileName);
			return new AndroidMusic(assetDescriptor);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Couldn't load music '" + fileName + "'");
		}
	}

	public Sound newSound(String fileName)
	{
		try
		{
			AssetFileDescriptor assetDescriptor = assetManager.openFd(fileName);
			int soundID = soundPool.load(assetDescriptor,  0);
			return new AndroidSound(soundPool, soundID);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Couldn't load sound '" + fileName + "'");
		} 
	}

}
