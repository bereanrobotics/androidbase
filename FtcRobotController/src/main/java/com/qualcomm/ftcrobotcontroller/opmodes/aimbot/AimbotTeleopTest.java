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
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class AimbotTeleopTest extends OpMode {

	final static double PUSHER_MIN = 0.0;
	final static double PUSHER_MAX = 1.0;

	DcMotorController.DeviceMode devMode;
	DcMotor frontLeft;
	DcMotor frontRight;
	DcMotor backLeft;
	DcMotor backRight;
	Servo rightButtonPusher;
	Servo leftButtonPusher;
	Servo dropper;
	Servo cattleGuard;

	double leftPosition = 0.0;
	double rightPosition = 0.0;
	double leftButtonDelta = 0.0;
	double rightButtonDelta = 0.0;

	boolean isRightPushed = false;
	boolean isLeftPushed = false;

	/**
	 * Constructor
	 */
	public AimbotTeleopTest() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        frontLeft = hardwareMap.dcMotor.get("left_front");
        frontRight = hardwareMap.dcMotor.get("right_front");
        backLeft = hardwareMap.dcMotor.get("left_back");
        backRight = hardwareMap.dcMotor.get("right_back");

		rightButtonPusher = hardwareMap.servo.get("right_button_push");
		leftButtonPusher = hardwareMap.servo.get("left_button_push");
		dropper = hardwareMap.servo.get("dropper");
		cattleGuard = hardwareMap.servo.get("cattleguard");


        //frontMotorController = hardwareMap.dcMotorController.get("wheels");
        //devMode = DcMotorController.DeviceMode.WRITE_ONLY;

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
		leftButtonPusher.setDirection(Servo.Direction.REVERSE);

		rightButtonPusher.setPosition(1.0);
		leftButtonPusher.setPosition(1.0);

		rightButtonPusher.setPosition(0.5);
		leftButtonPusher.setPosition(0.5);

		rightButtonPusher.setPosition(1.0);
		leftButtonPusher.setPosition(1.0);

		rightButtonPusher.setPosition(0.5);
		leftButtonPusher.setPosition(0.5);
			/*
        frontRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        */


    }
	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void start() {
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot.
		 *   
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */

		//devMode = DcMotorController.DeviceMode.WRITE_ONLY;
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		/*
		 * Gamepad 1
		 * 
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);
		
		// write the values to the motors
		frontLeft.setPower(left);
		backLeft.setPower(left);
		frontRight.setPower(right);
		backRight.setPower(right);

		// button pusher RIGHT
		if (gamepad1.b) {
			rightButtonPusher.setPosition(1.0);
		} else rightButtonPusher.setPosition(0.0);

		// button pusher left
		if (gamepad1.x) {
			leftButtonPusher.setPosition(1.0);
		} else leftButtonPusher.setPosition(0.0);

		if (gamepad1.a) {
			cattleGuard.setPosition(1.0);
		} else cattleGuard.setPosition(0.0);

		if (gamepad1.y) {
			dropper.setPosition(1.0);
		} else dropper.setPosition(0.0);

		// button pusher RIGHT
		/*if (gamepad1.y) {
			// if the Y button is pushed on gamepad1, decrease the position of
			// the arm servo.
			armPosition -= armDelta;
		}

        // update the position of the claw
        if (gamepad1.left_bumper) {
            clawPosition += clawDelta;
        }

        if (gamepad1.left_trigger > 0.25) {
            clawPosition -= clawDelta;
        }

        if (gamepad1.b) {
            clawPosition -= clawDelta;
        }

		// update the position of the big poopoo monster
		if (gamepad1.x) {
			clawPosition += clawDelta;
		}

		if (gamepad1.b) {
			clawPosition -= clawDelta;
		}

			// clip the position values so that they never exceed their allowed range.
		armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
		clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);

		// write position values to the wrist and claw servo
		arm.setPosition(armPosition);
		claw.setPosition(clawPosition);


		*/


		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

		telemetry.addData("Text", "*** Robot Data***");
		telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
		telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
		telemetry.addData("left pusher", "left val: " + String.format("%.2f", leftButtonPusher.getPosition()));
		telemetry.addData("right pusher", "right val: " + String.format("%.2f", rightButtonPusher.getPosition()));

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}
	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
		
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
