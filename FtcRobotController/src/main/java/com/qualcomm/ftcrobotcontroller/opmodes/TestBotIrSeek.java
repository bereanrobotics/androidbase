package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Jordan Burklund on 7/30/2015.
 * An example linear op mode where the pushbot
 * will track an IR beacon.
 */
public class TestBotIrSeek extends LinearOpMode {
    final static double kBaseSpeed = 0.15;  // Higher values will cause the robot to move faster

    final static double kMinimumStrength = 0.01; // Higher values will cause the robot to follow closer
    final static double kMaximumStrength = 0.50; // Lower values will cause the robot to stop sooner

    IrSeekerSensor irSeeker;
    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        irSeeker = hardwareMap.irSeekerSensor.get("sensor_ir");
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor = hardwareMap.dcMotor.get("right_drive");

        // Wait for the start button to be pressed
        waitForStart();

        // Continuously track the IR beacon
        while(opModeIsActive()) {
            double angle = irSeeker.getAngle() / 30;  // value between -4...4
            double strength = irSeeker.getStrength();
            if (strength>kMinimumStrength && strength<kMaximumStrength) {
                double leftSpeed = Range.clip(kBaseSpeed + (angle / 32), -1, 1);
                double rightSpeed = Range.clip(kBaseSpeed - (angle / 32), -1, 1);
                leftMotor.setPower(leftSpeed);
                rightMotor.setPower(rightSpeed);
            } else {
                leftMotor.setPower(0);
                rightMotor.setPower(0);
            }
            telemetry.addData("Seeker", irSeeker.toString());
            telemetry.addData("Speed", " Left=" + leftMotor.getPower() + " Right=" + rightMotor.getPower());

            //Wait one hardware cycle to avoid taxing the processor
            waitOneHardwareCycle();
        }

    }
}
