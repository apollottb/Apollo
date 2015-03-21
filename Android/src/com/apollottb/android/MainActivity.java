package com.apollottb.android;
import processing.core.*;

public class MainActivity extends PApplet
{
	PFont font;
	Robot robot;
	Car[] cars;
	final int CAR_COUNT = 4;
	
    boolean isPersonMoving = false;  
    int score;
    
    PImage imgBackgroundBack;
    PImage imgBackgroundGray; 
    PImage imgRobot;
    PImage imgRobot1;
    PImage imgRobotRunning1;
    PImage imgRobotRunning2;
    PImage imgRobotRunning3; 
    PImage imgRobotFalling1;
    PImage imgRobotFalling2;
    PImage imgRobotFalling3;
    PImage imgOpening;
    PImage imgBackgroundFull;
    PImage imgBackgroundFront;
    PImage imgRetry;
    PImage imgRetryPressed;
    //PImage imgCarBlue;
    //PImage imgCarPink;
    //PImage imgCarGreen;
    //PImage imgCarYellow;
    PImage imgWarp1;
    PImage imgWarp2;
    PImage imgWarp3;
    PImage imgWarp4;
    PImage imgWarpingRobot1;
    PImage imgWarpingRobot2;
    PImage imgWarpingRobot3;
    
    PImage[] imgRobotRunning;
    PImage[] imgWarping;
    PImage[] imgWarpingRobot;
    PImage[] imgRobotFalling;

    //screen touch & release
    boolean releaseScreen = false;
    boolean touchScreen = false; 
    boolean buttonDown;
    boolean buttonDragging;
    boolean isOnRetry;
    boolean mouseDragged = false;
    
    int timer;
    //final int displayWidth = 720;
    //final int displayHeight = 400;//1289
    final int WIDTH = 640;           // Original screen dimensions
    final int HEIGHT = 1136;
    
    float scale;     //adjustment from original to display
    float deltaX;
    float deltaY;
    
    //blink
    boolean blinkingOn = true;
    int blinkSpeed = 12;
        
    float[] distances; 
    
    //color
    int black = 0;
    int white = 255;  
    
    //animation range
    int warpingRange; 
    int fallingRange; 
       
    //listSetting
    //float[] carX;
    //float[] carY;
    //float carWidth;
    //float carHeight;
    //float[] carSpeed;
    
    //float robotWidth;
    //float robotHeight;
       
    //retry button
    float retryButtonX = 150;
    float retryButtonY = 580;
    float retryWidth = 320;
    float retryHeight = 140; 
      
