package com.berean.robotics.aimbot;

import com.berean.robotics.dopple.DoppleBot;
import com.berean.robotics.dopple.DoppleBotHistoryRecord;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * The Aimbot Robot represents the structure and state of the physical Aimbot robot.
 * It extends the DoppleBot class to inherit the ability to record and playback
 * movement and operations.
 *
 * Created by wdhoward on 3/3/16.
 */
public class AimbotDoppleBot extends DoppleBot {

    /**
     * Constants used to universally identify the robots components as configured in
     * the robot controller settings file.  Once defined here, they may be accessed
     * from the opmodes using the constants.  When the name changes, just change it here.
     */
    public final static String FRONT_LEFT_MOTOR_NAME = "left_front";
    public final static String FRONT_RIGHT_MOTOR_NAME = "right_front";
    public final static String BACK_LEFT_MOTOR_NAME = "left_back";
    public final static String BACK_RIGHT_MOTOR_NAME = "right_back";
    public final static String RIGHT_BUTTON_PUSHER_NAME = "right_button_push";
    public final static String LEFT_BUTTON_PUSHER_NAME = "left_button_push";
    public final static String DROPPER_NAME = "dropper";
    public final static String CATTLEGUARD_NAME = "cattleguard";

    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;
    Servo rightButtonPusher;
    Servo leftButtonPusher;
    Servo dropper;
    Servo cattleGuard;

    HardwareMap hardwareMap;

    /**
     * Constructor called by opMode which will map all of the robot's hardware into the
     * DoppleBot robotComponents property. You must pass in the hardware map from the
     * opmode, as the hardware map doesn't seem to initialize properly outside of the opmode
     * context.
     *
     * Use this class across all opmodes to consistently start and initialize the robot's component
     * hardware.
     *
     * @param opModeHardwareMap
     */
    public AimbotDoppleBot(HardwareMap opModeHardwareMap) {

        RobotLog.i("AIMBOT ROBOT - constructor");
        hardwareMap = opModeHardwareMap;
        createMotors();
        createServos();

    }

    /**
     * initializeRobot implements the abstract method from DoppleBot
     * good place to initialize the component start positions.
     *
     * Call from the init() method of an OpMode class.
     */
    public void initializeRobot(){

        initServos();
        initMotors();


    }

    /**
     * startRobot implements the abstract method from DoppleBot
     * good place to execute run once code for when the robot is first started.
     *
     * Call from the start() method of an OpMode class.
     */
    public void startRobot(){

    }

    /**
     * stopRobot implements the abstract method from DoppleBot.
     * Stop the robot and cleanup whatever needs to be cleaned up.
     *
     * Call from the stop() method of an OpMode class.
     */
    public void stopRobot(){

    }


    /**
     * used to create all the motors and map back to class variables
     * Also adds these to the robotComponents list for further use (required for recording)
     */
    private void createMotors(){

        RobotLog.i("AIMBOT ROBOT - creating motors");

        frontLeftMotor = hardwareMap.dcMotor.get(FRONT_LEFT_MOTOR_NAME);
        robotComponents.put(FRONT_LEFT_MOTOR_NAME,frontLeftMotor);

        frontRightMotor = hardwareMap.dcMotor.get(FRONT_RIGHT_MOTOR_NAME);
        robotComponents.put(FRONT_RIGHT_MOTOR_NAME, frontRightMotor);

        backLeftMotor = hardwareMap.dcMotor.get(BACK_LEFT_MOTOR_NAME);
        robotComponents.put(BACK_LEFT_MOTOR_NAME, backLeftMotor);

        backRightMotor = hardwareMap.dcMotor.get(BACK_RIGHT_MOTOR_NAME);
        robotComponents.put(BACK_RIGHT_MOTOR_NAME, backRightMotor);

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    /**
     * use to create all the servers and map back to class variables
     * Also adds these to the robotComponents list for further use (required for recording)
     */
    private void createServos(){

        RobotLog.i("AIMBOT ROBOT - creating servos");

        rightButtonPusher = hardwareMap.servo.get(RIGHT_BUTTON_PUSHER_NAME);
        robotComponents.put(RIGHT_BUTTON_PUSHER_NAME, rightButtonPusher);

        leftButtonPusher = hardwareMap.servo.get(LEFT_BUTTON_PUSHER_NAME);
        robotComponents.put(LEFT_BUTTON_PUSHER_NAME, leftButtonPusher);

        dropper = hardwareMap.servo.get(DROPPER_NAME);
        robotComponents.put(DROPPER_NAME, dropper);

        cattleGuard = hardwareMap.servo.get(CATTLEGUARD_NAME);
        robotComponents.put(CATTLEGUARD_NAME, cattleGuard);

        cattleGuard.setDirection(Servo.Direction.REVERSE);
    }

    private void initMotors(){

    }

    private void initServos(){

        leftButtonPusher.setPosition(1.0);
        cattleGuard.setPosition(0.0);

    }

    /**
     * special AimBot only class (you must cast a DoppleBot to an AimbotDoppleBot to use).
     * This class sets up the instructions for a fast speed 360degree spin.
     *
     * This is an example of how to 'playback' from a recording.  The .addHistoryValueRow
     * calls include data pulled from the output file of a 360degree spin.
     *
     * @return a DoppleBotHistoryRecord instance with all the necessary instructions
     */

    public DoppleBotHistoryRecord getFastSpinInstructions(){
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

        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(39, 0.11, 0.0, 1.0, 0.11, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(29, 0.44, 0.0, 1.0, 0.44, -0.18, 0.0, -0.18, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(2240, 1.0, 0.0, 1.0, 1.0, -1.0, 0.0, -1.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(38, 0.2, 0.0, 1.0, 0.2, -0.14, 0.0, -0.14, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(59, 0.0, 0.0, 1.0, 0.0, 0.05, 0.0, 0.05, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(868, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(35, -0.09, 0.0, 1.0, -0.09, 0.3, 0.0, 0.3, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(34, -0.44, 0.0, 1.0, -0.44, 1.0, 0.0, 1.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(61, -1.0, 0.0, 1.0, -1.0, 1.0, 0.0, 1.0, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(26, -0.37, 0.0, 1.0, -0.37, 0.37, 0.0, 0.37, 0.0)));
        historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(36, -0.18, 0.0, 1.0, -0.18, 0.11, 0.0, 0.11, 0.0)));
        //historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(477, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        //historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(62, 0.11, 0.0, 1.0, 0.11, -0.09, 0.0, -0.09, 0.0)));
        //historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(27, 0.69, 0.0, 1.0, 0.69, -0.69, 0.0, -0.69, 0.0)));
        //historyTable.addHistoryValueRow(new ArrayList(Arrays.asList(39, 0.3, 0.0, 1.0, 0.3, -0.14, 0.0, -0.14, 0.0)));

        return historyTable;
    }
}