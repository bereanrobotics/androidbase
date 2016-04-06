/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes.aimbot.dopple;

import android.os.Environment;

import com.berean.robotics.aimbot.AimbotDoppleBot;
import com.berean.robotics.dopple.DoppleBot;
import com.berean.robotics.dopple.DoppleBotHistoryRecord;
import com.berean.robotics.dopple.util.DoppleBotHistoryHelper;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class AimbotRubberWheelStraightTest extends LinearOpMode {

	private static String ROBOT_HISTORY_FILE_BASE_NAME = "robotRec-";
	private static String ROBOT_HISTORY_FILE_NAME_PATTERN = "yyMMdd_HHmmss";
	private static String ROBOT_HISTORY_FILE_EXT = ".txt";
	private static String ROBOT_HISTORY_DIRECTORY = "/ROBO_DATA/";
	private static String ROBOT_FILE_NAME = "ab_rubber_straight.txt";
	private static String LOG_TAG = "AIMBOT PLAYBACK FILE TEST - ";

	DoppleBot aimBot;

	DcMotor frontLeftMotor;
	DcMotor frontRightMotor;
	DcMotor backLeftMotor;
	DcMotor backRightMotor;
	Servo rightButtonPusher;
	Servo leftButtonPusher;
	Servo dropper;
	Servo cattleGuard;


	/**
	 * Constructor
	 */
	public AimbotRubberWheelStraightTest() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void runOpMode() throws InterruptedException {

		createAimBotRobot();
		RobotLog.i(LOG_TAG + "initializing");
		aimBot.initializeRobot();

		waitForStart();

		File historyFile = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + ROBOT_HISTORY_DIRECTORY+ROBOT_FILE_NAME );

		//File historyFile = DoppleBotHistoryHelper.lastFileModified(path.getAbsolutePath());

		if (historyFile.exists()){
			DoppleBotHistoryRecord historyToPlay = DoppleBotHistoryHelper.getHistoryFromFile(historyFile);
			aimBot.startPlayback(historyToPlay, this);
		} else RobotLog.w(LOG_TAG + String.format("Couldn't load most recent file from %s", historyFile.toString()));

		aimBot.stopRobot();


	}

	private void createAimBotRobot(){

		RobotLog.i(LOG_TAG + "creating the AimBot robot");

		aimBot = new AimbotDoppleBot(hardwareMap);

		RobotLog.i(LOG_TAG + "constructor, grabbing hardware from robot");
		frontLeftMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.FRONT_LEFT_MOTOR_NAME);
		frontRightMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.FRONT_RIGHT_MOTOR_NAME);
		backLeftMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.BACK_LEFT_MOTOR_NAME);
		backRightMotor = (DcMotor) aimBot.getRobotComponents().get(AimbotDoppleBot.BACK_RIGHT_MOTOR_NAME);
		rightButtonPusher = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.RIGHT_BUTTON_PUSHER_NAME);
		leftButtonPusher = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.LEFT_BUTTON_PUSHER_NAME);
		dropper = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.DROPPER_NAME);
		cattleGuard = (Servo) aimBot.getRobotComponents().get(AimbotDoppleBot.CATTLEGUARD_NAME);
		RobotLog.i(String.format(LOG_TAG + "%d hardware components grabbed.", aimBot.getRobotComponents().size()));

	}




	private void letTheRobotDoItsThing(long forMilliseconds) throws InterruptedException{
		//updateTelemetry();
		sleep(forMilliseconds);
	}

	private DoppleBotHistoryRecord getInstructions(){

		ArrayList<String> historyHeader = new ArrayList<String>();
		historyHeader.add("runtime(ms)");
        historyHeader.add("left_front");
		historyHeader.add("cattleguard");
        historyHeader.add("left_button_push");
        historyHeader.add("left_back");
        historyHeader.add("right_back");
        historyHeader.add("dropper");
        historyHeader.add("right_front");
        historyHeader.add("right_button_push");


		DoppleBotHistoryRecord historyTable = new DoppleBotHistoryRecord(historyHeader);

        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(1859, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(51, 0.13, 0.0, 1.0, 0.13, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(30, 0.25, 0.0, 1.0, 0.25, 0.05, 0.0, 0.05, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(441, 0.25, 0.0, 1.0, 0.25, 0.25, 0.0, 0.25, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(36, 0.13, 0.0, 1.0, 0.13, 0.25, 0.0, 0.25, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(34, 0.03, 0.0, 1.0, 0.03, 0.09, 0.0, 0.09, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(727, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(43, 0.03, 0.0, 1.0, 0.03, -0.01, 0.0, -0.01, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(40, 0.17, 0.0, 1.0, 0.17, -0.06, 0.0, -0.06, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(2175, 0.25, 0.0, 1.0, 0.25, -0.25, 0.0, -0.25, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(28, 0.06, 0.0, 1.0, 0.06, -0.11, 0.0, -0.11, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(25, 0.02, 0.0, 1.0, 0.02, -0.02, 0.0, -0.02, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(243, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(41, 0.0, 0.0, 1.0, 0.0, -0.02, 0.0, -0.02, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(41, 0.0, 0.0, 1.0, 0.0, -0.11, 0.0, -0.11, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(32, 0.04, 0.0, 1.0, 0.04, -0.25, 0.0, -0.25, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(772, 0.25, 0.0, 1.0, 0.25, -0.25, 0.0, -0.25, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(38, 0.13, 0.0, 1.0, 0.13, -0.25, 0.0, -0.25, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(49, 0.0, 0.0, 1.0, 0.0, -0.02, 0.0, -0.02, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(823, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(58, -0.06, 0.0, 1.0, -0.06, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(23, -0.21, 0.0, 1.0, -0.21, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(51, -0.25, 0.0, 1.0, -0.25, -0.02, 0.0, -0.02, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(38, -0.25, 0.0, 1.0, -0.25, -0.13, 0.0, -0.13, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(471, -0.25, 0.0, 1.0, -0.25, -0.25, 0.0, -0.25, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(47, -0.09, 0.0, 1.0, -0.09, -0.13, 0.0, -0.13, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(48, -0.01, 0.0, 1.0, -0.01, -0.03, 0.0, -0.03, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(3115, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(35, 0.0, 0.050980392156862786, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(43, 0.0, 0.14117647058823535, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(39, 0.0, 0.5529411764705883, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(634, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(50, 0.0, 0.14117647058823535, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));

    	return historyTable;
	}
}
