package com.michaelcamerongames.percula;

import java.util.Random;

import com.michaelcamerongames.framework.Graphics;

public class PieceQueue
{
	public enum Mode { OnePlayer, TwoPlayers };
	public Mode mode = Mode.OnePlayer;
	private int player = 1;
	public void changePlayers()
	{
		if (mode == Mode.TwoPlayers)
			player = ( player==1 ? 2 : 1 );
	}
	public int currentPlayer() { return player; }
	
	public Board board;
	public Background background;
	
	// 32
	// 01
	private int backlogVisibility = 3;
	public void setBacklogVisibility(int visibility)
	{
		if (visibility > 3)
			backlogVisibility = 3;
		else if (visibility < 0)
			backlogVisibility = 0;
		else
			backlogVisibility = visibility;
	}
	
	private Piece[][] backlog = new Piece[3][4];
	private Piece[] nextPiece = new Piece[4];
	private int nextPiecePosition;
	
	public float spawnTime = 0.5f;
	private static float spawnTimeRemaining;

	
	boolean topRowEmpty;

	
	public PieceQueue()
	{
		int b, p;
		
		for (p = 0; p < 4; p++)
			nextPiece[p] = null;
		
		for (b = 0; b < 3; b++)
			for (p = 0; p < 4; p++)
				backlog[b][p] = null;
	}
	
	public void init(Board _board, Background _background)
	{
		this.board = _board;
		this.background = _background;
	}
	
	public void update(float deltaTime)
	{
		if (GameScreen.IsRunning())
		{
			if (spawnTimeRemaining > 0) {
				spawnTimeRemaining -= deltaTime;
				if (spawnTimeRemaining <= 0)
				{
					spawnTimeRemaining = 0;
				}
			}
		}
	}
	
	public void draw(Graphics g)
	{
		int b, p;
		int qx = background.getX() + (nextPiecePosition * BackgroundSquare.getWidth());
		int qy = background.getY() - (2 * BackgroundSquare.getHeight());
		g.drawPixmap(Assets.QueueBackgroundPixmap, qx, qy);
		
		for (int i = 0; i < backlogVisibility; i++)
		{
			g.drawPixmap(Assets.QueueBackgroundPixmap, Layout.BacklogRect.left, Layout.GetBackLogTopByIndex(i));
		}
		
		for (p = 0; p < 4; p++)
		{
			if (nextPiece[p] != null) 
				nextPiece[p].draw(g);
		}
		for (b = 0; b < backlogVisibility; b++)
		{
			for (p = 0; p < 4; p++)
			{
				if (backlog[b][p] != null)
					backlog[b][p].draw(g);
			}
		}
	}
	
	
	public int getNextPieceX()
	{
		return nextPiecePosition;		
	}
	
	public void pickNextPiece(Random random)
	{
		int blanks = 0;
		int b, p;
		
		for (p = 0; p < 4; p++)
		{
			nextPiece[p] = backlog[0][p];
		}
		
		for (b = 1; b < 3; b++)
		{
			for (p = 0; p < 4; p++)
			{
				backlog[b-1][p] = backlog[b][p];
			}
		}
		
		for (p = 0; p < 4; p++)
		{
			if (blanks == 3 || random.nextBoolean())
			{
				backlog[2][p] = Piece.GetRandomPiece(random);
			}
			else {
				backlog[b-1][p] = null;
				blanks++;
			}
		}
		
		spawnTimeRemaining = spawnTime;
		updateNextPiece();
		updateBacklogPieces();
	}
	
	public void fillBacklogs(Random random)
	{
		int blanks;
		int b, p;
		for (b = 0; b < 3; b++)
		{
			blanks = 0;
			for (p = 0; p < 4; p++)
			{
				if (blanks == 3 || random.nextBoolean())
				{
					backlog[b][p] = Piece.GetRandomPiece(random);
				}
				else {
					backlog[b][p] = null;
					blanks++;
				}
			}
		}
	}
	
	public boolean rotateNextPieceClockwise()
	{
		//rotationRemaining -= 90f;
		Piece temp = nextPiece[0];
		nextPiece[0] = nextPiece[1];
		nextPiece[1] = nextPiece[2];
		nextPiece[2] = nextPiece[3];
		nextPiece[3] = temp;
		updateNextPiece();
		return true;
	}
	
	public boolean rotateNextPieceCounterClockwise()
	{
		//rotationRemaining += 90f;
		Piece temp = nextPiece[3];
		nextPiece[3] = nextPiece[2];
		nextPiece[2] = nextPiece[1];
		nextPiece[1] = nextPiece[0];
		nextPiece[0] = temp;
		updateNextPiece();
		return true;
	}
	
