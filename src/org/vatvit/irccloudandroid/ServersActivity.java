package org.vatvit.irccloudandroid;

import java.util.ArrayList;

import org.vatvit.irccloud.Client;
import org.vatvit.irccloud.Server;
import org.vatvit.irccloud.events.ServersListener;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ServersActivity extends ListActivity {
	private static final String TAG = "IRCCloudServersActivity";
	private ServerAdapter adapter;
	private Client client;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IRCCloudApplication app = ((IRCCloudApplication) getApplicationContext());
		client = app.getClient();
	
		adapter = new ServerAdapter(this, R.layout.server_item,
				client.getServers());
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				Server server = (Server)getListView().getItemAtPosition(position);
				Log.d(TAG, "Server selected "+server.toString());
				Intent serverIntent = new Intent(v.getContext(),
						ServerActivity.class);
				serverIntent.putExtra("cid", server.getCid());
				startActivity(serverIntent);
			}
			
		});
		
		final Handler hRefresh = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				adapter.notifyDataSetChanged();
			   }
			};


		client.addServerListener(new ServersListener() {

			@Override
			public void connectedToServer(Server server) {
				Log.d(TAG, "Server "+server.toString());
				hRefresh.sendEmptyMessage(0);
				

			}

			@Override
			public void disconnectedFromServer(Server server) {
				Log.d(TAG, "Server disconnect "+server.toString());
				hRefresh.sendEmptyMessage(0);
			}

			@Override
			public void update() {
				Log.d(TAG, "Server update");
				hRefresh.sendEmptyMessage(0);
			}

		});

	}

	public class ServerAdapter extends ArrayAdapter<Server> {
		private ArrayList<Server> servers;

		public ServerAdapter(Context context, int textViewResourceId,
				ArrayList<Server> servers) {
			super(context, textViewResourceId, servers);
			this.servers = servers;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = getLayoutInflater();
			View row;

			if (null == convertView) {
				row = mInflater.inflate(R.layout.server_item, null);
			} else {
				row = convertView;
			}
			Server server = servers.get(position);
			if (server != null) {
				TextView name = (TextView) row.findViewById(R.id.serverName);
				TextView info = (TextView) row.findViewById(R.id.serverInfo);
				name.setText(server.getName());
				info.setText(server.getNick() + "@" + server.getHostname()+" - "+server.getChannels().size()+" channels");
				
			}
			return row;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK && client.isLoggedIn()) {
	        moveTaskToBack(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
