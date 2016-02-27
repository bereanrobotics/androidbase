package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class RedAutonomous extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor spinner;
    DcMotor armmotor1;
    DcMotor armmotor2;
    DcMotor turner;


    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("leftdrive");
        rightMotor = hardwareMap.dcMotor.get("rightdrive");
        spinner = hardwareMap.dcMotor.get("spinner");
        turner = hardwareMap.dcMotor.get("turner");
        armmotor1 = hardwareMap.dcMotor.get("armmotor1");
        armmotor2 = hardwareMap.dcMotor.get("armmotor2");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
// Position the robot to line up with one wheel against the wall. Position it in the middle of the 4th square from the ramp, angled directly
        // toward the rescue beacon basket. It should dump the guys in the basket so yay!
        for(int i=0; i<1; i++) {
            sleep(10000);
            leftMotor.setPower(.7);
            rightMotor.setPower(.7);

            sleep(3200);

            leftMotor.setPower(0);
            rightMotor.setPower(0);

            sleep(1000);

        }


    }
}