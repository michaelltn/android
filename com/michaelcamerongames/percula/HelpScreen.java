package com.michaelcamerongames.percula;

import java.util.List;

import com.michaelcamerongames.framework.Game;
import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.implementation.AndroidScreen;
import com.michaelcamerongames.framework.Input.TouchEvent;

public class HelpScreen extends AndroidScreen
{
	int page;
	
	public HelpScreen(Game game)
	{
		super(game);
		page = 1;
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
				if (page < 7)
				{
					if (Settings.SoundEnabled) Assets.PageFlipSound.play(Settings.SFXVolume);
					page++;
				}
				else
				{
					if (!game.closeSubScreen())
					{
						if (Settings.SoundEnabled) Assets.ButtonSound.play(Settings.SFXVolume);
						game.setScreen(new MainMenuScreen(game));
					}
				}
//				if (event.y > 416 && event.x < 64)
//				{
//					if (page > 1)
//					{
//						if (Settings.SoundEnabled) Assets.PageFlipSound.play(Settings.SFXVolume);
//						page--;
//					}
//					else
//					{
//						if (!game.closeSubScreen())
//						{
//							if (Settings.SoundEnabled) Assets.ButtonSound.play(Settings.SFXVolume);
//							game.setScreen(new MainMenuScreen(game));
//						}
//					}
//				}
//				else if (event.y > 416 && event.x >= 256)
//				{
//					if (page < 7)
//					{
//						if (Settings.SoundEnabled) Assets.PageFlipSound.play(Settings.SFXVolume);
//						page++;
//					}
//					else
//					{
//						if (!game.closeSubScreen())
//						{
//							if (Settings.SoundEnabled) Assets.ButtonSound.play(Settings.SFXVolume);
//							game.setScreen(new MainMenuScreen(game));
//						}
//					}
//				}
				return;
			}
		}
	}

	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();
		switch (page)
		{
		case 1:
			g.drawPixmap(Assets.HelpScreen1, 0, 0);
			break;
		case 2:
			g.drawPixmap(Assets.HelpScreen2, 0, 0);
			break;
		case 3:
			g.drawPixmap(Assets.HelpScreen3, 0, 0);
			break;
		case 4:
			g.drawPixmap(Assets.HelpScreen4, 0, 0);
			break;
		case 5:
			g.drawPixmap(Assets.HelpScreen5, 0, 0);
			break;
		case 6:
			g.drawPixmap(Assets.HelpScreen6, 0, 0);
			break;
		default:
			g.drawPixmap(Assets.HelpScreen7, 0, 0);
			break;
		}
		//g.drawPixmap(Assets.BackButtonPixmap, 16, 432);
		//g.drawPixmap(Assets.OkayButtonPixmap, 272, 432);
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
