package com.michaelcamerongames.percula;

import android.graphics.Rect;


public class Layout
{
	public static final Rect ScreenRect = new Rect(  0,   0, 320, 480);

	public static final Rect PauseRect  = new Rect(  0,   0,  32,  32);
	public static final Rect ScoreRect  = new Rect( 48,   0, 232,  24);
	//public static final Rect ScoreRect  = new Rect( 48,   0, 256,  24);
	public static final Rect ClockRect  = new Rect(256,   6, 320,  32);
	//public static final Rect ClockRect  = new Rect(272,   0, 320,  32);

	public static final Rect QueueRect = new Rect( 16,  32, 240, 96);
	public static final Rect BoardRect = new Rect( 16,  96, 240, 448);

	public static final Rect BacklogRect = new Rect(248,  64, 312, 400);
	public static final int BacklogBuffer = 8;
	public static int GetBackLogTopByIndex(int index)
	{
		return BacklogRect.top + (index * (BacklogBuffer + Assets.QueueBackgroundPixmap.getHeight()));
	}

	public static final Rect MoveInput   = new Rect( 16,  96, 240, 480);
	public static final Rect RotateInput = new Rect(240, 400, 320, 480);
	public static final Rect DropInput   = new Rect(264, 344, 312, 392);
}