package com.michaelcamerongames.percula;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.percula.PieceQueue;
import com.michaelcamerongames.percula.Background;

public abstract class Board
{
	int targetPieceCount;
	int startingTargetPieces = 3;
	int startingAdditionalPieces = 14;
	
	private boolean resetDropTimeForEachLevel = true;
	public void changeDropTimeAfterDrop()
	{
		resetDropTimeForEachLevel = true;
	}
	public void changeDropTimeAfterLevel()
	{
		resetDropTimeForEachLevel = false;
	}

	boolean autoDrop = true;
	float autoDropAfter = 3f;
	private float autoDropTime = 3f;
	private float autoDropTimeRemaining;
	//boolean firstDrop;
	
	int increaseTargetPiecesPerLevel = 0;
	int maximumTargetPieces = 3;
	int increaseAdditionalPiecesPerLevel = 2;
	int maximumAdditionalPieces = 46;
	float multiplyDropTimeBy = 0.9f;
	float minimumDropTime = 0.5f;
	boolean twoPlayers = false;
	int movesPerTurn = 1;
	
	
	public static FrameAnimator TargetAnimator;
	Background background;
	PieceQueue queue;
	ShotClock clock;
	ScoreManager scoreManager;
	
	int spacesWide = 7;
	int spacesTall = 11;
	int totalSpaces;
	Space[] space;
	List<Piece> dieingPieces;
	List<Piece> killStack;
	public Space[] getSpaces() { return space; }
	public int getSpacesWide() { return spacesWide; }
	public int getSpacesTall() { return spacesTall; }
	public int getTotalSpaces() { return totalSpaces; }
	
	int killThreshold = 4;
	int bonus;
	
	public static int animationLock = 0;
	protected boolean gameOver = false;
	protected boolean win = false;
	protected boolean next = false;
	
	boolean pieceLandingTrigger;
	
	static Random random = new Random(System.currentTimeMillis());
	
	public Board()
	{
		dieingPieces = new ArrayList<Piece>();
		killStack = new ArrayList<Piece>();
		TargetAnimator = new FrameAnimator(12, 8, FrameAnimator.WrapMode.LOOP);
		TargetAnimator.start();
		totalSpaces = spacesWide * spacesTall;
		space = new Space[totalSpaces];
		for (int i = 0; i < totalSpaces; i++)
			space[i] = new Space();
		
		clock = new ShotClock(33, Assets.ClockAnimPixmap);
	}
	
	protected abstract void onGameInit();
	protected abstract void onPieceDropBegin();
	protected abstract void onPieceDropComplete();
	protected abstract void onTargetRemoved();
	protected abstract void onAllTargetsRemoved();
	protected abstract void onNoSpaceDrop();

	
	public void init(Background _background, PieceQueue _queue)
	{
		this.background = _background;
		this.queue = _queue;
		Piece.scoreManager = this.scoreManager;
		onGameInit();
	}
	
	
	public int getIndex(int r, int c)
	{
		return (r * spacesWide) + c;
	}
	
	public Space getSpace(int row, int col)
	{
		return (space[getIndex(row, col)]);
	}
	
