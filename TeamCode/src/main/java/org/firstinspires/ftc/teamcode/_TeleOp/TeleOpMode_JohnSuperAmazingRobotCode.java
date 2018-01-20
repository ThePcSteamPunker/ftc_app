/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode._TeleOp;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="TeleOpMode_JohnSuperAmazingRobotCode", group="Iterative Opmode")
public class TeleOpMode_JohnSuperAmazingRobotCode extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftfrontDrive = null;
    private DcMotor rightfrontDrive = null;
    private DcMotor leftbackDrive = null;
    private DcMotor rightbackDrive = null;
    private Servo jewelArm = null;
    private double jewelArmPos = 0;
    private double jewelArmChange = 0;
    private double gripperPos = 0;
    private double gripperChange = 0;
    private BNO055IMU imu;
    private DcMotor gripperLeft;
    private DcMotor gripperRight;
    private DcMotor gripperLift;
    private DcMotor gripperTurn;

    private boolean debugLeftFrontDrive = false;
    private boolean debugRightFrontDrive = false;
    private boolean debugLeftBackDrive = false;
    private boolean debugRightBackDrive = false;
    private boolean debugjewelArm = false;
    private boolean debugImu = false;
    private boolean debugGripperLeft = false;
    private boolean debugGripperRight = false;
    private boolean debugGripperLift = false;
    private boolean debugGripperTurn = false;

    private int triggerSet;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        triggerSet = 0;

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        try {
            leftfrontDrive = hardwareMap.get(DcMotor.class, "frontLeft");
            leftfrontDrive.setDirection(DcMotor.Direction.FORWARD);
            leftfrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (IllegalArgumentException iax) {
            debugLeftFrontDrive = true;
            telemetry.addData("IllegalArgumentException", "frontLeft");
        }
        try{
            rightfrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
            rightfrontDrive.setDirection(DcMotor.Direction.REVERSE);
            rightfrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (IllegalArgumentException iax) {
            debugRightFrontDrive = true;
            telemetry.addData("IllegalArgumentException", "frontRight");
        }
        try{
            leftbackDrive = hardwareMap.get(DcMotor.class, "backLeft");
            leftbackDrive.setDirection(DcMotor.Direction.FORWARD);
            leftbackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (IllegalArgumentException iax) {
            debugLeftBackDrive = true;
            telemetry.addData("IllegalArgumentException", "backLeft");
        }
        try{
            rightbackDrive = hardwareMap.get(DcMotor.class, "backRight");
            rightbackDrive.setDirection(DcMotor.Direction.REVERSE);
            rightbackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (IllegalArgumentException iax) {
            debugRightBackDrive = true;
            telemetry.addData("IllegalArgumentException", "backRight");
        }
        try{
            jewelArm = hardwareMap.get(Servo.class, "jewelArm");
            jewelArm.setPosition(0.5);
        }
        catch (IllegalArgumentException iax) {
            debugjewelArm = true;
            telemetry.addData("IllegalArgumentException", "jewelArm");
        }
        try {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json";
            parameters.loggingEnabled      = true;
            parameters.loggingTag          = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
            //gripper.setPosition(0);
        }
        catch (IllegalArgumentException iax) {
            debugImu = true;
            telemetry.addData("IllegalArgumentException", "imu");
        }
        try{
            gripperLeft = hardwareMap.get(DcMotor.class, "gripperLeft");
        }
        catch(IllegalArgumentException iax){
            debugGripperLeft = true;
            telemetry.addData("IllegalArgumentException", "gripperLeft");
        }
        try{
            gripperRight = hardwareMap.get(DcMotor.class, "gripperRight");
        }
        catch(IllegalArgumentException iax){
            debugGripperRight = true;
            telemetry.addData("IllegalArgumentException", "gripperRight");
        }
        try{
            gripperLift = hardwareMap.get(DcMotor.class, "gripperLift");
        }
        catch(IllegalArgumentException iax){
            debugGripperLift = true;
            telemetry.addData("IllegalArgumentException", "gripperLift");
        }
        try{
            gripperTurn = hardwareMap.get(DcMotor.class, "gripperTurn");
        }
        catch(IllegalArgumentException iax){
            debugGripperTurn = true;
            telemetry.addData("IllegalArgumentException", "gripperTurn");
        }

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        if (!debugLeftFrontDrive) {
            leftfrontDrive.setPower(0);
        }
        if(!debugRightFrontDrive){
            rightfrontDrive.setPower(0);
        }
        if(!debugLeftBackDrive){
            leftbackDrive.setPower(0);
        }
        if(!debugRightBackDrive){
            rightbackDrive.setPower(0);
        }

        gripperChange = 0;
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double powerLeftY;
        double powerRightX;
        double powerLeftX;
        double powerLeftFront = 0;
        double powerRightFront = 0;
        double powerLeftBack = 0;
        double powerRightBack = 0;
        double powerLeft = 0;
        double powerRight = 0;
        double powerMax = 1;
        double powerGripperLeft;
        double powerGripperRight;
        double leftTrigger;
        double rightTrigger;
        boolean xButton;
        boolean bButton;
        boolean padRight = false;
        boolean padLeft = false;
        boolean triggerLeft = false;
        boolean bumperLeft = false;
        double gripperIncrement = .01;


        powerLeftY = -gamepad1.left_stick_y;
        powerRightX = -gamepad1.right_stick_x;
        powerLeftX = gamepad1.left_stick_x;
        padLeft = gamepad1.dpad_left;
        padRight = gamepad1.dpad_right;
        triggerLeft = gamepad1.right_bumper;
        bumperLeft = gamepad1.left_bumper;
        powerLeftX = scaleInput(powerLeftX, powerMax);
        powerRightX = scaleInput(powerRightX, powerMax);

        powerGripperLeft = gamepad2.left_stick_y;
        powerGripperRight = -gamepad2.right_stick_y;
        leftTrigger = gamepad2.left_trigger;
        rightTrigger = gamepad2.right_trigger;
        xButton = gamepad2.x;
        bButton = gamepad2.b;
        powerGripperLeft = scaleInput(powerGripperLeft, powerMax);
        powerGripperRight = scaleInput(powerGripperRight, powerMax);


        powerMax = Collections.max(Arrays.asList(powerMax, powerLeftFront, powerLeftBack, powerRightFront, powerRightBack, powerLeftX));

        boolean allZero = (abs(powerRightX) == 0) && (abs(powerLeftY) == 0) && (abs(powerLeftX) == 0);
        double powerTotal = (abs(powerRightX) + abs(powerLeftY) + abs(powerLeftX)) > powerMax ? (abs(powerRightX) + abs(powerLeftY) + abs(powerLeftX)) : powerMax;

        powerLeftFront = allZero ? 0 : ((-powerRightX + powerLeftY + powerLeftX) / (powerTotal * powerMax));
        powerLeftBack = allZero ? 0 : ((-powerRightX + powerLeftY - powerLeftX) / (powerTotal * powerMax));
        powerRightFront = allZero ? 0 : ((powerRightX + powerLeftY - powerLeftX) / (powerTotal * powerMax));
        powerRightBack = allZero ? 0 : ((powerRightX + powerLeftY + powerLeftX) / (powerTotal * powerMax));

        if (triggerLeft && jewelArmPos > 0) {
            jewelArmChange = -gripperIncrement;
        } else if (bumperLeft && jewelArmPos <= 1) {
            jewelArmChange = gripperIncrement;
        }

        if (padLeft) {
            gripperChange = -gripperIncrement;
        } else if (padRight) {
            gripperChange = gripperIncrement;
        } else {
            gripperChange = 0;
        }

        gripperPos = gripperPos + gripperChange;
        jewelArmPos = jewelArmPos + jewelArmChange;

        gripperPos = gripperPos > 1 ? 1 : gripperPos < 0 ? 0 : gripperPos;

        if (!debugLeftFrontDrive) {
            leftfrontDrive.setPower(powerLeftFront);
        }
        if (!debugRightFrontDrive) {
            rightfrontDrive.setPower(powerRightFront);
        }
        if (!debugLeftBackDrive) {
            leftbackDrive.setPower(powerLeftBack);
        }
        if (!debugRightBackDrive) {
            rightbackDrive.setPower(powerRightBack);
        }
        if (!debugjewelArm) {
            jewelArm.setPosition(.5);
        }
        }
        if(!debugGripperLeft){
            gripperLeft.setPower(powerGripperLeft);
        }
        if(!debugGripperRight){
            gripperRight.setPower(powerGripperRight);
        }
        if(!debugGripperLift){
            if(leftTrigger > 0){
                gripperLift.setPower(-leftTrigger);
            }
            else if(rightTrigger > 0){
                gripperLift.setPower(rightTrigger);
            }
        }
        if(!debugGripperTurn) {
            if (gamepad2.x == true && triggerSet == 0) {
                runtime.reset();
                while(runtime.seconds() < 2){
                    gripperTurn.setPower(3);
                }
                triggerSet = 1;
            }
            else if (triggerSet == 1 && gamepad2.x == false){
                triggerSet = 0;
            }

            if (gamepad2.b == true && triggerSet == 0) {
                runtime.reset();
                while(runtime.seconds() < 2){
                    gripperTurn.setPower(-3);
                }
                triggerSet = 1;
            }
            else if (triggerSet == 1 && gamepad2.b == false){
                triggerSet = 0;
            }
        }
        //add gripperTurn

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "leftFront (%.2f), rightFront (%.2f), leftBack (%.2f), rightBack (%.2f)", powerLeftFront, powerRightFront, powerLeftBack, powerRightBack);
        telemetry.addData("padRight", padRight);
        telemetry.addData("padLeft", padLeft);
        telemetry.addData("powerMax", powerMax);
        telemetry.addData("allZero", allZero);
        telemetry.addData("powerTotal", powerTotal);
        telemetry.addData("powerLeftX", powerLeftX);
        telemetry.addData("powerRightX", powerRightX);
        telemetry.addData("powerLeftY", powerLeftY);
        telemetry.addData("powerGripperLeft", powerGripperLeft);
        telemetry.addData("powerGripperRight", powerGripperLeft);
        telemetry.addData("leftTrigger", leftTrigger);
        telemetry.addData("rightTrigger", rightTrigger);
        telemetry.addData("xButton", xButton);
        telemetry.addData("bButton", bButton);
        if (!debugImu) {
            final Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addLine()
                    .addData("status", imu.getSystemStatus().toShortString())
                    .addData("calib", imu.getCalibrationStatus().toString())
                    .addData("heading", formatAngle(angles.angleUnit, angles.firstAngle));
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    double scaleInput(double dVal, double max_controller)  {
        return pow(dVal, 3)/pow(max_controller, 3);
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
