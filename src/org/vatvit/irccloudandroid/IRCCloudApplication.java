package org.vatvit.irccloudandroid;

import org.vatvit.irccloud.Client;

import android.app.Application;

public class IRCCloudApplication extends Application {
	private Client client;

	public IRCCloudApplication() {
		this.client = new Client();
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	
	
}
