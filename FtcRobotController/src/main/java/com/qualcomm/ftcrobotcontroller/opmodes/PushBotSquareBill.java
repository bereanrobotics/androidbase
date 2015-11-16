package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class PushBotSquareBill extends LinearOpMode {

    private static final String LEFT_MOTOR_NAME = "left_drive";
    private static final String RIGHT_MOTOR_NAME = "right_drive";
    private static final int CLOCKWISE = 1;
    private static final int COUNTER_CLOCKWISE = -1;
    private static final double FORWARD_POWER = 1.0;
    private static final double TURN_POWER = .5;
    private static final long FORWARD_TIME_IN_MS = 1000;
    private static final long RIGHT_ANGLE_TURN_TIME_IN_MS = 700;
    private static final long HOLD_TIME_IN_MS = 1000;

    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        setupMotors();

        waitForStart();

        driveInASquareClockwise();

        holdStillFor(HOLD_TIME_IN_MS);

        driveInASquareCounterClockwise();

        shutDownMotors();

    }

    private void setupMotors(){

        leftMotor = hardwareMap.dcMotor.get(LEFT_MOTOR_NAME);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        rightMotor = hardwareMap.dcMotor.get(RIGHT_MOTOR_NAME);
        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }

    private void driveInASquareClockwise() throws InterruptedException{

        for(int totalTurns=0; totalTurns<4; totalTurns++) {
            moveForwardFor(FORWARD_TIME_IN_MS);
            makeRightAngleTurn(CLOCKWISE);
        }

    }

    private void driveInASquareCounterClockwise() throws InterruptedException{

        for(int totalTurns=0; totalTurns<4; totalTurns++) {
            moveForwardFor(FORWARD_TIME_IN_MS);
            makeRightAngleTurn(COUNTER_CLOCKWISE);
        }

    }

    private void moveForwardFor(long forTimeInMilliseconds) throws InterruptedException{
        leftMotor.setPower(FORWARD_POWER);
        rightMotor.setPower(FORWARD_POWER);
        letTheRobotDoItsThing(forTimeInMilliseconds);
    }

    private void makeRightAngleTurn(int direction) throws InterruptedException{
        leftMotor.setPower((TURN_POWER*direction));
        rightMotor.setPower((-TURN_POWER*direction));
        letTheRobotDoItsThing(RIGHT_ANGLE_TURN_TIME_IN_MS);
    }

    private void shutDownMotors(){
        leftMotor.setPowerFloat();
        rightMotor.setPowerFloat();
    }

    private void holdStillFor(long forTimeInMilliseconds) throws InterruptedException{
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        letTheRobotDoItsThing(forTimeInMilliseconds);
    }

    private void letTheRobotDoItsThing(long forTimeInMilliseconds) throws InterruptedException{
        sleep(forTimeInMilliseconds);
    }
}
