package org.vatvit.irccloudandroid;

import org.json.JSONObject;
import org.vatvit.irccloud.Connection;
import org.vatvit.irccloud.events.EventListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private static final String TAG = "IRCCloudMainActivity";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText emailField = (EditText) findViewById(R.id.emailField);
        final EditText passwordField = (EditText) findViewById(R.id.passwordField);
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Connection ircConn = new Connection(emailField.getText().toString(), passwordField.getText().toString());
        		
        		ircConn.addEventListener(new EventListener(){
        			public void onEvent(JSONObject event) {
        				Log.v(TAG, "Event: "+event.toString());
        			}	
        		});
        		
        		if(ircConn.login()) {
        			Log.v(TAG, "Login successful. Session: "+ircConn.getSession());
        		} else {
        			Log.v(TAG, "Login failed.");
        		}
  
            }
        });
 
    }
}