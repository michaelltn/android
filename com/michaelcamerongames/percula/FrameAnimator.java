package com.michaelcamerongames.percula;

public class FrameAnimator
{
	public enum WrapMode
	{
		ONCE,
		CLAMP,
		LOOP,
		PING_PONG
	};
	
	private boolean isPlaying;
	
	private int totalFrames;
	private float timePerFrame;
	
	private int frame;
	private int direction;
	private WrapMode wrapMode;
	
	private float timeBuffer;
	
	public int getTotalFrames() { return totalFrames; }
	public int getCurrentFrame() { return frame; }
	public int getCurrentFrameIndex() { return frame-1; }
	
	public FrameAnimator(int frameCount, float framesPerSecond, WrapMode wrapMode)
	{
		totalFrames = (frameCount > 0 ? frameCount : 1);
		timePerFrame = (framesPerSecond > 0 ? 1f/framesPerSecond : 1f);
		this.wrapMode = wrapMode;
		
		frame = 1;
		direction = 1;
		timeBuffer = 0;
		isPlaying = false;
	}
	
	public void start()
	{
		isPlaying = true;
	}
	
	public void stop()
	{
		isPlaying = false;		
	}
	
	public void reset()
	{
		isPlaying = false;
		frame = 1;
		direction = 1;
		timeBuffer = 0;
	}
	
	public void update(float deltaTime)
	{
		if (isPlaying)
		{
			timeBuffer += deltaTime;
			while (timeBuffer >= timePerFrame)
			{
				timeBuffer -= timePerFrame;
				nextFrame();
			}
		}
	}
	
	private void nextFrame()
	{
		if (totalFrames > 1)
		{
			frame += direction;
			
			if (frame > totalFrames)
			{
				if (wrapMode == WrapMode.ONCE)
				{
					frame = 1;
					isPlaying = false;
					return;
				}
				if (wrapMode == WrapMode.CLAMP)
				{
					frame = totalFrames;
				}
				if (wrapMode == WrapMode.LOOP)
				{
					frame = 1;
				}
				if (wrapMode == WrapMode.PING_PONG)
				{
					frame = totalFrames - 1;
					direction *= -1;
				}
			}
			if (frame < 1)
			{
				if (wrapMode == WrapMode.ONCE)
				{
					frame = 1;
					isPlaying = false;
					return;
				}
				if (wrapMode == WrapMode.CLAMP)
				{
					frame = 1;
				}
				if (wrapMode == WrapMode.LOOP)
				{
					frame = totalFrames;
				}
				if (wrapMode == WrapMode.PING_PONG)
				{
					frame = 2;
					direction *= -1;
				}
			}
		}
	}
	
	
}
