package com.qualcomm.ftcrobotcontroller.opmodes.learn;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class AlvinsOpMode extends LinearOpMode {
    DcMotor leftMotorF;
    DcMotor leftMotorB;
    DcMotor rightMotorF;
    DcMotor rightMotorB;

    @Override
    public void runOpMode() throws InterruptedException {

        leftMotorF = hardwareMap.dcMotor.get("leftFront");
        leftMotorB = hardwareMap.dcMotor.get("leftBack");
        rightMotorF = hardwareMap.dcMotor.get("rightFront");
        rightMotorB = hardwareMap.dcMotor.get("rightBack");
        leftMotorF.setDirection(DcMotor.Direction.REVERSE);
        leftMotorF.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotorF.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        waitForStart();

        leftMotorF.setPower(.5);
        leftMotorB.setPower(.5);
        rightMotorF.setPower(.5);
        rightMotorB.setPower(.5);

        sleep(10000);

        leftMotorF.setPower(0);
        leftMotorB.setPower(0);
        rightMotorF.setPower(0);
        rightMotorB.setPower(0);

        /* for(int i=0; i<4; i++) {
            leftMotorF.setPower(1.0);
            rightMotorF.setPower(1.0);
            sleep(1000);

            leftMotorF.setPower(0.5);
            rightMotorF.setPower(-0.5);

            sleep(500);
        } */

        leftMotorF.setPowerFloat();
        leftMotorB.setPowerFloat();
        rightMotorF.setPowerFloat();
        rightMotorB.setPowerFloat();

    }
}
