package com.michaelcamerongames.percula;

public class ArcadeBoard extends Board
{
	ScoreManager playerScore;

	@Override
	protected void onGameInit()
	{
		startingTargetPieces = 3;
		increaseTargetPiecesPerLevel = 0;
		maximumTargetPieces = 3;
		
		startingAdditionalPieces = 14;
		increaseAdditionalPiecesPerLevel = 3;
		maximumAdditionalPieces = 46;
		
		this.changeDropTimeAfterDrop();
		autoDrop = true;
		autoDropAfter = 3f;
		multiplyDropTimeBy = 0.97f; // 0.97 = 36 drops to reach 1 second from 3 seconds
		minimumDropTime = 1.0f;
		
		twoPlayers = false;
		
		queue.setBacklogVisibility(2);
		
		playerScore = new ScoreManager();
		scoreManager = playerScore;
		Piece.scoreManager = playerScore;
	}

	@Override
	protected void onPieceDropBegin()
	{

	}

	@Override
	protected void onPieceDropComplete()
	{

	}

	@Override
	protected void onTargetRemoved()
	{

	}

	@Override
	protected void onAllTargetsRemoved()
	{
		gameOver = true;
		win = true;
	}
	
	@Override
	protected void onNoSpaceDrop()
	{
		int highScorePos = Settings.CheckNewHighScore(scoreManager.getScore());
		HighScoresScreen.SetHighlight(highScorePos);
		if (highScorePos >= 0)
		{
			Settings.Save();
			GameScreen.setGameState(GameScreen.GameState.NewHighScore);
		}
		else
		{
			GameScreen.setGameState(GameScreen.GameState.GameOver);
		}
	}

}
