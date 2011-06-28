package org.vatvit.irccloudandroid;

import org.json.JSONObject;
import org.vatvit.irccloud.Client;
import org.vatvit.irccloud.Connection;
import org.vatvit.irccloud.events.EventListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "IRCCloudMainActivity";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	MainActivity self = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText emailField = (EditText) findViewById(R.id.emailField);
        final EditText passwordField = (EditText) findViewById(R.id.passwordField);
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
            	
            	
            	final Client client = new Client(emailField.getText().toString(), passwordField.getText().toString());
            	if(client.login()) {
        			Log.v(TAG, "Login successful.");
        			Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
        			Intent serversIntent = new Intent(v.getContext(), ServersActivity.class);
        			startActivity(serversIntent);
            	} else {
        			Log.v(TAG, "Login failed.");
        			Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
        		}
    			
        		

            }
        });
 
    }
}