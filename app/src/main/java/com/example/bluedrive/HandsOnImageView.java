package com.example.bluedrive;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HandsOnImageView extends ImageView {
	public Paint paint;
	private int desiredHeading;
	public RadialTriangle triangles;
	public TrianglePoints currPoints;
	public int triangleLocation[];
	private boolean autoDrive;
	private boolean autoOn;
    private boolean movingDesiredHeading;
	private Point centerPoint;
	private boolean touchClicked;
	public Path pathTriangles;
    public int[] imageAttributes;
    public static final int X=0;
    public static final int Y=1;
    public static final int WIDTH=2;
    public static final int HEIGHT=3;
    public static final int IMAGE_WIDTH=428;
    public static final int IMAGE_HEIGHT=428;


	/**
	 * @param context
	 */

	public HandsOnImageView(Context context) {
		super(context);
        setBackgroundColor(0xFFFFFF);
        initImage();
	}

    /**
     * @param context
     * @param attrs
     */
    public HandsOnImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImage();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public HandsOnImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initImage();
    }

	public void initImage(){
        paint = new Paint(Paint.LINEAR_TEXT_FLAG);
        imageAttributes = getBitmapPositionInsideImageView(this);
        centerPoint = new Point(adjustX(214), adjustY(214));
        triangles = new RadialTriangle(new Point(centerPoint.x, centerPoint.y),calculateScale(imageAttributes));
        currPoints = triangles.getTriangles(270);
        desiredHeading = 0;
        triangleLocation = new int[6];
        triangleLocation[TrianglePoints.BIG_TRIANGLE] = 45;
        triangleLocation[TrianglePoints.BIG_TRIANGLE_TAIL] = 225;
        triangleLocation[TrianglePoints.MED_TRIANGLE] = 90;
        triangleLocation[TrianglePoints.MED_TRIANGLE_TAIL] = 270;
        triangleLocation[TrianglePoints.SMALL_TRIANGLE] = 0;
        triangleLocation[TrianglePoints.SMALL_TRIANGLE_TAIL] = 180;
        desiredHeading = triangleLocation[TrianglePoints.MED_TRIANGLE];
        autoDrive = false;
        movingDesiredHeading = false;
        //get bitmap position
        touchClicked = false;
        autoOn = false;
    }

	@Override
	protected void onDraw(Canvas canvas) {

		// TODO Auto-generated method stub
		super.onDraw(canvas);
        paintAllAreas(canvas);
	}
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		Point touchedPoint = new Point((int) event.getX(), (int) event.getY());
		if (!autoOn
				&& distance(touchedPoint, this.triangles.getTriangle(
						this.triangleLocation[TrianglePoints.MED_TRIANGLE],
						RadialTriangle.TriangleType.CIRCLE)) < 100) {
            movingDesiredHeading = true;
			this.invalidate();

			return true;
			// Log.d("TouchTest", "Touch down");
		}
		if (action == MotionEvent.ACTION_DOWN) {
			touchClicked = true;
		} else if (action == MotionEvent.ACTION_MOVE) {
            if(movingDesiredHeading){
                this.triangleLocation[TrianglePoints.MED_TRIANGLE] = triangles.getAngleToNorth(touchedPoint);
                this.desiredHeading = this.triangleLocation[TrianglePoints.MED_TRIANGLE];
                this.triangleLocation[TrianglePoints.MED_TRIANGLE_TAIL] = triangles
                        .getOppisiteAngle(this.triangleLocation[TrianglePoints.MED_TRIANGLE]);
            }

		} else if (action == MotionEvent.ACTION_UP) {
			if (touchClicked) {
				// touch press complete, show toast
				if (distance(touchedPoint, centerPoint) < 50) {
					if (autoDrive) {
						autoDrive = false;
					} else {
						autoDrive = true;
					}
					this.invalidate();
				}
				touchClicked = false;
			}

            movingDesiredHeading = false;
			return true;
		}
		// TODO Auto-generated method stub
		Log.d("Hello Android", "Got a touch event: " + event.getAction());
		// return super.onTouchEvent(event);
		return false;

	}

	public int distance(Point one, Point two) {
		return (int) Math.round(Math.sqrt(((one.x - two.x) * (one.x - two.x))
				+ ((one.y - two.y) * (one.y - two.y))));
	}
	public void paintAllAreas(Canvas canvas){
        paintTextAreas(canvas);
        paintTriangles(canvas);
    }
	public void paintTextAreas(Canvas canvas){
        paint.setTextSize(adjustX(46));
        paint.setAntiAlias(true);
        //draw status letter indicator
        if (autoOn) {
            paint.setColor(android.graphics.Color.argb(150, 240, 225, 17));
            canvas.drawText("A", adjustX(203), adjustY(232), paint);
        } else {
            paint.setColor(android.graphics.Color.argb(215, 230, 0, 0));
            canvas.drawText("P", adjustX(203), adjustY(232), paint);
        }
        //set color for borders
        paint.setColor(android.graphics.Color.argb(255,180,90,8));
        canvas.drawRect(0,0, adjustX(48), adjustY(78),paint);
        //draw box for gps/curr/desired display area
        paint.setColor(android.graphics.Color.argb(255, 255, 250, 180));
        paint.setStrokeWidth(0);
        canvas.drawRect(adjustX(2),adjustY(2),adjustX(46),adjustY(26),paint);
        canvas.drawRect(adjustX(2),adjustY(28),adjustX(46),adjustY(50),paint);
        canvas.drawRect(adjustX(2),adjustY(52),adjustX(46),adjustY(76),paint);

        //set text size for gps/curr/desired number display
        paint.setTextSize(adjustX(22));
        //Draw heading values
        paint.setColor(android.graphics.Color.argb(255, 0, 0, 150));
        canvas.drawText(triangleLocation[TrianglePoints.BIG_TRIANGLE]+"",adjustX(5),adjustY(23),paint);
        paint.setColor(android.graphics.Color.argb(255, 0, 120, 0));
        canvas.drawText(triangleLocation[TrianglePoints.MED_TRIANGLE]+"",adjustX(5),adjustY(48),paint);
        paint.setColor(android.graphics.Color.BLACK);
        canvas.drawText(triangleLocation[TrianglePoints.SMALL_TRIANGLE]+"",adjustX(5),adjustY(73),paint);



    }

	public void paintTriangles(Canvas canvas) {
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);

        for(int i=0;i<5;i+=2) {
            switch(i){
                case TrianglePoints.BIG_TRIANGLE:
                    // draw big blue triangle and tail for DIGITAL COMPASS HEADING
                    paint.setColor(android.graphics.Color.argb(255, 0, 0, 150));
                    currPoints = triangles.getTriangles(triangleLocation[TrianglePoints.BIG_TRIANGLE]);
                    break;
                case TrianglePoints.MED_TRIANGLE:
                    //draw desired heading in green
                    paint.setColor(android.graphics.Color.argb(255, 0, 120, 0));
                    currPoints = triangles.getTriangles(triangleLocation[TrianglePoints.MED_TRIANGLE]);
                    canvas.drawCircle(currPoints.getCircle().x, currPoints.getCircle().y,
                            adjustX(15), paint);
                    break;
                case TrianglePoints.SMALL_TRIANGLE:
                    //draw gps heading in black
                    paint.setColor(android.graphics.Color.BLACK);
                    currPoints = triangles.getTriangles(triangleLocation[TrianglePoints.SMALL_TRIANGLE]);
                    break;
            }
            pathTriangles = new Path();
            pathTriangles.setFillType(Path.FillType.EVEN_ODD);
            pathTriangles.moveTo(currPoints.getCircleEdgeNeg().x,
                    currPoints.getCircleEdgeNeg().y);
            pathTriangles.lineTo(currPoints.getCircleEdgePos().x,
                    currPoints.getCircleEdgePos().y);
            pathTriangles.lineTo(currPoints.getTrianglePoint(i).x,
                    currPoints.getTrianglePoint(i).y);
            pathTriangles.lineTo(currPoints.getCircleEdgeNeg().x,
                    currPoints.getCircleEdgeNeg().y);
            pathTriangles.close();
            canvas.drawPath(pathTriangles, paint);

            //tail
            if(i!=TrianglePoints.SMALL_TRIANGLE) {
                currPoints = triangles.getTriangles(triangleLocation[i+1]);
                pathTriangles = new Path();
                pathTriangles.setFillType(Path.FillType.EVEN_ODD);
                pathTriangles.moveTo(currPoints.getCircleEdgeNeg().x,
                        currPoints.getCircleEdgeNeg().y);
                pathTriangles.lineTo(currPoints.getCircleEdgePos().x,
                        currPoints.getCircleEdgePos().y);
                pathTriangles.lineTo(currPoints.getSmallTriangle().x,
                        currPoints.getSmallTriangle().y);
                pathTriangles.lineTo(currPoints.getCircleEdgeNeg().x,
                        currPoints.getCircleEdgeNeg().y);
                pathTriangles.close();
                canvas.drawPath(pathTriangles, paint);
            }

        }

	}

	public int getTriangleLocation(int location) {
		return triangleLocation[location];
	}

	public void setTriangleLocation(int location, int degrees) {
		this.triangleLocation[location] = degrees;
	}

	public void setDigitalCompass(int degrees) {
		setTriangleLocation(0, degrees);
		setTriangleLocation(1, this.triangles.getOppisiteAngle(degrees));
    }

	public int getDesiredHeading() {
		return desiredHeading;
	}

	public void setDesiredHeading(int desiredHeading) {
		setTriangleLocation(2, desiredHeading);
		setTriangleLocation(3, desiredHeading);
		this.desiredHeading = desiredHeading;
    }
    public int getGPSCorrectedHeading(){
        int difference=degreeDifferance(false,desiredHeading,getGPSBearing());
        return handleLoop(desiredHeading+difference);
    }
	public boolean isAutoDrive() {
		return autoDrive;
	}

	public void setAutoDrive(boolean autoDrive) {
		this.autoDrive = autoDrive;
	}

	public void setGPSBearing(int bearing) {
		setTriangleLocation(4, bearing);
		// setTriangleLocation(5, this.triangles.getOppisiteAngle(bearing));
	}
    public int getGPSBearing(){
        return triangleLocation[TrianglePoints.SMALL_TRIANGLE];
    }

	public boolean isAutoOn() {
		return autoOn;
	}

	public void setAutoOn(boolean autoOn) {
		this.autoOn = autoOn;
	}
    int degreeDifferance(boolean absoluteValue,int value,int minus){
        int compDiff=0;
        boolean accrossN=false;
        compDiff=Math.abs(value-minus);
        if(compDiff>180){
            compDiff=Math.abs(compDiff-360);
            accrossN=true;
        }
        if(absoluteValue || ((value>minus && !accrossN) || (accrossN && value<minus))){
            return compDiff;
        }
        else{
            return (-1*compDiff);
        }
    }
    private int handleLoop(int degrees) {
        if (degrees > 359) {
            degrees -= 360;
        } else if (degrees < 0) {
            degrees += 360;
        }
        return degrees;
    }
	/**
	 * Returns the bitmap position inside an imageView.
	 * @param imageView source ImageView
	 * @return 0: left, 1: top, 2: width, 3: height
	 */
	public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
		int[] ret = new int[4];

		if (imageView == null || imageView.getDrawable() == null)
			return ret;

		// Get image dimensions
		// Get image matrix values and place them in an array
		float[] f = new float[9];
		imageView.getImageMatrix().getValues(f);

		// Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
		final float scaleX = f[Matrix.MSCALE_X];
		final float scaleY = f[Matrix.MSCALE_Y];

		// Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
		final Drawable d = imageView.getDrawable();
		final int origW = d.getIntrinsicWidth();
		final int origH = d.getIntrinsicHeight();

		// Calculate the actual dimensions
		final int actW = Math.round(origW * scaleX);
		final int actH = Math.round(origH * scaleY);

		ret[2] = actW;
		ret[3] = actH;

		// Get image position
		// We assume that the image is centered into ImageView
		int imgViewW = imageView.getWidth();
		int imgViewH = imageView.getHeight();

		int top = (int) (imgViewH - actH)/2;
		int left = (int) (imgViewW - actW)/2;

		ret[0] = left;
		ret[1] = top;

		return ret;
	}
	private int adjustX(int cordinate){
        float newCoridinate  = ((float)cordinate)*(((float)(imageAttributes[WIDTH]))/((float)IMAGE_WIDTH));
        return Math.round(newCoridinate);

    }
    private int adjustY(int cordinate){
        float newCoridinate  = ((float)cordinate)*(((float)(imageAttributes[HEIGHT]))/((float)IMAGE_HEIGHT));
        return Math.round(newCoridinate);
    }
    private float calculateScale(int[] attributes){
        return (((float)(attributes[WIDTH]))/((float)IMAGE_WIDTH));
    }
}