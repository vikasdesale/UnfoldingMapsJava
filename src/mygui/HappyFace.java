package mygui;
import processing.core.PApplet;
import processing.core.PImage;

public class HappyFace extends PApplet{

	public void setup(){
		size(400,450);
		background(200,200,600);
		
	}
	public void draw(){
	
		fill(255,255,0);
		ellipse(200,200,390,390);
	    fill(0,0,0);
	    ellipse(100,130,50,70);
	    ellipse(280,130,50,70);
	    arc(200,280,75,75,0,PI);
	    fill(255,0,0);
	    textSize(24);

	    text("Created by Vikas Desale", 10, 430);
	}
	
}
