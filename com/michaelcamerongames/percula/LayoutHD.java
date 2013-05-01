package com.michaelcamerongames.percula;

import android.graphics.Rect;


public class LayoutHD
{
	public static final Rect ScreenRect = new Rect(  0,   0, 640, 960);

	public static final Rect PauseRect  = new Rect(  0,   0,  64,  64);
	public static final Rect ScoreRect  = new Rect( 96,   0, 512,  48);
	public static final Rect ClockRect  = new Rect(544,   0, 640,  64);

	public static final Rect QueueRect = new Rect( 32,  64, 480, 192);
	public static final Rect BoardRect = new Rect( 32, 192, 480, 896);

	public static final Rect BacklogRect = new Rect(496, 128, 624, 800);
	public static final int BacklogBuffer = 16;
	public static int GetBackLogTopByIndex(int index)
	{
		return LayoutHD.BacklogRect.top + (index * (LayoutHD.BacklogBuffer + Assets.QueueBackgroundPixmap.getHeight()));
	}

	public static final Rect MoveInput = new Rect( 32, 192, 480, 960);
	public static final Rect RotateInput = new Rect(480, 800, 640, 960);
	public static final Rect DropInput = new Rect(528, 688, 624, 784);
}