    //define state 
    int state = 0;
    final int START = 0;
    final int WAIT_FOR_CLICK = 1;
    final int ROBOT_MOVING = 2;
    final int ROBOT_WARPING = 3;
    final int ROBOT_FROM_FACTORY = 4;
    final int SCREEN_SHAKING = 5;
    final int GAME_OVER = 6; 
   
 
    public void setup()
    {
        frameRate(60);
        //size(displayWidth, displayHeight);
        background(black); 
        
        robot = new Robot();
        robot.x = 130.0f;
        robot.y = 550.0f;
        
        cars = new Car[CAR_COUNT];
        for (int i = 0; i < CAR_COUNT; ++i)
        {
        	cars[i] = new Car();
        }
        
        
        if (WIDTH/ displayWidth > HEIGHT / displayHeight)
        {
            scale = (float) WIDTH/ (float) displayWidth;
            deltaX = 0;
            deltaY = (displayHeight - HEIGHT/scale) / 2.0f;           
        }
        else
        {
            scale = (float) HEIGHT/ (float) displayHeight;
            deltaX = (displayWidth - WIDTH/scale) /2.0f;
            deltaY = 0;     
            
        }
        
        font = createFont("NEOTECHPRO-BLACK.OTF", 70/scale);
       
        //image array
        imgRobotRunning = new PImage[3];
        imgWarping = new PImage[4];
        imgWarpingRobot = new PImage[3];
        imgRobotFalling = new PImage[3];
 
        
        // -----------------------------------------
        // -------- Load & resize images -----------
        // -----------------------------------------
        
        String directory = "";
        
        imgBackgroundBack = loadImage(directory + "Layer_Background-2.1.png");
        resize(imgBackgroundBack, 1136);
        
        imgRobot = loadImage(directory + "Robot-Runner.png");
        resize(imgRobot, 70);
        
        imgRobot1 = loadImage(directory + "Robot-Runner.png");
        resize(imgRobot1, 70);
        
        imgRobotRunning[0] = loadImage(directory + "Robot_animation1.png");
        imgRobotRunning[1] = loadImage(directory + "Robot_animation2.png");
        imgRobotRunning[2] = loadImage(directory + "Robot_animation3.png");
        for (int i = 0; i < 3 ;i ++)
        {
            resize(imgRobotRunning[i], 70);
        }
        
        imgRobotFalling[0] = loadImage(directory + "Robot-fall1.png");
        imgRobotFalling[1] = loadImage(directory + "Robot-fall2.png");
        imgRobotFalling[2] = loadImage(directory + "Robot-fall3.png");
        for(int i = 0; i < 3; i ++)
        {
            resize(imgRobotFalling[i], 70);
        }
        
        imgOpening = loadImage(directory + "Opening-2.1.png");
        resize(imgOpening, 1136);
 
        imgBackgroundFull = loadImage(directory + "Background.jpg");
        resize(imgBackgroundFull, 1136);
        
        
        imgBackgroundFront = loadImage(directory + "Layer_Buildings-2.1.png"); 
        resize(imgBackgroundFront, 1136);
        
        imgRetry = loadImage(directory + "Retry2.1_Button.png"); 
        resize(imgRetry, 1136);
        
        
        imgRetryPressed = loadImage(directory + "Retry2.1_ButtonEngaged.png"); 
        resize(imgRetryPressed, 1136);
        
        String[] carImgFileNames = {"Car_bl.png", "Car_pi.png", "Car_gr.png", "Car_ye.png"};
        for (int i = 0; i < CAR_COUNT; ++i)
        {
        	Car c = cars[i];
        	c.image = loadImage(directory + carImgFileNames[i]);
        	resize(c.image, 70);
        }
        
        
        /*
        imgCarBlue =  loadImage(directory + "Car_bl.png"); 
        resize(imgCarBlue, 70);
        
        imgCarPink =  loadImage(directory + "Car_pi.png"); 
        resize(imgCarPink, 70);
        
        imgCarGreen =  loadImage(directory + "Car_gr.png"); 
        resize(imgCarGreen, 70);
        
        imgCarYellow = loadImage(directory + "Car_ye.png");
        resize(imgCarYellow, 70);
        */    
        imgWarping[0] = loadImage(directory + "warp1.png");
        imgWarping[1] = loadImage(directory + "warp2.png");
        imgWarping[2] = loadImage(directory + "warp3.png");
        imgWarping[3] = loadImage(directory + "warp4.png");
        for (int i = 0; i < 4; i++)
        {
            resize(imgWarping[i], 70);
        }
        
        imgWarpingRobot[0] =loadImage(directory + "Robot-warp.png"); 
        imgWarpingRobot[1] = loadImage(directory + "Robot-warp2.png");
        imgWarpingRobot[2]= loadImage(directory + "Robot-warp2.png");
        for(int i =0; i < 3; i++)
        {
            resize(imgWarpingRobot[i], 70);
        }
        
        // Distance between cars and robot.
        distances = new float[4]; 
        
        // Car positions.
        float[] tmpX = {224.0f, 274.0f, 326.0f, 376.0f};
        float[] tmpY = {0.0f, 0.0f, HEIGHT, HEIGHT};
        for (int i = 0; i < CAR_COUNT; ++i)
        {
        	cars[i].x = getX(tmpX[i]);
        	cars[i].y = getY(tmpY[i]);
        }
        /*
        carX = new float[4];
        carX[0] = getX(224.0f);
        carX[1] = getX(274.0f);
        carX[2] = getX(326.0f); 
        carX[3] = getX(376.0f);
        
        carY = new float[4];
        carY[0] = getY(0);
        carY[1] = getY(0);
        carY[2] = getY(HEIGHT); 
        carY[3] = getY(HEIGHT);
        */
        
        // Car dimensions.
        //carWidth = 35.0f/scale;
        //carHeight = 60.0f/scale;
        Car.width = 35.0f / scale;
        Car.height = 60.0f / scale;
        
        //robot dimensions.
        //robotWidth = 70.0f/scale;
        //robotHeight = 70.0f/scale;
        robot.width = 70.0f/scale;
        robot.height = 70.0f/scale;
        
        
        //carSpeed
        for (int i = 0; i < CAR_COUNT; ++i)
        {
        	cars[i].speed = (2 + random(10))/scale;
        }
        /*
        carSpeed = new float[4];
        for(int i = 0 ; i < 4 ; i++)
        {
            carSpeed[i] = (2 + random(10))/scale;   
        }
        */
       
        robot.x = getX(130);
        robot.y = getY(550);
        
         retryButtonX = getX(150);
         retryButtonY = getY(580);
         retryWidth = 320/scale;
         retryHeight = 140/scale; 
       
    }
    
