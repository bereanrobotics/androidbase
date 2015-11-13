package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import android.media.MediaPlayer;

/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class GavinBotTeleop extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor armmotor1;
    DcMotor armmotor2;
    Servo zipLine1;
    Servo zipLine2;
    //Servo arm;
    MediaPlayer mediaPlayer;
    boolean mediaPlaying;
    boolean armOn;
    boolean reverseOn = false;
    // TETRIX VALUES.
    final static double ARM_MIN_RANGE  = 0.20;
    final static double ARM_MAX_RANGE  = 0.90;

    // position of the arm servo.
    double zipPosition1;
    double zipPosition2;
    // amount to change the arm servo position.
    double zipDelta1 = 0.5;
    double zipDelta2 = 0.5;


    private void initMediaPlayer()
    {
        String PATH_TO_FILE = "/storage/emulated/0/Music/honk.mp3";
        //String ALT_PATH_TO_FILE = "/storage/emulated/0/Music/Sickmemes.mp3";
        mediaPlayer = new  MediaPlayer();
        mediaPlaying = false;
        try {
            telemetry.addData("Media1", PATH_TO_FILE);
            mediaPlayer.setDataSource(PATH_TO_FILE);
            mediaPlayer.prepare();
        /*} catch (IllegalArgumentException e) {
            telemetry.addData("Media", e.getMessage());
            e.printStackTrace();
        } catch (IllegalStateException e) {
             telemetry.addData("Media", e.getMessage());
             e.printStackTrace(); */
        } catch (java.io.IOException e) {
             telemetry.addData("Media", e.getMessage());
             e.printStackTrace();
        }
    }

    private void stopMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void toggleMediaPlayer() {

        if (!mediaPlaying) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            mediaPlaying = true;
        } else {
            mediaPlayer.pause();
            mediaPlaying = false;
         }
    }


    @Override
    public void runOpMode() throws InterruptedException {

        initMediaPlayer();

        leftMotor = hardwareMap.dcMotor.get("leftdrive");
        rightMotor = hardwareMap.dcMotor.get("rightdrive");
        armmotor1 = hardwareMap.dcMotor.get("armmotor1");
        armmotor2 = hardwareMap.dcMotor.get("armmotor2");
        zipLine1 = hardwareMap.servo.get("servo1");
        zipLine2 = hardwareMap.servo.get("servo2");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        armOn = false;
        waitForStart();

        boolean b_down = false;

        while (opModeIsActive()) {

            float throttle_r = -gamepad1.left_stick_y;
            float throttle_l = -gamepad1.right_stick_y;
            float right = throttle_r;
            float left = throttle_l;


            // assign the starting position of the wrist and claw
            zipPosition1 = 0;
            zipPosition2 = 0;

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            // write the values to the motors
            if (!reverseOn) {
                rightMotor.setPower(-right);
                leftMotor.setPower(-left);
            }
            if (reverseOn) {
                rightMotor.setPower(right);
                leftMotor.setPower(left);
            }

            if (gamepad1.a) {
                // move the arms to hit the ziplines
                zipPosition1 += zipDelta1;
                zipPosition2 += zipDelta2;
            }
            if (gamepad1.right_bumper) {
                if (reverseOn) {
                    reverseOn = false;
                }
                if (!reverseOn) {
                    reverseOn = true;
                }
            }

            if (gamepad1.y) {
                armmotor1.setPower(0.2);
                armmotor2.setPower(-0.2);
                armOn = true;
            } else if (gamepad1.b) {
                armmotor1.setPower(-0.2);
                armmotor2.setPower(0.2);
                armOn = true;
            }
            else {
                if (armOn) {
                    armmotor1.setPower(0);
                    armmotor2.setPower(0);
                    armOn = false;
                }
            }


            // clip the position values so that they never exceed their allowed range.
            zipPosition1 = Range.clip(zipPosition1, ARM_MIN_RANGE, ARM_MAX_RANGE);
            zipPosition2 = Range.clip(zipPosition2, ARM_MIN_RANGE, ARM_MAX_RANGE);

            // write position values to the wrist and claw servo
            zipLine1.setPosition(zipPosition1);
            zipLine2.setPosition(zipPosition2);

            if (gamepad1.left_bumper) {
                if (!b_down) { // don't do this if the key is still down
                    toggleMediaPlayer();
                    b_down = true;
                }
            } else {
                b_down = false;
            }
		    /*
		    * Send telemetry data back to driver station.
		    */
            telemetry.addData("Text", "*** TestBot Data***");
            telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
            telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

            waitOneHardwareCycle();
        }

        stopMediaPlayer();
    }
}
