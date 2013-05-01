package com.michaelcamerongames.framework.implementation;

import android.graphics.Bitmap;

import com.michaelcamerongames.framework.Pixmap;
import com.michaelcamerongames.framework.Graphics.PixmapFormat;

public class AndroidPixmap implements Pixmap
{
	Bitmap bitmap;
	PixmapFormat format;
	
	public AndroidPixmap(Bitmap _bitmap, PixmapFormat _format)
	{
		this.bitmap = _bitmap;
		this.format = _format;
	}

	public int getWidth()
	{
		return bitmap.getWidth(); 
	}

	public int getHeight()
	{
		return bitmap.getHeight(); 
	}

	public PixmapFormat getFormat()
	{
		return format;
	}

	public void dispose()
	{
		bitmap.recycle();
	}

}
