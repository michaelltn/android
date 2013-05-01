package com.michaelcamerongames.percula;

import java.util.List;

import android.graphics.Rect;

import com.michaelcamerongames.framework.Game;
import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.implementation.AndroidScreen;
import com.michaelcamerongames.framework.Input.TouchEvent;

public class SettingsScreen extends AndroidScreen
{

	// low-res
	private Rect muteButtonPosition = new Rect(144, 113, 144+32, 113+32);
	private Rect sfxSliderPosition = new Rect(10, 190, 10+300, 190+60);
	private Rect musicSliderPosition = new Rect(10, 289, 10+300, 289+60);
	// high-res
	//private Rect muteButtonPosition = new Rect(288, 226, 288+64-1, 226+64-1);
	//private Rect sfxSliderPosition = new Rect(20, 380, 20+600-1, 380+120-1);
	//private Rect musicSliderPosition = new Rect(20, 578, 20+600-1, 578+120-1);
	
	public SettingsScreen(Game game)
	{
		super(game);
	}
	
	public void update(float deltaTime)
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for (int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP)
			{
				if (muteButtonPosition.contains(event.x, event.y))
				{
					Settings.SoundEnabled = !Settings.SoundEnabled;
					if (Settings.SoundEnabled) Assets.ButtonSound.play(Settings.SFXVolume);
					if (Settings.SoundEnabled)
					{
						Assets.MusicTrack1.play();
					}
					else
					{
						Assets.MusicTrack1.pause();
					}
				}
				if (Layout.RotateInput.contains(event.x, event.y))
				{
					if (Settings.SoundEnabled) Assets.ButtonSound.play(Settings.SFXVolume);
					Settings.RotationButtonIsClockwise = !Settings.RotationButtonIsClockwise;
				}
				if (event.x < 48 && event.y > 431)
				{
					if (Settings.SoundEnabled) Assets.ButtonSound.play(Settings.SFXVolume);
					Settings.Save();
					if (!game.closeSubScreen())
					{
						game.setScreen(new MainMenuScreen(game));
					}
					return;
				}
			}
			if (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)
			{
				if (Settings.SoundEnabled)
				{
					if (sfxSliderPosition.contains(event.x, event.y))
					{
						Settings.SetSFXVolume(((float)event.x - (float)sfxSliderPosition.left) / (float)sfxSliderPosition.width());
					}
					if (musicSliderPosition.contains(event.x, event.y))
					{
						Settings.SetMusicVolume(((float)event.x - (float)musicSliderPosition.left) / (float)musicSliderPosition.width());
						Assets.MusicTrack1.setVolume(Settings.MusicVolume);
					}
				}
			}
		}
	}

	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.SettingsScreen, 0, 0);
		g.drawPixmap(Assets.OkayButtonPixmap, 8, 440);
		
		if (Settings.RotationButtonIsClockwise)
			g.drawPixmap(Assets.RotateRightButtonPixmap, Layout.RotateInput.left, Layout.RotateInput.top);
		else
			g.drawPixmap(Assets.RotateLeftButtonPixmap, Layout.RotateInput.left, Layout.RotateInput.top);

		Settings.DrawMuteButton(g, muteButtonPosition.left, muteButtonPosition.top);
		if (Settings.SoundEnabled)
		{
			Settings.DrawSFXVolumeSlider(g, sfxSliderPosition.left, sfxSliderPosition.top);
			Settings.DrawMusicVolumeSlider(g, musicSliderPosition.left, musicSliderPosition.top);
		}
		else
		{
			Settings.DrawVolumeSlider(g, 0, sfxSliderPosition.left, sfxSliderPosition.top);
			Settings.DrawVolumeSlider(g, 0, musicSliderPosition.left, musicSliderPosition.top);
		}
	}

	public void pause()
	{
	}

	public void resume()
	{
	}

	public void dispose()
	{
	}
	
}