	public boolean moveNextPieceLeft(int min)
	{
		if (nextPiecePosition > min)
		{
			nextPiecePosition--;
			updateNextPiece();
			return true;
		}
		return false;
	}
	
	public boolean moveNextPieceRight(int max)
	{
		if (nextPiecePosition < max-1)
		{
			nextPiecePosition++;
			updateNextPiece();
			return true;
		}
		return false;
	}
	
	public boolean moveNextPieceTo(int pos, int min, int max)
	{
		//if (pos < min) pos = min;
		//if (pos >= max) pos = max-1;
		if (pos >= min && pos <= max-1)
		{
			nextPiecePosition = pos;
			updateNextPiece();
			return true;
		}
		return false;
	}
	
	
	public boolean nextPieceReady()
	{
		return (spawnTimeRemaining <= 0);
	}
	
	public boolean dropNextPiece()
	{
		//finishRotating();
		topRowEmpty = false;
		if (nextPiece[2] == null && nextPiece[3] == null)
			topRowEmpty = true;
		
		int p;
		int[] c = new int[4];
		int[] r = new int[4];
		
		c[0] = nextPiecePosition;
		r[0] = board.getSpacesTall() - (topRowEmpty ? 1 : 2);
		c[1] = c[0] + 1;
		r[1] = r[0];
		c[2] = c[1];
		r[2] = r[1] + 1;
		c[3] = c[2] - 1;
		r[3] = r[2];
		
		for (p = 0; p < 4; p++)
		{
			if (nextPiece[p] != null)
			{
				if (!board.spaceIsEmpty(c[p], r[p]))
				{
					return false;
				}
					
			}
		}
		
		for (p = 0; p < 4; p++)
		{
			int fallDistance = (topRowEmpty ? 1 : 2);
			if (nextPiece[p] != null)
			{
				int targetR = r[p];
				int targetC = c[p];
				board.getSpace(targetR, targetC).piece = nextPiece[p];
				while (board.movePieceDown(targetC, targetR))
				{
					fallDistance++;
					targetR--;
				}
				board.getSpace(targetR, targetC).piece.quickFall(fallDistance);
			}
		}
		
		return true;
	}
	
	private void updateNextPiece()
	{
		int[] x = new int[4];
		int[] y = new int[4];
		
		x[0] = background.getX() + (this.nextPiecePosition * BackgroundSquare.getWidth());
		y[0] = background.getY() - BackgroundSquare.getHeight(); //+ background.getHeight();
		
		x[1] = x[0] + BackgroundSquare.getWidth();
		y[1] = y[0];

		x[2] = x[1];
		y[2] = y[1] - BackgroundSquare.getHeight();

		x[3] = x[2] - BackgroundSquare.getWidth();
		y[3] = y[2];

		for (int p = 0; p < 4; p++)
		{
			if (nextPiece[p] != null)
				nextPiece[p].setPosition(x[p], y[p]);
		}
	}
	
	private void updateBacklogPieces()
	{
		int[][] x = new int[3][4];
		int[][] y = new int[3][4];
		
		for (int i = 0; i < 3; i++)
		{
			x[i][0] = Layout.BacklogRect.left;
			y[i][0] = Layout.GetBackLogTopByIndex(i) + BackgroundSquare.getHeight();
		}
		
		for (int b = 0; b < 3; b++)
		{
		
			//x[b][1] = x[b][0] + (int)(BackgroundSquare.getWidth() * Layout.BacklogScale);
			x[b][1] = x[b][0] + BackgroundSquare.getWidth();
			y[b][1] = y[b][0];
	
			x[b][2] = x[b][1];
			y[b][2] = y[b][1] - BackgroundSquare.getHeight();
	
			x[b][3] = x[b][2] - BackgroundSquare.getWidth();
			y[b][3] = y[b][2];
	
			for (int p = 0; p < 4; p++)
			{
				if (backlog[b][p] != null)
					backlog[b][p].setPosition(x[b][p], y[b][p]);
			}
		}
	}

	public void killNextPiece()
	{
		for (int p = 0; p < 3; p++)
		{
			if (nextPiece[p] != null)
			{
				nextPiece[p].free();
				nextPiece[p] = null;
			}
		}
	}
	
	public void killBacklog()
	{
		for (int b = 0; b < 3; b++)
		{
			for (int p = 0; p < 4; p++)
			{
				if (backlog[b][p] != null)
				{
					backlog[b][p].free();
					backlog[b][p] = null;
				}
			}
		}
		
	}
	
}
