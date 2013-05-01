package com.michaelcamerongames.percula;

import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.Music;
import com.michaelcamerongames.framework.Pixmap;
import com.michaelcamerongames.framework.Sound;

public class Assets
{
	// use getter and setters at some point....
	
	// Screens
	public static Pixmap TitleScreen;
	public static Pixmap MainMenuScreen;
	public static Pixmap SettingsScreen;
	public static Pixmap HighScoresScreen;
	public static Pixmap HelpScreen1;
	public static Pixmap HelpScreen2;
	public static Pixmap HelpScreen3;
	public static Pixmap HelpScreen4;
	public static Pixmap HelpScreen5;
	public static Pixmap HelpScreen6;
	public static Pixmap HelpScreen7;
	
	public static Pixmap ReadyScreen;
	public static Pixmap Player1ReadyScreen;
	public static Pixmap Player2ReadyScreen;
	public static Pixmap PauseScreen;
	public static Pixmap GameOverScreen;
	public static Pixmap NewHighScoreMessage;
	
	// Settings
	public static Pixmap VolumeSliderPixmap;
	public static Pixmap MutePixmap;
	
	// Common
	public static Pixmap BackgroundPixmap;
	public static Pixmap ForegroundPixmap;
	public static Pixmap Numbers20Pixmap;
	public static Pixmap Numbers40Pixmap;
	public static Pixmap HighScoreHighlight;
	public static Pixmap OkayButtonPixmap;
	public static Pixmap BackButtonPixmap;
	
	// Main Menu
	//public static Pixmap PlayButtonPixmap;
	//public static Pixmap HighScoresButtonPixmap;
	
	// Game
	public static Pixmap PauseButtonPixmap;
	public static Pixmap MoveBarPixmap;
	//public static Pixmap MoveLeftButtonPixmap;
	//public static Pixmap MoveRightButtonPixmap;
	public static Pixmap RotateLeftButtonPixmap;
	public static Pixmap RotateRightButtonPixmap;
	public static Pixmap DropButtonPixmap;
	
	//public static Pixmap ControllerPixmap;
	
	public static Pixmap BackgroundSquarePixmap;
	public static Pixmap QueueBackgroundPixmap;
	public static Pixmap BluePiecePixmap;
	public static Pixmap GreenPiecePixmap;
	public static Pixmap RedPiecePixmap;
	public static Pixmap YellowPiecePixmap;
	public static Pixmap DropArrowAnimPixmap;
	public static Pixmap OrbShineAnimPixmap;
	public static Pixmap OrbGlowAnimPixmap;
	public static Pixmap BlueBurstAnimPixmap;
	public static Pixmap GreenBurstAnimPixmap;
	public static Pixmap RedBurstAnimPixmap;
	public static Pixmap YellowBurstAnimPixmap;
	public static Pixmap ClockAnimPixmap;
	
	// SFX
	public static Sound ButtonSound;
	public static Sound PageFlipSound;
	public static Sound OrbLandSound;
	public static Sound OrbBurstSound;
	
	// Music
	public static Music MusicTrack1;
	
	
	public static void DrawNumbers40(Graphics g, String line, int x, int y)
	{
		DrawNumbers40(g, line, x, y, true);
	}
	public static void DrawNumbers40(Graphics g, String line, int x, int y, boolean rightJustified)
	{
		int i;
		int len = line.length();
		char character;
		
		for (i = 0; i < len; i++)
		{
			if (rightJustified)
				character = line.charAt(len - 1 - i);
			else
				character = line.charAt(i);
			
			if (character == ' ')
			{
				x = x + (rightJustified ? -20 : 20);
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			if (character == '.')
			{
				srcX = 300;
				srcWidth = 20;
			}
			else if (character < '0' && character > '9')
			{
				x = x + (rightJustified ? -30 : 30);
				continue;
			}
			else
			{
				srcX = (character - '0') * 30;
				srcWidth = 30;
			}
			
			if (rightJustified)
			{
				x -= srcWidth;
				g.drawPixmap(Assets.Numbers40Pixmap, x, y, srcX, 0, srcWidth, 40);
			}
			else
			{
				g.drawPixmap(Assets.Numbers40Pixmap, x, y, srcX, 0, srcWidth, 40);
				x += srcWidth;
			}
			
		}
	}
	
	
	public static void DrawNumbers20(Graphics g, String line, int x, int y)
	{
		DrawNumbers20(g, line, x, y, true);
	}
	public static void DrawNumbers20(Graphics g, String line, int x, int y, boolean rightJustified)
	{
		int i;
		int len = line.length();
		char character;
		
		for (i = 0; i < len; i++)
		{
			if (rightJustified)
				character = line.charAt(len - 1 - i);
			else
				character = line.charAt(i);
			
			if (character == ' ')
			{
				x = x + (rightJustified ? -10 : 10);
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			if (character == '.')
			{
				srcX = 150;
				srcWidth = 10;
			}
			else if (character < '0' && character > '9')
			{
				x = x + (rightJustified ? -15 : 15);
				continue;
			}
			else
			{
				srcX = (character - '0') * 15;
				srcWidth = 15;
			}
			
			if (rightJustified)
			{
				x -= srcWidth;
				g.drawPixmap(Assets.Numbers20Pixmap, x, y, srcX, 0, srcWidth, 20);
			}
			else
			{
				g.drawPixmap(Assets.Numbers20Pixmap, x, y, srcX, 0, srcWidth, 20);
				x += srcWidth;
			}
		}
	}
	
}
