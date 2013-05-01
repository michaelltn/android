package com.michaelcamerongames.percula;

import com.michaelcamerongames.framework.Audio;
import com.michaelcamerongames.framework.Game;
import com.michaelcamerongames.framework.Graphics;
import com.michaelcamerongames.framework.Graphics.PixmapFormat;
import com.michaelcamerongames.framework.implementation.AndroidScreen;


public class LoadingScreen extends AndroidScreen
{
	private boolean screenDrawn = false;
	private boolean beginLoad = true;
	private float displayTime = 3.0f;
	private float displayTimeRemaining;
	
	Graphics g;
	Audio a;
	
	public LoadingScreen(Game game)
	{
		super(game);
		g = game.getGraphics();
		a = game.getAudio();
		displayTimeRemaining = displayTime;
		Assets.TitleScreen = g.newPixmap("images/low-res/screens/TitleScreen.png", PixmapFormat.ARGB4444);
	}

	@Override
	public void update(float deltaTime)
	{
		if (beginLoad && screenDrawn)
		{
			beginLoad = false;
			// Screens
			Assets.MainMenuScreen = g.newPixmap("images/low-res/screens/MainMenuScreen.png", PixmapFormat.ARGB4444);
			Assets.SettingsScreen = g.newPixmap("images/low-res/screens/SettingsScreen.png", PixmapFormat.ARGB4444);
			Assets.HighScoresScreen = g.newPixmap("images/low-res/screens/HighScoresScreen.png", PixmapFormat.ARGB4444);
			Assets.HelpScreen1 = g.newPixmap("images/low-res/screens/HelpScreen1.png", PixmapFormat.ARGB4444); // ***
			Assets.HelpScreen2 = g.newPixmap("images/low-res/screens/HelpScreen2.png", PixmapFormat.ARGB4444); // ***
			Assets.HelpScreen3 = g.newPixmap("images/low-res/screens/HelpScreen3.png", PixmapFormat.ARGB4444); // ***
			Assets.HelpScreen4 = g.newPixmap("images/low-res/screens/HelpScreen4.png", PixmapFormat.ARGB4444); // ***
			Assets.HelpScreen5 = g.newPixmap("images/low-res/screens/HelpScreen5.png", PixmapFormat.ARGB4444); // ***
			Assets.HelpScreen6 = g.newPixmap("images/low-res/screens/HelpScreen6.png", PixmapFormat.ARGB4444); // ***
			Assets.HelpScreen7 = g.newPixmap("images/low-res/screens/HelpScreen7.png", PixmapFormat.ARGB4444); // ***
			
			Assets.ReadyScreen = g.newPixmap("images/low-res/screens/ReadyScreen.png", PixmapFormat.ARGB4444);
			Assets.Player1ReadyScreen = g.newPixmap("images/low-res/screens/ReadyScreenPlayer1.png", PixmapFormat.ARGB4444); // ***
			Assets.Player2ReadyScreen = g.newPixmap("images/low-res/screens/ReadyScreenPlayer2.png", PixmapFormat.ARGB4444); // ***
			Assets.PauseScreen = g.newPixmap("images/low-res/screens/PauseScreen.png", PixmapFormat.ARGB4444);
			Assets.GameOverScreen = g.newPixmap("images/low-res/screens/GameOverScreen.png", PixmapFormat.ARGB4444);
			Assets.NewHighScoreMessage = g.newPixmap("images/low-res/screens/HighScoreMessage.png", PixmapFormat.ARGB4444);
			
			// Settings Assets
			Assets.MutePixmap = g.newPixmap("images/low-res/controls/MuteButton_32_2.png", PixmapFormat.ARGB4444);
			Assets.VolumeSliderPixmap = g.newPixmap("images/low-res/controls/VolumeSlider_300.png", PixmapFormat.ARGB4444);
			
			// Common Assets
			Assets.BackgroundPixmap = g.newPixmap("images/low-res/Background_320.png", PixmapFormat.ARGB4444);
			Assets.ForegroundPixmap = g.newPixmap("images/low-res/Foreground_320.png", PixmapFormat.ARGB4444);
			Assets.Numbers20Pixmap = g.newPixmap("images/low-res/Numbers_20.png", PixmapFormat.ARGB4444);
			Assets.Numbers40Pixmap = g.newPixmap("images/low-res/Numbers_40.png", PixmapFormat.ARGB4444);
			Assets.HighScoreHighlight = g.newPixmap("images/low-res/HighScoreHighlight_48.png", PixmapFormat.ARGB4444);
			Assets.OkayButtonPixmap = g.newPixmap("images/low-res/controls/OkayButton_32.png", PixmapFormat.ARGB4444);
			Assets.BackButtonPixmap = g.newPixmap("images/low-res/controls/BackButton_32.png", PixmapFormat.ARGB4444);
			
			// Game Assets
			Assets.PauseButtonPixmap = g.newPixmap("images/low-res/controls/PauseButton_32.png", PixmapFormat.ARGB4444);
			Assets.MoveBarPixmap = g.newPixmap("images/low-res/controls/TouchBar_224.png", PixmapFormat.ARGB4444);
			Assets.RotateLeftButtonPixmap =  g.newPixmap("images/low-res/controls/RotateLeftButton_80.png", PixmapFormat.ARGB4444);
			Assets.RotateRightButtonPixmap =  g.newPixmap("images/low-res/controls/RotateRightButton_80.png", PixmapFormat.ARGB4444);
			Assets.DropButtonPixmap =  g.newPixmap("images/low-res/controls/DropButton_48_2.png", PixmapFormat.ARGB4444);
			
			Assets.BackgroundSquarePixmap = g.newPixmap("images/low-res/BackgroundSquare_32.png", PixmapFormat.ARGB4444);
			Assets.DropArrowAnimPixmap = g.newPixmap("images/low-res/DropArrowAnim_32_6.png", PixmapFormat.ARGB4444);
			Assets.QueueBackgroundPixmap = g.newPixmap("images/low-res/QueueBackground_64.png", PixmapFormat.ARGB4444);
			Assets.BluePiecePixmap = g.newPixmap("images/low-res/orbs/BlueOrb_32.png", PixmapFormat.ARGB4444);
			Assets.GreenPiecePixmap = g.newPixmap("images/low-res/orbs/GreenOrb_32.png", PixmapFormat.ARGB4444);
			Assets.RedPiecePixmap = g.newPixmap("images/low-res/orbs/RedOrb_32.png", PixmapFormat.ARGB4444);
			Assets.YellowPiecePixmap = g.newPixmap("images/low-res/orbs/YellowOrb_32.png", PixmapFormat.ARGB4444);
			Assets.OrbShineAnimPixmap = g.newPixmap("images/low-res/orbs/OrbShineAnim_32_6.png", PixmapFormat.ARGB4444);
			Assets.OrbGlowAnimPixmap = g.newPixmap("images/low-res/orbs/OrbGlowAnim_32_12.png", PixmapFormat.ARGB4444);
			Assets.BlueBurstAnimPixmap = g.newPixmap("images/low-res/orbs/OrbBurstBlueAnim_32_8.png", PixmapFormat.ARGB4444);
			Assets.GreenBurstAnimPixmap = g.newPixmap("images/low-res/orbs/OrbBurstGreenAnim_32_8.png", PixmapFormat.ARGB4444);
			Assets.RedBurstAnimPixmap = g.newPixmap("images/low-res/orbs/OrbBurstRedAnim_32_8.png", PixmapFormat.ARGB4444);
			Assets.YellowBurstAnimPixmap = g.newPixmap("images/low-res/orbs/OrbBurstYellowAnim_32_8.png", PixmapFormat.ARGB4444);
			Assets.ClockAnimPixmap = g.newPixmap("images/low-res/ClockAnim_48_33.png", PixmapFormat.ARGB4444);
			
			Assets.ButtonSound = a.newSound("sounds/button-20.mp3");
			Assets.PageFlipSound = a.newSound("sounds/page-flip-4.mp3");
			Assets.OrbLandSound = a.newSound("sounds/OrbLanding.mp3");
			Assets.OrbBurstSound = a.newSound("sounds/OrbBursting.mp3");
			
			Assets.MusicTrack1 = a.newMusic("music/destination.mp3");
			
			Settings.File = game.getFileIO();
			Settings.Load();
			Assets.MusicTrack1.setVolume(Settings.MusicVolume);
			Assets.MusicTrack1.setLooping(true);
			
			if (Settings.SoundEnabled) Assets.MusicTrack1.play();
		}
		
		displayTimeRemaining -= deltaTime;
		if (displayTimeRemaining <= 0)
		{
			game.setScreen(new MainMenuScreen(game));
		}
	}

	@Override
	public void present(float deltaTime)
	{
		if (Assets.TitleScreen != null)
		{
			g = game.getGraphics();
			g.drawPixmap(Assets.TitleScreen, 0, 0);
			screenDrawn = true;
		}
	}

	@Override
	public void pause()
	{
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
