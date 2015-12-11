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
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;


public class AimbotResQTeleop extends OpMode {


	DcMotorController.DeviceMode devMode;
	DcMotor leftSideMotor;
	DcMotor rightSideMotor;
	float sniperMode;
	DcMotor conveyorMotor;
	DcMotor hookMotor1;
	DcMotor hookMotor2;
	DcMotor leftTilter;
	DcMotor rightTilter;
	DcMotor armPivot;



	public AimbotResQTeleop() {

	}

	@Override
	public void init() {



        leftSideMotor = hardwareMap.dcMotor.get("left");
        rightSideMotor = hardwareMap.dcMotor.get("right");
		conveyorMotor = hardwareMap.dcMotor.get("conveyor");
		hookMotor1 = hardwareMap.dcMotor.get("hook1");
		hookMotor2 = hardwareMap.dcMotor.get("hook2");
		leftTilter = hardwareMap.dcMotor.get("leftTilter");
		rightTilter = hardwareMap.dcMotor.get("rightTilter");
		armPivot = hardwareMap.dcMotor.get("armPivot");


		sniperMode = 1;

        rightSideMotor.setDirection(DcMotor.Direction.REVERSE);
		hookMotor2.setDirection(DcMotor.Direction.REVERSE);

        rightSideMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        leftSideMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		conveyorMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		hookMotor1.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		hookMotor2.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		leftTilter.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		rightTilter.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		armPivot.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);


    }

	@Override
	public void start() {
	}


	@Override
	public void loop() {

		if (gamepad2.x) armPivot.setPower(0.5);
		if (gamepad2.b) armPivot.setPower(-0.5);
		if (!gamepad2.x && !gamepad2.b) armPivot.setPower(0);

		if (gamepad2.y) {
			hookMotor1.setPower(0.5);
			hookMotor2.setPower(0.5);
		}
		if (gamepad2.a) {
			hookMotor1.setPower(-0.5);
			hookMotor2.setPower(-0.5);
		}
		if (!gamepad2.y && !gamepad2.a) {
			hookMotor1.setPower(0);
			hookMotor2.setPower(0);
		}

		float leftTilt = gamepad2.left_stick_x;
		float rightTilt = gamepad2.right_stick_x;

		leftTilt = Range.clip(leftTilt, -1, 1);
		rightTilt = Range.clip(rightTilt, -1, 1);

		leftTilt = (float)scaleInput(leftTilt);
		rightTilt = (float)scaleInput(rightTilt);

		leftTilter.setPower(leftTilt);
		rightTilter.setPower(rightTilt);


        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

		if (gamepad1.x) sniperMode = 1.0f;
		if (gamepad1.y) sniperMode = .6f;
		if (gamepad1.b) sniperMode = .3f;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);

		if (gamepad2.right_trigger > 0) conveyorMotor.setPower(1);
		if (gamepad2.left_trigger > 0) conveyorMotor.setPower(-1);
		if (gamepad2.right_trigger == 0 && gamepad2.left_trigger == 0) conveyorMotor.setPower(0);

		
		// write the values to the motors
		leftSideMotor.setPower(left * sniperMode);
		rightSideMotor.setPower(right * sniperMode);


		telemetry.addData("Text", "*** Robot Data***");
		telemetry.addData("Snipermode value", "Snipermode value " + String.format("%.2f",sniperMode));
		telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
		telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
	}


	@Override
	public void stop() {

		conveyorMotor.setPowerFloat();
		leftSideMotor.setPowerFloat();
		rightSideMotor.setPowerFloat();
		armPivot.setPowerFloat();
		hookMotor1.setPowerFloat();
		hookMotor2.setPowerFloat();
		leftTilter.setPowerFloat();
		rightTilter.setPowerFloat();


	}
	

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
