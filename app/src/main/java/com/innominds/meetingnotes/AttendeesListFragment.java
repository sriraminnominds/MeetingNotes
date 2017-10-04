package com.innominds.meetingnotes;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.innominds.meetingnotes.wifi.WifiP2PHelper;

import java.util.List;

/**
 * Created by sgarimella on 04/10/17.
 */

public class AttendeesListFragment extends ListFragment implements WifiP2PHelper.DeviceActionListener {
    private AttendeesListAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendees_list, null);
        initialiseViews(view);
        return view;
    }

    private void initialiseViews(View v) {
        mListAdapter = new AttendeesListAdapter(getActivity(), R.layout.view_row_attendee, ((MeetingNotesActivity) getActivity()).getP2pHelper().getPeerDevices());
        this.setListAdapter(mListAdapter);
    }

    @Override
    public void onConnection(WifiP2pInfo config) {

    }

    @Override
    public void onPeersAvailable(List<WifiP2pDevice> list) {
        mListAdapter.setItems(list);
    }

    @Override
    public void onDisconnect() {

    }

    private class AttendeesListAdapter extends ArrayAdapter<WifiP2pDevice> {
        private List<WifiP2pDevice> items;

        public AttendeesListAdapter(Context context, int textViewResourceId, List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;
        }

        public void setItems(List<WifiP2pDevice> list) {
            this.items = list;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.view_row_attendee, null);
            }
            WifiP2pDevice device = items.get(position);
            if (device != null) {
                TextView top = (TextView) v.findViewById(R.id.device_name);
                TextView bottom = (TextView) v.findViewById(R.id.device_details);
                if (top != null) {
                    top.setText(device.deviceName);
                }
                if (bottom != null) {
                    bottom.setText(getDeviceStatus(device.status));
                }
            }
            return v;
        }
    }

    private static String getDeviceStatus(int deviceStatus) {
        Log.v(MeetingNotesActivity.TAG, "Peer status :" + deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";
        }
    }
}