    public void resize(PImage img, int imgHeight)
    {
        img.resize(0, (int) (imgHeight / scale));
        img.loadPixels();
    }
    
    public float getX(float originalX)
    {
        return originalX/scale + deltaX;
    }
    
     public float getY(float originalY)
    {
        return originalY/scale + deltaY;
    }
    
    
    
    public void draw() 
    {
        switch (state)
        {
            case START:
                image(imgOpening, getX(0), getY(0));
                if (frameCount % blinkSpeed == 0) { blinkingOn = !blinkingOn; }
                if (blinkingOn) 
                {
                    fill(color(255, 0, 0));
                    textFont(font);
                    textAlign(CENTER);
                    textSize(60/scale);
                    text("Tap to Start", getX(320), getY(750)); 
                    fill(white);
                }            
                
                if (releaseScreen == true) 
                {
                    
                    touchScreen = false;
                    releaseScreen = false;
                    background(white);
                    state = WAIT_FOR_CLICK;
                     timer = 0;
                     
                }
                break;
            case WAIT_FOR_CLICK:
                image(imgBackgroundBack,getX(0), getY(0));  
                image(imgRobot1, getX(10), getY(25));
                image(imgRobot, robot.x, robot.y);
                image(imgBackgroundFront, getX(0), getY(0));
                gamePlay();
                if ( touchScreen == true) 
                {
                    gamePlay();
                    isPersonMoving = true;
                    touchScreen = false;
                    state = ROBOT_MOVING;
                    timer = 0;
                }
                break;
            case ROBOT_MOVING:
                image(imgBackgroundBack,getX(0),getY(0));  
                image(imgRobot1, getX(10), getY(25));
                image(imgBackgroundFront,getX(0),getY(0));
                gamePlay();
               
                timer = timer + 1;
                for (int i = 0; i < 4; i++)
                {
                    //float dx = (carX[i] + carWidth/2.0f) - (robot.x + robotWidth/2.0f);
                    //float dy = (carY[i] + carHeight/2.0f) - (robot.y + robotHeight/2.0f);
                    float dx = (cars[i].x + Car.width/2.0f) - (robot.x + robot.width/2.0f);
                    float dy = (cars[i].y + Car.height/2.0f) - (robot.y + robot.height/2.0f);
                    distances[i] = (float) Math.sqrt(dx * dx + dy * dy); 
                }
                
                
                if (timer >= 0 && timer < 28)
                {
                    drawRobotRunning(timer % 3);
                }
                if (timer >= 28)
                {    
                    isPersonMoving = false;
                    state = ROBOT_WARPING;
                    timer = 0;
                 }
               if (distances[0] < 50.0f/scale || distances[1] < 50.0f/scale || distances[2] < 50.0f/scale || distances[3] <  50.0f/scale ) 
                {
                    state = SCREEN_SHAKING; 
                    touchScreen = false;
                    releaseScreen = false;
                    isPersonMoving = false;
                    timer = 0;
                 }
                break;

            case ROBOT_WARPING:
                image(imgBackgroundBack,getX(0),getY(0));  
                image(imgRobot1, getX(10), getY(25));
                image(imgBackgroundFront,getX(0),getY(0));
                gamePlay();
                timer = timer + 1;  
                
                if(timer >= 1 && timer < 30)
                {
                    warpingRange = round(timer/5);
                    drawRobotWarping(warpingRange % 3, timer-1); 
                }
                
                if(timer == 30)
                {
                    robot.x = getX(0);
                    isPersonMoving = true;
                    state = ROBOT_FROM_FACTORY;
                    timer = 0;
                    score= score + 1;
                }
                break;
            case ROBOT_FROM_FACTORY:
                timer = timer + 1;
                image(imgBackgroundBack,getX(0),getY(0));  
                image(imgRobot1, getX(10), getY(25));
                image(imgBackgroundFront,getX(0),getY(0));
                gamePlay();               
                if(timer >= 0 && timer < 13)
                {  
                   drawRobotRunning(timer % 3);
                   image(imgBackgroundFront,getX(0),getY(0));
                }
                if(timer == 13)
                {
                	touchScreen = false;
                    timer = 0;
                    drawRobotRunning(0);
                    isPersonMoving = false;
                    state = WAIT_FOR_CLICK;                
                }
                break;

            case SCREEN_SHAKING:  
                timer = timer + 1;
                background (white);
                if(timer >= 10 && timer < 30)
                {
                	background(black);
                    drawBackground(random(10)-5, random(10)-5); 
                    image(imgRobot1, getX(10), getY(25));
                    textFont(font);
                    fill(255, 0, 0);
                    text(score, getX(140), getY(85));
                    fallingRange = round(timer/15); 
                    drawRobotFalling(fallingRange % 3);
                }
                if (timer >= 30 && timer < 60)
	                drawBackground(0, 0); 
	                image(imgRobot1, getX(10), getY(25));
	                textFont(font);
	                fill(255, 0, 0);
	                text(score, getX(140), getY(85));
	                fallingRange = round(timer/15); 
	                drawRobotFalling(fallingRange % 3);
                if (timer == 60)
                {  
                    drawBackground(0, 0);
                    image(imgRobot1, getX(10), getY(25));
                    textFont(font);
                    fill(255, 0, 0);
                    text(score, getX(140), getY(85));
                    state = GAME_OVER;
                    timer = 0;
                }
                break;
                
        case GAME_OVER:
            if(buttonDown == true)
            {
                drawButtonDown();
            }
            else
            {
                image(imgRetry, getX(0),getY (0));
                textFont(font);
                fill(black);
                textSize(100/scale);
                text(score, getX(WIDTH/2), getY(300));
            }
            break;
        }
    }
    
