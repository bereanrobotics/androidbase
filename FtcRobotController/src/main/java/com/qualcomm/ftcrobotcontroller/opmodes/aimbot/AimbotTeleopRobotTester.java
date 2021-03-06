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

package com.qualcomm.ftcrobotcontroller.opmodes.aimbot;

import com.berean.robotics.aimbot.AimbotDoppleBot;
import com.berean.robotics.dopple.DoppleBot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class AimbotTeleopRobotTester extends OpMode {

	final static double BUTTON_OFF = 0.0;
	final static double BUTTON_ON = 1.0;
	final static double DROPPER_MIN_POSITION = 0.0;
	final static double DROPPER_MAX_POSITION = .75;
	final static double SLOW_SPEED_FACTOR = .25;
	final static double FAST_SPEED_FACTOR = 1.0;

	DoppleBot aimBot = new AimbotDoppleBot(hardwareMap);

	DcMotor frontLeftMotor;
	DcMotor frontRightMotor;
	DcMotor backLeftMotor;
	DcMotor backRightMotor;
	Servo rightButtonPusher;
	Servo leftButtonPusher;
	Servo dropper;
	Servo cattleGuard;

	double speedFactor = SLOW_SPEED_FACTOR;
	boolean climbMode = false;

	/**
	 * Constructor
	 */
	public AimbotTeleopRobotTester() {

		frontLeftMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.FRONT_LEFT_MOTOR_NAME);
		frontRightMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.FRONT_RIGHT_MOTOR_NAME);
		backLeftMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.BACK_LEFT_MOTOR_NAME);
		backRightMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.BACK_RIGHT_MOTOR_NAME);
		rightButtonPusher = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.RIGHT_BUTTON_PUSHER_NAME);
		leftButtonPusher = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.LEFT_BUTTON_PUSHER_NAME);
		dropper = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.DROPPER_NAME);
		cattleGuard = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.CATTLEGUARD_NAME);

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {

        aimBot.initializeRobot();
		initTelemetry();

    }

	private void initTelemetry(){

		telemetry.addData("Speed","SLOW SPEED");
		telemetry.addData("left_pwr", "L power: " + String.format("%.2f", 0.0));
		telemetry.addData("right_pwr", "R power: " + String.format("%.2f", 0.0));
		telemetry.addData("L_pusher","L Push OFF");
		telemetry.addData("R_pusher", "R Push OFF");
		telemetry.addData("Guard", "Guard Down");
		telemetry.addData("Dropper", "Waiting");
	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void start() {
		aimBot.startRobot();

	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		driveBot();
		deployAccessories();
		aimBot.updateRecording();

	}

	private void driveBot(){

		checkDriveSpeed();
		//climbMode = gamepad1.a;

		float leftStickInput = -gamepad1.left_stick_y;
		float rightStickInput = -gamepad1.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		rightStickInput = Range.clip(rightStickInput, -1, 1);
		leftStickInput = Range.clip(leftStickInput, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		rightStickInput = (float)scaleInput(rightStickInput);
		leftStickInput =  (float)scaleInput(leftStickInput);

		// write the values to the motors
		if (!climbMode){
			frontLeftMotor.setPower(leftStickInput * (float) speedFactor);
			backLeftMotor.setPower(leftStickInput * (float) speedFactor);

			frontRightMotor.setPower(rightStickInput * (float) speedFactor);
			backRightMotor.setPower(rightStickInput * (float) speedFactor);
		} else {
			frontLeftMotor.setPower(leftStickInput * (float) FAST_SPEED_FACTOR);
			backLeftMotor.setPower(leftStickInput * (float) SLOW_SPEED_FACTOR);

			frontRightMotor.setPower(rightStickInput * (float) FAST_SPEED_FACTOR);
			backRightMotor.setPower(rightStickInput * (float) SLOW_SPEED_FACTOR);
		}


		telemetry.addData("left_pwr", "L power: " + String.format("%.2f", leftStickInput * (float) speedFactor));
		telemetry.addData("right_pwr", "R power: " + String.format("%.2f", rightStickInput * (float) speedFactor));

	}

	private void checkDriveSpeed(){

		if (gamepad1.left_bumper){
			speedFactor = SLOW_SPEED_FACTOR;
			telemetry.addData("Speed","SLOW SPEED");
		}
		if (gamepad1.right_bumper) {
			speedFactor = FAST_SPEED_FACTOR;
			telemetry.addData("Speed","FAST SPEED");
		}

	}



	private void deployAccessories(){

		float driver_right_trigger_value = gamepad1.right_trigger;
		float gunner_right_trigger_value = gamepad2.right_trigger;
		float cattleGuardPositionValue;

		if (driver_right_trigger_value > 0) {
			cattleGuardPositionValue = driver_right_trigger_value;
		} else cattleGuardPositionValue = gunner_right_trigger_value;

		cattleGuard.setPosition((float)scaleInput(cattleGuardPositionValue));
		if (cattleGuardPositionValue > 0){
			telemetry.addData("Guard","Guard Up");
		} else telemetry.addData("Guard","Guard Down");

		// button pusher RIGHT
		if (gamepad2.b) {
			rightButtonPusher.setPosition(BUTTON_ON);
			telemetry.addData("R_pusher","R Push ON");
		} else {
			rightButtonPusher.setPosition(BUTTON_OFF);
			telemetry.addData("R_pusher","R Push OFF");
		}

		// button pusher left
		if (gamepad2.x) {
			leftButtonPusher.setPosition(BUTTON_OFF);
			telemetry.addData("L_pusher","L Push ON");
		} else {
			leftButtonPusher.setPosition(BUTTON_ON);
			telemetry.addData("L_pusher","L Push OFF");
		}

		if (gamepad2.y) {
			dropper.setPosition(DROPPER_MAX_POSITION);
			telemetry.addData("Dropper","Dropping");
		} else {
			dropper.setPosition(DROPPER_MIN_POSITION);
			telemetry.addData("Dropper","Waiting");
		}

	}
	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

		aimBot.stopRobot();

	}
	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		//double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
		//		0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

		double[] scaleArray = { 0,0.05,0.09,0.12,0.12,0.14,0.14,0.18,0.21,0.25,0.3,0.38,0.45,0.55,0.7,0.85,1.00 };
		
		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);
		if (index < 0) {
			index = -index;
		} else if (index > 16) {
			index = 16;
		}
		
		double dScale;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}
		
		return dScale;
	}

}
