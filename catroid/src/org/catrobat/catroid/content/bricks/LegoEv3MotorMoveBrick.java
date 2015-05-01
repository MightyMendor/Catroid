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
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class LegoEv3MotorMoveBrick extends FormulaBrick implements OnClickListener {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;
	private transient AdapterView<?> adapterView;
	private String motor;
	private String unit;
	private transient Motor motorEnum;

	private transient Unit unitEnum;

	public static enum Motor {
		MOTOR_A, MOTOR_B, MOTOR_C, MOTOR_D
	}

	public static enum Unit {
		DURATION_IN_SECONDS, ROTATIONS, DEGREE
	}

	public LegoEv3MotorMoveBrick() {
		addAllowedBrickField(BrickField.LEGO_EV3_POWER);
		addAllowedBrickField(BrickField.LEGO_EV3_STEP1_SETTING);
		addAllowedBrickField(BrickField.LEGO_EV3_STEP2_SETTING);
		addAllowedBrickField(BrickField.LEGO_EV3_STEP3_SETTING);
	}

	public LegoEv3MotorMoveBrick(Motor motor, Unit unit, int powerValue, int step1Value, int step2Value, int step3Value) {
		this.motorEnum = motor;
		this.motor = motorEnum.name();

		this.unitEnum = unit;

		initializeBrickFields(new Formula(powerValue), new Formula(step1Value), new Formula(step2Value), new Formula(step3Value));
	}

	public LegoEv3MotorMoveBrick(Motor motor, Unit unit, Formula powerFormula, Formula step1Formula, Formula step2Formula, Formula step3Formula) {
		this.motorEnum = motor;
		this.motor = motorEnum.name();

		this.unitEnum = unit;

		initializeBrickFields(powerFormula, step1Formula, step2Formula, step3Formula);
	}

//	protected Object readResolve() {
//		if (motor != null) {
//			motorEnum = Motor.valueOf(motor);
//		}
//		return this;
//	}

	private void initializeBrickFields(Formula power, Formula step1, Formula step2, Formula step3) {

		addAllowedBrickField(BrickField.LEGO_EV3_POWER);
		setFormulaWithBrickField(BrickField.LEGO_EV3_POWER, power);

		addAllowedBrickField(BrickField.LEGO_EV3_STEP1_SETTING);
		setFormulaWithBrickField(BrickField.LEGO_EV3_STEP1_SETTING, step1);

		addAllowedBrickField(BrickField.LEGO_EV3_STEP2_SETTING);
		setFormulaWithBrickField(BrickField.LEGO_EV3_STEP2_SETTING, step2);

		addAllowedBrickField(BrickField.LEGO_EV3_STEP3_SETTING);
		setFormulaWithBrickField(BrickField.LEGO_EV3_STEP3_SETTING, step3);

	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_LEGO_EV3 | getFormulaWithBrickField(BrickField.LEGO_EV3_POWER).getRequiredResources()
				| getFormulaWithBrickField(BrickField.LEGO_EV3_STEP1_SETTING).getRequiredResources()
				| getFormulaWithBrickField(BrickField.LEGO_EV3_STEP2_SETTING).getRequiredResources()
				| getFormulaWithBrickField(BrickField.LEGO_EV3_STEP3_SETTING).getRequiredResources();
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, R.layout.brick_ev3_motor_move, null);

		TextView textPower = (TextView) prototypeView.findViewById(R.id.brick_ev3_motor_move_power_prototype_text_view);
		textPower.setText(String.valueOf(BrickValues.LEGO_POWER));

		Spinner unitSpinner = (Spinner) prototypeView.findViewById(R.id.brick_ev3_motor_move_unit_spinner);
		unitSpinner.setFocusableInTouchMode(false);
		unitSpinner.setFocusable(false);

		ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(context, R.array.ev3_move_motor_unit_chooser,
				android.R.layout.simple_spinner_item);
		unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		unitSpinner.setAdapter(unitAdapter);
		unitSpinner.setSelection(unitEnum.ordinal());

		TextView textStep1 = (TextView) prototypeView.findViewById(R.id.brick_ev3_motor_move_step1_prototype_text_view);
		textStep1.setText(String.valueOf(BrickValues.LEGO_STEP1));
		TextView textStep2 = (TextView) prototypeView.findViewById(R.id.brick_ev3_motor_move_step2_prototype_text_view);
		textStep2.setText(String.valueOf(BrickValues.LEGO_STEP2));
		TextView textStep3 = (TextView) prototypeView.findViewById(R.id.brick_ev3_motor_move_step3_prototype_text_view);
		textStep3.setText(String.valueOf(BrickValues.LEGO_STEP3));
		//todo checkboxen... eher ne

//		CheckBox checkBoxMotorA = (CheckBox) prototypeView.findViewById(R.id.brick_ev3_motor_move_motor_A_checkbox);
//
//		CheckBox checkBoxMotorB = (CheckBox) prototypeView.findViewById(R.id.brick_ev3_motor_move_motor_B_checkbox);
//
//		CheckBox checkBoxMotorC = (CheckBox) prototypeView.findViewById(R.id.brick_ev3_motor_move_motor_C_checkbox);
//
//		CheckBox checkBoxMotorD = (CheckBox) prototypeView.findViewById(R.id.brick_ev3_motor_move_motor_D_checkbox);
//
//		CheckBox checkBoxBrake = (CheckBox) prototypeView.findViewById(R.id.brick_ev3_motor_move_brake_checkbox);

		return prototypeView;
	}

