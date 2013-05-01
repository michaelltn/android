package com.michaelcamerongames.percula;

public class Space
{
	public Piece piece = null;
	public boolean flag = false;
	public boolean isEmpty() { return piece == null; }
	public void empty() { piece = null; }
}
