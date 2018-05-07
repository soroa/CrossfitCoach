/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
! *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mac.crossfitcoach.screens.ble_list;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mac.crossfitcoach.R;
import com.example.mac.crossfitcoach.utils.UtilsKt;

import java.util.List;

/**
 * Provides a binding from {@link NotificationCompat.Style} data set to views displayed within the
 * {@link WearableRecyclerView}.
 */
public class BleDevicesRecyclerAdapter extends
        WearableRecyclerView.Adapter<BleDevicesRecyclerAdapter.BleDeviceVh> {

    private List<BluetoothDevice> devices;
    private final OnBleDeviceClicked listener;


    public static class BleDeviceVh extends WearableRecyclerView.ViewHolder {


        private final TextView deviceName;
        private final ProgressBar progressSpiner;

        public BleDeviceVh(View view) {
            super(view);
            deviceName = view.findViewById(R.id.device_name);
            progressSpiner = view.findViewById(R.id.ble_device_progress);
        }

        public void setConnected(boolean connected) {
            if (connected) {
                deviceName.setTextColor(Color.GREEN);
            } else {
                deviceName.setTextColor(Color.WHITE);
            }
        }

        @Override
        public String toString() {
            return (String) deviceName.getText();
        }
    }

    public BleDevicesRecyclerAdapter(List<BluetoothDevice> bluetoothDevices, OnBleDeviceClicked listener) {
        devices = bluetoothDevices;
        this.listener = listener;
    }

    @Override
    public BleDeviceVh onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_ble_device_item, viewGroup, false);
        return new BleDeviceVh(view);
    }

    @Override
    public void onBindViewHolder(final BleDeviceVh bleDeviceVh, final int position) {
        bleDeviceVh.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                bleDeviceVh.progressSpiner.setVisibility(View.VISIBLE);
                listener.onBleDeviceClicked(devices.get(position));
            }
        });
        if (devices.get(position).getName() == null) {
            bleDeviceVh.deviceName.setText("UNKNOWN");
        } else {
            bleDeviceVh.deviceName.setText(devices.get(position).getName());
        }
        UtilsKt.addTouchEffect(bleDeviceVh.deviceName);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devices.size();
    }


    public interface OnBleDeviceClicked {

        void onBleDeviceClicked(BluetoothDevice device);
    }
}