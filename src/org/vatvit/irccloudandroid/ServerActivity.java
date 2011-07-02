package org.vatvit.irccloudandroid;

import java.util.ArrayList;

import org.vatvit.irccloud.Channel;
import org.vatvit.irccloud.Client;
import org.vatvit.irccloud.Private;
import org.vatvit.irccloud.Server;
import org.vatvit.irccloud.events.ServerListener;
import org.vatvit.irccloudandroid.ServersActivity.ServerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ServerActivity extends Activity {
	private static final String TAG = "IRCCloudServerActivity";
	private ChannelAdapter channelAdapter;
	private PrivateAdapter privateAdapter;
	private Server server = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Server activity oppened");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);

		IRCCloudApplication app = ((IRCCloudApplication) getApplicationContext());
		Client client = app.getClient();
		Bundle extras = getIntent().getExtras();

		int cid = extras.getInt("cid");
		for (Server serv : client.getServers()) {
			if (serv.getCid() == cid) {
				server = serv;
				break;
			}
		}
		if (server == null) {
			Toast.makeText(app.getApplicationContext(),
					"Server was not found. This should not happen",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.d(TAG, "Server found "+server);
		setTitle(server.getName());
		
		final ListView channelList = (ListView) findViewById(R.id.channelsListView);
		channelAdapter = new ChannelAdapter(this, R.layout.channel_item,
				server.getChannels());
		channelList.setAdapter(channelAdapter);
		channelList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				Channel channel = (Channel)channelList.getItemAtPosition(position);
				Log.d(TAG, "Channel selected "+channel.toString());
				Intent channelIntent = new Intent(v.getContext(),
						ChannelActivity.class);
				channelIntent.putExtra("cid", server.getCid());
				channelIntent.putExtra("chan", channel.getName());
				startActivity(channelIntent);
			}
			
		});

		ListView privateList = (ListView) findViewById(R.id.privateChatsListView);
		privateAdapter = new PrivateAdapter(this, R.layout.channel_item,
				server.getPrivates());
		privateList.setAdapter(privateAdapter);

		final Handler channelRefresh = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				channelAdapter.notifyDataSetChanged();
			}
		};
		final Handler privateRefresh = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				privateAdapter.notifyDataSetChanged();
			}
		};

		server.addServerListener(new ServerListener(){

			@Override
			public void channelRemoved(Channel arg0) {
				channelRefresh.sendEmptyMessage(0);
				
			}

			@Override
			public void newChannel(Channel arg0) {
				channelRefresh.sendEmptyMessage(0);
			}
			
		});
		
	}

	public class ChannelAdapter extends ArrayAdapter<Channel> {
		private ArrayList<Channel> channels;

		public ChannelAdapter(Context context, int textViewResourceId,
				ArrayList<Channel> channels) {
			super(context, textViewResourceId, channels);
			this.channels = channels;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = getLayoutInflater();
			View row;

			if (null == convertView) {
				row = mInflater.inflate(R.layout.channel_item, null);
			} else {
				row = convertView;
			}
			Channel channel = channels.get(position);
			if (channel != null) {
				TextView name = (TextView) row.findViewById(R.id.channelName);
				TextView topic = (TextView) row.findViewById(R.id.topic);
				name.setText(channel.getName());
				if(channel.getTopic()!=null && !channel.getTopic().equalsIgnoreCase("null")) {
					topic.setText(channel.getTopic()+" - by "+channel.getTopicAuthor());
				} else {
					topic.setText("");
				}
			}
			return row;
		}
	}

	public class PrivateAdapter extends ArrayAdapter<Private> {
		private ArrayList<Private> privates;

		public PrivateAdapter(Context context, int textViewResourceId,
				ArrayList<Private> privates) {
			super(context, textViewResourceId, privates);
			this.privates = privates;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = getLayoutInflater();
			View row;

			if (null == convertView) {
				row = mInflater.inflate(R.layout.private_item, null);
			} else {
				row = convertView;
			}
			Private priv = privates.get(position);
			if (priv != null) {

			}
			return row;
		}
	}

}
