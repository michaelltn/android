package com.michaelcamerongames.framework;

public interface Graphics
{
	public static enum PixmapFormat { ARGB8888, ARGB4444, RGB565 };
	
	public Pixmap newPixmap(String fileName, PixmapFormat format);
	public void clear(int colour);
	public void drawPixel(int x, int y, int colour);
	public void drawLine(int x1, int y1, int x2, int y2, int colour);
	public void drawRect(int x, int y, int width, int height, int colour);
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
	public void drawPixmap(Pixmap pixmap, int x, int y);
	public int getWidth();
	public int getHeight();
}