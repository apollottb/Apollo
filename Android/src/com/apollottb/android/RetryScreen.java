package com.apollottb.android;

import processing.core.PApplet;
import processing.core.PImage;

public class RetryScreen extends DisplayObject
{
	public int score;
	public boolean isReadyForNewGame;
	
	private PImage imgPressed;
	private int buttonX;
	private int buttonY;
	private int buttonWidth;
	private int buttonHeight;
	private boolean isButtonDown;
	private boolean isButtonDragging;
	private int textX;
	private int textY;
	private float textSize;
	
	private final int BLACK = 0;
	private final static String IMAGE_FILE_NAME = "retry_screen.png";
	private final static String PRESSED_IMG_FILE_NAME = "retry_screen_button_pressed.png";
	
	public RetryScreen(PApplet applet, Transformation screenTransformation)
	{
		super(applet, screenTransformation, IMAGE_FILE_NAME);
		
		buttonX = 150;
		buttonY = 580;
		buttonWidth = 320;
		buttonHeight = 140;
		
		textSize = 100;
		textX = GAME_WIDTH / 2; //320
		textY = 300;
		
		isReadyForNewGame = false;
		
		imgPressed = applet.loadImage(PRESSED_IMG_FILE_NAME);
		resizeHelper(imgPressed, screenTransformation.scale);
	}
	
	
	public void reset(int score)
	{
		this.score = score;
		isReadyForNewGame = false;

		applet.fill(BLACK);
		applet.textAlign(PApplet.CENTER);
		applet.textSize(textSize * screenTransformation.scale);
	}
	
	
	@Override
	public void resize(float scale)
	{
		super.resize(scale);
		buttonX *= scale;
		buttonY *= scale;
		buttonWidth *= scale;
		buttonHeight *= scale;
		
		if (imgPressed == null) return;
		
		resizeHelper(imgPressed, scale);
	}
	
	
	public boolean isOnButton()
	{
		float x = getScreenX(buttonX);
		float y = getScreenY(buttonY);
		float w = getScreenX(buttonWidth);
		float h = getScreenY(buttonHeight);
		
		return (x < applet.mouseX && 
				y < applet.mouseY &&
				x + w > applet.mouseX &&
				y + h > applet.mouseY);
	}
	
	
	public void mousePressed()
	{
		if (isOnButton())
		{
			isButtonDown = true;
			isButtonDragging = true;
		}
	}
	
	
	public void mouseReleased()
	{
		isReadyForNewGame = isButtonDragging && isOnButton();
		
		isButtonDown = false;
		isButtonDragging = false;
	}
	
	
	public void mouseDragged()
	{
		isButtonDown = isOnButton() && isButtonDragging;
	}
	
	
	@Override
	public void draw()
	{
		if (isButtonDown)
		{
			applet.image(imgPressed, getScreenX(x), getScreenY(y));
		}
		else
		{
			super.draw();
		}
		applet.text(score, getScreenX(textX), getScreenY(textY));
	}
}
