package com.example.mypro;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class Videoplay extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoplay);
VideoView videoView =(VideoView)findViewById(R.id.videoView2);  
        
        //Creating MediaController  
MediaController mediaController= new MediaController(this);  
    mediaController.setAnchorView(videoView);          
 
      //specify the location of media file  
       String d=getIntent().getExtras().getString("distance");
       Log.d("distanceintent","intent"+ d);
       String uriPath;
       uriPath = "android.resource://com.example.mypro/raw/aot";
       if(d!=null)
       {
       if(Integer.parseInt(d)>1000)
    	   uriPath = "android.resource://com.example.mypro/raw/aot1";	//if distance from clc is >1 km play another video
       }
      
  
   Uri uri = Uri.parse(uriPath);
   //video.setVideoURI(uri);
      //Setting MediaController and URI, then starting the videoView  
   videoView.setMediaController(mediaController);  
   videoView.setVideoURI(uri);          
   videoView.requestFocus();  
   videoView.start(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.videoplay, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
}
