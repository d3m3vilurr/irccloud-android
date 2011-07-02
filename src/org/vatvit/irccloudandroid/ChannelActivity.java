package org.vatvit.irccloudandroid;

import java.util.ArrayList;

import org.vatvit.irccloud.Channel;
import org.vatvit.irccloud.Client;
import org.vatvit.irccloud.Message;
import org.vatvit.irccloud.Server;
import org.vatvit.irccloud.events.ChannelListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChannelActivity extends Activity {
	private static final String TAG = "IRCCloudChannelActivity";
	private Server server;
	private Channel channel;
	private MessageAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel);
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
		Log.d(TAG, "Server found from channel "+server);
		
		String chanName = extras.getString("chan");
		for (Channel chan : server.getChannels()) {
			if (chan.getName().equalsIgnoreCase(chanName)) {
				channel = chan;
				break;
			}
		}
		if (channel == null) {
			Toast.makeText(app.getApplicationContext(),
					"Channel was not found. This should not happen",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.d(TAG, "Channel found "+channel);
		

		setTitle(channel.getName()+(channel.getTopic()!=null && !channel.getTopic().equalsIgnoreCase("null")?" - "+channel.getTopic()+" - by "+channel.getTopicAuthor():""));
	
		
		ListView messages = (ListView) findViewById(R.id.messages);
		adapter = new MessageAdapter(this, R.layout.message_item,
				channel.getMessages());
		messages.setAdapter(adapter);

		final Handler refresh = new Handler() {
			public void handleMessage(Message msg) {
				adapter.notifyDataSetChanged();
			}
		};
		

		channel.addChannelListener(new ChannelListener(){
			@Override
			public void newMessage(Message message) {
				Log.d(TAG, "New message "+message.getMsg()+" message count is "+channel.getMessages().size());
				
				refresh.sendEmptyMessage(0);
			}
			
		});
	
	
	}
	
	public class MessageAdapter extends ArrayAdapter<Message> {
		private ArrayList<Message> messages;

		public MessageAdapter(Context context, int textViewResourceId,
				ArrayList<Message> messages) {
			super(context, textViewResourceId, messages);
			this.messages = messages;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = getLayoutInflater();
			View row;

			if (null == convertView) {
				row = mInflater.inflate(R.layout.message_item, null);
			} else {
				row = convertView;
			}
			Message message = messages.get(position);
			if (message != null) {
				TextView by = (TextView) row.findViewById(R.id.by);
				TextView time = (TextView) row.findViewById(R.id.time);
				TextView msg = (TextView) row.findViewById(R.id.message);
				by.setText(message.getFrom());
				msg.setText(message.getMsg());
				time.setText(message.getTime());
				
			}
			return row;
		}
	}

}
