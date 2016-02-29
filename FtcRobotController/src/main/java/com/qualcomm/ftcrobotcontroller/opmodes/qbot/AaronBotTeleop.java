package com.qualcomm.ftcrobotcontroller.opmodes.qbot;

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
public class AaronBotTeleop extends LinearOpMode {
    DcMotor armturner1;
    DcMotor armturner2;
    DcMotor armlift1;
    DcMotor armlift2;
    DcMotor rightdrive;
    DcMotor leftdrive;
    DcMotor dumper;
    Servo bumper1;
    Servo bumper2;
    Servo basket;
    MediaPlayer mediaPlayer;
    boolean mediaPlaying;
    final double armLowPower = 0.2;
    final double armHighPower = 0.6;
    final double liftLowPower = 0.8;
    final double liftHighPower = 1;
    final double basketdefault = 0.35;
    final double bumper1default = 0.275;
    final double bumper2default = 0.41;
    float throttle_r;
    float throttle_l;
    float arm_throttle;
    float waitfortime = 0;
    // double armPowerMode = armHighPower;
    // double liftPowerMode = liftHighPower;
    // double armPower = 0;
    // double liftPower = 0;

    boolean armturnOn = false;
    boolean sniperOn = false;
    boolean armliftOn = false;
    boolean dumpOn = false;
    boolean turnpowerMode = false;
    boolean liftpowerMode = true;
    boolean timerOn = false;
    double bumper1Position;
    double bumper2Position;
    double basketPosition;
    boolean getoutthewaymode = false;
    boolean reversemode = false;

