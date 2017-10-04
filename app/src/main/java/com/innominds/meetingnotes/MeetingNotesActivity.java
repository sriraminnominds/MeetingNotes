package com.innominds.meetingnotes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.innominds.meetingnotes.wifi.WifiBroadcastReceiver;
import com.innominds.meetingnotes.wifi.WifiP2PHelper;

public class MeetingNotesActivity extends BaseActivity {
    private WifiBroadcastReceiver mReceiver;
    private WifiP2PHelper mP2pHelper;

    public static final String TAG = "MeetingNotesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_notes);

        mP2pHelper = new WifiP2PHelper();
        mP2pHelper.initialiseP2p(this);
        //loadFragment(R.id.frame_container, new DeviceListFragment());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WifiBroadcastReceiver(mP2pHelper);
        registerReceiver(mReceiver, mP2pHelper.getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.discover_peers:
                mP2pHelper.discoverPeers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
