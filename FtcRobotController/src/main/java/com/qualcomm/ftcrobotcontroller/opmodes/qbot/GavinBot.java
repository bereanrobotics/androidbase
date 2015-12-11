package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import android.media.MediaPlayer;
import com.qualcomm.robotcore.hardware.DcMotorController;


/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class GavinBot extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor spinner;
    DcMotor armmotor1;
    DcMotor armmotor2;


    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("leftdrive");
        rightMotor = hardwareMap.dcMotor.get("rightdrive");
        spinner = hardwareMap.dcMotor.get("Spinner controller");
        armmotor1 = hardwareMap.dcMotor.get("armmotor1");
        armmotor2 = hardwareMap.dcMotor.get("armmotor2");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
// Position the robot to line up with one wheel against the wall. Position it in the middle of the 4th square from the ramp, angled directly
        // toward the rescue beacon basket. It should dump the guys in the basket so yay!
        for(int i=0; i<1; i++) {
            leftMotor.setPower(1.0);
            rightMotor.setPower(1.0);

            sleep(4500);

            leftMotor.setPower(0);
            rightMotor.setPower(0);
            armmotor1.setPower(-0.2);
            armmotor2.setPower(0.2);

            sleep(2800);

            armmotor1.setPower(0);
            armmotor2.setPower(0);

            sleep(2000);
        }


    }
}