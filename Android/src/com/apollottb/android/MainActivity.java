package com.apollottb.android;
import processing.core.*;


public class MainActivity extends PApplet
{
    PFont Font1;
    float personPositionX = 130.0f;
    float personPositionY = 550.0f;
    boolean isPersonMoving = false;  
    int score;
    
    PImage imgBackgroudBack;
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
    PImage imgCarBlue;
    PImage imgCarPink;
    PImage imgCarGreen;
    PImage imgCarYellow;
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
    boolean isOnRetry;
    
    int timer;
    
    //blink
    boolean blinkingOn = true;
    int blinkSpeed = 12;
        
    float[] listDistance; 
    
    //color
    int black = 0;
    int white = 255;  
    
    //animation range
    int warpingRange; 
    int fallingRange; 
       
    //listSetting
    float[] listX;
    float[] listY;
    float[] listWidth;
    float[] listHeight;
    float[] listSpeed;
       
    //retry button
    int retryButtonX = 150;
    int retryButtonY = 580;
    int retryWidth = 200;
    int retryHeight = 140; 
      
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
        //size(640, 1136);
        background(white); 
        
        Font1 = createFont("NEOTECHPRO-BLACK.OTF", 70);
        
        //image array
        imgRobotRunning = new PImage[3];
        imgWarping = new PImage[4];
        imgWarpingRobot = new PImage[3];
        imgRobotFalling = new PImage[3];
 
        
        //image     
        String dir = "";
        imgBackgroudBack = loadImage(dir + "Layer_Background-2.1.png");
        imgBackgroundGray = loadImage(dir + "Background.jpg");
        imgBackgroundGray.filter(GRAY);
        imgRobot = loadImage(dir + "Robot-Runner.png");
        imgRobot1 = loadImage(dir + "Robot-Runner.png");
        imgRobotRunning[0] = loadImage(dir + "Robot_animation1.png");
        imgRobotRunning[1] = loadImage(dir + "Robot_animation2.png");
        imgRobotRunning[2] = loadImage(dir + "Robot_animation3.png");
        imgRobotRunning[0].resize(0, 70);
        imgRobotRunning[1].resize(0, 70);
        imgRobotRunning[2].resize(0, 70);
        imgRobotRunning[0].loadPixels();
        imgRobotRunning[1].loadPixels();
        imgRobotRunning[2].loadPixels();
        imgRobotFalling[0] = loadImage(dir + "Robot-fall1.png");
        imgRobotFalling[1] = loadImage(dir + "Robot-fall2.png");
        imgRobotFalling[2] = loadImage(dir + "Robot-fall3.png");
        imgOpening = loadImage(dir + "Opening-2.1.png");
        imgRobotFalling[0].resize(0, 70);
        imgRobotFalling[1].resize(0, 70);
        imgRobotFalling[2].resize(0, 70);
        imgRobotFalling[0].loadPixels();
        imgRobotFalling[1].loadPixels();
        imgRobotFalling[2].loadPixels();
        imgBackgroundFull = loadImage(dir + "Background.jpg");
        imgBackgroundFront = loadImage(dir + "Layer_Buildings-2.1.png"); 
        
        imgRetry = loadImage(dir + "Retry2.1_Button.png"); 
        imgRetryPressed = loadImage(dir + "Retry2.1_ButtonEngaged.png"); 
        imgCarBlue =  loadImage(dir + "Car_bl.png"); 
        imgCarPink =  loadImage(dir + "Car_pi.png"); 
        imgCarGreen =  loadImage(dir + "Car_gr.png"); 
        imgCarYellow = loadImage(dir + "Car_ye.png");
        imgWarping[0] = loadImage(dir + "warp1.png");
        imgWarping[1] = loadImage(dir + "warp2.png");
        imgWarping[2] = loadImage(dir + "warp3.png");
        imgWarping[3] = loadImage(dir + "warp4.png");
        imgWarping[0].resize(0, 70);
        imgWarping[1].resize(0, 70);
        imgWarping[2].resize(0, 70);
        imgWarping[3].resize(0, 70);
        imgWarping[0].loadPixels();
        imgWarping[1].loadPixels();
        imgWarping[2].loadPixels();
        imgWarping[3].loadPixels();
        imgWarpingRobot[0] =loadImage(dir + "Robot-warp.png"); 
        imgWarpingRobot[1] = loadImage(dir + "Robot-warp2.png");
        imgWarpingRobot[2]= loadImage(dir + "Robot-warp2.png");
        imgWarpingRobot[0].resize(0, 70);
        imgWarpingRobot[1].resize(0, 70);
        imgWarpingRobot[2].resize(0, 70);
        imgWarpingRobot[0].loadPixels();
        imgWarpingRobot[1].loadPixels();
        imgWarpingRobot[2].loadPixels();
        
