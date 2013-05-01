package com.michaelcamerongames.percula;

import com.michaelcamerongames.framework.implementation.AndroidPixmap;
import com.michaelcamerongames.framework.Graphics;

public class Background
{
	public AndroidPixmap pixmap;
	
	int x, y;
	
	public BackgroundSquare[] backgroundSquares;
	int totalSquares;
	
	public PieceQueue queue;
	public Board board;
	
	public static FrameAnimator Animator;
	
	public Background()
	{
		Animator = new FrameAnimator(6, 9, FrameAnimator.WrapMode.PING_PONG);
		Animator.start();
	}
	
	public void init(PieceQueue _queue, Board _board)
	{
		int i, sx, sy;
		queue = _queue;
		board = _board;
		
		int totalSquares = board.getTotalSpaces();
		backgroundSquares = new BackgroundSquare[totalSquares];
		
		for (int r = 0; r < board.getSpacesTall(); r++)
		{
			for (int c = 0; c < board.getSpacesWide(); c++)
			{
				i = board.getIndex(r, c);
				sx = c * BackgroundSquare.getWidth();
				sy = (board.getSpacesTall() - r - 1) * BackgroundSquare.getHeight();
				
				backgroundSquares[i] = new BackgroundSquare();
				backgroundSquares[i].setHighlight(false);
				
				backgroundSquares[i].setPosition(sx, sy);
			}
		}
	}
	
	public void setPosition(int _x, int _y)
	{
		this.x = _x;
		this.y = _y;
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getWidth()
	{
		if (board == null) return 0;
		return board.getSpacesWide() * BackgroundSquare.getWidth();
	}
	public int getHeight()
	{
		if (board == null) return 0;
		return board.getSpacesTall() * BackgroundSquare.getHeight();
	}
	
	
	public void draw(Graphics g)
	{
		int i = 0;
		for (int r = 0; r < board.getSpacesTall(); r++)
		{
			for (int c = 0; c < board.getSpacesWide(); c++)
			{
				i = board.getIndex(r, c);
				boolean isHighlighted = (c == queue.getNextPieceX() || c == queue.getNextPieceX()+1);				
				backgroundSquares[i].setHighlight(isHighlighted);
				backgroundSquares[i].draw(g, x, y);
			}
		}
	}
	
	public void update(float deltaTime)
	{
		if (Animator != null)
			Animator.update(deltaTime);
	}

}
