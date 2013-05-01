package com.michaelcamerongames.percula;

import java.util.List;

import android.graphics.Rect;

import com.michaelcamerongames.framework.Game;
import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.Input.TouchEvent;
import com.michaelcamerongames.framework.implementation.AndroidScreen;

public class GameScreen extends AndroidScreen
{
	Graphics graphics;
	
	private Board board;
	private PieceQueue queue;
	private Background background;
	
	private Rect resumeButton = new Rect();
	private Rect settingsButton = new Rect();
	private Rect helpButton = new Rect();
	private Rect quitButton = new Rect();
	
	private static float gameOverDelay = 1f;
	private static float gameOverDelayRemaining;
	
	
	enum GameState
	{
		Ready,
		Running,
		Paused,
		GameOver,
		NewHighScore
	};
	
	public static GameScreen main;
	public static void setGameState(GameState newState)
	{
		if (state != newState)
		{
			state = newState;
			if (newState == GameState.GameOver || newState == GameState.NewHighScore)
				gameOverDelayRemaining = gameOverDelay;
		}
	}
	public static boolean isInGameState(GameState checkState)
	{
		return (state == checkState);
	}
	private static GameState state = GameState.Running;
	private static GameState previousState = GameState.Running;
	public static boolean IsRunning() { return state == GameState.Running; }

	/*
	// do stuff with this stuff
	private int score;
	private String scoreString;
	
	public void addPoints(int points)
	{
		score += points;
		scoreString = Integer.toString(score);
	}
	
	public void updateHighScores()
	{
		Settings.AddScore(score);
        Settings.Save();
	}
	*/
	
	public GameScreen(Game game)
	{
		super(game);
		
		main = this;
		
		state = GameState.Ready;
		//score = 0;
		//scoreString = "0";

		graphics = game.getGraphics();
		
		// low-res
		resumeButton.set(    64, 181, 256, 236);
		settingsButton.set(  64, 236, 256, 291);
		helpButton.set(      64, 291, 256, 346);
		quitButton.set(      64, 346, 256, 401);
		// high-res
		//resumeButton.set(   128, 362, 511, 471);
		//settingsButton.set( 128, 472, 511, 581);
		//helpButton.set(     128, 582, 511, 691);
		//quitButton.set(     128, 692, 511, 801);

		Piece.initPool(93); // 11x7 (board) + 4 (current queue) + 12 (backlog) 
		board = new ArcadeBoard();
		background = new Background();
		queue = new PieceQueue();
		
		board.init(background, queue);
		background.init(queue, board);
		queue.init(board, background);
		
		background.setPosition(Layout.BoardRect.left, Layout.BoardRect.top);
		
		board.reset();
	}
	

