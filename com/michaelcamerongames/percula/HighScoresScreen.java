package com.michaelcamerongames.percula;

import java.util.List;

import com.michaelcamerongames.framework.Game;
import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.implementation.AndroidScreen;
import com.michaelcamerongames.framework.Input.TouchEvent;

public class HighScoresScreen extends AndroidScreen 
{
	String posText[] = new String[5];
	String scoreText[] = new String[5];
	
	private static int Highlight = -1;
	
	public HighScoresScreen(Game game)
	{
		super(game);
		
		for (int i = 0; i < 5; i++)
		{
			posText[i] = "" + (i+1) + ". ";
			scoreText[i] = "" + Settings.HighScores[i];
		}
	}
	
	public static void SetHighlight(int highlightPosition)
	{
		Highlight = highlightPosition;
	}

	@Override
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
				if (event.x < 48 && event.y > 432)
				{
					if (!game.closeSubScreen())
					{
						if (Settings.SoundEnabled) Assets.ButtonSound.play(Settings.SFXVolume);
						game.setScreen(new MainMenuScreen(game));
					}
					return;
				}
			}
		}

	}

	@Override
	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.HighScoresScreen, 0, 0);
		
		int y = 100;
		for (int i = 0; i < 5; i++)
		{
			if (Highlight == i)
			{
				g.drawPixmap(Assets.HighScoreHighlight, 0, y-4);
			}
			Assets.DrawNumbers40(g, posText[i], 24, y, false);
			Assets.DrawNumbers40(g, scoreText[i], g.getWidth()-24, y, true);
			y += Assets.Numbers40Pixmap.getHeight() + 8;
		}
		
		g.drawPixmap(Assets.OkayButtonPixmap, 8, 440);
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void dispose()
	{
	}

}
