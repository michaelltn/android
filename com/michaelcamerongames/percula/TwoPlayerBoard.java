package com.michaelcamerongames.percula;

public class TwoPlayerBoard extends Board
{
	ScoreManager player1Score, player2Score;
	private int turn;
	private int drops;

	@Override
	protected void onGameInit()
	{
		startingTargetPieces = 1;
		startingAdditionalPieces = 48;
		autoDrop = false;
		twoPlayers = true;
		queue.setBacklogVisibility(1);
		turn = 1;
		drops = 0;
		
		player1Score = new ScoreManager();
		player2Score = new ScoreManager();
		setScoreManager();
	}

	@Override
	protected void onPieceDropBegin()
	{
		drops++;
		switch (turn)
		{
		case 1:
			if (drops == 1)
			{
				turn++;
				drops = 0;
				queue.setBacklogVisibility(2);
				queue.changePlayers();
				setScoreManager();
			}
			break;
		case 2:
			if (drops == 2)
			{
				turn++;
				drops = 0;
				queue.setBacklogVisibility(3);
				queue.changePlayers();
				setScoreManager();
			}
			break;
			
		default:
			if (drops == 3)
			{
				turn++;
				drops = 0;
				queue.changePlayers();
				setScoreManager();
			}
			break;
		}
	}
	
	private void setScoreManager()
	{
		if (queue.currentPlayer() == 1)
		{
			scoreManager = player1Score;
			Piece.scoreManager = player1Score;
		}
		else
		{
			scoreManager = player2Score;
			Piece.scoreManager = player2Score;
		}
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

	}
	
	@Override
	protected void onNoSpaceDrop()
	{
		
	}


}
