package com.michaelcamerongames.percula;

import com.michaelcamerongames.framework.Graphics;

public class ScoreManager
{
	private int score;
	private String scoreString;
	
	public ScoreManager()
	{
		score = 0;
		scoreString = "0";
	}
	
	public void reset()
	{
		score = 0;
		scoreString = "0";
	}
	
	public int getScore() { return score; }
	public String getScoreString() { return scoreString; }
	
	public void addPoints(int points)
	{
		if (points > 0)
		{
			score += points;
			scoreString = Integer.toString(score);
		}
	}
	
	public void draw(Graphics g)
	{
		Assets.DrawNumbers20(g, scoreString, Layout.ScoreRect.right, Layout.ScoreRect.top, true);
	}
	
}
