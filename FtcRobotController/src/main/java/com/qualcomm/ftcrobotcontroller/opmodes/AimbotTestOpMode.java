package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class AimbotTestOpMode extends LinearOpMode {

    private static final String LEFT_FRONT_MOTOR_NAME = "left_front";
    private static final String RIGHT_FRONT_MOTOR_NAME = "right_front";
    private static final String LEFT_BACK_MOTOR_NAME =  "left_back";
    private static final String RIGHT_BACK_MOTOR_NAME = "right_back";
    private static final int CLOCKWISE = 1;
    private static final int COUNTER_CLOCKWISE = -1;
    private static final double FORWARD_POWER = 1.0;
    private static final double TURN_POWER = .5;
    private static final long FORWARD_TIME_IN_MS = 1000;
    private static final long RIGHT_ANGLE_TURN_TIME_IN_MS = 500;
    private static final long HOLD_TIME_IN_MS = 1000;

    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;

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

        leftFrontMotor = hardwareMap.dcMotor.get(LEFT_FRONT_MOTOR_NAME);
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftFrontMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        rightFrontMotor = hardwareMap.dcMotor.get(RIGHT_FRONT_MOTOR_NAME);
        rightFrontMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        leftBackMotor = hardwareMap.dcMotor.get(LEFT_BACK_MOTOR_NAME);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        rightBackMotor = hardwareMap.dcMotor.get(RIGHT_BACK_MOTOR_NAME);
        rightBackMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

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
        leftFrontMotor.setPower(FORWARD_POWER);
        leftBackMotor.setPower(FORWARD_POWER);
        rightFrontMotor.setPower(FORWARD_POWER);
        rightBackMotor.setPower(FORWARD_POWER);
        letTheRobotDoItsThing(forTimeInMilliseconds);
    }

    private void makeRightAngleTurn(int direction) throws InterruptedException{
        leftFrontMotor.setPower((TURN_POWER * direction));
        leftBackMotor.setPower((TURN_POWER * direction));
        rightFrontMotor.setPower((-TURN_POWER * direction));
        rightBackMotor.setPower((-TURN_POWER * direction));
        letTheRobotDoItsThing(RIGHT_ANGLE_TURN_TIME_IN_MS);
    }

    private void shutDownMotors(){
        leftFrontMotor.setPowerFloat();
        leftBackMotor.setPowerFloat();
        rightFrontMotor.setPowerFloat();
        rightBackMotor.setPowerFloat();
    }

    private void holdStillFor(long forTimeInMilliseconds) throws InterruptedException{
        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
        letTheRobotDoItsThing(forTimeInMilliseconds);
    }

    private void letTheRobotDoItsThing(long forTimeInMilliseconds) throws InterruptedException{
        sleep(forTimeInMilliseconds);
    }
}
