package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class BlueAaronBotAutonomous extends LinearOpMode {
    DcMotor leftdrive;
    DcMotor rightdrive;
    Servo bumper1;
    Servo bumper2;
    double bumper1Position;
    double bumper2Position;
    final double bumper1default = 0.275;
    final double bumper2default = 0.41;


    @Override
    public void runOpMode() throws InterruptedException {
        leftdrive = hardwareMap.dcMotor.get("leftdrive");
        rightdrive = hardwareMap.dcMotor.get("rightdrive");
        bumper1 = hardwareMap.servo.get("bumper1");
        bumper2 = hardwareMap.servo.get("bumper2");
        leftdrive.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
// Position the robot to line up with one wheel against the wall. Position it in the middle of the 4th square from the ramp, angled directly
        // toward the rescue beacon basket. It should dump the guys in the basket so yay!
        for(int i=0; i<1; i++) {
            sleep(10000);
            leftdrive.setPower(-.43);
            rightdrive.setPower(-.7);
            for (int l=0; l<3; l++)
            {
                bumper1Position = bumper1default;
                bumper2Position = bumper2default;
                bumper1.setPosition(bumper1Position);
                bumper2.setPosition(bumper2Position);
                sleep(1000);
                bumper1Position = 1.0;
                bumper2Position = 0;
                bumper1.setPosition(bumper1Position);
                bumper2.setPosition(bumper2Position);
                sleep(1000);
            }
            sleep(1500);

            leftdrive.setPower(0);
            rightdrive.setPower(0);

            sleep(1000);

        }


    }
}