	@Override
	public void update(float deltaTime)
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		if (state == GameState.Ready)
		{
			updateReady(touchEvents);
			if (!adVisible && aGame != null)
			{
				adVisible = true;
				//aGame.showAds(true);
			}
		}
		else
		{
			if (state == GameState.Running)
			{
				updateRunning(touchEvents, deltaTime);
			}
			if (state == GameState.Paused)
			{
				updatePaused(touchEvents);
			}
			if (state == GameState.GameOver)
			{
				if (gameOverDelayRemaining > 0)
					gameOverDelayRemaining -= deltaTime;
				updateGameOver(touchEvents);			
			}
			if (state == GameState.NewHighScore)
			{
				if (gameOverDelayRemaining > 0)
					gameOverDelayRemaining -= deltaTime;
				updateNewHighScore(touchEvents);			
			}
			
			if (adVisible && aGame != null)
			{
				adVisible = false;
				//aGame.showAds(false);
			}
		}
	}
	
	private void updateReady(List<TouchEvent> touchEvents)
	{
		if(touchEvents.size() > 0)
			state = GameState.Running;
    }
	
	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime)
	{
		// set values to Controller
		Controller.Reset();
		int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP)
            {
                if (Layout.PauseRect.contains(event.x, event.y))
                {
                    //if (Settings.soundEnabled)
                    //    Assets.click.play(1);
                    state = GameState.Paused;
                    return;
                }
            }
            
            if (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED)
            {
            	if (Layout.MoveInput.contains(event.x, event.y))
            	{
            		// 
            		//Controller.MoveLeftPressed = true;
            		// set position based on the input.
            		int xInput = (event.x - Layout.MoveInput.left - (Assets.BackgroundSquarePixmap.getWidth()/2)) / Assets.BackgroundSquarePixmap.getWidth();
            		queue.moveNextPieceTo(xInput, 0, board.getSpacesWide()-1);
            	}
            }
            
            if (event.type == TouchEvent.TOUCH_DOWN)
            {
            	if (Layout.RotateInput.contains(event.x, event.y))
            	{
            		if (Settings.RotationButtonIsClockwise)
            			Controller.RotateRightPressed = true;
            		else
            			Controller.RotateLeftPressed = true;
            	}
            	if (Layout.DropInput.contains(event.x, event.y))
            	{
           			Controller.DropPressed = true;
            	}
            }
        }
		
		board.update(deltaTime);
	}
	
	private void updatePaused(List<TouchEvent> touchEvents)
	{
		int len = touchEvents.size();
		for (int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP)
			{
				if (resumeButton.contains(event.x, event.y))
				{
					state = previousState;
					//state = GameState.Running;
				}
				if (settingsButton.contains(event.x, event.y))
				{
					game.addSubScreen(new SettingsScreen(game));
					return;
				}
				if (helpButton.contains(event.x, event.y))
				{
					game.addSubScreen(new HelpScreen(game));
					return;
				}
				if (quitButton.contains(event.x, event.y))
				{
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}
	
	private void updateGameOver(List<TouchEvent> touchEvents)
	{
		int len = touchEvents.size();
		for (int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if (gameOverDelayRemaining <= 0)
			{
				if (event.type == TouchEvent.TOUCH_UP)
				{
					game.setScreen(new MainMenuScreen(game));
				}
			}
		}
	}
	
	private void updateNewHighScore(List<TouchEvent> touchEvents)
	{
		int len = touchEvents.size();
		for (int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if (gameOverDelayRemaining <= 0)
			{
				if (event.type == TouchEvent.TOUCH_UP)
				{
					game.setScreen(new MainMenuScreen(game));
					game.addSubScreen(new HighScoresScreen(game));
				}
			}
		}
	}
	
	

	@Override
	public void present(float deltaTime)
	{
		graphics.drawPixmap(Assets.BackgroundPixmap, 0, 0);
		drawWorld();
		
		if (state == GameState.Ready)
		{
			drawReadyUI();
		}
		if (state == GameState.Running)
		{
			drawRunningUI();
		}
		if (state == GameState.Paused)
		{
			drawPausedUI();
		}
		if (state == GameState.GameOver)
		{
			drawGameOverUI();
		}
		if (state == GameState.NewHighScore)
		{
			drawNewHighScoreUI();
		}
	}
	
	private void drawWorld()
	{
		board.draw(graphics);
	}
	
	private void drawReadyUI()
	{
		graphics.drawPixmap(Assets.ReadyScreen, 0, 0);
	}

	private void drawRunningUI()
	{
		graphics.drawPixmap(Assets.PauseButtonPixmap, Layout.PauseRect.left, Layout.PauseRect.top);
		//graphics.drawPixmap(Assets.MoveBarPixmap, Layout.MoveInput.left, Layout.MoveInput.bottom - Assets.MoveBarPixmap.getHeight() + 1);
		
		if (Controller.DropButtonEnabled)
			graphics.drawPixmap(Assets.DropButtonPixmap, Layout.DropInput.left, Layout.DropInput.top, 0, 0, Assets.DropButtonPixmap.getWidth(), Assets.DropButtonPixmap.getHeight()/2);
		else
			graphics.drawPixmap(Assets.DropButtonPixmap, Layout.DropInput.left, Layout.DropInput.top, 0, Assets.DropButtonPixmap.getHeight()/2, Assets.DropButtonPixmap.getWidth(), Assets.DropButtonPixmap.getHeight()/2);
		
		if (Settings.RotationButtonIsClockwise)
			graphics.drawPixmap(Assets.RotateRightButtonPixmap, Layout.RotateInput.left, Layout.RotateInput.top);
		else
			graphics.drawPixmap(Assets.RotateLeftButtonPixmap, Layout.RotateInput.left, Layout.RotateInput.top);
			
		//graphics.drawPixmap(Assets.MoveLeftButtonPixmap, Layout.MoveLeftButtonRect.left, Layout.MoveLeftButtonRect.top);
		//graphics.drawPixmap(Assets.MoveRightButtonPixmap, Layout.MoveRightButtonRect.left, Layout.MoveRightButtonRect.top);
		//graphics.drawPixmap(Assets.DropButtonPixmap, Layout.DropButtonRect.left, Layout.DropButtonRect.top);
	}

	private void drawPausedUI()
	{
		graphics.drawPixmap(Assets.PauseScreen, 0, 0);
	}

	private void drawGameOverUI()
	{
		graphics.drawPixmap(Assets.GameOverScreen, 0, 0);
	}
	
	private void drawNewHighScoreUI()
	{
		graphics.drawPixmap(Assets.NewHighScoreMessage, 0, 0);
	}

	@Override
	public void pause()
	{
		if (state != GameState.Paused)
		{
			previousState = state;
			state = GameState.Paused;
		}
		//if(state == GameState.Running) 
        //    state = GameState.Paused;
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void dispose()
	{
	}

}
