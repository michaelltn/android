package com.michaelcamerongames.framework;

import com.michaelcamerongames.framework.Graphics.PixmapFormat;

public interface Pixmap
{
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}