package com.example.mypro;


import com.example.mypro.Point;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.location.Location;
import android.renderscript.Sampler.Value;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DrawSurfaceView extends View  {
//Location loc=new Location(-33.870932d, 151.204727d);

	Canvas canvas1;
	Bitmap spot;
	static Point me=new Point(8.558154, 76.880706, "Myloc");//just for initializing .value will change in onlocation changed listener
	static Point u;
	static int cnt=0;
	static double rlat=	me.latitude+Math.random()-0.1;	
	static double rlon=	me.longitude+Math.random()-0.1;	
	Paint mPaint = new Paint();						//setting bitmap and paint
	private Bitmap[] mSpots;
	private double screenWidth, screenHeight = 0d;
	public DrawSurfaceView(Context context, AttributeSet set) {
	    super(context, set);
	    mPaint.setColor(Color.GREEN);
	    mPaint.setTextSize(50);
	    mPaint.setStrokeWidth(DpiUtils.getPxFromDpi(getContext(), 2));
	    mPaint.setAntiAlias(true);
	    mSpots=new Bitmap[1];
	    mSpots[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.play1);//loading play button image
	    
	}
	//mSpots = new Bitmap;
	private double OFFSET = 0d;
	public void setOffset(float offset) {
	    this.OFFSET = offset;
	}
	public void setMyLocation(double latitude, double longitude) {				//setting location from location update
	    me.latitude = latitude;
	    me.longitude = longitude;
	    rlat=	me.latitude+Math.random()-0.1;				//taking a random location wrt current location	
	     rlon=	me.longitude+Math.random()-0.1;	
	  //  Log.d("setting locn","to"+me.latitude);
	}
	//Point me = new Point(-33.870932d, 151.204727d, "Myloc");
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	    super.onSizeChanged(w, h, oldw, oldh);
	    screenWidth = (double) w;
	    screenHeight = (double) h;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		
			canvas1=canvas;

			 spot = mSpots[0];
			//Log.d("Random locn","lat"+rlat);
			 u = new Point(rlat,rlon,"clc");//setting u as a random point
			double dist = distInMetres(me, u);
			Log.d("latitude","lat"+me.latitude);
			Location src = new Location("dummyprovider");;
			src.setLatitude(me.latitude);
			src.setLongitude(me.longitude);
			Location dest=new Location("dummyprovider");;
			dest.setLatitude(8.558154 );
			dest.setLongitude( 76.880706);
			src.distanceTo(dest);
			Log.d("distance","draw"+src.distanceTo(dest));
			
			if(dist > 70)
				dist = 70; //we have set points very far away for demonstration
			
			double angle = bearing(me.latitude, me.longitude, u.latitude, u.longitude) - OFFSET;
			double xPos, yPos;
			
			if(angle < 0)
				angle = (angle+360)%360;
			
			xPos = Math.sin(Math.toRadians(angle)) * dist;
			yPos = Math.sqrt(Math.pow(dist, 2) - Math.pow(xPos, 2));

			if (angle > 90 && angle < 270)
				yPos *= -1;
			
			double posInPx = angle * (screenWidth / 90d);
			
			
			
			
			
			//reuse xPos
			int spotCentreX = spot.getWidth() / 2;
			int spotCentreY = spot.getHeight() / 2;
			xPos = posInPx - spotCentreX;
			
			if (angle <= 45) 
				u.x = (float) ((screenWidth / 2) + xPos);
			
			else if (angle >= 315) 
				u.x = (float) ((screenWidth / 2) - ((screenWidth*4) - xPos));
			
			else
				u.x = (float) (float)(screenWidth*9); //somewhere off the screen
			
			u.y = (float)screenHeight/2 + spotCentreY;
			if(cnt==0)
			{
			canvas1.drawBitmap(spot, u.x, u.y-460, mPaint); //camera spot
			canvas1.drawText(u.description, u.x, u.y-480, mPaint); //text
			}
			else
			{
				  Paint paint = new Paint();
			  paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));		//for clearing bitmap once touched
			  canvas1.drawPaint(paint);
			  paint.setXfermode(new PorterDuffXfermode(Mode.SRC));	
			//canvas1=canvas;
			  //cnt=0;
			}
		}
	//video is played in the on touch event
	public boolean onTouchEvent(MotionEvent event){
	  Log.d("inside","ontouched");
	
	   
		int action = event.getAction();
		Log.d("Action",String.valueOf(action));
	    float x = event.getX();  // or getRawX();
	    float y = event.getY();
	    Log.d("x value", String.valueOf(x));
	    Log.d("y",String.valueOf(y));
	   
	    switch(action){
	    case MotionEvent.ACTION_DOWN:
	        if (x >= u.x && x < (u.x + spot.getWidth())
	                && y >= u.y-460 && y < (u.y + spot.getHeight())) {
	            //tada, if this is true, you've started your click inside your bitmap
	        		Log.d("touched","onbitmap");
	        		cnt++;
	        		MainActivity m = new MainActivity();
	      		   //Toast.makeText(this, "", Toast.LENGTH_LONG);
	      		   m.intent();
	        }
	        break;
	    }
		return false;
	}
	
	//this 2 methods are used to determine where to spot distance on screen
	protected double distInMetres(Point me, Point u) {

		double lat1 = me.latitude;
		double lng1 = me.longitude;

		double lat2 = u.latitude;
		double lng2 = u.longitude;

		double earthRadius = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist * 1000;
	}
	protected static double bearing(double lat1, double lon1, double lat2, double lon2) {
		double longDiff = Math.toRadians(lon2 - lon1);
		double la1 = Math.toRadians(lat1);
		double la2 = Math.toRadians(lat2);
		double y = Math.sin(longDiff) * Math.cos(la2);
		double x = Math.cos(la1) * Math.sin(la2) - Math.sin(la1) * Math.cos(la2) * Math.cos(longDiff);

		double result = Math.toDegrees(Math.atan2(y, x));
		return (result+360.0d)%360.0d;
	}
}
