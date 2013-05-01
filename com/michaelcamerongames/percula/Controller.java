package com.michaelcamerongames.percula;

public class Controller
{
	public static boolean MoveLeftPressed;
	public static boolean MoveRightPressed;
	public static boolean DropPressed;
	public static boolean RotateLeftPressed;
	public static boolean RotateRightPressed;
	
	public static boolean DropButtonEnabled = true;
	
	public static void Reset()
	{
		MoveLeftPressed = false;
		MoveRightPressed = false;
		DropPressed = false;
		RotateLeftPressed = false;
		RotateRightPressed = false;
	}

}
