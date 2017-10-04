package com.innominds.meetingnotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by sgarimella on 04/10/17.
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private WifiP2PHelper mHelper;

    public WifiBroadcastReceiver(WifiP2PHelper helper) {
        this.mHelper = helper;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                mHelper.setIsWifiP2pEnabled(true);
            } else {
                mHelper.setIsWifiP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mHelper.getManager() != null) {
                mHelper.getManager().requestPeers(mHelper.getChannel(), mHelper);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (mHelper.getManager() == null) {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                mHelper.getManager().requestConnectionInfo(mHelper.getChannel(), mHelper);
            } else {
                Log.v(MeetingNotesActivity.TAG, " :not connected:: ");
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice device = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            Log.v(MeetingNotesActivity.TAG, " ::: " + device.deviceName);
        }
    }
}