    public void gamePlay()
    {
    	for (int i = 0; i < CAR_COUNT; ++i)
    	{
    		Car c = cars[i];
    		
    		switch(i)
    		{
    		case 0:
    		case 1:
    			c.y += c.speed / scale;
    			
    	        if (c.y > displayHeight)
    	        {
    	            c.y = 0.0f;      
    	        }
    			break;
    		case 2:
    		case 3:
    			c.y -= c.speed / scale;
    			
    	        if (c.y < 0)
    	        {
    	            c.y = displayHeight;
    	        }
    			break;
    		}
    		c.speed = 5.0f + random(10.0f)/scale;
    		image(c.image, c.x, c.y);
    	}
    	/*
        // Move rectangle1 downward.
        carY[0] = carY[0] + carSpeed[0]/scale;
    	
        
        // If rectangle1 goes outside of the screen, reset its position and change its speed.
        if (carY[0] > displayHeight)
        {
            carY[0] = 0;
            carSpeed[0] = 5 + random(10.0f)/scale;      
        }
        
        // Move rectangle2 a little bit downward.
        carY[1] = carY[1] + carSpeed[1]/scale;
        
        // If rectangle1 goes outside of the screen, reset its position and change its speed.
        if (carY[1] > displayHeight)
        {
            carY[1] = 0;
            carSpeed[1] = 5 +random(10.0f)/scale;
        }
        
        // Move rectangle1 a little bit upward.
        carY[2] = carY[2] - carSpeed[2]/scale;
        
        // If rectangle1 goes outside of the screen, reset its position and change its speed.
        if (carY[2] < 0)
        {
            carY[2] = HEIGHT;
            carSpeed[2] = 2 + random(10.0f)/scale;
        }    
        // Move rectangle1 a little bit upward.
        carY[3] = carY[3] - carSpeed[3];
        
        // If rectangle1 goes outside of the screen, reset its position and change its speed.
        if (carY[3] < 0)
        {
            carY[3] = HEIGHT;
            carSpeed[3] = 2 + random(10.0f)/scale;
        }
        
               
        // Car1
        image(imgCarBlue, carX[0], carY[0]);
        
        //Car2
        image(imgCarPink, carX[1], carY[1]); 
        
        //Car3
        image(imgCarGreen, carX[2], carY[2]);
        
        //Car4
        image(imgCarYellow, carX[3], carY[3]);
        */

        if (isPersonMoving == true)
        {
        	robot.x = robot.x + 10/scale;
        }        
              
        //score board
        textFont(font);
        fill(255, 0, 0);
        text(score, getX(140), getY(85));
 
    }
    