//	@Override
//	public Brick clone() {
//		return new LegoEv3MotorMoveBrick(motorEnum, unitEnum, getFormulaWithBrickField(BrickField.LEGO_EV3_POWER).clone(),
//				getFormulaWithBrickField(BrickField.LEGO_EV3_STEP1_SETTING).clone(),
//				getFormulaWithBrickField(BrickField.LEGO_EV3_STEP2_SETTING).clone(),
//				getFormulaWithBrickField(BrickField.LEGO_EV3_STEP3_SETTING).clone());
//	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}

		view = View.inflate(context, R.layout.brick_ev3_motor_move, null);
		view = getViewWithAlpha(alphaValue);
		setCheckboxView(R.id.brick_ev3_motor_move_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView textPower = (TextView) view.findViewById(R.id.brick_ev3_motor_move_power_prototype_text_view);
		TextView editPower = (TextView) view.findViewById(R.id.ev3_motor_move_power_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_EV3_POWER).setTextFieldId(R.id.ev3_motor_move_power_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_EV3_POWER).refreshTextField(view);
		textPower.setVisibility(View.GONE);
		editPower.setVisibility(View.VISIBLE);
		editPower.setOnClickListener(this);

		TextView textStep1 = (TextView) view.findViewById(R.id.brick_ev3_motor_move_step1_prototype_text_view);
		TextView editStep1 = (TextView) view.findViewById(R.id.ev3_motor_move_step1_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_EV3_STEP1_SETTING).setTextFieldId(R.id.ev3_motor_move_step1_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_EV3_STEP1_SETTING).refreshTextField(view);
		textStep1.setVisibility(View.GONE);
		editStep1.setVisibility(View.VISIBLE);
		editStep1.setOnClickListener(this);

		TextView textStep2 = (TextView) view.findViewById(R.id.brick_ev3_motor_move_step2_prototype_text_view);
		TextView editStep2 = (TextView) view.findViewById(R.id.ev3_motor_move_step2_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_EV3_STEP2_SETTING).setTextFieldId(R.id.ev3_motor_move_step2_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_EV3_STEP2_SETTING).refreshTextField(view);
		textStep2.setVisibility(View.GONE);
		editStep2.setVisibility(View.VISIBLE);
		editStep2.setOnClickListener(this);

		TextView textStep3 = (TextView) view.findViewById(R.id.brick_ev3_motor_move_step3_prototype_text_view);
		TextView editStep3 = (TextView) view.findViewById(R.id.ev3_motor_move_step3_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_EV3_STEP3_SETTING).setTextFieldId(R.id.ev3_motor_move_step3_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_EV3_STEP3_SETTING).refreshTextField(view);
		textStep3.setVisibility(View.GONE);
		editStep3.setVisibility(View.VISIBLE);
		editStep3.setOnClickListener(this);


		ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(context, R.array.ev3_move_motor_unit_chooser,
				android.R.layout.simple_spinner_item);
		unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner unitSpinner = (Spinner) view.findViewById(R.id.brick_ev3_motor_move_unit_spinner);

		if (!(checkbox.getVisibility() == View.VISIBLE)) {
			unitSpinner.setClickable(true);
			unitSpinner.setEnabled(true);
		} else {
			unitSpinner.setClickable(false);
			unitSpinner.setEnabled(false);
		}

		unitSpinner.setAdapter(unitAdapter);
		unitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				unitEnum = Unit.values()[position];
				unit = unitEnum.name();
				adapterView = arg0;

				// TODO set all unit-texts to string of selected unit
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		unitSpinner.setSelection(unitEnum.ordinal());

		return view;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}

		switch (view.getId()) {
			case R.id.ev3_motor_move_power_edit_text:
				FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.LEGO_EV3_POWER));
				break;
			case R.id.ev3_motor_move_step1_edit_text:
				FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.LEGO_EV3_STEP1_SETTING));
				break;
			case R.id.ev3_motor_move_step2_edit_text:
				FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.LEGO_EV3_STEP2_SETTING));
				break;
			case R.id.ev3_motor_move_step3_edit_text:
				FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.LEGO_EV3_STEP3_SETTING));
				break;
		}

	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(R.id.brick_ev3_motor_move_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textLegoMotorMoveLabel = (TextView) view.findViewById(R.id.brick_ev3_motor_move_label);
			TextView textLegoMotorMovePower = (TextView) view.findViewById(R.id.brick_ev3_motor_move_power_text);
			TextView textLegoMotorMoveStep1 = (TextView) view.findViewById(R.id.brick_ev3_motor_move_step1_text);
			TextView textLegoMotorMoveStep2 = (TextView) view.findViewById(R.id.brick_ev3_motor_move_step2_text);
			TextView textLegoMotorMoveStep3 = (TextView) view.findViewById(R.id.brick_ev3_motor_move_step3_text);

			textLegoMotorMoveLabel.setTextColor(textLegoMotorMoveLabel.getTextColors().withAlpha(alphaValue));
			textLegoMotorMovePower.setTextColor(textLegoMotorMovePower.getTextColors().withAlpha(alphaValue));
			textLegoMotorMoveStep1.setTextColor(textLegoMotorMoveStep1.getTextColors().withAlpha(alphaValue));
			textLegoMotorMoveStep2.setTextColor(textLegoMotorMoveStep2.getTextColors().withAlpha(alphaValue));
			textLegoMotorMoveStep3.setTextColor(textLegoMotorMoveStep3.getTextColors().withAlpha(alphaValue));

			TextView editLegoMotorMovePowerText = (TextView) view.findViewById(R.id.ev3_motor_move_power_edit_text);
			TextView editLegoMotorMoveStep1Text = (TextView) view.findViewById(R.id.ev3_motor_move_step1_edit_text);
			TextView editLegoMotorMoveStep2Text = (TextView) view.findViewById(R.id.ev3_motor_move_step2_edit_text);
			TextView editLegoMotorMoveStep3Text = (TextView) view.findViewById(R.id.ev3_motor_move_step3_edit_text);

			editLegoMotorMovePowerText.setTextColor(editLegoMotorMovePowerText.getTextColors().withAlpha(alphaValue));
			editLegoMotorMovePowerText.getBackground().setAlpha(alphaValue);
			editLegoMotorMoveStep1Text.setTextColor(editLegoMotorMoveStep1Text.getTextColors().withAlpha(alphaValue));
			editLegoMotorMoveStep1Text.getBackground().setAlpha(alphaValue);
			editLegoMotorMoveStep2Text.setTextColor(editLegoMotorMoveStep2Text.getTextColors().withAlpha(alphaValue));
			editLegoMotorMoveStep2Text.getBackground().setAlpha(alphaValue);
			editLegoMotorMoveStep3Text.setTextColor(editLegoMotorMoveStep3Text.getTextColors().withAlpha(alphaValue));
			editLegoMotorMoveStep3Text.getBackground().setAlpha(alphaValue);

			Spinner spinnerLegoMotorMoveUnit = (Spinner) view.findViewById(R.id.brick_ev3_motor_move_unit_spinner);
			spinnerLegoMotorMoveUnit.getBackground().setAlpha(alphaValue);

			ColorStateList color = textLegoMotorMoveLabel.getTextColors().withAlpha(alphaValue);

			if (adapterView != null) {
				((TextView) adapterView.getChildAt(0)).setTextColor(color);
			}

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.legoEv3MotorMove(sprite,
				getFormulaWithBrickField(BrickField.LEGO_EV3_POWER),
				getFormulaWithBrickField(BrickField.LEGO_EV3_STEP1_SETTING),
				getFormulaWithBrickField(BrickField.LEGO_EV3_STEP2_SETTING),
				getFormulaWithBrickField(BrickField.LEGO_EV3_STEP3_SETTING)));
		return null;
	}

}
