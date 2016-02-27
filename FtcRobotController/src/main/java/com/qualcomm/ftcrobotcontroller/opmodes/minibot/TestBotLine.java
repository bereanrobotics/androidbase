/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes.minibot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class TestBotLine extends OpMode {

	final static double MOTOR_POWER = 0.15; // Higher values will cause the robot to move faster
	final static double MOTOR_POWER_HIGH = 0.35; // Higher values will cause the robot to move faster
	final static double LIGHT_THRESHOLD = 10;

	DcMotor motorRight;
	DcMotor motorLeft;
	LightSensor reflectedLight;
	ColorSensor color;
	ColorSensor color1;
	UltrasonicSensor sonicRight;
	UltrasonicSensor sonicLeft;

	Boolean seekingLine;
	int loopCount;
	/**
	 * Constructor
	 */
	public TestBotLine() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {

		motorRight = hardwareMap.dcMotor.get("right");
		motorLeft = hardwareMap.dcMotor.get("left");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		reflectedLight = hardwareMap.lightSensor.get("light");
		color = hardwareMap.colorSensor.get("color");
		color1 = hardwareMap.colorSensor.get("color1");
		sonicRight = hardwareMap.ultrasonicSensor.get("sonic1");
		sonicLeft = hardwareMap.ultrasonicSensor.get("sonic2");

		// turn on LED of light sensor.
		reflectedLight.enableLed(true);
		//color.enableLed(true);
		color.enableLed(false);

		seekingLine = true;
		loopCount = 0;
	}


	@Override
	public void start() {
		seekingLine = true;
	}
	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		double left, right;
		int reflection = 0;
		double distanceRight = 256;
		double distanceLeft = 256;
		/*
		 * read the light sensor.
		 */
		//reflection = reflectedLight.getLightLevel();
		reflection = reflectedLight.getLightDetectedRaw();
		double temp = sonicRight.getUltrasonicLevel();
		if (temp<255)
			distanceRight = temp;
		temp = sonicLeft.getUltrasonicLevel();
		if (temp<255)
			distanceLeft = temp;

		if ((distanceLeft > 255) && (distanceRight > 255)) {
			right = MOTOR_POWER_HIGH;
			left = MOTOR_POWER_HIGH;
		} else if (distanceLeft > distanceRight) {
			right = 0;
			left = MOTOR_POWER;
		} else if (distanceRight > distanceLeft) {
			right = MOTOR_POWER;
			left = 0;
		} else {
			right = MOTOR_POWER;
			left = MOTOR_POWER;
		}
		if ((distanceLeft < 10 || distanceRight < 10)) {
			right = 0;
			left = 0;
		}
		/*
		else if (seekingLine) {
			if ((loopCount > 10) && (reflection < LIGHT_THRESHOLD)) {
				left = 0.0;
				right = 0.0;
				seekingLine = false;
			} else {
				left = MOTOR_POWER_HIGH;
				right = MOTOR_POWER_HIGH;
			}
		} else {
			if (reflection < LIGHT_THRESHOLD) {
			 //
			 // if reflection is less than the threshold value, then assume we are above dark spot.
			 // turn to the right.
			 //
				left = MOTOR_POWER;
				right = 0.0;
			} else {
			//
			//assume we are over a light spot.
			// turn to the left.
			//
				left = 0.0;
				right = MOTOR_POWER;
			}
		}
*/
		
		/*
		 * set the motor power
		 */
		motorRight.setPower(right);
		motorLeft.setPower(left);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

		telemetry.addData("Text", "Loop: " + Integer.toString(loopCount));
		//telemetry.addData("reflection", "reflection:  " + Double.toString(reflection));
		telemetry.addData("Color", "Color: "+ String.format("r=%d,g=%d,b=%d,", color.red(),color.green(), color.blue()));
		telemetry.addData("Color1", "Color1: "+ String.format("r=%d,g=%d,b=%d,", color1.red(),color1.green(), color1.blue()));
		telemetry.addData("distance", "Distance: R"+ Double.toString(distanceRight) + "  L"+ Double.toString(distanceLeft));
		telemetry.addData("left tgt pwr",  "left  pwr: " + Double.toString(left));
		telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(right));
		loopCount++;
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

}