    private void initMediaPlayer() {
        String ALT_PATH_TO_FILE = "/storage/emulated/0/Music/honk.mp3";
        mediaPlayer = new MediaPlayer();
        mediaPlaying = false;
        try {
            telemetry.addData("Media1", ALT_PATH_TO_FILE);
            mediaPlayer.setDataSource(ALT_PATH_TO_FILE);
            mediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
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
    }



    @Override
    public void runOpMode() throws InterruptedException {

        initMediaPlayer();

        armturner1 = hardwareMap.dcMotor.get("armturner1");
        armturner2 = hardwareMap.dcMotor.get("armturner2");
        armlift1 = hardwareMap.dcMotor.get("armlift1");
        armlift2 = hardwareMap.dcMotor.get("armlift2");
        rightdrive = hardwareMap.dcMotor.get("rightdrive");
        leftdrive = hardwareMap.dcMotor.get("leftdrive");
        dumper = hardwareMap.dcMotor.get("dumper");
        bumper1 = hardwareMap.servo.get("bumper1");
        bumper2 = hardwareMap.servo.get("bumper2");
        basket = hardwareMap.servo.get("basket");
        leftdrive.setDirection(DcMotor.Direction.REVERSE);
        bumper1Position = bumper1default;
        bumper2Position = bumper2default;
        basketPosition = basketdefault;
        waitForStart();

        while (opModeIsActive()) {


            // Drive Code
            if (reversemode)
            {
                throttle_r = gamepad1.right_stick_y;
                throttle_l = gamepad1.left_stick_y;
            }
            else
            {
                throttle_r = -gamepad1.right_stick_y;
                throttle_l = -gamepad1.left_stick_y;
            }
            float right = throttle_r;
            float left = throttle_l;

            //Sniper Mode Control
            if (!sniperOn) {
                // clip the right/left values so that the values never exceed +/- 1
                right = Range.clip(right, -1, 1);
                left = Range.clip(left, -1, 1);
            }
            else {
                // clip the right/left values so that the values never exceed +/- .3
                right = Range.clip(right, -.4f, .4f);
                left = Range.clip(left, -.4f, .4f);
            }
            //Sniper Mode Control End

            rightdrive.setPower(right);
            leftdrive.setPower(left);


            // Drive Code End


            // Arm Turn around Code


            if (gamepad1.y) {
                dumper.setPower(0.4);
                dumpOn = true;
            }

            else if (gamepad1.x) {
                dumper.setPower(-0.4);
                dumpOn = true;
            }

            else {
                if (dumpOn) {
                    dumper.setPower(0);
                    dumpOn = false;
                }
            }

            // Arm Turner Code End

            // Arm Turn around Code



            if (gamepad2.a) {
                if (turnpowerMode)
                {
                    armturner1.setPower(-armHighPower);
                    armturner2.setPower(armHighPower);
                }
                else
                {
                    armturner1.setPower(-armLowPower);
                    armturner2.setPower(armLowPower);
                }
                armturnOn = true;
            }

            else if (gamepad2.b) {
                if (turnpowerMode)
                {
                    armturner1.setPower(armHighPower);
                    armturner2.setPower(-armHighPower);
                }
                else
                {
                    armturner1.setPower(armLowPower);
                    armturner2.setPower(-armLowPower);
                }
                armturnOn = true;
            }
            else if (gamepad2.right_trigger > 0)
            {
                arm_throttle = Range.clip(gamepad2.right_trigger, 0f, 1.0f);
                float triggerarmpower = arm_throttle / 2.5f;
                armturner1.setPower(triggerarmpower);
                armturner2.setPower(-triggerarmpower);
                armturnOn = true;
            }
            else {
                if (armturnOn) {
                    armturner1.setPower(0);
                    armturner2.setPower(0);
                    armturnOn = false;
                }

            }

            // Arm Turner Code End

            // Arm Lift Code Begin


            if (gamepad2.x) {
                if (liftpowerMode)
                {
                    armlift1.setPower(liftHighPower);
                    armlift2.setPower(-liftHighPower);
                }
                else
                {
                    armlift1.setPower(liftLowPower);
                    armlift2.setPower(-liftLowPower);
                }
                armliftOn = true;
            }

            else if (gamepad2.y) {
                if (liftpowerMode)
                {
                    armlift1.setPower(-liftHighPower);
                    armlift2.setPower(liftHighPower);
                }
                else
                {
                    armlift1.setPower(-liftLowPower);
                    armlift2.setPower(liftLowPower);
                }
                armliftOn = true;
            }

            else {
                if (armliftOn) {
                    armlift1.setPower(0);
                    armlift2.setPower(0);
                    armliftOn = false;
                }
            }

            // Arm Lift Code End

            // Power Mode for Arm Turner

            if (gamepad2.right_bumper) {
                turnpowerMode = true;
            }

            if (gamepad2.left_bumper) {
                turnpowerMode = false;
            }

            //Power Mode for Arm Turner End

            // Power Mode for Arm Lifter

            if (gamepad2.dpad_up) {
                liftpowerMode = true;
            }

            if (gamepad2.dpad_down) {
                liftpowerMode = false;
            }

            // Power Mode for Arm Lifter End

            // Bumper Code for pushing debris into the collector
            if (gamepad1.right_trigger > 0) {
                //move the servos to hit the ziplines
                bumper1Position = 1.0;
                bumper2Position = 0;
            }
            else if (!getoutthewaymode)
            {
                //DEFAULT MODE CODE
                bumper1Position = bumper1default;
                bumper2Position = bumper2default;
            }
            /*if (waitfortime > 0)
            {
                waitfortime -= 1;
                if (waitfortime <= 0)
                {
                    bumper2Position = bumper2default;
                }
            }*/
            if (gamepad1.left_trigger > 0)
            {
                getoutthewaymode = true;
                bumper1Position = 0.71f;
                bumper2Position = 0.24f;
            }
            else
            {
                getoutthewaymode = false;
            }
            bumper1.setPosition(bumper1Position);
            bumper2.setPosition(bumper2Position);

            // Bumper Code End

            //Basket on arm Code
            if (gamepad2.dpad_left) {
                basketPosition = .6f;
            }
            else if (gamepad2.dpad_right) {
                basketPosition = .1f;
            }
            else
            {
                basketPosition = basketdefault;
            }

            basket.setPosition(basketPosition);

            //Basket on arm Code


            //Sniper Mode Trigger Code

            if (gamepad1.right_bumper) {
                if (sniperOn) {
                    sniperOn = false;
                } else  {
                    sniperOn = true;
                }
            }

            //Sniper Mode Trigger Code End

            //Reverse Mode Control
            if (gamepad1.left_bumper) {
                if (reversemode) {
                    reversemode = false;
                } else  {
                    reversemode = true;
                }
            }
            //Reverse Mode Control Code End

            //Music Code Optional


            if (gamepad1.b)
            {
                toggleMediaPlayer();
            }

            //Music Code End

		    /*
		    * Send telemetry data back to driver station.
		    */
            telemetry.addData("left tgt pwr", "left drive power: " + String.format("%.2f", left));
            telemetry.addData("tgt pwr", "right drive power: " + String.format("%.2f", right));

            waitOneFullHardwareCycle();
        }
        stopMediaPlayer();

    }
}

