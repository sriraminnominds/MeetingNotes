package com.innominds.meetingnotes.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.innominds.meetingnotes.MeetingNotesActivity;

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

    private WifiP2pInfo mConnectionInfo;
    private WifiP2pDevice mThisDevice;
    private List<WifiP2pDevice> mPeerDevices = new ArrayList<WifiP2pDevice>();

    private DeviceActionListener mListener;

    public WifiP2PHelper(Context context, DeviceActionListener listener) {
        this.mListener = listener;
        initialiseP2p(context);
    }

    private void initialiseP2p(Context context) {
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
        Log.v(MeetingNotesActivity.TAG, "Channel Disconnected.");
        if (mListener != null) {
            mListener.onDisconnect();
        }
    }

    public WifiP2pManager getManager() {
        return mManager;
    }

    public WifiP2pManager.Channel getChannel() {
        return mChannel;
    }

    public List<WifiP2pDevice> getPeerDevices() {
        return mPeerDevices;
    }

    public IntentFilter getIntentFilter() {
        return mIntentFilter;
    }

    public void setIsWifiP2pEnabled(boolean enabled) {
        this.mIsWifiP2pEnabled = enabled;
    }

    public WifiP2pDevice getThisDevice() {
        return mThisDevice;
    }

    public void setThisDevice(WifiP2pDevice device) {
        this.mThisDevice = device;
    }

    public WifiP2pInfo getConnectionInfo() {
        return mConnectionInfo;
    }

    public void discoverPeers() {
        if (mIsWifiP2pEnabled) {
            mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.v(MeetingNotesActivity.TAG, "Discovery Successful.");
                }

                @Override
                public void onFailure(int reasonCode) {
                    Log.v(MeetingNotesActivity.TAG, "Discovery Failed." + reasonCode);
                }
            });
        } else {
            Log.v(MeetingNotesActivity.TAG, "P2p is not enabled on this phone.");
        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo connectionInfo) {
        this.mConnectionInfo = connectionInfo;
        Log.v(MeetingNotesActivity.TAG, "Connection Info." + connectionInfo.toString());
        if (mListener != null) {
            mListener.onConnection(mConnectionInfo);
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        mPeerDevices.clear();
        mPeerDevices.addAll(peerList.getDeviceList());
        Log.v(MeetingNotesActivity.TAG, "Peer List." + peerList.toString());
        if (mListener != null) {
            mListener.onPeersAvailable(mPeerDevices);
        }
    }

    public interface DeviceActionListener {
        void onConnection(WifiP2pInfo config);

        void onPeersAvailable(List<WifiP2pDevice> list);

        void onDisconnect();
    }
}
