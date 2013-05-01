package com.michaelcamerongames.percula;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.michaelcamerongames.framework.FileIO;
import com.michaelcamerongames.framework.Graphics;

public class Settings
{
	public static FileIO File;
	public static boolean RotationButtonIsClockwise = false;
	public static boolean SoundEnabled = true;
	public static float SFXVolume = 1.0f;
	public static float MusicVolume = 1.0f;
	public static int[] HighScores = new int[] { 500, 400, 300, 200, 100 };
	
	public static boolean Load()
	{
		if (File == null) return false;
		
		BufferedReader in = null;
		
		try
		{
			in = new BufferedReader(new InputStreamReader(File.readFile("data/.percula_1_2")));
			RotationButtonIsClockwise = Boolean.parseBoolean(in.readLine());
			SoundEnabled = Boolean.parseBoolean(in.readLine());
			SetSFXVolume(Float.parseFloat(in.readLine()));
			SetMusicVolume(Float.parseFloat(in.readLine()));
			for (int i = 0; i < 5; i++)
			{
				HighScores[i] = Integer.parseInt(in.readLine());
			}
		}
		catch (FileNotFoundException e)
		{
			Load_1_1();
		}
		catch (IOException e)
		{}
		catch (NumberFormatException e)
		{}
		finally
		{
			try
			{
				if (in != null)
					in.close();
			}
			catch (IOException e) {}
		}
		return true;
	}
	
	private static void Load_1_1()
	{
		if (File == null) return;
		
		BufferedReader in = null;
		
		try
		{
			in = new BufferedReader(new InputStreamReader(File.readFile("data/.percula")));
			RotationButtonIsClockwise = false;
			SoundEnabled = Boolean.parseBoolean(in.readLine());
			SetSFXVolume(Float.parseFloat(in.readLine()));
			SetMusicVolume(Float.parseFloat(in.readLine()));
			for (int i = 0; i < 5; i++)
			{
				HighScores[i] = Integer.parseInt(in.readLine());
			}
		}
		catch (IOException e)
		{}
		catch (NumberFormatException e)
		{}
		finally
		{
			try
			{
				if (in != null)
					in.close();
			}
			catch (IOException e) {}
		}
	}
	
	public static boolean Save()
	{
		if (File == null) return false;
		
		BufferedWriter out = null;
		try
		{
			out = new BufferedWriter(new OutputStreamWriter(File.writeFile("data/.percula_1_2")));
			out.write(Boolean.toString(RotationButtonIsClockwise));
			out.write("\n");
			out.write(Boolean.toString(SoundEnabled));
			out.write("\n");
			out.write(Float.toString(SFXVolume));
			out.write("\n");
			out.write(Float.toString(MusicVolume));
			out.write("\n");
			for (int i = 0; i < 5; i++)
			{
				out.write(Integer.toString(HighScores[i]));
				if (i < 4) out.write("\n");
			}
		}
		catch (IOException e) {}
		finally
		{
			try {
				if (out != null)
					out.close();
			}
			catch (IOException e) {}
		}
		return true;
	}
	
	public static int CheckNewHighScore(int score)
	{
		for (int i = 0; i < 5; i++)
		{
			if (HighScores[i] < score)
			{
				for (int j = 4; j > i; j--)
					HighScores[j] = HighScores[j-1];
				HighScores[i] = score;
				return i;
			}
		}
		return -1;
	}
	
	public static void SetSFXVolume(float volume)
	{
		if (volume > 1.0f)
			SFXVolume = 1.0f;
		else if (volume < 0)
			SFXVolume = 0;
		else
			SFXVolume = volume;
	}
	
	public static void SetMusicVolume(float volume)
	{
		if (volume > 1.0f)
			MusicVolume = 1.0f;
		else if (volume < 0)
			MusicVolume = 0;
		else
			MusicVolume = volume;
	}
	
	
	public static void DrawMuteButton(Graphics g, int x, int y)
	{
		int srcX = 0;
		int srcY = 0;
		int srcWidth = Assets.MutePixmap.getWidth();
		int srcHeight = Assets.MutePixmap.getHeight() / 2;
		if (!SoundEnabled)
			srcY += srcHeight;
		g.drawPixmap(Assets.MutePixmap, x, y, srcX, srcY, srcWidth, srcHeight);
	}

	public static void DrawSFXVolumeSlider(Graphics g, int x, int y)
	{
		DrawVolumeSlider(g, SFXVolume, x, y);
	}

	public static void DrawMusicVolumeSlider(Graphics g, int x, int y)
	{
		DrawVolumeSlider(g, MusicVolume, x, y);
	}

	
	public static void DrawVolumeSlider(Graphics g, float volume, int x, int y)
	{
		int dstX = x;
		int dstY = y;
		int srcX = 0;
		int srcY = 0;
		int srcWidth = Math.round(Assets.VolumeSliderPixmap.getWidth() * volume);
		int srcHeight = Assets.VolumeSliderPixmap.getHeight() / 2;
		g.drawPixmap(Assets.VolumeSliderPixmap, dstX, dstY, srcX, srcY, srcWidth, srcHeight);
		
		srcY = srcHeight;
		srcX = srcWidth;
		dstX += srcWidth;
		srcWidth = Assets.VolumeSliderPixmap.getWidth() - srcWidth;
		g.drawPixmap(Assets.VolumeSliderPixmap, dstX, dstY, srcX, srcY, srcWidth, srcHeight);
	}
	
	
}
