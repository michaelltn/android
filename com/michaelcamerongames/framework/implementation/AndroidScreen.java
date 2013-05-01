package com.michaelcamerongames.framework.implementation;

import com.michaelcamerongames.framework.Game;
import com.michaelcamerongames.framework.Screen;

public abstract class AndroidScreen extends Screen
{
	protected AndroidGame aGame;
	protected boolean adVisible;

	public AndroidScreen(Game game)
	{
		super(game);
		aGame = (AndroidGame)game;
		adVisible = false;
//		if (aGame != null)
//			aGame.showAds(false);
	}

	/*
	@Override
	public void update(float deltaTime)
	{
		
	}

	@Override
	public void present(float deltaTime)
	{

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
	 */
}
