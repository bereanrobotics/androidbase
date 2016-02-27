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
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class TestBotTeleOp extends OpMode {

    DcMotorController motorController;
	DcMotor motorRight;
	DcMotor motorLeft;
    final int channelLeft = 1;
    final int channelRight = 2;
	IrSeekerSensor irSeeker;
	OpticalDistanceSensor distanceSensor;
	LightSensor light;
	//ColorSensor color;
	Boolean hasMotors = false;
	/**
	 * Constructor
	 */
	public TestBotTeleOp() {

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
		
		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *   
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        //motorController = hardwareMap.dcMotorController.get("drive_controller");
		motorRight = hardwareMap.dcMotor.get("right");
		motorLeft = hardwareMap.dcMotor.get("left");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);
		motorController = motorRight.getController();  // get the controller using one of the motors
        //reset_drive_encoders();

		//color = hardwareMap.colorSensor.get("color");
		//color.enableLed(false);
		light = hardwareMap.lightSensor.get("light");

		// turn on LED of light sensor.
		light.enableLed(true);

        //irSeeker = hardwareMap.irSeekerSensor.get("sensor_ir");
		//distanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_distance");

/*
		PWMOutput fan = hardwareMap.pwmOutput.get("fan");
		int time = 0;
		int period = 0;
		fan.setPulseWidthOutputTime(time);
		fan.setPulseWidthPeriod(period);
*/
		}

    //--------------------------------------------------------------------------
    //
    // run_using_encoders
    //
    /**
     * Sets both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_using_encoders ()
    {
        DcMotorController.RunMode l_mode = motorController.getMotorChannelMode(channelLeft);
        if (l_mode == DcMotorController.RunMode.RESET_ENCODERS)
        {
            motorController.setMotorChannelMode(channelLeft, DcMotorController.RunMode.RUN_USING_ENCODERS );
        }

        l_mode = motorController.getMotorChannelMode( channelRight );
        if (l_mode == DcMotorController.RunMode.RESET_ENCODERS)
        {
            motorController.setMotorChannelMode(channelRight, DcMotorController.RunMode.RUN_USING_ENCODERS);
        }

    } // PushBotAuto::run_using_encoders

    //--------------------------------------------------------------------------
    //
    // reset_drive_encoders
    //
    /**
     * Resets both drive wheel encoders.
     */
    public void reset_drive_encoders ()
    {
        //
        // Reset the motor encoders on the drive wheels.
        //
        motorController.setMotorChannelMode(channelLeft, DcMotorController.RunMode.RESET_ENCODERS);

        motorController.setMotorChannelMode(channelRight, DcMotorController.RunMode.RESET_ENCODERS);

    } // PushBotAuto::reset_drive_encoders


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
        //run_using_encoders ();

		// throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
		// 1 is full down
		// direction: left_stick_x ranges from -1 to 1, where -1 is full left
		// and 1 is full right
		float throttle = -gamepad1.left_stick_y;
		float direction = gamepad1.left_stick_x;
		float right = throttle - direction;
		float left = throttle + direction;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);


		// write the values to the motors
		motorRight.setPower(right);
		motorLeft.setPower(left);

		/*
		 * Send telemetry data back to driver station.
		 */
        telemetry.addData("Text", "*** TestBot Data***");
		//telemetry.addData("Color", "Color: "+ String.format("r=%d,g=%d,b=%d,", color.red(),color.green(), color.blue()));
        //telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        // telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
		//.addData("Seeker", irSeeker.toString());
        //telemetry.addData("Distance", "Distance: "+ String.format("%.2f", distanceSensor.getLightDetected()));
        telemetry.addData("Light", "Light: "+ String.format("%d", light.getLightDetectedRaw()));
		//reflection = reflectedLight.getLightLevel();
        //telemetry.addData("encoders",  String.format("Encoders Left:%d,Right:%d", motorLeft.getCurrentPosition(),motorRight.getCurrentPosition()));
        //telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        //telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

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
