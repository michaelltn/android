package com.michaelcamerongames.percula;

import java.util.List;

import android.graphics.Rect;

import com.michaelcamerongames.framework.Game;
import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.Input.TouchEvent;
import com.michaelcamerongames.framework.implementation.AndroidScreen;

public class MainMenuScreen extends AndroidScreen
{

	private Rect playButton = new Rect();
	private Rect settingsButton = new Rect();
	private Rect helpButton = new Rect();
	private Rect highScoresButton = new Rect();
	
	public MainMenuScreen(Game game)
	{
		super(game);
		
		// low-res
		playButton.set(        64, 181, 256, 236);
		settingsButton.set(    64, 236, 256, 291);
		helpButton.set(        64, 291, 256, 346);
		highScoresButton.set(  64, 346, 256, 401);
		// high-res
		//playButton.set(       128, 362, 511, 471);
		//settingsButton.set(   128, 472, 511, 581);
		//helpButton.set(       128, 582, 511, 691);
		//highScoresButton.set( 128, 692, 511, 801);
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
				if (playButton.contains(event.x, event.y))
				{
					game.setScreen(new GameScreen(game));
					return;
				}
				if (settingsButton.contains(event.x, event.y))
				{
					game.addSubScreen(new SettingsScreen(game));
					return;
				}
				if (helpButton.contains(event.x, event.y))
				{
					game.addSubScreen(new HelpScreen(game));
					return;
				}
				if (highScoresButton.contains(event.x, event.y))
				{
					HighScoresScreen.SetHighlight(-1);
					game.addSubScreen(new HighScoresScreen(game));
					return;
				}
			}
		}

	}
	
	@Override
	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.MainMenuScreen, 0, 0);
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
