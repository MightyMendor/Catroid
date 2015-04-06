/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.devices.mindstorms.ev3;


import android.content.Context;
import android.util.Log;

import org.catrobat.catroid.bluetooth.base.BluetoothConnection;
import org.catrobat.catroid.bluetooth.base.BluetoothDevice;
import org.catrobat.catroid.devices.mindstorms.MindstormsConnection;
import org.catrobat.catroid.devices.mindstorms.MindstormsConnectionImpl;
import org.catrobat.catroid.devices.mindstorms.MindstormsException;

import java.util.UUID;

public class LegoEv3Impl implements LegoEV3{

	private static final UUID LEGO_EV3_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final String TAG = LegoEv3Impl.class.getSimpleName();

	private boolean isInitialized = false;

	private short commandCounter = 1;

	protected MindstormsConnection mindstormsConnection;
	protected Context context;

	private EV3Motor motorA;
	private EV3Motor motorB;
	private EV3Motor motorC;
	private EV3Motor motorD;

	//private EV3Sensor sensor1;
	//private EV3Sensor sensor2;
	//private EV3Sensor sensor4;

	public LegoEv3Impl(Context applicationContext) {
		this.context = applicationContext;
		Log.d("juc", "LegoEv3Impl constructor");
	}

	public short getCommandCounter() {
		return commandCounter;
	}

	public void incCommandCounter() {
		commandCounter++;
	}


	@Override
	public String getName() {
		return "Lego Mindstorms EV3";
	}

	@Override
	public Class<? extends BluetoothDevice> getDeviceType() {
		return BluetoothDevice.LEGO_EV3;
	}

	// TODO: bugfix: volume decrease by every use of brick. gl future juc....
	@Override
	public void playTone(int frequencyInHz, int durationInMs, int volumeInPercent) {

		Log.d("juc", "LegoEv3Impl | playTone ");

		if (durationInMs <= 0) {
			return;
		}

		//different sources suggest different min./max. values
		if (frequencyInHz > 10000) {
			frequencyInHz = 10000;
		}
		else if (frequencyInHz < 250) {
			frequencyInHz = 250;
		}

		if (volumeInPercent <= 0) {
			return;
		}
		else if (volumeInPercent > 100) {
			volumeInPercent = 100;
		}

		EV3Command command = new EV3Command(getCommandCounter(), EV3CommandType.DIRECT_COMMAND_NO_REPLY, 0, 0, EV3CommandOpCode.OP_SOUND);
		incCommandCounter();

		command.append((byte) 0x01); //cmd Play_TONE. TODO: enum?

		command.append((byte) (volumeInPercent & 0xFF));
		command.append((byte) (frequencyInHz & 0x00FF));
		command.append((byte) ((frequencyInHz & 0xFF00) >> 8));
		command.append((byte) (durationInMs & 0x00FF));
		command.append((byte) ((durationInMs & 0xFF00) >> 8));

		Log.d("juc", "LegoEv3Impl | playTone | command = " + command.toHexString(command));

		try {
			//byte[] response = mindstormsConnection.sendAndReceive(command); // just for debugging, tone-command should not require a response
			mindstormsConnection.send(command);
			Log.d("juc", "LegoEv3Impl | playTone | command was sent");

			//Log.d("juc", "LegoEv3Impl | playTone | response = " + response.toString() );
		}
		catch (MindstormsException e) {
			Log.e(TAG, e.getMessage());
		}


	}

	@Override
	public EV3Motor getMotorA() {
		return motorA;
	}

	@Override
	public EV3Motor getMotorB() {
		return motorB;
	}

	@Override
	public EV3Motor getMotorC() {
		return motorC;
	}

	@Override
	public EV3Motor getMotorD() {
		return motorD;
	}

	@Override
	public boolean isAlive() {

		// TODO: implement isAlive
		return true;
	}

	@Override
	public void setConnection(BluetoothConnection btConnection) {
		this.mindstormsConnection = new MindstormsConnectionImpl(btConnection);
	}


	@Override
	public synchronized void initialise() {

		Log.d("juc", "LegoEv3Inpl | initialise");

		if (isInitialized) {
			return;
		}

		mindstormsConnection.init();

		//motorA = new EV3Motor(0, mindstormsConnection);
		//motorB = new EV3Motor(1, mindstormsConnection);
		//motorC = new EV3Motor(2, mindstormsConnection);
		//motorD = new EV3Motor(3, mindstormsConnection);

		// TODO: Sensor Init
		//assignSensorsToPorts();

		isInitialized = true;
	}

	@Override
	public void start() {
		initialise();
		//sensorService.resumeSensorUpdate();
	}

	@Override
	public void pause() {
		//stopAllMovements();
		//sensorService.pauseSensorUpdate();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void stopAllMovements() {
		motorA.stop();
		motorB.stop();
		motorC.stop();
		motorD.stop();
	}

	@Override
	public void disconnect() {
		if (mindstormsConnection.isConnected()) {
			this.stopAllMovements();
			//sensorService.destroy();
			mindstormsConnection.disconnect();
		}
	}

	@Override
	public UUID getBluetoothDeviceUUID() {
		return LEGO_EV3_UUID;
	}

}
