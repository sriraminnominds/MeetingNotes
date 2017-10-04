package com.innominds.meetingnotes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MeetingNotesActivity extends BaseActivity {
    private WifiBroadcastReceiver mReceiver;
    private WifiP2PHelper mHelper;

    public static final String TAG = "MeetingNotesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_notes);

        mHelper = new WifiP2PHelper();
        mHelper.initialiseP2p(this);
        //loadFragment(R.id.frame_container, new DeviceListFragment());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WifiBroadcastReceiver(mHelper);
        registerReceiver(mReceiver, mHelper.getIntentFilter());
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
                mHelper.discoverPeers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
