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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class AimbotAutonomousBlue extends LinearOpMode {

	final static double BUTTON_OFF = 0.0;
	final static double BUTTON_ON = 1.0;
	final static double DROPPER_MIN_POSITION = 0.0;
	final static double DROPPER_MAX_POSITION = .75;
	final static double LOW_POWER = .40;
	final static double HIGH_POWER = .60;

	DcMotor frontLeftMotor;
	DcMotor frontRightMotor;
	DcMotor backLeftMotor;
	DcMotor backRightMotor;
	Servo rightButtonPusher;
	Servo leftButtonPusher;
	Servo dropper;
	Servo cattleGuard;
	LightSensor lineFinder;
	UltrasonicSensor distanceFinder;

	//Robot State Variables
	float leftSideMotorPower;
	float rightSideMotorPower;
	String robotStatus;

	/**
	 * Constructor
	 */
	public AimbotAutonomousBlue() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void runOpMode() throws InterruptedException {

		initMotors();
		initServos();
		initSensors();
		updateTelemetry();

		waitForStart();

		for(int i=0; i<1; i++) {

			robotStatus = "Waiting 10s";
			letTheRobotDoItsThing(10000); //wait 10 seconds to start

			driveForward(LOW_POWER);
			letTheRobotDoItsThing(2800); // go past expected to clear
			kickGuardOut();

			driveBackward(LOW_POWER);
			letTheRobotDoItsThing(1000);

			turnRight(HIGH_POWER);
			letTheRobotDoItsThing(500);

			driveForward(LOW_POWER);
			letTheRobotDoItsThing(1000);
			//look for white line - get
			//turn right and track line
			//move forward tracking line until ultrasonic = ???

			dropClimbers();

		}


	}

	private void initMotors(){
		frontLeftMotor = hardwareMap.dcMotor.get("left_front");
		frontRightMotor = hardwareMap.dcMotor.get("right_front");
		backLeftMotor = hardwareMap.dcMotor.get("left_back");
		backRightMotor = hardwareMap.dcMotor.get("right_back");
		frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
		backRightMotor.setDirection(DcMotor.Direction.REVERSE);
	}

	private void initServos(){
		rightButtonPusher = hardwareMap.servo.get("right_button_push");
		leftButtonPusher = hardwareMap.servo.get("left_button_push");
		dropper = hardwareMap.servo.get("dropper");
		cattleGuard = hardwareMap.servo.get("cattleguard");

		leftButtonPusher.setPosition(BUTTON_ON);
		//leftButtonPusher.setDirection(Servo.Direction.REVERSE);
		cattleGuard.setDirection(Servo.Direction.REVERSE);
		cattleGuard.setPosition(0.0);

	}

	private void initSensors(){
		lineFinder = hardwareMap.lightSensor.get("lineFinder");
		distanceFinder = hardwareMap.ultrasonicSensor.get("distanceFinder");
	}

	private void updateTelemetry(){

		telemetry.addData("Status", robotStatus);
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

	private void letTheRobotDoItsThing(long forMilliseconds) throws InterruptedException{
		updateTelemetry();
		sleep(forMilliseconds);
	}

	private void driveForward (double power) throws InterruptedException{

		robotStatus = "Driving Forward";
		frontLeftMotor.setPower(power);
		backLeftMotor.setPower(power);
		frontRightMotor.setPower(power);
		backRightMotor.setPower(power);
		updateTelemetry();
	}

	private void driveBackward (double power) throws InterruptedException{

		robotStatus = "Driving Backward";
		frontLeftMotor.setPower(-power);
		backLeftMotor.setPower(-power);
		frontRightMotor.setPower(-power);
		backRightMotor.setPower(-power);
		updateTelemetry();
	}



	private void turnRight(double power) throws InterruptedException{

		robotStatus = "Turning Right";
		frontLeftMotor.setPower(power);
		backLeftMotor.setPower(power);
		frontRightMotor.setPower(-power);
		backRightMotor.setPower(-power);
		updateTelemetry();
	}

	private void kickGuardOut() throws InterruptedException{

		robotStatus = "Guard Kick Out";
		cattleGuard.setPosition(1.0);
		letTheRobotDoItsThing(500);
		cattleGuard.setPosition(0.0);
		robotStatus = "Guard Down";
		updateTelemetry();

	}


	private void dropClimbers() throws InterruptedException{

		dropper.setPosition(DROPPER_MAX_POSITION);
		letTheRobotDoItsThing(1000);

		dropper.setPosition(DROPPER_MIN_POSITION);
		robotStatus = "Climbers Dropped";
		updateTelemetry();

	}
}
