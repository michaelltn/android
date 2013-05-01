package com.michaelcamerongames.percula;

import java.util.Random;
import java.util.List;

import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.Pixmap;
import com.michaelcamerongames.framework.Pool;
import com.michaelcamerongames.framework.Pool.PoolObjectFactory;

public class Piece
{
	public enum Colour { BLUE, GREEN, RED, YELLOW };
	public static final int ColourCount = 4;
	
	public static ScoreManager scoreManager;
	public int pointValue = 0;
	
	static Pool<Piece> piecePool;
	static List<Piece> activePieces;

	public static float killTime = 0.33f;
	public static float settleFallSpeed = 360.0f;
	public static float dropFallSpeed = 720.0f;
	
	private float x, y;
	
	public Colour colour;
	public boolean isTarget = false;
	
	public enum AnimState { IDLE, FALLING, DIEING };
	private AnimState animState;
	private float fallSpeed = 1000.0f;
	private float fallTo;
	private float killTimeRemaining;
	private boolean triggerKillActions;
	
	
	static void initPool(int size)
	{
		PoolObjectFactory<Piece> factory = new PoolObjectFactory<Piece>()
		{ 
			public Piece createObject()
			{ 
				return new Piece(); 
			} 
		}; 
		piecePool = new Pool<Piece>(factory, size);
	}
	
	public Piece()
	{
	}

	
	public static Piece GetRandomPiece(Random random)
	{
		int colourIndex = random.nextInt(ColourCount);
		Colour newColour;
		switch (colourIndex)
		{
		case 0:
			newColour = Colour.BLUE;
			break;
		case 1:
			newColour = Colour.GREEN;
			break;
		case 2:
			newColour = Colour.RED;
			break;
		default:
			newColour = Colour.YELLOW;
			break;
		}
		
		Piece newPiece = piecePool.newObject();
		newPiece.colour = newColour;
		newPiece.isTarget = false;
		newPiece.x = newPiece.y = 0;
		newPiece.animState = Piece.AnimState.IDLE;
		return newPiece;
	}
	
	
	
	public void setPosition(int _x, int _y)
	{
		this.x = _x;
		this.y = _y;
	}
	
	public int getWidth()
	{
		return Assets.BluePiecePixmap.getWidth();
	}
	public int getHeight()
	{
		return Assets.BluePiecePixmap.getHeight();
	}
	
	public void update(float deltaTime)
	{
		if (GameScreen.IsRunning())
		{
			if (animState == Piece.AnimState.FALLING)
			{
				if (fallStep(deltaTime))
				{
					Board.animationLock -= 1;
					animState = Piece.AnimState.IDLE;
				}
			}
			if (animState == Piece.AnimState.DIEING)
			{
				if (killStep(deltaTime))
				{
					// don't kill here... let the board clean this up.
					//Board.animationLock -= 1;
					//piecePool.free(this);
				}
			}
		}
	}
	
	public void draw(Graphics g)
	{
		final int killFrames = 8;
		Pixmap animPixmap = null;
		int index;
		int animWidth, animHeight;
		if (animState == Piece.AnimState.DIEING && killTimeRemaining <= killTime)
		{
			if (this.colour == Colour.BLUE)
				animPixmap = Assets.BlueBurstAnimPixmap;
			if (this.colour == Colour.GREEN)
				animPixmap = Assets.GreenBurstAnimPixmap;
			if (this.colour == Colour.RED)
				animPixmap = Assets.RedBurstAnimPixmap;
			if (this.colour == Colour.YELLOW)
				animPixmap = Assets.YellowBurstAnimPixmap;
			
			if (animPixmap != null)
			{
				index = Math.round((1f-(killTimeRemaining/killTime)) * killFrames);
				if (index < 0) index = 0;
				if (index > 7) index = killFrames-1;
				animWidth = animPixmap.getWidth();
				animHeight = animPixmap.getHeight() / 8;
				
				g.drawPixmap(animPixmap, Math.round(x), Math.round(y), 0, index * getHeight(), animWidth, animHeight);
			}
		}
		else
		{
			if (this.colour == Colour.BLUE)
				g.drawPixmap(Assets.BluePiecePixmap, Math.round(x), Math.round(y));
			if (this.colour == Colour.GREEN)
				g.drawPixmap(Assets.GreenPiecePixmap, Math.round(x), Math.round(y));
			if (this.colour == Colour.RED)
				g.drawPixmap(Assets.RedPiecePixmap, Math.round(x), Math.round(y));
			if (this.colour == Colour.YELLOW)
				g.drawPixmap(Assets.YellowPiecePixmap, Math.round(x), Math.round(y));
			
			if (this.isTarget)
			{
				index = Board.TargetAnimator.getCurrentFrameIndex();
				animWidth = Assets.OrbGlowAnimPixmap.getWidth();
				animHeight = Assets.OrbGlowAnimPixmap.getHeight() / Board.TargetAnimator.getTotalFrames();
				g.drawPixmap(Assets.OrbGlowAnimPixmap, Math.round(x), Math.round(y), 0, index * getHeight(), animWidth, animHeight);
			}
		}
	}
	
	
	
	public void instantFall(int numberOfSpaces)
	{
		if (animState == Piece.AnimState.IDLE)
		{
			y += (numberOfSpaces * BackgroundSquare.getHeight());
		}
	}
	public void quickFall(int numberOfSpaces)
	{
		if (animState == Piece.AnimState.IDLE)
		{
			fallTo = y + (numberOfSpaces * BackgroundSquare.getHeight());
			fallSpeed = dropFallSpeed;
			Board.animationLock += 1;
			animState = Piece.AnimState.FALLING;
		}
	}
	public void fall(int numberOfSpaces)
	{
		if (animState == Piece.AnimState.IDLE)
		{
			fallTo = y + (numberOfSpaces * BackgroundSquare.getHeight());
			fallSpeed = settleFallSpeed;
			Board.animationLock += 1;
			animState = Piece.AnimState.FALLING;
		}
	}
	private boolean fallStep(float deltaTime)
	{
		y += deltaTime * fallSpeed;
		if (y > fallTo)
			y = fallTo;
		
		return (y == fallTo);
	}
	
	public void free()
	{
		piecePool.free(this);
	}
	public void kill(float killDelay)
	{
		if (animState == Piece.AnimState.IDLE)
		{
			isTarget = false;
			killTimeRemaining = killTime + killDelay;
			triggerKillActions = true;
			animState = Piece.AnimState.DIEING;
			Board.animationLock += 1;
			//piecePool.free(this);
		}
	}
	public boolean killStep(float deltaTime)
	{
		killTimeRemaining -= deltaTime;
		if (triggerKillActions && killTimeRemaining <= killTime)
		{
			scoreManager.addPoints(pointValue);
			if (Settings.SoundEnabled) Assets.OrbBurstSound.play(Settings.SFXVolume);
			triggerKillActions = false;
		}
		return killTimeRemaining <= 0;
	}
	
	public boolean isDead()
	{
		return (animState == Piece.AnimState.DIEING && killTimeRemaining <= 0); 
	}
}
