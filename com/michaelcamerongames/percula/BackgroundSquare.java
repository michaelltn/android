package com.michaelcamerongames.percula;

import android.graphics.Rect;

import com.michaelcamerongames.framework.Graphics;

public class BackgroundSquare
{
	Board board;
	PieceQueue pieceQueue;
	
	boolean isHighlighted;
	
	Rect frameRect = new Rect();
	int x, y;
	
	public BackgroundSquare()
	{
	}
	
	public static int getWidth()
	{
		return Assets.BackgroundSquarePixmap.getWidth();
	}

	public static int getHeight()
	{
		return Assets.BackgroundSquarePixmap.getHeight();
	}
	
	
	
	public void AssignBoard(Board _board)
	{
		this.board = _board;
	}
	
	public void AssignQueue(PieceQueue _queue)
	{
		this.pieceQueue = _queue;
	}
	
	
	public void setPosition(int _x, int _y)
	{
		this.x = _x;
		this.y = _y;
	}
	
	public void setHighlight(boolean highlighted)
	{
		isHighlighted = highlighted;
	}
	
	public void draw(Graphics graphics, int parentX, int parentY)
	{
		// removed as unnecessary.  highlight arrows should be moved somewhere else.
		//graphics.drawPixmap(Assets.BackgroundSquarePixmap, parentX + x, parentY + y);
		
		if (isHighlighted)
		{
			int index = Background.Animator.getCurrentFrameIndex();
			int animWidth = Assets.DropArrowAnimPixmap.getWidth();
			int animHeight = Assets.DropArrowAnimPixmap.getHeight() / Background.Animator.getTotalFrames();
			graphics.drawPixmap(Assets.DropArrowAnimPixmap, parentX + x, parentY + y, 0, index * getHeight(), animWidth, animHeight);
		}
	}

}
