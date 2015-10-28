package edu.cmu.cs.graphics.crowdsim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.vecmath.Point2d;



public class BoidsTest {
	
	
	static String pt(Point2d p){
		return p.x + "," + p.y;
	}
	public static void main(String[] args) throws Exception{
		File fout = new File("../../CrowdSimulation/Assets/Core/corescript");
		
		Point2d r1 = new Point2d(0, -10);
		Point2d r2 = new Point2d(0, 10);
		
		Point2d d1 = new Point2d(0, 20);
		Point2d d2 = new Point2d(0, -20);
		
		double epsilon = 1.0;
		
		PrintStream out = System.out;//new PrintStream(new FileOutputStream(fout));
		
		out.println("1=position:" +pt(r1)+";attacking:false;dead:false;team:red;direction:" + (-1.2) + ";face:happy;");
		out.println("2=position:" +pt(r2)+";attacking:false;dead:false;team:blue;direction:" + (-1.2 + Math.PI) + ";face:happy;");
		out.println("flag-red=position:" + pt(d1) + ";");
		out.println("flag-blue=position:" + pt(d2) + ";");
		out.println("(endframe)");
		
		final double diameter = 1.0;
		int count = 0;
		while(r1.distance(d1) > epsilon && r2.distance(d2) > epsilon){
			count++;
			double playerDist = r1.distance(r2);
			double r1Left = (- 0.1 * Math.abs(r1.x - d1.x)) * 0.05;
			double r2Right = (- 0.1 * Math.abs(r2.x - d2.x)) * 0.05;
			if(playerDist < 3.5){
				r1Left += 1.0/(playerDist) * .1;
				r2Right += 1.0/(playerDist) * .1;
			}
//			double r1Left = (1.0/(playerDist) - 0.1 * r1.distance(d1)) * 0.05;
//			
//			r1Left = r1Left * r1Left;
//			r2Right = r2Right * r2Right;
			
			r1.x -= r1Left;
			r2.x += r2Right;
			
			r1.y += Math.min(0.1, 0.2 * Math.abs(r1.y - d1.y));
			r2.y -= Math.min(0.1, 0.2 * Math.abs(r2.y - d2.y));
			
			out.println("1=position:"+pt(r1)+";");
			out.println("2=position:"+pt(r2)+";");
			out.println("(endframe)");
			
//			if(count > 100) return;
		}
		
		
//		out = new File("../boidsTest2");
		
		
		
	}
}


