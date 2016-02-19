package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class AaronBotTeleop extends LinearOpMode {
    DcMotor armturner1;
    DcMotor armturner2;
    DcMotor armlift1;
    DcMotor armlift2;
    DcMotor rightdrive;
    DcMotor leftdrive;
    final double armLowPower = 0.4;
    final double armHighPower = 0.6;
    final double liftLowPower = 0.6;
    final double liftHighPower = 0.8;
    // double armPowerMode = armHighPower;
    // double liftPowerMode = liftHighPower;
    // double armPower = 0;
    // double liftPower = 0;

    boolean armturnOn = false;
    boolean armliftOn = false;
    boolean turnpowerMode = false;
    boolean liftpowerMode = false;


    @Override
    public void runOpMode() throws InterruptedException {

        //initMediaPlayer();

        armturner1 = hardwareMap.dcMotor.get("armturner1");
        armturner2 = hardwareMap.dcMotor.get("armturner2");
        armlift1 = hardwareMap.dcMotor.get("armlift1");
        armlift2 = hardwareMap.dcMotor.get("armlift2");
        rightdrive = hardwareMap.dcMotor.get("rightdrive");
        leftdrive = hardwareMap.dcMotor.get("leftdrive");
        leftdrive.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {


            // Drive Code

            float throttle_r = -gamepad1.right_stick_y;
            float throttle_l = -gamepad1.left_stick_y;
            float right = throttle_r;
            float left = throttle_l;

            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            rightdrive.setPower(right);
            leftdrive.setPower(left);

            // Drive Code End


            // new arm Code
            /*
            if (gamepad1.dpad_right) {
                armPowerMode = armHighPower;
            }
            if (gamepad1.dpad_left) {
                armPowerMode = armLowPower;
            }
            if (gamepad1.a) {
                armPower = armPowerMode;
            } else if (gamepad1.b) {
                armPower = -armPowerMode;
            } else {
                armPower = 0;
            }
            armturner1.setPower(-armPower);
            armturner2.setPower(armPower);

            // lift code
            if (gamepad1.dpad_up) {
                liftPowerMode = liftHighPower;
            }
            if (gamepad1.dpad_down) {
                liftPowerMode = liftLowPower;
            }
            if (gamepad1.x) {
                liftPower = liftPowerMode;
            } else if (gamepad1.y) {
                liftPower = -liftPowerMode;
            } else {
                liftPower = 0;
            }
            armlift1.setPower(-armPower);
            armlift2.setPower(armPower);

            */

            // Arm Turn around Code



            if (gamepad1.a) {
                if (turnpowerMode)
                {
                    armturner1.setPower(-armHighPower);
                    armturner2.setPower(armHighPower);
                }
                else
                {
                    armturner1.setPower(-armLowPower);
                    armturner2.setPower(armLowPower);
                }
                armturnOn = true;
            }

            else if (gamepad1.b) {
                if (turnpowerMode)
                {
                    armturner1.setPower(armHighPower);
                    armturner2.setPower(-armHighPower);
                }
                else
                {
                    armturner1.setPower(armLowPower);
                    armturner2.setPower(-armLowPower);
                }
                armturnOn = true;
            }

            else {
                if (armturnOn) {
                    armturner1.setPower(0);
                    armturner2.setPower(0);
                    armturnOn = false;
                }
            }

            // Arm Turner Code End

            // Arm Lift Code Begin


            if (gamepad1.x) {
                if (liftpowerMode)
                {
                    armlift1.setPower(-liftHighPower);
                    armlift2.setPower(liftHighPower);
                }
                else
                {
                    armlift1.setPower(-liftLowPower);
                    armlift2.setPower(liftLowPower);
                }
                armliftOn = true;
            }

            else if (gamepad1.y) {
                if (liftpowerMode)
                {
                    armlift1.setPower(liftHighPower);
                    armlift2.setPower(-liftHighPower);
                }
                else
                {
                    armlift1.setPower(liftLowPower);
                    armlift2.setPower(-liftLowPower);
                }
                armliftOn = true;
            }

            else {
                if (armliftOn) {
                    armlift1.setPower(0);
                    armlift2.setPower(0);
                    armliftOn = false;
                }
            }

            // Arm Lift Code End

            // Power Mode for Arm Turner

            if (gamepad1.dpad_right) {
                turnpowerMode = true;
            }

            if (gamepad1.dpad_left) {
                turnpowerMode = false;
            }

            //Power Mode for Arm Turner End

            // Power Mode for Arm Lifter

            if (gamepad1.dpad_up) {
                liftpowerMode = true;
            }

            if (gamepad1.dpad_down) {
                liftpowerMode = false;
            }

            // Power Mode for Arm Lifter End

		    /*
		    * Send telemetry data back to driver station.
		    */
            telemetry.addData("left tgt pwr", "left drive power: " + String.format("%.2f", left));
            telemetry.addData("tgt pwr", "right drive power: " + String.format("%.2f", right));

            waitOneFullHardwareCycle();
        }

    }
}

