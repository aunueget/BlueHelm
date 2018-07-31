package com.example.bluedrive;
import android.graphics.Point;
public class TrianglePoints {
	private Point bigTriangle;
	private Point medTriangle;
	private Point smallTriangle;
	private Point circleEdgeNeg;
	private Point circleEdgePos;
	private Point circle;
	public static final int BIG_TRIANGLE=0;
	public static final int BIG_TRIANGLE_TAIL=1;
	public static final int MED_TRIANGLE=2;
	public static final int MED_TRIANGLE_TAIL=3;
	public static final int SMALL_TRIANGLE=4;
	public static final int SMALL_TRIANGLE_TAIL=5;



	public TrianglePoints(Point big,Point med,Point small,Point circ,Point neg,Point pos){
		bigTriangle=big;
		this.medTriangle=med;
		smallTriangle=small;
		circleEdgeNeg=neg;
		circleEdgePos=pos;
		circle=circ;
	}
	public Point getBigTriangle() {
		return bigTriangle;
	}
	public void setBigTriangle(Point bigTriangle) {
		this.bigTriangle = bigTriangle;
	}
	public Point getSmallTriangle() {
		return smallTriangle;
	}
	public void setSmallTriangle(Point smallTriangle) {
		this.smallTriangle = smallTriangle;
	}
	public Point getCircleEdgeNeg() {
		return circleEdgeNeg;
	}
	public void setCircleEdgeNeg(Point circleEdgeNeg) {
		this.circleEdgeNeg = circleEdgeNeg;
	}
	public Point getCircleEdgePos() {
		return circleEdgePos;
	}
	public void setCircleEdgePos(Point circleEdgePos) {
		this.circleEdgePos = circleEdgePos;
	}
	public Point getMedTriangle() {
		return medTriangle;
	}
	public void setMedTriangle(Point medTriangle) {
		this.medTriangle = medTriangle;
	}
	public Point getCircle() {
		return circle;
	}
	public void setCircle(Point circle) {
		this.circle = circle;
	}
	public Point getTrianglePoint(int triangle){
		switch (triangle){
			case BIG_TRIANGLE:
				return getBigTriangle();
			case MED_TRIANGLE:
				return getMedTriangle();
			case SMALL_TRIANGLE:
				return getSmallTriangle();
		}
		return new Point(0,0);
	}

}
