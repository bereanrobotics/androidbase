package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
//import android.media.MediaPlayer;

/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class QBotTeleop extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor armmotor1;
    DcMotor armmotor2;
    DcMotor spinner;
    DcMotor turner;
    Servo zipLine1;
    Servo zipLine2;
    //Servo arm;
    //MediaPlayer mediaPlayer;
    //boolean mediaPlaying;
    boolean armOn;
    boolean reverseOn = false;
    boolean sniperOn = false;

    // position of the arm servo.
    double zipPosition1;
    double zipPosition2;
    // amount to change the arm servo position.
    double zipDelta1 = -0.8D;
    double zipDelta2 = 0.7D;


    /*private void initMediaPlayer() {
        String PATH_TO_FILE = "/storage/emulated/0/Music/honk.mp3";
        //String ALT_PATH_TO_FILE = "/storage/emulated/0/Music/Sickmemes.mp3";
        mediaPlayer = new MediaPlayer();
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
             e.printStackTrace();
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
    }*/


    @Override
    public void runOpMode() throws InterruptedException {

        //initMediaPlayer();

        leftMotor = hardwareMap.dcMotor.get("leftdrive");
        rightMotor = hardwareMap.dcMotor.get("rightdrive");
        armmotor1 = hardwareMap.dcMotor.get("armmotor1");
        armmotor2 = hardwareMap.dcMotor.get("armmotor2");
        spinner = hardwareMap.dcMotor.get("spinner");
        turner = hardwareMap.dcMotor.get("turner");
        zipLine1 = hardwareMap.servo.get("servo1");
        zipLine2 = hardwareMap.servo.get("servo2");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        armOn = false;
        waitForStart();

        // assign the starting position of the servos
        zipPosition1 = 1.0;
        zipPosition2 = 0;

        while (opModeIsActive()) {

            //Change Direction
            if (gamepad1.right_bumper) {
                if (reverseOn) {
                    reverseOn = false;
                } else if (!reverseOn) {
                    reverseOn = true;
                }
            }

            float throttle_r = -gamepad1.right_stick_y;
            float throttle_l = -gamepad1.left_stick_y;
            float right = throttle_r;
            float left = throttle_l;

            //This is for sniper mode
            if (gamepad1.a) {
                if (sniperOn) {
                    sniperOn = false;
                } else  {
                    sniperOn = true;
                }
            }
            if (!sniperOn) {
                // clip the right/left values so that the values never exceed +/- 1
                right = Range.clip(right, -1, 1);
                left = Range.clip(left, -1, 1);

                // write the values to the motors
                rightMotor.setPower(right);
                leftMotor.setPower(left);
            }
            else if (sniperOn) {
                // clip the right/left values so that the values never exceed +/- .5
                right = Range.clip(right, -.5f, .5f);
                left = Range.clip(left, -.5f, .5f);

                // write the values to the motors
                rightMotor.setPower(right);
                leftMotor.setPower(left);
            }


            if (gamepad2.dpad_down) {
                //move the servos to hit the ziplines
                zipPosition1 = 0.2D;
                zipPosition2 = 0.7D;
            }

            if (gamepad2.dpad_up) {
                zipPosition1 = 1.0;
                zipPosition2 = 0;
            }

            if (gamepad2.y) {
                spinner.setPower(-0.9);
            } else if (gamepad2.x) {
                spinner.setPower(0.9);
            } else {
                if (!gamepad2.y && !gamepad2.x) {
                    spinner.setPower(0);
                }
            }
            if (gamepad2.right_bumper) {
                turner.setPower(-0.5);
            }
            else if (gamepad2.left_bumper) {
                turner.setPower(0.5);
            }
            else {
                if (!gamepad2.right_bumper && !gamepad2.left_bumper) {
                    turner.setPower(0);
                }
            }

            if (gamepad2.a) {
                armmotor1.setPower(-0.2);
                armmotor2.setPower(0.2);
                armOn = true;
            } else if (gamepad2.b) {
                armmotor1.setPower(0.2);
                armmotor2.setPower(-0.2);
                armOn = true;
            } else {
                if (armOn) {
                    armmotor1.setPower(0);
                    armmotor2.setPower(0);
                    armOn = false;
                }
            }

            //if (gamepad2.a){
            //    spinnerMotor.setPower(1);
            //}
            //if (gamepad2.x){
            //    spinnerMotor.setPower(-1);
            //}
            //if (!gamepad2.a && !gamepad2.x){
            //    spinnerMotor.setPowerFloat();
            //}


            // write position values to the wrist and claw servo
            zipLine1.setPosition(zipPosition1);
            zipLine2.setPosition(zipPosition2);

            //if (gamepad1.left_bumper) {
            //    toggleMediaPlayer();
            //}


		    /*
		    * Send telemetry data back to driver station.
		    */
            telemetry.addData("Text", "*** TestBot Data***");
            telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
            telemetry.addData("tgt pwr", "right pwr: " + String.format("%.2f", right));

            waitOneFullHardwareCycle();

            //stopMediaPlayer();
        }

    }
}

