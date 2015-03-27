package com.apollottb.android;
import processing.core.PApplet;
import processing.core.PImage;

public class DisplayObject
{
	// Position and dimension without screen transformation.
	public float x;
	public float y;
	public float width;
	public float height;
	public static int GAME_HEIGHT;
	public static int GAME_WIDTH;
	
	protected PApplet applet;
	protected Transformation screenTransformation;
	protected PImage image;
	
	
	public DisplayObject(PApplet applet, Transformation screenTransformation, String imgFileName)
	{
		this.applet = applet;
		this.screenTransformation = screenTransformation;
		x = 0.0f;
		y = 0.0f;
		
		image = applet.loadImage(imgFileName);
		height = image.height;
		width = image.width;
		resizeHelper(image, screenTransformation.scale);
	}
	
	
	public void draw()
	{
		if (image == null) return;
		
		applet.image(image, getScreenX(x), getScreenY(y));
	}
	
	
	protected float getScreenX(float x)
	{
		return x * screenTransformation.scale + screenTransformation.x;
	}
	
	
	protected float getScreenY(float y)
	{
		return y * screenTransformation.scale + screenTransformation.y;
	}
	
	
	public void resize(float scale)
	{
		if (image == null) return;
		
		height *= scale;
		width *= scale;
		resizeHelper(image, scale);
	}
	
	
	protected void resizeHelper(PImage img, float scale)
	{
		img.resize(0, (int) (img.height * scale));
		img.loadPixels();
	}
}
