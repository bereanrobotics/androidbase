package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class AaronBotAutonomous extends LinearOpMode {
    DcMotor leftdrive;
    DcMotor rightdrive;


    @Override
    public void runOpMode() throws InterruptedException {
        leftdrive = hardwareMap.dcMotor.get("leftdrive");
        rightdrive = hardwareMap.dcMotor.get("rightdrive");
        leftdrive.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
// Position the robot to line up with one wheel against the wall. Position it in the middle of the 4th square from the ramp, angled directly
        // toward the rescue beacon basket. It should dump the guys in the basket so yay!
        for(int i=0; i<1; i++) {
            sleep(10000);
            leftdrive.setPower(-.7);
            rightdrive.setPower(-.7);

            sleep(6500);

            leftdrive.setPower(0);
            rightdrive.setPower(0);

            sleep(1000);

        }


    }
}