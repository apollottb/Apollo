package com.apollottb.android;

import processing.core.PApplet;
import processing.core.PImage;

public class GameScreen extends DisplayObject
{
	public boolean isGameOver;
	public int score;
	
	private Robot robot;
	private Car[] cars;
	private final int CAR_COUNT = 4;
	
	private PImage imgTopLayer;
	private DisplayObject topLayer;
	private DisplayObject robotScoreLogo;
	
	private int robotStartX = 130;
	private int robotStartY = 550;
	private float[] carsStartX = {224.0f, 274.0f, 326.0f, 376.0f};
	private float textSize;
	
	private final static String IMAGE_FILE_NAME = "bg_layer_bottom.png";
	private final static String TOP_LAYER_IMG_FILE_NAME = "bg_layer_top.png";
	private final static String[] CAR_IMG_FILE_NAMES = {"car_blue.png", "car_pink.png", "car_green.png", "car_yellow.png"};
	private final static String ROBOT_IMG_FILE_NAME = "robot.png";
	private final static String[] ROBOT_ANIM_RUNNING_FILE_NAMES = {"robot_running1.png", "robot_running2.png", "robot_running3.png"};
	private final static String[] ROBOT_ANIM_BREAKING_FILE_NAMES = {"robot_breaking1.png", "robot_breaking2.png", "robot_breaking3.png"};
	private final static String[] ROBOT_ANIM_WARPING_FILE_NAMES = {"robot_warping1.png", "robot_warping2.png", "robot_warping3.png"};
	private final static String WARP_BOTTOM_LAYER_IMG_FILE_NAME = "warp1.png";
	private final static String WARP_TOP_LAYER_IMG_FILE_NAME = "warp2.png";
	
	private final static float COLLISION_DISTANCE_SQUARED = 2500.0f;
	private final static float SCREEN_SHAKE_MAGNITUDE = 15.0f;
	private final static int WHITE = 255;
	private final static int RED = -65536; // 0xFF0000
	
	private int timer;
	
	private enum State
	{
		WAITING_FOR_TAP,
		ROBOT_CROSSING,
		ROBOT_WARPING,
		NEW_ROBOT_COMING,
		SCREEN_FLASHING,
		ROBOT_BREAKING
	}
	