	public boolean spaceIsEmpty(int col, int row)
	{
		return space[getIndex(row,col)].isEmpty();
	}
	
	
	public void update(float deltaTime)
	{
		background.update(deltaTime);
		TargetAnimator.update(deltaTime);
		int i;
		for (i = 0; i < totalSpaces; i++)
		{
			if (space[i].piece != null)
				space[i].piece.update(deltaTime);
		}
		for (i = 0; i < dieingPieces.size(); )
		{
			dieingPieces.get(i).update(deltaTime);
			if (dieingPieces.get(i).isDead())
			{
				animationLock--;
				dieingPieces.get(i).free();
				dieingPieces.remove(i);
			}
			else i++;
		}
		
		queue.update(deltaTime);
		
		if (GameScreen.IsRunning())
		{
			if (!gameOver)
			{
				// manage input
				if (Controller.MoveLeftPressed)
				{
					queue.moveNextPieceLeft(0);
				}
				if (Controller.MoveRightPressed)
				{
					queue.moveNextPieceRight(spacesWide-1);
				}
				if (Controller.RotateLeftPressed)
				{
					queue.rotateNextPieceCounterClockwise();
				}
				if (Controller.RotateRightPressed)
				{
					queue.rotateNextPieceClockwise();
				}
			}
			
			if (animationLock == 0)
			{
				if (pieceLandingTrigger)
				{
					onPieceDropComplete();
					if (Settings.SoundEnabled) Assets.OrbLandSound.play(Settings.SFXVolume);
					pieceLandingTrigger = false;
				}
				
				if (next)
				{
					gameOver = win = next = false;
					nextLevel();
				}
				else if (gameOver)
				{
					queue.killNextPiece();
					queue.killBacklog();
					if (win)
					{
						killAllPieces();
						next = true;
					}
					else
					{
						onNoSpaceDrop();
					}
				}
				else
				{
					if (checkForConnectedPieces(true))
					{
						bonus *= 2;
					}
					else
					{
						if (!settle())
						{
							if (startingTargetPieces > 0 && targetPieceCount == 0)
							{
								onAllTargetsRemoved();
							}
							else
							{
								//if (autoDrop && !firstDrop) autoDropTimeRemaining -= deltaTime;
								if (autoDrop) autoDropTimeRemaining -= deltaTime;
								
								if (queue.nextPieceReady())
								{
									if (dropTrigger())
									{
										onPieceDropBegin();
										//if (firstDrop) firstDrop = false;
										if (!queue.dropNextPiece())
										{
											gameOver = true;
										}
										else
										{
											queue.pickNextPiece(random);
											bonus = 1;
											if (autoDrop)
											{
												autoDropTimeRemaining = autoDropTime;
												if (resetDropTimeForEachLevel)
													changeDropTime();
											}
											pieceLandingTrigger = true;
										}
									}
								}
								if (autoDrop)
									clock.updateClock(autoDropTimeRemaining, autoDropTime);
							} // target check
						} // settle check
					} // connected pieces check
				} // game over check
			}
			Controller.DropButtonEnabled = (animationLock == 0);
		} // pause check
	}
	
	
	public void draw(Graphics g)
	{
		background.draw(g);
		queue.draw(g);
		int i;
		for (i = 0; i < totalSpaces; i++)
		{
			if (space[i].piece != null)
				space[i].piece.draw(g);
		}
		for (i = 0; i < dieingPieces.size(); i++)
		{
			dieingPieces.get(i).draw(g);
		}
		g.drawPixmap(Assets.ForegroundPixmap, 0, 0);
		if (autoDrop) clock.draw(g);
		scoreManager.draw(g);
	}
	
	
	
	public void movePieceTo(Piece piece, int col, int row)
	{
		int pieceX, pieceY;
		if (space[getIndex(row,col)].piece != null)
		{
			pieceX = background.getX() + (col * BackgroundSquare.getWidth());
			pieceY = background.getY() + ((getSpacesTall() - row - 1) * BackgroundSquare.getHeight());
			piece.setPosition(pieceX, pieceY);
		}
	}	
	
	
	public void reset()
	{
		gameOver = win = next = false;
		pieceLandingTrigger = false;
		clearSpaceFlags();
		initLevel();
	}
	
