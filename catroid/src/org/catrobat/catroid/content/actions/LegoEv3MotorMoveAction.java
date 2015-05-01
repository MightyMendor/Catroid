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
package org.catrobat.catroid.content.actions;

import android.util.Log;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.bluetooth.base.BluetoothDevice;
import org.catrobat.catroid.bluetooth.base.BluetoothDeviceService;
import org.catrobat.catroid.common.CatroidService;
import org.catrobat.catroid.common.ServiceProvider;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.devices.mindstorms.ev3.LegoEV3;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;

public class LegoEv3MotorMoveAction extends TemporalAction {

	private Formula power;
	private Formula step1;
	private Formula step2;
	private Formula step3;
	private Sprite sprite;

	private BluetoothDeviceService btService = ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE);


	@Override
	protected void update(float percent) {
		int powerInterpretation;
		int step1Interpretation;
		int step2Interpretation;
		int step3Interpretation;

		try {
			powerInterpretation = power.interpretInteger(sprite);
		} catch (InterpretationException interpretationException) {
			powerInterpretation = 0;
			Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
		}

		try {
			step1Interpretation = step1.interpretInteger(sprite);
		} catch (InterpretationException interpretationException) {
			step1Interpretation = 0;
			Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
		}

		try {
			step2Interpretation = step2.interpretInteger(sprite);
		} catch (InterpretationException interpretationException) {
			step2Interpretation = 0;
			Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
		}

		try {
			step3Interpretation = step3.interpretInteger(sprite);
		} catch (InterpretationException interpretationException) {
			step3Interpretation = 0;
			Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
		}

		LegoEV3 ev3 = btService.getDevice(BluetoothDevice.LEGO_EV3);
		if (ev3 == null) {

			Log.d("juc", "LegoEv3PlayToneAction | ev3 ==null");
			return;
		}

		//Log.d("juc", "LegoEv3PlayToneAction | playTone params herz=" + hertzInterpretation + " | duration=" + durationInterpretation + " | volume= " + volumeInterpretation);

		//ev3.playTone(hertzInterpretation, durationInterpretation * 1000, volumeInterpretation);
	}

	public void setPower(Formula power) {
		this.power = power;
	}

	public void setStep1(Formula step1) {
		this.step1 = step1;
	}

	public void setStep2(Formula step2) {
		this.step2 = step2;
	}

	public void setStep3(Formula step3) {
		this.step3 = step3;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}
