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

package org.catrobat.catroid.devices.mindstorms.ev3.sensors;

import android.util.Log;

import org.catrobat.catroid.devices.mindstorms.MindstormsConnection;
import org.catrobat.catroid.devices.mindstorms.MindstormsException;
import org.catrobat.catroid.devices.mindstorms.MindstormsSensor;

import java.util.Locale;

public abstract class EV3Sensor implements MindstormsSensor {

	public enum Sensor {
		NO_SENSOR,
		TOUCH,
		INFRARED;

		public static String[] getSensorCodes() {
			String[] valueStrings = new String[values().length];

			for (int i = 0; i < values().length; ++i) {
				valueStrings[i] = values()[i].name();
			}

			return valueStrings;
		}

		public String getSensorCode() {
			return getSensorCode(this);
		}

		public static String getSensorCode(EV3Sensor.Sensor sensor) {
			return sensor.name();
		}

		public static EV3Sensor.Sensor getSensorFromSensorCode(String sensorCode) {
			if (sensorCode == null) {
				return Sensor.NO_SENSOR;
			}

			try {
				return valueOf(sensorCode);
			} catch (IllegalArgumentException e) {
				return Sensor.NO_SENSOR;
			}
		}
	}

	protected final int port;
	protected final EV3SensorType sensorType;
	protected final EV3SensorMode sensorMode;
	protected final int updateInterval = 250;

	protected final MindstormsConnection connection;

	protected boolean hasInit;
	protected int lastValidValue = 0;

	public static final String TAG = EV3Sensor.class.getSimpleName();

	public EV3Sensor(int port, EV3SensorType sensorType, EV3SensorMode sensorMode, MindstormsConnection connection) {
		this.port = port;
		this.sensorType = sensorType;
		this.sensorMode = sensorMode;

		this.connection = connection;
	}

	@Override
	public int getUpdateInterval() {
		return updateInterval;
	}

	@Override
	public void updateLastSensorValue() {
		try {
			lastValidValue = getValue();
		} catch (MindstormsException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public int getLastSensorValue() {
		return lastValidValue;
	}

	@Override
	public String getName() {
		return String.format(Locale.getDefault(), "%s_%s_%d", TAG, sensorType.name(), port);
	}

	@Override
	public int getConnectedPort() {
		return port;
	}
}
