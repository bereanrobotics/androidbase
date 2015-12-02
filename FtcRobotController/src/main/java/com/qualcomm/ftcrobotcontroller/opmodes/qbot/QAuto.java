package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;

/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class QAuto extends LinearOpMode {

    final static double MOTOR_POWER = 0.15; // Higher values will cause the robot to move faster
    final static double LIGHT_THRESHOLD = 170;

    DcMotor motorRight;
    DcMotor motorLeft;
    LightSensor reflectedLight;

    @Override
    public void runOpMode() throws InterruptedException {

        setupMotors();

        waitForStart();

        lineSearch();

        followLine();
    }

    private void setupMotors(){


        motorRight = hardwareMap.dcMotor.get("right_drive");
        motorLeft = hardwareMap.dcMotor.get("left_drive");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);


        reflectedLight = hardwareMap.lightSensor.get("light_sensor");

        // turn on LED of light sensor.
        reflectedLight.enableLed(true);

    }

    private void lineSearch(){



    }

    private void followLine() throws InterruptedException{

        while(opModeIsActive()) {
            int reflection = 0;
            double left, right = 0.0;

 		/*
		 * read the light sensor.
		 */

            //reflection = reflectedLight.getLightLevel();
            reflection = reflectedLight.getLightDetectedRaw();

		/*
		 * compare measured value to threshold.
		 */
            if (reflection < LIGHT_THRESHOLD) {
			/*
			 * if reflection is less than the threshold value, then assume we are above dark spot.
			 * turn to the right.
			 */
                left = MOTOR_POWER;
                right = 0.0;
            } else {
			/*
			 * assume we are over a light spot.
			 * turn to the left.
			 */
                left = 0.0;
                right = MOTOR_POWER;
            }

		/*
		 * set the motor power
		 */
            motorRight.setPower(left);
            motorLeft.setPower(right);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

            telemetry.addData("Text", "*** Robot Data***");
            telemetry.addData("reflection", "reflection:  " + Double.toString(reflection));
            telemetry.addData("left tgt pwr",  "left  pwr: " + Double.toString(left));
            telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(right));
            //Wait one hardware cycle to avoid taxing the processor
            waitOneHardwareCycle();
        }
    }
}
