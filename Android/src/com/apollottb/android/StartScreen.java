package com.apollottb.android;

import processing.core.PApplet;

public class StartScreen extends DisplayObject
{
	public boolean isReadyForNextState;
	
	private int frameCount;
	private boolean isTextVisible;
	private int textX;
	private int textY;
	private float textSize;
	private final static int BLINK_PERIOD = 12;
	private final static int RED = -65536; // 0xFF0000
	private final static String IMAGE_FILE_NAME = "start_screen.png";
	
	
	public StartScreen(PApplet applet, Transformation screenTransformation)
	{
		super(applet, screenTransformation, IMAGE_FILE_NAME);
		isReadyForNextState = false;
		frameCount = 0;
		isTextVisible = false;
		textX = 320;
		textY = 750;
		textSize = 60;
	}
	
	
	public void reset()
	{
		applet.fill(RED);
		applet.textAlign(PApplet.CENTER);
		applet.textSize(textSize * screenTransformation.scale);
	}
	
	
	@Override
	public void resize(float scale)
	{
		super.resize(scale);
		textX *= scale;
		textY *= scale;
		textSize *= scale;
	}
	
	
	public void tick()
	{
		if (frameCount > BLINK_PERIOD)
		{
			frameCount = 0;
			isTextVisible = !isTextVisible;
		}
		else
		{
			frameCount ++;
		}
	}
	
	
	@Override
	public void draw()
	{
		super.draw();
		if (isTextVisible)
		{
			applet.text("Tap to Start", getScreenX(textX), getScreenY(textY));
		}
	}
	
	
	public void mouseReleased()
	{
		isReadyForNextState = true;
	}
}