    public void mousePressed()
    {
        touchScreen = true;
        if (state == GAME_OVER)
        {
             if(isOnRetry() == true)
                {
                    buttonDown = true;
                    buttonDragging = true;
                }
        }
    }
    
    public void mouseReleased()  
    {
        releaseScreen = true;
        if(state == GAME_OVER)
        {
            if(buttonDragging == true && isOnRetry() == true) 
            {
                state = WAIT_FOR_CLICK;
                robot.x = getX(130);
                score = 0;
                touchScreen = false;
            }     
        }
        buttonDown = false;
        buttonDragging = false;
    }   
    
    boolean isOnRetry()
    {
        if  (retryButtonX < mouseX && retryButtonY < mouseY && retryButtonX + retryWidth > mouseX && retryButtonY + retryHeight > mouseY)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void drawBackground(float x, float y)
    {
        image(imgBackgroundBack,getX(x),getY(y));  
        image(imgBackgroundFront,getX(x),getY(y));
        /*
        image(imgCarBlue, carX[0], carY[0]);
        image(imgCarPink, carX[1], carY[1]);
        image(imgCarGreen, carX[2], carY[2]);
        image(imgCarYellow, carX[3], carY[3]);
        */
        for (int i = 0; i < CAR_COUNT; ++i)
        {
        	Car c = cars[i];
        	image(c.image, c.x, c.y);
        }
    }
   
   
   public void drawRobotRunning(int i)
    {
	   image(imgRobotRunning[i], robot.x, robot.y);
    }
  
   int[] isWarpVisible = {1,1,1,1,1, 0,0,0,0,0, 1,1,1,1,0, 0,0,0,0,0, 1,1,0,0,0, 0,0,1,0,0};
   public void drawRobotWarping(int i, int j)
   {
	   image(imgWarping[0], robot.x, robot.y);
       if (isWarpVisible[j] == 1)
       {
    	   image(imgWarpingRobot[i], robot.x, robot.y);
       }
       image(imgWarping[1], robot.x, robot.y);
   }
   
   public void drawRobotFalling(int i)
   {
	   image(imgRobotFalling[i], robot.x, robot.y);
   }
   
   public void drawButtonDown()
   {
       image(imgRetryPressed, getX(0), getY(0));
       textFont(font);
       fill(black);
       textSize(100/scale);
       text(score, getX(WIDTH/2), getY(300));
   }
   
   public void mouseDragged()
   {
           if (state == GAME_OVER)
           {
               if(isOnRetry() && buttonDragging)
               {
                   buttonDown = true;
               }
               else
               {     
                   buttonDown = false;
               }
           }
   }
 
}  
   