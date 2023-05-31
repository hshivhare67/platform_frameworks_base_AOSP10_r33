/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.android.settingslib.bluetooth;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSap;

import com.android.settingslib.testutils.shadow.ShadowBluetoothAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowBluetoothAdapter.class})
public class SapProfileTest {

    @Mock
    private CachedBluetoothDeviceManager mDeviceManager;
    @Mock
    private LocalBluetoothProfileManager mProfileManager;
    @Mock
    private BluetoothSap mService;
    @Mock
    private BluetoothDevice mBluetoothDevice;
    private BluetoothProfile.ServiceListener mServiceListener;
    private SapProfile mProfile;
    private ShadowBluetoothAdapter mShadowBluetoothAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mShadowBluetoothAdapter = Shadow.extract(BluetoothAdapter.getDefaultAdapter());
        mProfile = new SapProfile(RuntimeEnvironment.application, mDeviceManager, mProfileManager);
        mServiceListener = mShadowBluetoothAdapter.getServiceListener();
        mServiceListener.onServiceConnected(BluetoothProfile.SAP, mService);
    }

    @Test
    public void connect_shouldConnectBluetoothSap() {
        mProfile.connect(mBluetoothDevice);
        verify(mService).connect(mBluetoothDevice);
    }

    @Test
    public void disconnect_shouldDisconnectBluetoothSap() {
        mProfile.disconnect(mBluetoothDevice);
        verify(mService).disconnect(mBluetoothDevice);
    }

    @Test
    public void getConnectionStatus_shouldReturnConnectionState() {
        when(mService.getConnectionState(mBluetoothDevice)).
                thenReturn(BluetoothProfile.STATE_CONNECTED);
        assertThat(mProfile.getConnectionStatus(mBluetoothDevice)).
                isEqualTo(BluetoothProfile.STATE_CONNECTED);
    }
}