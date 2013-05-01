package com.michaelcamerongames.framework.implementation;

import android.media.SoundPool;

import com.michaelcamerongames.framework.Sound;

public class AndroidSound implements Sound
{
	int soundID;
	SoundPool soundPool;
	
	public AndroidSound(SoundPool _soundPool, int _soundID)
	{
		this.soundPool = _soundPool;
		this.soundID = _soundID;
	}

	public void play(float volume)
	{
		soundPool.play(soundID,  volume, volume, 0, 0, 1);
	}

	public void dispose()
	{
		soundPool.unload(soundID);
	}

}