	private void clearSpaceFlags()
	{
		for (int i = 0; i < totalSpaces; i++)
		{
			space[i].flag = false;
		}
	}
	
	
	public void killAllPieces()
	{
		for (int c = 0; c < spacesWide; c++)
		{
			for (int r = 0; r < spacesTall; r++)
			{
				if (space[getIndex(r,c)].piece != null)
				{
					space[getIndex(r,c)].flag = true;
					//space[getIndex(r,c)].piece.kill(0);
					//dieingPieces.add(space[getIndex(r,c)].piece);
					//space[getIndex(r,c)].empty();
				}
			}
		}
		killFlaggedPieces(0);
	}
	
	
	private void initLevel()
	{
		targetPieceCount = startingTargetPieces;
		int boardResetCount = 0;
		placeStartingPieces();
		while (checkForConnectedPieces(false))
		{
			clearBoard();
			placeStartingPieces();
			boardResetCount++;
		}
		//Debug.Log("Board reset " + boardResetCount + " times.");
		
		queue.fillBacklogs(random);
		queue.pickNextPiece(random);
		//firstDrop = true;
		if (resetDropTimeForEachLevel)
			autoDropTime = autoDropAfter;
		autoDropTimeRemaining = autoDropTime;
		GameScreen.setGameState(GameScreen.GameState.Ready);
		animationLock = 0;
	}
	
	public void increaseDifficulty()
	{
		if (startingTargetPieces < maximumTargetPieces)
		{
			startingTargetPieces += increaseTargetPiecesPerLevel;
			if (startingTargetPieces > maximumTargetPieces)
				startingTargetPieces = maximumTargetPieces;
		}
		if (startingAdditionalPieces < maximumAdditionalPieces)
		{
			startingAdditionalPieces += increaseAdditionalPiecesPerLevel;
			if (startingAdditionalPieces > maximumAdditionalPieces)
				startingAdditionalPieces = maximumAdditionalPieces;
		}
		if (!resetDropTimeForEachLevel)
			changeDropTime();
	}
	
	private void changeDropTime()
	{
		if (autoDropTime > minimumDropTime)
		{
			autoDropTime *= multiplyDropTimeBy;
			if (autoDropTime < minimumDropTime)
				autoDropTime = minimumDropTime;
		}
	}

	public void nextLevel()
	{
		increaseDifficulty();
		initLevel();
	}
	
	
	private void clearBoard()
	{
		for (int c = 0; c < spacesWide; c++)
		{
			for (int r = 0; r < spacesTall; r++)
			{
				if (space[getIndex(r,c)].piece != null)
				{
					space[getIndex(r,c)].piece.free();
					space[getIndex(r,c)].piece = null;
				}
			}
		}
		//Piece.TargetPieceCount = 0;
	}
	
	
	private void placeStartingPieces()
	{
		int i, col, row, rand;
		int[] colList = new int[spacesWide];
		int spacesRemaining;
		for (i = 0; i < spacesWide; i++)
			colList[i] = i;
		spacesRemaining = spacesWide;
		
		row = 0;
		int totalPieces = startingTargetPieces + startingAdditionalPieces;
		for (int p = 0; p < totalPieces; p++)
		{
			if (spacesRemaining == 0)
			{
				row++;
				for (i = 0; i < spacesWide; i++)
					colList[i] = i;
				spacesRemaining = spacesWide;
			}

			rand = random.nextInt(spacesRemaining);
			col = colList[rand];
			colList[rand] = colList[spacesRemaining-1];
			spacesRemaining--;
			
			space[getIndex(row,col)].piece = Piece.GetRandomPiece(random);
			space[getIndex(row,col)].piece.isTarget = (p < startingTargetPieces);
			
			movePieceTo(space[getIndex(row,col)].piece, col, row);
		}
	}

	
	private boolean dropTrigger()
	{
		//if (!firstDrop && autoDrop)
		if (autoDrop)
		{
			return (Controller.DropPressed || autoDropTimeRemaining <= 0);
		}
		else
		{
			return (Controller.DropPressed);
		}
	}
	
	
	public boolean movePieceDown(int col, int row)
	{
		if (row > 0)
		{
			Space currentSpace = space[getIndex(row,col)];
			Space nextSpace = space[getIndex(row-1,col)];
			if (!currentSpace.isEmpty() && nextSpace.isEmpty())
			{
				nextSpace.piece = currentSpace.piece;
				currentSpace.empty();
				return true;
			}
		}
		return false;
	}
	
