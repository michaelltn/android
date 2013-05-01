package com.michaelcamerongames.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.Pixmap;

public class AndroidGraphics implements Graphics
{
	AssetManager assetManager;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();
	
	public AndroidGraphics(AssetManager _assetManager, Bitmap _frameBuffer)
	{
		this.assetManager = _assetManager;
		this.frameBuffer = _frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}
	
	public Pixmap newPixmap(String fileName, PixmapFormat format)
	{
		Config config = null;
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		if (format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		if (format == PixmapFormat.ARGB8888)
			config = Config.ARGB_8888;
		
		Options options = new Options();
		options.inPreferredConfig = config;
		
		InputStream in = null;
		Bitmap bitmap = null;
		try
		{
			in = assetManager.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap == null)
			{
				throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
		}
		finally
		{
			if (in != null)
			{
				try { in.close(); }
				catch (IOException e) {}
			}
		}
		
		if (bitmap.getConfig() == Config.RGB_565)
			format = PixmapFormat.RGB565;
		if (bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		if (bitmap.getConfig() == Config.ARGB_8888)
			format = PixmapFormat.ARGB8888;
		
		return new AndroidPixmap(bitmap, format);
	}

	public void clear(int colour)
	{
		canvas.drawRGB((colour & 0xff0000) >> 16, (colour & 0xff00) >> 8, colour & 0xff);
	}

	public void drawPixel(int x, int y, int colour)
	{
		paint.setColor(colour);
		canvas.drawPoint(x, y, paint);
	}
	
	public void drawLine(int x1, int y1, int x2, int y2, int colour)
	{
		paint.setColor(colour);
		canvas.drawLine(x1,  y1, x2, y2, paint);
	}

	public void drawRect(int x, int y, int width, int height, int colour)
	{
		paint.setColor(colour);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x,  y, x+width-1, y+height-1, paint);
	}

	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
	{
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;
		
		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;
		
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
	}
	
	public void drawPixmap(Pixmap pixmap, int x, int y)
	{
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, x, y, null);
	}

	public int getWidth()
	{
		return frameBuffer.getWidth();
	}

	public int getHeight()
	{
		return frameBuffer.getHeight();
	}

}
