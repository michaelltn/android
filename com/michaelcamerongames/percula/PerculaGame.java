package com.michaelcamerongames.percula;

import com.michaelcamerongames.framework.Screen;
import com.michaelcamerongames.framework.implementation.AndroidGame;

public class PerculaGame extends AndroidGame
{
	public Screen getStartScreen()
	{
		return new LoadingScreen(this);
	}
	
	@Override
	public void onBackPressed()
	{
		if (this.getCurrentScreen() == GameScreen.main)
		{
			if (GameScreen.isInGameState(GameScreen.GameState.Paused))
				setScreen(new MainMenuScreen(this));
			else
				this.getCurrentScreen().pause();
		}
		else
		{
			super.onBackPressed();
		}
	}
}