	private boolean checkForConnectedPieces(boolean killPieces)
	{
		boolean connectionsFound = false;
		for (int c = 0; c < spacesWide; c++)
		{
			for (int r = 0; r < spacesTall; r++)
			{
				if (space[getIndex(r,c)].piece != null)
				{
					clearSpaceFlags();
					space[getIndex(r,c)].flag = true;
					int likeSpaces = numberOfUnflaggedConnections(c, r, 1);
					if (likeSpaces >= killThreshold) {
						connectionsFound =  true;
						if (killPieces) {
							killFlaggedPieces(bonus);
							//if (startingTargetPieces > 0 && targetPieceCount == 0)
							//{
							//	onAllTargetsRemoved();
							//}
						}
					}
				}
			}
		}
		return connectionsFound;
	}
	
	private int numberOfUnflaggedConnections(int col, int row, int countSoFar)
	{
		if (space[getIndex(row,col)].piece == null)
			return countSoFar;
		
		int count = countSoFar;
		Piece.Colour checkColour = space[getIndex(row,col)].piece.colour;
		
		Space checkSpace;
		if (col > 0)
		{
			checkSpace = space[getIndex(row,col-1)];
			if (checkSpace.piece != null && !checkSpace.flag && checkSpace.piece.colour == checkColour)
			{
				checkSpace.flag = true;
				count = numberOfUnflaggedConnections(col-1, row, count+1);
			}
		}
		if (col < spacesWide-1)
		{
			checkSpace = space[getIndex(row,col+1)];
			if (checkSpace.piece != null && !checkSpace.flag && checkSpace.piece.colour == checkColour)
			{
				checkSpace.flag = true;
				count = numberOfUnflaggedConnections(col+1, row, count+1);
			}
		}
		if (row > 0)
		{
			checkSpace = space[getIndex(row-1,col)];
			if (checkSpace.piece != null && !checkSpace.flag && checkSpace.piece.colour == checkColour)
			{
				checkSpace.flag = true;
				count = numberOfUnflaggedConnections(col, row-1, count+1);
			}
		}
		if (row < spacesTall-1)
		{
			checkSpace = space[getIndex(row+1,col)];
			if (checkSpace.piece != null && !checkSpace.flag && checkSpace.piece.colour == checkColour)
			{
				checkSpace.flag = true;
				count = numberOfUnflaggedConnections(col, row+1, count+1);
			}
		}
		return count;
	}
	
	private void killFlaggedPieces(int pointValue)
	{
		int i;
		float t;
		killStack.clear();
		for (i = 0; i < totalSpaces; i++)
		{
			if (space[i].flag)
			{
				if (space[i].piece.isTarget)
				{
					targetPieceCount--;
					onTargetRemoved();
				}
				space[i].piece.pointValue = pointValue;
				killStack.add(space[i].piece);
				space[i].empty();
				space[i].flag = false;
			}
		}
		
		t = 0;
		while (!killStack.isEmpty())
		{
			i = random.nextInt(killStack.size());
			killStack.get(i).kill(t);
			dieingPieces.add(killStack.get(i));
			killStack.remove(i);
			t += 0.1f;
		}
	}
	
	public boolean settle() {
		boolean didSettle = false;
		for (int c = 0; c < spacesWide; c++)
		{
			for (int s = 1; s < spacesTall; s++)
			{
				if (!space[getIndex(s,c)].isEmpty())
				{
					int r = s;
					while (movePieceDown(c, r))
						r--;
					if (r < s)
					{
						space[getIndex(r,c)].piece.fall(s-r);
						if (!didSettle) didSettle = true;
					}
				}
			}
		}
		return didSettle;
	}

}
