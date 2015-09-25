package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    MediaPlayer mediaPlayer;
    boolean mediaPlaying;

    private void initMediaPlayer()
    {
        String PATH_TO_FILE = "/storage/emulated/0/Music/JohnCena.mp3";
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

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        boolean b_down = false;

        while (opModeIsActive()) {

            float throttle = -gamepad1.left_stick_y;
            float direction = gamepad1.left_stick_x;
            float right = throttle - direction;
            float left = throttle + direction;

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            // write the values to the motors
            rightMotor.setPower(right);
            leftMotor.setPower(left);

            if (gamepad1.b) {
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

