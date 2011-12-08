package org.vatvit.irccloudandroid;

import org.vatvit.irccloud.Client;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "IRCCloudMainActivity";
	private Client client;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MainActivity self = this;
		final IRCCloudApplication app = ((IRCCloudApplication) getApplicationContext());
		client = app.getClient();
		
		if(client.isLoggedIn()) {
			Intent serversIntent = new Intent(this.getBaseContext(),
					ServersActivity.class);
			startActivity(serversIntent);
			return;
		}
		
		setContentView(R.layout.main);

		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final Button loginButton = (Button) findViewById(R.id.loginButton);
		final EditText emailField = (EditText) findViewById(R.id.emailField);
		final EditText passwordField = (EditText) findViewById(R.id.passwordField);

		String email = pref.getString("email", "");
		String password = pref.getString("password", "");
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				Toast.makeText(app.getApplicationContext(), "Trying to login",
						Toast.LENGTH_SHORT).show();
				(new Thread() {
					@Override
					public void run() {
						Editor e = pref.edit();
						String email = emailField.getText().toString().trim();
						String password = passwordField.getText().toString().trim();
						e.putString("email", email);
						
						if (client.login(email, password)) {
							Log.d(TAG, "Login successful.");
							e.putString("password", password);
							Intent serversIntent = new Intent(v.getContext(),
									ServersActivity.class);
							startActivity(serversIntent);
						} else {
							Log.d(TAG, "Login failed.");
							e.putString("password", "");
							Toast.makeText(app.getApplicationContext(),
									"Login failed", Toast.LENGTH_SHORT).show();
						}
						e.commit();
					}
				}).run();

			}
		});
		
		if (!(email.equals("") || password.equals(""))) {
			loginButton.performClick();
		} else {
			emailField.setText(email);
			passwordField.setText(password);
		}
	}
	
}