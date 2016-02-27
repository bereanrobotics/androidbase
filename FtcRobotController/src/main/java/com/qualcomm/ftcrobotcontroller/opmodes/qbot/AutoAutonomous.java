package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;


/*
 * Delay, drive forward and stop when close to a wall.

 */
public class AutoAutonomous extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    UltrasonicSensor sonic;
    DigitalChannel switch1;


    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("leftdrive");
        rightMotor = hardwareMap.dcMotor.get("rightdrive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        sonic = hardwareMap.ultrasonicSensor.get("sonic");
        switch1 = hardwareMap.digitalChannel.get("sw1");
        switch1.setMode(DigitalChannelController.Mode.INPUT);

        waitForStart();
// Position the robot to line up with one wheel against the wall. Position it in the middle of the 4th square from the ramp, angled directly
        // toward the rescue beacon basket. It should dump the guys in the basket so yay!

        if (switch1.getState()) { //wait for 10 seconds
            sleep(10000);
        }
        leftMotor.setPower(.7);
        rightMotor.setPower(.7);

        double distance = 256;
        while(distance > 10) {
            double temp = sonic.getUltrasonicLevel();
            if (temp < 255)
                distance = temp;
            waitOneFullHardwareCycle();
        }

        leftMotor.setPower(0);
        rightMotor.setPower(0);


    }
}