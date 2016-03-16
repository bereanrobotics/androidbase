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

package com.qualcomm.ftcrobotcontroller.opmodes.aimbot.dopple;

import com.berean.robotics.aimbot.AimbotDoppleBot;
import com.berean.robotics.dopple.DoppleBot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class AimbotPlaybackTestOne extends LinearOpMode {


	DoppleBot aimBot;

	DcMotor frontLeftMotor;
	DcMotor frontRightMotor;
	DcMotor backLeftMotor;
	DcMotor backRightMotor;
	Servo rightButtonPusher;
	Servo leftButtonPusher;
	Servo dropper;
	Servo cattleGuard;


	/**
	 * Constructor
	 */
	public AimbotPlaybackTestOne() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void runOpMode() throws InterruptedException {

		createAimBotRobot();
		RobotLog.i("AimbotPlaybackTestOne - initializing with recording off.");
		aimBot.initializeRobot();


		waitForStart();

		for(int i=0; i<1; i++) {
			letTheRobotDoItsThing(692);
			frontLeftMotor.setPower(.05);
			backLeftMotor.setPower(.05);
			letTheRobotDoItsThing(41);
			frontLeftMotor.setPower(.25);
			backLeftMotor.setPower(.25);
			frontRightMotor.setPower(-.05);
			backRightMotor.setPower(-.05);
			letTheRobotDoItsThing(39);
			frontLeftMotor.setPower(.25);
			backLeftMotor.setPower(.25);
			frontRightMotor.setPower(-.25);
			backRightMotor.setPower(-.25);
			letTheRobotDoItsThing(1441);
			frontLeftMotor.setPower(.25);
			backLeftMotor.setPower(.25);
			frontRightMotor.setPower(-.13);
			backRightMotor.setPower(-.13);
			letTheRobotDoItsThing(348);
			frontLeftMotor.setPower(0.0);
			backLeftMotor.setPower(0.0);
			frontRightMotor.setPower(0.0);
			backRightMotor.setPower(0.0);

		}


	}

	private void createAimBotRobot(){

		RobotLog.i("AimbotPlaybackTestOne - creating the AimBot robot");

		aimBot = new AimbotDoppleBot(hardwareMap);

		RobotLog.i("AimbotPlaybackTestOne - constructor, grabbing hardware from robot");
		frontLeftMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.FRONT_LEFT_MOTOR_NAME);
		frontRightMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.FRONT_RIGHT_MOTOR_NAME);
		backLeftMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.BACK_LEFT_MOTOR_NAME);
		backRightMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.BACK_RIGHT_MOTOR_NAME);
		rightButtonPusher = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.RIGHT_BUTTON_PUSHER_NAME);
		leftButtonPusher = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.LEFT_BUTTON_PUSHER_NAME);
		dropper = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.DROPPER_NAME);
		cattleGuard = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.CATTLEGUARD_NAME);
		RobotLog.i(String.format("AimbotPlaybackTestOne - %d hardware components grabbed.", aimBot.getRobotComponents().size()));

	}




	private void letTheRobotDoItsThing(long forMilliseconds) throws InterruptedException{
		//updateTelemetry();
		sleep(forMilliseconds);
	}


}
