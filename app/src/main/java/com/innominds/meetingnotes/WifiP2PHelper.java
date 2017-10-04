package com.innominds.meetingnotes;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgarimella on 04/10/17.
 */

public class WifiP2PHelper implements WifiP2pManager.ChannelListener, WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private boolean mIsWifiP2pEnabled = false;
    private IntentFilter mIntentFilter;

    private List<WifiP2pDevice> mPeers = new ArrayList<WifiP2pDevice>();

    public void initialiseP2p(Context context) {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(context, context.getMainLooper(), this);
    }

    @Override
    public void onChannelDisconnected() {

    }

    public WifiP2pManager getManager() {
        return mManager;
    }

    public WifiP2pManager.Channel getChannel() {
        return mChannel;
    }

    public List<WifiP2pDevice> getPeers() {
        return mPeers;
    }

    public IntentFilter getIntentFilter() {
        return mIntentFilter;
    }

    public void setIsWifiP2pEnabled(boolean enabled) {
        this.mIsWifiP2pEnabled = enabled;
    }

    public void discoverPeers() {
        if (mIsWifiP2pEnabled) {
            mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int reasonCode) {
                }
            });
        } else {
        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        mPeers.clear();
        mPeers.addAll(peerList.getDeviceList());
    }
}
