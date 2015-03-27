package com.apollottb.android;

import processing.core.PApplet;
import processing.core.PImage;

public class Robot extends DisplayObject
{
	public int frameNumber;
	
	private PImage[] animRunning;
	private PImage[] animWarping;
	private PImage[] animBreaking;
	private PImage imgWarpBottom;
	private PImage imgWarpTop;
	
	
	public Robot(PApplet applet, Transformation screenTransformation, String imgFileName)
	{
		super(applet, screenTransformation, imgFileName);
		
		frameNumber = 0;
	}
	
	
	public void setAnimRunning(String[] fileNames)
	{
		animRunning = getAnim(fileNames);
	}
	
	
	public void setAnimWarping(String[] fileNames)
	{
		animWarping = getAnim(fileNames);
	}
	
	
	public void setAnimBreaking(String[] fileNames)
	{
		animBreaking = getAnim(fileNames);
	}
	
	
	public void setImagesWarp(String bottomFileName, String topFileName)
	{
		imgWarpBottom = applet.loadImage(bottomFileName);
		imgWarpTop = applet.loadImage(topFileName);
		
		resizeHelper(imgWarpBottom, screenTransformation.scale);
		resizeHelper(imgWarpTop, screenTransformation.scale);
	}
	
	
	public void tick()
	{
		frameNumber ++;
	}
	
	public void drawRunning()
	{
		applet.image(animRunning[frameNumber % 3], getScreenX(x), getScreenY(y));
	}
	
	
	public void drawBreaking()
	{
		
		applet.image(animBreaking[PApplet.floor(frameNumber / 20) % 3], getScreenX(x), getScreenY(y));
	}
	
	
	public void drawWarping()
	{
		int f = PApplet.floor(frameNumber / 8) % 3;
		int[] opacity = {255, 126, 63};
		
		applet.image(imgWarpBottom, getScreenX(x), getScreenY(y));
		applet.tint(255, opacity[f]);
		applet.image(animWarping[f], getScreenX(x), getScreenY(y - f*3));
		applet.noTint();
		applet.image(imgWarpTop, getScreenX(x), getScreenY(y));
	}
	
	
	@Override
	public void resize(float scale)
	{
		super.resize(scale);
		
		resizeAnim(animRunning, scale);
		resizeAnim(animWarping, scale);
		resizeAnim(animBreaking, scale);

		if (imgWarpBottom != null) resizeHelper(imgWarpBottom, scale);
		if (imgWarpTop != null) resizeHelper(imgWarpTop, scale);
	}
	
	
	private void resizeAnim(PImage[] images, float scale)
	{
		if (images != null)
		{
			for (PImage img : images)
			{
				resizeHelper(img, scale);
			}
		}
	}
	
	
	private PImage[] getAnim(String[] frameFileNames)
	{
		int frameCount = frameFileNames.length;
		PImage[] frames = new PImage[frameCount];
		
		for (int i = 0; i < frameCount; ++i)
		{
			frames[i] = applet.loadImage(frameFileNames[i]);
			resizeHelper(frames[i], screenTransformation.scale);
		}
		
		return frames;
	}
}
