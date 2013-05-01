package com.michaelcamerongames.percula;

import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.Pixmap;

public class ShotClock
{
	private float remaining;
	private int frameCount;
	private int currentFrameIndex;
	private Pixmap clockPixmap;
	
	private int frameWidth, frameHeight;
	
	public ShotClock(int frameCount, Pixmap clockPixmap)
	{
		this.frameCount = frameCount;
		this.currentFrameIndex = frameCount-1;
		this.clockPixmap = clockPixmap;
		this.frameWidth = this.clockPixmap.getWidth();
		this.frameHeight = this.clockPixmap.getHeight() / this.frameCount;
		remaining = 1.0f;
	}
	
	public void updateClock(float timeRemaining, float maxTime)
	{
		if (maxTime > 0)
		{
			remaining = timeRemaining/maxTime;
			
			if (remaining >= 1)
			{
				currentFrameIndex = frameCount-1;
				return;
			}
			if (remaining > 0)
			{
				currentFrameIndex = Math.round(remaining * (frameCount-1));
				return;
			}
			currentFrameIndex = 0;
		}
	}
	
	public void draw(Graphics g)
	{
		g.drawPixmap(clockPixmap, Layout.ClockRect.left, Layout.ClockRect.top, 0, frameHeight * currentFrameIndex, frameWidth, frameHeight);
	}
}
