package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import android.media.MediaPlayer;
import com.qualcomm.robotcore.hardware.DcMotorController;


/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class GavinBot extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    MediaPlayer mediaPlayer;
    boolean mediaPlaying;

    private void initMediaPlayer()
    {
        String PATH_TO_FILE = "/storage/emulated/0/Music/honk.mp3";
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
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        initMediaPlayer();
        waitForStart();

        for(int i=0; i<1; i++) {
            leftMotor.setPower(1.0);
            rightMotor.setPower(1.0);
            sleep(1000);

            toggleMediaPlayer();
        }

        leftMotor.setPowerFloat();
        rightMotor.setPowerFloat();

    }
}
