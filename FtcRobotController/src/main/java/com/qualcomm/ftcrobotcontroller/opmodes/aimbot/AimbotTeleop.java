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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class AimbotTeleop extends OpMode {

	final static double BUTTON_OFF = 0.0;
	final static double BUTTON_ON = 1.0;
	final static double DROPPER_MIN_POSITION = 0.0;
	final static double DROPPER_MAX_POSITION = .75;
	final static double SLOW_SPEED_FACTOR = .25;
	final static double FAST_SPEED_FACTOR = 1.0;

	DcMotor frontLeftMotor;
	DcMotor frontRightMotor;
	DcMotor backLeftMotor;
	DcMotor backRightMotor;
	Servo rightButtonPusher;
	Servo leftButtonPusher;
	Servo dropper;
	Servo cattleGuard;

	//Robot State Variables
	float leftSideMotorPower;
	float rightSideMotorPower;
	double speedFactor = SLOW_SPEED_FACTOR;

	/**
	 * Constructor
	 */
	public AimbotTeleop() {


	}

	/*
	 * Code to run when the op mode is initialized goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {

		createMotors();
		createServos();
        initMotors();
		initServos();
		updateTelemetry();

    }

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void start() {

	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		tryToDrive();
		tryToOperateSpecials();
		updateTelemetry();

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

	//
	// PRIVATE METHODS
	//
	//

	private void createMotors(){
		frontLeftMotor = hardwareMap.dcMotor.get("left_front");
		frontRightMotor = hardwareMap.dcMotor.get("right_front");
		backLeftMotor = hardwareMap.dcMotor.get("left_back");
		backRightMotor = hardwareMap.dcMotor.get("right_back");
	}

	private void createServos(){
		rightButtonPusher = hardwareMap.servo.get("right_button_push");
		leftButtonPusher = hardwareMap.servo.get("left_button_push");
		dropper = hardwareMap.servo.get("dropper");
		cattleGuard = hardwareMap.servo.get("cattleguard");
	}

	private void initMotors(){


		frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
		backRightMotor.setDirection(DcMotor.Direction.REVERSE);

	}

	private void initServos(){


		cattleGuard.setDirection(Servo.Direction.REVERSE);

		leftButtonPusher.setPosition(BUTTON_ON);
		cattleGuard.setPosition(0.0);

	}

	private void updateTelemetry(){

		if (speedFactor==SLOW_SPEED_FACTOR){
			telemetry.addData("Speed","speed: SLOW");
		} else
			telemetry.addData("Speed","speed: FAST");

		telemetry.addData("left_pwr", "l power: " + String.format("%.2f", leftSideMotorPower));
		telemetry.addData("right_pwr", "r power: " + String.format("%.2f", rightSideMotorPower));

		if (leftButtonPusher.getPosition() < 1){ // the left pusher is reversed, but we aren't using reverse mode
			telemetry.addData("L_pusher","left: PUSHING");
		} else
			telemetry.addData("L_pusher","left: WAITING");

		if (rightButtonPusher.getPosition() > 0){
			telemetry.addData("R_pusher", "right: PUSHING");
		} else
			telemetry.addData("R_pusher", "right: WAITING");

		if (cattleGuard.getPosition() > 0){
			telemetry.addData("Guard","guard: UP");
		} else
			telemetry.addData("Guard","guard: DOWN");

		if (dropper.getPosition() > 0 ){
			telemetry.addData("Dropper", "dropper: DROP");
		} else
			telemetry.addData("Dropper", "dropper: WAIT");
	}

	/**
	 * Main drive train control logic - controlling the wheels only
	 * Implemented as tank drive with 4 motors
	 * Control mapped to the left and right sticks
	 */
	private void tryToDrive(){

		checkDriveSpeed();

		float rightStickInput = Range.clip(-gamepad1.right_stick_y, -1, 1);
		float leftStickInput = Range.clip(-gamepad1.left_stick_y, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		rightStickInput = (float)scaleInput(rightStickInput);
		leftStickInput =  (float)scaleInput(leftStickInput);

		setRightPower(rightStickInput);
		setLeftPower(leftStickInput);

	}

	/**
	 * Looks for the left and right bumpers to see if they are pressed.
	 * left bumper will turn on slow speed
	 * right bumper will turn on fast speed
	 *
	 * If no button is pressed, then speed remains unchanged
	 */
	private void checkDriveSpeed(){

		if (gamepad1.left_bumper){
			speedFactor = SLOW_SPEED_FACTOR;
			//telemetry.addData("Speed","SLOW SPEED");
		}
		if (gamepad1.right_bumper) {
			speedFactor = FAST_SPEED_FACTOR;
			//telemetry.addData("Speed","FAST SPEED");
		}

	}

	private void setLeftPower(float leftPowerValue){
		frontLeftMotor.setPower(leftPowerValue * (float) speedFactor);
		backLeftMotor.setPower(leftPowerValue * (float) speedFactor);
		frontLeftMotor.getPower();
		//telemetry.addData("left_pwr", "L power: " + String.format("%.2f", leftPowerValue * (float) speedFactor));

	}

	private void setRightPower(float rightPowerValue){
		frontRightMotor.setPower(rightPowerValue * (float) speedFactor);
		backRightMotor.setPower(rightPowerValue * (float) speedFactor);
		//telemetry.addData("right_pwr", "R power: " + String.format("%.2f", rightPowerValue * (float) speedFactor));

	}


	/**
	 * Main method to control non-drive robot components.
	 * driver controls:
	 * - Cattleguard with right trigger
	 *
	 * gunner controls:
	 * - Cattleguard with right trigger (driver control takes precedence)
	 * - right button pusher with "b" button
	 * - left button pusher with "x" button
	 * - dropper with "y" button
	 */
	private void tryToOperateSpecials(){

		operateCattleGuard();
		operateButtonPushers();
		operateDropper();

	}

	private void operateCattleGuard(){

		float driverCattleguardValue = Range.clip(gamepad1.right_trigger,0,1);
		float gunnerCattleguardValue = Range.clip(gamepad2.right_trigger,0,1);

		float cattleGuardPositionValue;

		if (driverCattleguardValue > 0) {
			cattleGuardPositionValue = driverCattleguardValue;
		} else cattleGuardPositionValue = gunnerCattleguardValue;

		cattleGuard.setPosition(cattleGuardPositionValue);
		/*
		if (cattleGuardPositionValue > 0){
			telemetry.addData("Guard","Guard Up");
		} else telemetry.addData("Guard","Guard Down");
		 */
	}

	private void operateButtonPushers(){
		// button pusher RIGHT
		if (gamepad2.b) {
			rightButtonPusher.setPosition(BUTTON_ON);
			//telemetry.addData("R_pusher","R Push ON");
		} else {
			rightButtonPusher.setPosition(BUTTON_OFF);
			//telemetry.addData("R_pusher","R Push OFF");
		}

		// button pusher left is oriented in reverse, but
		// we aren't using reverse mode.
		if (gamepad2.x) {
			leftButtonPusher.setPosition(BUTTON_OFF);
			//telemetry.addData("L_pusher","L Push ON");
		} else {
			leftButtonPusher.setPosition(BUTTON_ON);
			//telemetry.addData("L_pusher","L Push OFF");
		}

	}

	private void operateDropper(){
		if (gamepad2.y) {
			dropper.setPosition(DROPPER_MAX_POSITION);
			//telemetry.addData("Dropper","Dropping");
		} else {
			dropper.setPosition(DROPPER_MIN_POSITION);
			//telemetry.addData("Dropper","Waiting");
		}
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
		
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}
		
		return dScale;
	}

}