	private State state;
	
	
	public GameScreen(PApplet applet, Transformation screenTransformation)
	{
		super(applet, screenTransformation, IMAGE_FILE_NAME);
		createRobot();
		createCars();
		
		topLayer = new DisplayObject(applet, screenTransformation, TOP_LAYER_IMG_FILE_NAME);
		robotScoreLogo = new DisplayObject(applet, screenTransformation, ROBOT_IMG_FILE_NAME);
		robotScoreLogo.resize(70.0f / 300.0f);
		robotScoreLogo.x = 10;
		robotScoreLogo.y = 25;
		
		textSize = 60.0f;
	}
	
	
    private void createRobot()
	{
		robot = new Robot(applet, screenTransformation, ROBOT_IMG_FILE_NAME);
		robot.setAnimRunning(ROBOT_ANIM_RUNNING_FILE_NAMES);
		robot.setAnimBreaking(ROBOT_ANIM_BREAKING_FILE_NAMES);
		robot.setAnimWarping(ROBOT_ANIM_WARPING_FILE_NAMES);
		robot.setImagesWarp(WARP_BOTTOM_LAYER_IMG_FILE_NAME, WARP_TOP_LAYER_IMG_FILE_NAME);
		robot.resize(70.0f / 300.0f);
	}
    
    
	public void createCars()
	{	
		cars = new Car[CAR_COUNT];
		for (int i = 0; i < CAR_COUNT; ++i)
		{
			cars[i] = new Car(applet, screenTransformation, CAR_IMG_FILE_NAMES[i]);
			cars[i].resize(70.0f / 120.0f);
		}
	}
	
	
	public void setImageTopLayer(String fileName)
	{
		imgTopLayer = applet.loadImage(fileName);
		resizeHelper(imgTopLayer, screenTransformation.scale);
	}
	
	
	@Override
	public void resize(float scale)
	{
		super.resize(scale);
		robotStartX *= scale;
		robotStartY *= scale;
		// Must resize other variables as well.
		
		if (imgTopLayer == null) return;
		
		resizeHelper(imgTopLayer, scale);
	}
	
	
	public void tick()
	{
		if (state != State.SCREEN_FLASHING &&
			state != State.ROBOT_BREAKING)
		{
			for (int i = 0; i < CAR_COUNT; ++i)
			{
				cars[i].tick();
				if (cars[i].y < -cars[i].height)
				{
					cars[i].y = GAME_HEIGHT;
					setRandomCarSpeed(i);
				}
				if (cars[i].y > GAME_HEIGHT)
				{
					cars[i].y = -cars[i].height;
					setRandomCarSpeed(i);
				}
			}
		}
		
		timer ++;
		
		switch(state)
		{
		case WAITING_FOR_TAP:
			break;
			
		case ROBOT_CROSSING:
			robot.tick();
			robot.x += 10;
			if (robot.x > 400)
			{
				timer = 0;
				robot.frameNumber = 0;
				state = State.ROBOT_WARPING;
			}
			else if (isRobotColliding())
			{
				timer = 0;
				state = State.SCREEN_FLASHING;
			}
			break;
			
		case ROBOT_WARPING:
			robot.tick();
			if (timer > 20)
			{
				score ++;
				robot.x = 0;
				state = State.NEW_ROBOT_COMING;
			}
			break;
			
		case NEW_ROBOT_COMING:
			robot.tick();
			robot.x += 10;
			if (robot.x > robotStartX)
			{
				state = State.WAITING_FOR_TAP;
			}
			break;
			
		case SCREEN_FLASHING:
			if (timer > 10)
			{
				timer = 0;
				robot.frameNumber = 0;
				state = State.ROBOT_BREAKING;
			}
			break;
		
		case ROBOT_BREAKING:
			robot.tick();
			if (timer < 20)
			{
				x = applet.random(SCREEN_SHAKE_MAGNITUDE) - SCREEN_SHAKE_MAGNITUDE/2.0f;
				y = applet.random(SCREEN_SHAKE_MAGNITUDE) - SCREEN_SHAKE_MAGNITUDE/2.0f;
			}
			else
			{
				x = 0;
				y = 0;
			}
			topLayer.x = x;
			topLayer.y = y;
			
			if (timer > 50)
			{
				isGameOver = true;
			}
			break;
		}
	}
	
	
	@Override
	public void draw()
	{
		if (state == State.SCREEN_FLASHING)
		{
			applet.background(WHITE);
			return;
		}
		
		super.draw();
		
		for (Car car : cars)
		{
			car.draw();
		}
		
		switch(state)
		{
		case WAITING_FOR_TAP:
			robot.draw();
			break;
			
		case ROBOT_CROSSING:
		case NEW_ROBOT_COMING:
			robot.drawRunning();
			break;
			
		case ROBOT_WARPING:
			robot.drawWarping();
			break;
			
		case SCREEN_FLASHING:
			break;
			
		case ROBOT_BREAKING:
			robot.drawBreaking();
			break;
		}
		
		topLayer.draw();
		robotScoreLogo.draw();
		
		applet.text(score, getScreenX(robotScoreLogo.x + robotScoreLogo.width + 10), getScreenY(robotScoreLogo.y + robotScoreLogo.height / 2));
	}
	
	
	private boolean isRobotColliding()
	{
		for (Car car : cars)
		{
			float carX = car.x + car.width / 2;
			float carY = car.y + car.height / 2;
			float robotX = robot.x + robot.width / 2;
			float robotY = robot.y + robot.height / 2;
			float dx = carX - robotX;
			float dy = carY - robotY;
			
			if (dx * dx + dy * dy < COLLISION_DISTANCE_SQUARED)
			{
				return true;
			}
		}
		return false;
	}
	
	
	public void reset()
	{
		state = State.WAITING_FOR_TAP;
		score = 0;
		isGameOver = false;
		applet.fill(RED);
		applet.textAlign(PApplet.LEFT, PApplet.CENTER);
		applet.textSize(textSize * screenTransformation.scale);
		
		
		robot.x = robotStartX;
		robot.y = robotStartY;
		
		float carHeight = cars[0].height;
		for (int i = 0; i < CAR_COUNT; ++i)
		{
			cars[i].x = carsStartX[i];
			cars[i].y = applet.random(GAME_HEIGHT + carHeight) - carHeight;
			setRandomCarSpeed(i);
		}
	}
	
	
	private void setRandomCarSpeed(int i)
	{
		cars[i].speed = 5.0f + applet.random(10.0f);
		if (i > 1)
		{
			cars[i].speed *= -1;
		}
	}
	
	
	public void mousePressed()
	{
		if (state == State.WAITING_FOR_TAP)
		{
			state = State.ROBOT_CROSSING;
		}
	}
}
