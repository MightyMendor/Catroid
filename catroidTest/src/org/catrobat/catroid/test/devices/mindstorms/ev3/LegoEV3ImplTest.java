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
package org.catrobat.catroid.test.devices.mindstorms.ev3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;

import org.catrobat.catroid.common.bluetooth.ConnectionDataLogger;
import org.catrobat.catroid.devices.mindstorms.ev3.LegoEV3;
import org.catrobat.catroid.devices.mindstorms.ev3.LegoEv3Impl;

public class LegoEV3ImplTest extends AndroidTestCase {

	private Context applicationContext;
	private SharedPreferences preferences;

	private LegoEV3 ev3;
	ConnectionDataLogger logger;

	private static final int PREFERENCES_SAVE_DELAY = 50;
	private static final int BASIC_MESSAGE_BYTE_OFFSET = 5;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		applicationContext = this.getContext().getApplicationContext();
		preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);

		ev3 = new LegoEv3Impl(this.applicationContext);
		logger = ConnectionDataLogger.createLocalConnectionLogger();
		ev3.setConnection(logger.getConnectionProxy());
	}

	public void testSimplePlayToneTest() {

		int inputHz = 10000;
		int expectedHz = 10000;
		int durationInMs = 3000;
		int volume = 100;

		ev3.initialise();
		ev3.playTone(inputHz, durationInMs, volume);

		byte[] setOutputState = logger.getNextSentMessage(0, BASIC_MESSAGE_BYTE_OFFSET);

		assertEquals("Expected Hz not same as input Hz", (byte)expectedHz, setOutputState[2]);
		assertEquals("Expected Hz not same as input Hz", (byte)(expectedHz >> 8), setOutputState[3]);
	}

//	public void testPlayToneHzOverMaxValue() {
//
//		// MaxHz = 14000;
//		int inputHz = 16000;
//		int expectedHz = 14000;
//		int durationInMs = 5000;
//
//		nxt.initialise();
//		nxt.playTone(inputHz, durationInMs);
//
//		byte[] setOutputState = logger.getNextSentMessage(0, 2);
//
//		assertEquals("Expected Hz over maximum Value (max. Value = 14000)", (byte)expectedHz, setOutputState[2]);
//		assertEquals("Expected Hz over maximum Value (max. Value = 14000)", (byte)(expectedHz >> 8), setOutputState[3]);
//	}
//
//	public void testcheckDurationOfTone() {
//
//		int inputHz = 13000;
//		int inputDurationInMs = 6000;
//		int expectedDurationInMs = 6000;
//
//		nxt.initialise();
//		nxt.playTone(inputHz, inputDurationInMs);
//
//		byte[] setOutputState = logger.getNextSentMessage(0, 2);
//
//		assertEquals("Expected Duration not same as Input Duration", (byte)expectedDurationInMs, setOutputState[4]);
//		assertEquals("Expected Duration not same as Input Duration", (byte)(expectedDurationInMs >> 8), setOutputState[5]);
//	}

	public void testWithZeroDuration() {

		int inputHz = 13000;
		int inputDurationInMs = 0;
		int volume = 100;

		ev3.initialise();
		ev3.playTone(inputHz, inputDurationInMs, volume);

		byte[] command = logger.getNextSentMessage(0, 2);

		assertEquals("LastSentCommand Should be NULL", null, command);
	}
}