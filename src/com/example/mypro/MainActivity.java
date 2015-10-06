package com.example.mypro;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {
	public double d;
	private DrawSurfaceView mDrawView;
	private static boolean DEBUG = false;
	private SensorManager mSensorManager;
	LocationManager locMgr;
	private Sensor mSensor;
	VideoView videoView;
	static boolean flag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		locMgr = (LocationManager) this.getSystemService(LOCATION_SERVICE); // <2>
		final LocationProvider high = locMgr.getProvider(locMgr.getBestProvider(LocationUtils.createFineCriteria(), true));
		//Location location = locationManager.getLastKnownLocation(provider);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawView =  (DrawSurfaceView)findViewById(R.id.drawSurfaceView);
		/*VideoView videoView =(VideoView)findViewById(R.id.videoView1);  
        
        //Creating MediaController  
MediaController mediaController= new MediaController(this);  
    mediaController.setAnchorView(videoView);          
 
      //specify the location of media file  
           
   String uriPath = "android.resource://com.example.mypro/raw/aot";
   Uri uri = Uri.parse(uriPath);
   //video.setVideoURI(uri);
      //Setting MediaController and URI, then starting the videoView  
   videoView.setMediaController(mediaController);  
   videoView.setVideoURI(uri);          
   videoView.requestFocus();  
   videoView.start();  */
		locMgr.requestLocationUpdates(high.getName(), 0, 0f, new LocationListener() {

		   
		    public void onStatusChanged(String s, int i, Bundle bundle) {

		    }

		    public void onProviderEnabled(String s) {
		        // try switching to a different provider
		    }

		    public void onProviderDisabled(String s) {
		        // try switching to a different provider
		    }

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				Log.d("location","updated");
				mDrawView.setMyLocation(location.getLatitude(), location.getLongitude());
				mDrawView.invalidate();
				Location l = new Location("dummyprovider");
				l.setLatitude(8.5656);
				l.setLongitude( 76.8747);
				d=l.distanceTo(location);
			Log.d("distance",String.valueOf(l.distanceTo(location)));	//calculating distance btween clc and current location
				//location.distanceTo(dest)
			//	Log.d("d","loc"+location.getLatitude()+","+location.getLongitude());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
		}*/
		return super.onOptionsItemSelected(item);
	}
	public void intent()
	{
		Log.d("in"," Main Activity");
		flag=true;
		
		//Toast.makeText(getApplicationContext(), "dd", Toast.LENGTH_SHORT).show();
		/*Intent i = new Intent(MainActivity.this,Videoplay.class);
		startActivity(i);*/
	}
	
	private final SensorEventListener mListener = new SensorEventListener() {
		   
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }

		@Override
		public void onSensorChanged(SensorEvent event) {			//to use the device compass
			// TODO Auto-generated method stub
			if(flag==true){
				Intent in=new Intent(MainActivity.this,Videoplay.class);	//If the play image is clicked flag will be true
				in.putExtra("distance", d);
				startActivity(in);
				flag=false;
			}
			//Log.d("compass", "sensorChanged (" + event.values[0] + ", " + event.values[1]);
			if (mDrawView != null) {
	            mDrawView.setOffset(event.values[0]);
	            mDrawView.invalidate();
	        }
		}
	};
	@Override
	protected void onResume() {
		if (DEBUG)
			Log.d("TAG", "onResume");
		super.onResume();

		mSensorManager.registerListener(mListener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		if (DEBUG)
			Log.d("TAG", "onStop");
		mSensorManager.unregisterListener(mListener);
		super.onStop();
	}
}
