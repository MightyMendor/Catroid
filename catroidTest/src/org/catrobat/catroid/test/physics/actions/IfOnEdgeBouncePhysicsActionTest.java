/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.physics.actions;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;

import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.physics.PhysicsObject;
import org.catrobat.catroid.physics.PhysicsWorld;
import org.catrobat.catroid.physics.content.ActionFactory;
import org.catrobat.catroid.test.utils.Reflection;

public class IfOnEdgeBouncePhysicsActionTest extends PhysicsActionTestCase {

	public void testNormalBehavior() {
		assertTrue("getLookData is null", sprite.look.getLookData() != null);
		int i = 0;

		PhysicsObject physicsObject = physicsWorld.getPhysicsObject(sprite);
		//		physicsWorld.changeLook(physicsObject, sprite.look);
		//		physicsObject.setVelocity(0.0f, -10.0f);

		i = 0;
		while (i < 10) {
			physicsWorld.step(0.3f);
			Log.d("IfOnEdgeBouncePhysicsActionTest", "physicsObject.getY():" + physicsObject.getY());
			i++;
		}

		float setYValue = -ScreenValues.SCREEN_HEIGHT / 2; // So the half of the rectangle should be outside of the screen
		physicsObject.setY(setYValue + 15);

		assertTrue("Unexpected Y-value", physicsObject.getY() == (setYValue + 15));
		physicsWorld.step(1.2f);

		ActionFactory factory = sprite.getActionFactory();
		Action ifOnEdgeBouncePhysicsAction = factory.createIfOnEdgeBounceAction(sprite);
		Log.d("IfOnEdgeBouncePhysicsActionTest", "ifOnEdgeBouncePhysicsAction ....getY():" + physicsObject.getY());
		ifOnEdgeBouncePhysicsAction.act(0.1f);
		Log.d("IfOnEdgeBouncePhysicsActionTest", "ifOnEdgeBouncePhysicsAction.act ....getY():" + physicsObject.getY());
		physicsWorld.step(2.3f);
		Log.d("IfOnEdgeBouncePhysicsActionTest", "ifOnEdgeBouncePhysicsAction.act ....getY():" + physicsObject.getY());

		assertTrue(physicsObject.getY() + " >= " + setYValue, (physicsObject.getY() > setYValue));

		Vector2 gravityVector2 = ((World) Reflection.getPrivateField(PhysicsWorld.class, physicsWorld, "world"))
				.getGravity();

		assertEquals("Unexpected gravityX value", PhysicsWorld.DEFAULT_GRAVITY.x, gravityVector2.x);
		assertEquals("Unexpected gravityY value", PhysicsWorld.DEFAULT_GRAVITY.y, gravityVector2.y);

		i = 0;
		while (i < 20) {
			physicsWorld.step(2.3f);
			Log.d("IfOnEdgeBouncePhysicsActionTest", "physicsObject.getY():" + physicsObject.getY());
			i++;
		}

	}
}