/*
 * Copyright (C) 2023 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.pad_parts.stylus;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.provider.Settings;
import android.widget.Toast;
import android.util.Log;

import org.lineageos.pad_parts.R;

public class StylusReceiver extends BroadcastReceiver {

    private static final String TAG = "StylusReceiver";
    private static final boolean DEBUG = true;

    protected static final String INTENT_ACTION_PAIR_STYLUS = "org.lineageos.pad_parts.action.PAIR_STYLUS";
    protected static final String INTENT_ACTION_STYLUS_VISIBILITY_CHANGED
           = "org.lineageos.pad_parts.action.STYLUS_VISIBILITY_CHANGED";
    protected static final String EXTRA_MAC_ADDRESS = "org.lineageos.pad_parts.extra.MAC_ADDRESS";

    @Override
    public void onReceive(Context context, Intent intent) {
        String mac = intent.getStringExtra(EXTRA_MAC_ADDRESS);

        if (INTENT_ACTION_PAIR_STYLUS.equals(intent.getAction())) {
            pairStylus(context, mac);
        } else if (INTENT_ACTION_STYLUS_VISIBILITY_CHANGED.equals(intent.getAction())) {
            updateNotification(context, mac);
        }
    }

    private void pairStylus(Context context, String mac) {
        if (mac == null) {
            return;
        }
        if (DEBUG) Log.d(TAG, String.format("received stylus bonding: %s", mac));

        StylusUtils.cancelNotification(context);

        BluetoothAdapter adapter = getBluetoothAdapter(context);
        if (adapter != null) {
            BluetoothDevice device = adapter.getRemoteDevice(mac);
            if (device.getBondState() != BluetoothDevice.BOND_NONE) {
                return;
            }

            device.createBond();
        }

        Intent pairingIntent = new Intent(Settings.ACTION_BLUETOOTH_PAIRING_SETTINGS);
        pairingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityAsUser(pairingIntent, UserHandle.CURRENT);
    }

    private void updateNotification(Context context, String mac) {
        if (DEBUG) Log.d(TAG, String.format("received visibility changed %s", mac));
        BluetoothAdapter adapter = getBluetoothAdapter(context);

        if (mac == null) {
            StylusUtils.cancelNotification(context);
        } else if (adapter != null) {
            BluetoothDevice device = adapter.getRemoteDevice(mac);
            if (device.getBondState() != BluetoothDevice.BOND_NONE) {
                return;
            }

            StylusUtils.sendNotification(context, mac);
        }
    }

    private BluetoothAdapter getBluetoothAdapter(Context context) {
        BluetoothAdapter adapter = context.getSystemService(BluetoothManager.class).getAdapter();
        if (!adapter.isEnabled()) {
            return null;
        }

        return adapter;
    }

}