        //distance
        listDistance = new float[4]; 
     
        //carX
        listX = new float[4];
        listX[0] = 224.0f;
        listX[1] = 274.0f;
        listX[2] = 326.0f; 
        listX[3] = 376.0f;
        
        //carY
        listY = new float[4];
        listY[0] = 0;
        listY[1] = 0;
        listY[2] = 800.0f; 
        listY[3] = 800.0f;
        
        //carWidth
        listWidth = new float[4];
        listWidth[0] = 35.0f;
        listWidth[1] = 35.0f;
        listWidth[2] = 35.0f; 
        listWidth[3] = 35.0f;
        
        //carHeight
        listHeight = new float[4];
        listHeight[0] = 60.0f; 
        listHeight[1] = 60.0f;
        listHeight[2] = 60.0f; 
        listHeight[3] = 60.0f;
        
        //carSpeed
        listSpeed = new float[4];
        listSpeed[0] = 2 + random(10);
        listSpeed[1] = 2 + random(10);
        listSpeed[2] = 2 + random(10); 
        listSpeed[3] = 2 + random(10);
    }
 

    public void draw() 
    {
    	background(color(0));
        switch (state)
        {
            case START:
                image(imgOpening, 0, 0);
                if (frameCount % blinkSpeed == 0) { blinkingOn = !blinkingOn; }
                if (blinkingOn) 
                {
                    textAlign(CENTER);
                    textFont(Font1);
                    textSize(60);
                    fill(255);
                    text("Tap to Start", 320, 750); 
                    textSize(58);
                    fill(color(255, 0, 0));
                    text("Tap to Start", 320, 750); 
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
                image(imgBackgroudBack,0,0);  
                imgRobot1.resize(0, 50);
                imgRobot1.loadPixels();
                
                image(imgRobot1, 10, 25);
                imgRobot.resize(0, 70);
                imgRobot.loadPixels();
                
                image(imgRobot, personPositionX, personPositionY);
                image(imgBackgroundFront,0,0);
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
                image(imgBackgroudBack,0,0);  
                imgRobot1.resize(0, 50);
                imgRobot1.loadPixels();
                image(imgRobot1, 10, 25);
                imgRobot.resize(0, 70);
                imgRobot.loadPixels();
                image(imgBackgroundFront,0,0);
                gamePlay();
               
                timer = timer + 1;
                for (int i = 0; i < 4; i++)
                {
                    listDistance[i]=(float) Math.sqrt(Math.pow(listX[i]+ 30 - personPositionX,2) + Math.pow(listY[i] - 17.5- 568, 2)); 
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
               if (listDistance[1] < 70.0f || listDistance[1] < 70.0f || listDistance[2] < 70.0f || listDistance[3] <  50.0f ) 
                {
                    state = SCREEN_SHAKING; 
                    touchScreen = false;
                    releaseScreen = false;
                    isPersonMoving = false;
                    timer = 0;
                 }
                break;

            case ROBOT_WARPING:
                image(imgBackgroudBack,0,0);  
                imgRobot1.resize(0, 50);
                imgRobot1.loadPixels();
                image(imgRobot1, 10, 25);
                image(imgBackgroundFront,0,0);
                gamePlay();
                timer = timer + 1;  

                if(timer >= 1 && timer < 30)
                {
                     warpingRange = round(timer/5);
                    drawRobotWarping(warpingRange % 3); 
                }
                if(timer == 30)
                {
                    personPositionX = 0; 
                    isPersonMoving = true;
                    state = ROBOT_FROM_FACTORY;
                    timer = 0;
                }
                break;
            case ROBOT_FROM_FACTORY:
                timer = timer + 1;
                image(imgBackgroudBack,0,0);  
                imgRobot1.resize(0, 50);
                imgRobot1.loadPixels();
                image(imgRobot1, 10, 25);
                image(imgBackgroundFront,0,0);
                gamePlay();               
                if(timer >= 0 && timer < 13)
                {  
                   drawRobotRunning(timer % 3);
                   image(imgBackgroundFront,0,0);
                }
                if(timer == 13)
                {
                    timer = 0;
                    drawRobotRunning(0);
                    isPersonMoving = false;
                    state = WAIT_FOR_CLICK;                
                }
                break;

            case SCREEN_SHAKING:  
                timer = timer + 1;
                background (white);
                if(timer >= 10 && timer < 60)
                {
                    drawBackground(); 
                    imgRobot1.resize(0, 50);
                    imgRobot1.loadPixels();
                    image(imgRobot1, 10, 25);
                    textFont(Font1);
                    fill(255, 0, 0);
                    text(score, 100, 78);
                    fallingRange = round(timer/15); 
                    drawRobotFalling(fallingRange % 3);
                }
                if (timer == 60)
                {  
                    drawBackground();
                    imgRobot1.resize(0, 50);
                    imgRobot1.loadPixels();
                    image(imgRobot1, 10, 25);
                    textFont(Font1);
                    fill(255, 0, 0);
                    text(score, 100, 78);
                    state = GAME_OVER;
                    timer = 0;
                }
                break;
            case GAME_OVER:
            image(imgRetry, 0, 0);
            textFont(Font1);
            fill(black);
            textSize(100);
            text(score, 290, 300);
            if (isOnRetry() == true && touchScreen == true)
            {
                image(imgRetryPressed, 0, 0);
                fill(black);
                textSize(100);
                text(score, 290, 300);           
            }
            if (isOnRetry() == true && releaseScreen == true)
            {
                image(imgRetry, 0, 0);
                fill(black);
                textSize(100);
                text(score, 290, 300);    
                state = WAIT_FOR_CLICK;
                score = 0;
                touchScreen = false;
                personPositionX = 130;
            }
            break;
        }
    }
    
    public void gamePlay()
    {
    	
        //distance
        float[]listDistance = new float[4];          
        
        // Move rectangle1 a little bit downward.
        listY[0] = listY[0] + listSpeed[0];
        
        // If rectangle1 goes outside of the screen, reset its position and change its speed.
        if (listY[0] > displayHeight)
        {
            listY[0] = 0;
            listSpeed[0] = 2 + random(10.0f);      
        }
        
        // Move rectangle2 a little bit downward.
        listY[1] = listY[1] + listSpeed[1];
        
        // If rectangle1 goes outside of the screen, reset its position and change its speed.
        if (listY[1] > displayHeight)
        {
            listY[1] = 0;
            listSpeed[1] = 2 +random(10.0f);
        }
        
        // Move rectangle1 a little bit upward.
        listY[2] = listY[2] - listSpeed[2];
        
        // If rectangle1 goes outside of the screen, reset its position and change its speed.
        if (listY[2] < 0)
        {
            listY[2] = displayHeight;
            listSpeed[2] = 2 + random(10.0f);
        }    
        // Move rectangle1 a little bit upward.
        listY[3] = listY[3] - listSpeed[3];
        
        // If rectangle1 goes outside of the screen, reset its position and change its speed.
        if (listY[3] < 0)
        {
            listY[3] = displayHeight;
            listSpeed[3] = 2 + random(10.0f);
        }
               
        // Car1
        imgCarBlue.resize(0, 80);
        imgCarBlue.loadPixels();
        image(imgCarBlue, listX[0], listY[0]);
        
        //Car2
        imgCarPink.resize(0, 80);
        imgCarPink.loadPixels();
        image(imgCarPink, listX[1], listY[1]); 
        
        //Car3
        imgCarGreen.resize(0, 80);
        imgCarGreen.loadPixels();
        image(imgCarGreen, listX[2], listY[2]);
        
        //Car4
        imgCarYellow.resize(0, 80);
        imgCarYellow.loadPixels();
        image(imgCarYellow, listX[3], listY[3]);

        if (isPersonMoving == true)
        {
            personPositionX= personPositionX + 10; 
        }        
              
        //score board
        textFont(Font1);
        fill(255, 0, 0);
        text(score, 100, 78);
        
        //add score
        if (personPositionX == 400 )
        {
            score= score + 1;
        }
    }
    
    public void mousePressed()
    {
        touchScreen = true;
    }
    
    public void mouseReleased()  
    {
       releaseScreen = true;
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
    public void drawBackground()
    {
        image(imgBackgroudBack,0,0);  
        image(imgBackgroundFront,0,0);
        image(imgCarBlue, listX[0], listY[0]);
        image(imgCarPink, listX[1], listY[1]);
        image(imgCarGreen, listX[2], listY[2]);
        image(imgCarYellow, listX[3], listY[3]);
        
    }
    
    
   
   public void drawRobotRunning(int i)
    {
        image(imgRobotRunning[i], personPositionX, personPositionY);
    }
  
   public void drawRobotWarping(int i)
   {
       image(imgWarping[i], personPositionX, personPositionY);
       image(imgWarpingRobot[i], personPositionX, personPositionY);
   }
   
   public void drawRobotFalling(int i)
   {
       image(imgRobotFalling[i], personPositionX, personPositionY);
   }
}  
