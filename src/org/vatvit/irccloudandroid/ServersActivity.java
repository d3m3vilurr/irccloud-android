package org.vatvit.irccloudandroid;

import org.vatvit.irccloud.Client;

import android.app.Activity;
import android.os.Bundle;

public class ServersActivity extends Activity {
	
	private Client client;
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.servers);
	        
	}
}
