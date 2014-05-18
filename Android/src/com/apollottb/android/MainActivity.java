package com.apollottb.android;

import processing.core.PApplet;
import processing.core.PImage;

public class MainActivity extends PApplet
{
	PImage im;
	
	public void setup()
	{
		im = loadImage("cat.jpg");
	}
	
	public void draw()
	{
		background(0);
		
		image(im, 300, 300);
		
		fill(255);
		ellipse(mouseX, mouseY, 100, 100);
	}
}
