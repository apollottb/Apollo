package com.apollottb.android;
import processing.core.*;

public class MainActivity extends PApplet
{
	RetryScreen retryScreen;
	StartScreen startScreen;
	GameScreen gameScreen;
	
	// For use outside Android:
	//final int displayWidth = 720;
	//final int displayHeight = 400;//1289
	
	// Original game screen dimensions
	final static int WIDTH = 640;
	final static int HEIGHT = 1136;
	
	final int BLACK = 0;
	
	enum State
	{
		START_SCREEN,
		GAME_SCREEN,
		RETRY_SCREEN
	}
	
	State state;
	
	
	public void setup()
	{
		frameRate(60);
		orientation(PORTRAIT);
		
		
		Transformation screenTransformation;
		screenTransformation = getScreenTransformation();
		
		PFont font = createFont("neotechpro_black.otf", 70*screenTransformation.scale);
		textFont(font);
		
		
		DisplayObject.GAME_HEIGHT = HEIGHT;
		DisplayObject.GAME_WIDTH = WIDTH;
		startScreen = new StartScreen(this, screenTransformation);
		gameScreen = new GameScreen(this, screenTransformation);
		retryScreen = new RetryScreen(this, screenTransformation);
		
		
		state = State.START_SCREEN;
		startScreen.reset();
	}


	public void draw() 
	{
		switch (state)
		{
		case START_SCREEN:
			startScreen.tick();
			background(BLACK);
			startScreen.draw();
			
			if (startScreen.isReadyForNextState)
			{
				gameScreen.reset();
				state = State.GAME_SCREEN;
			}
			break;
			
		case GAME_SCREEN:
			gameScreen.tick();
			background(BLACK);
			gameScreen.draw();
			
			if (gameScreen.isGameOver)
			{
				retryScreen.reset(gameScreen.score);
				state = State.RETRY_SCREEN;
			}
			break;
			
		case RETRY_SCREEN:
			retryScreen.draw();
			
			if (retryScreen.isReadyForNewGame)
			{
				gameScreen.reset();
				state = State.GAME_SCREEN;
			}
			break;
        }
    }
	
	
	public Transformation getScreenTransformation()
	{
		// Note: scale = screen / original
		Transformation t = new Transformation();
		
		if (WIDTH/ displayWidth > HEIGHT / displayHeight)
		{
			t.scale = (float) displayWidth / (float) WIDTH;
			t.x = 0;
			t.y = (displayHeight - HEIGHT*t.scale) / 2.0f;
		}
		else
		{
			t.scale = (float) displayHeight / (float) HEIGHT;
			t.x = (displayWidth - WIDTH*t.scale) /2.0f;
			t.y = 0;
		}
		
		return t;
	}
	
	
	public void mousePressed()
	{
		switch (state)
		{
		case START_SCREEN:
			break;
			
		case GAME_SCREEN:
			gameScreen.mousePressed();
			break;
			
		case RETRY_SCREEN:
			retryScreen.mousePressed();
			break;
		}
	}

	
	public void mouseReleased()  
	{
		switch (state)
		{
		case START_SCREEN:
			startScreen.mouseReleased();
			break;
			
		case GAME_SCREEN:
			break;
			
		case RETRY_SCREEN:
			retryScreen.mouseReleased();
			break;
		}
	}   

	
	public void mouseDragged()
	{
		switch (state)
		{
		case START_SCREEN:
		case GAME_SCREEN:
			break;
			
		case RETRY_SCREEN:
			retryScreen.mouseDragged();
			break;
		}
	}
}  
   