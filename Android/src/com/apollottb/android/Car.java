package com.apollottb.android;

import processing.core.PApplet;

public class Car extends DisplayObject
{
	public float speed;
	
	
	public Car(PApplet applet, Transformation screenTransformation, String imgFileName)
	{
		super(applet, screenTransformation, imgFileName);
	}
	
	
	public void tick()
	{
		y += speed;
	}
}
