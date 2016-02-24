package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

import android.media.MediaPlayer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class LukeBotTeleOp extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor armlift1;
    DcMotor armlift2;
    DcMotor armturner1;
    DcMotor armturner2;
    //Servo arm;
    boolean liftOn;
    boolean turnOn;


    @Override
    public void runOpMode() throws InterruptedException {

        leftMotor = hardwareMap.dcMotor.get("leftdrive");
        rightMotor = hardwareMap.dcMotor.get("rightdrive");
        armlift1 = hardwareMap.dcMotor.get("armlift1");
        armlift2 = hardwareMap.dcMotor.get("armlift2");
        armturner1 = hardwareMap.dcMotor.get("armturner1");
        armturner2 = hardwareMap.dcMotor.get("armturner2");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        liftOn = false;
        turnOn = false;
        waitForStart();

        while (opModeIsActive()) {

            float throttle_r = -gamepad1.right_stick_y;
            float throttle_l = -gamepad1.left_stick_y;
            float right = throttle_r;
            float left = throttle_l;

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            // write the values to the motors
            rightMotor.setPower(right);
            leftMotor.setPower(left);

            if (gamepad2.a) {
                armlift1.setPower(-0.2);
                armlift2.setPower(0.2);
                liftOn = true;
            } else if (gamepad2.b) {
                armlift1.setPower(0.2);
                armlift2.setPower(-0.2);
                liftOn = true;
            } else {
                if (liftOn) {
                    armlift1.setPower(0);
                    armlift2.setPower(0);
                    liftOn = false;
                }
            }
            if (gamepad2.x) {
                armturner1.setPower(-0.2);
                armturner2.setPower(0.2);
                turnOn = true;
            } else if (gamepad2.y) {
                armturner1.setPower(0.2);
                armturner2.setPower(-0.2);
                turnOn = true;
            } else {
                if (turnOn) {
                    armturner1.setPower(0);
                    armturner2.setPower(0);
                    turnOn = false;
                }
            }


		    /*
		    * Send telemetry data back to driver station.
		    */
            telemetry.addData("Text", "*** TestBot Data***");
            telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
            telemetry.addData("tgt pwr", "right pwr: " + String.format("%.2f", right));

            waitOneFullHardwareCycle();
        }

    }
}
