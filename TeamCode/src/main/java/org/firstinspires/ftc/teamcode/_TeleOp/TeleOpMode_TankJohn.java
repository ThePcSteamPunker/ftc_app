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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

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

@TeleOp(name="TeleOpMode_TankJohn", group="Iterative Opmode")
public class TeleOpMode_TankJohn extends OpMode
{
    boolean debug = false;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftfrontDrive = null;
    private DcMotor rightfrontDrive = null;
    private DcMotor leftbackDrive = null;
    private DcMotor rightbackDrive = null;
    private Servo servo = null;
    private GyroSensor gyro = null;

    int x;
    int triggerSet;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        x = 0;
        triggerSet = 0;
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        try {
            leftfrontDrive = hardwareMap.get(DcMotor.class, "frontLeft");
            rightfrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
            leftbackDrive = hardwareMap.get(DcMotor.class, "backLeft");
            rightbackDrive = hardwareMap.get(DcMotor.class, "backRight");
            servo = hardwareMap.get(Servo.class, "gripper");
            gyro = hardwareMap.get(GyroSensor.class, ""); //Find name on device

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            leftfrontDrive.setDirection(DcMotor.Direction.REVERSE);
            rightfrontDrive.setDirection(DcMotor.Direction.FORWARD);
            leftbackDrive.setDirection(DcMotor.Direction.REVERSE);
            rightbackDrive.setDirection(DcMotor.Direction.FORWARD);
        }
        catch (IllegalArgumentException iax) {
            debug = true;
        }
        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        leftfrontDrive.setPower(0);
        rightfrontDrive.setPower(0);
        leftbackDrive.setPower(0);
        rightbackDrive.setPower(0);
        servo.setPosition(0);
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftFrontPower = 0;
        double leftRearPower = 0;
        double rightFrontPower = 0;
        double rightRearPower = 0;
        double gripper = 0;
        double joyStick;
        double gyroValue;
        String nothing = "";


        // Choose to drive using either Tank Mode, or POV Mode
        // Comment out the method that's not used.  The default below is POV.

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.
       // double drive = -gamepad1.left_stick_y;
        //double turn  =  gamepad1.right_stick_x;
        //leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        //rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

        // Tank Mode uses one stick to control each wheel.
        // - This requires no math, but it is hard to drive forward slowly and keep straight.
         joyStick  = -gamepad1.left_stick_y;
         //leftRearPower = -gamepad1.;
         //rightFrontPower = -gamepad1.y;
         //rightRearPower = -gamepad1.y;
         //gripper = -gamepad1.y;

         if((gamepad1.right_trigger > 0) && (triggerSet == 0)){
             x = x + 1;
             triggerSet = 1;
             if(x > 4){
                 x = 0;
             }
         }
         else if((triggerSet == 1) && (gamepad1.right_trigger == 0)) {
             triggerSet = 0;
         }

         if(x == 0){
             leftFrontPower = joyStick;
         }
         else if(x == 1){
             rightFrontPower = joyStick;
        }
        else if(x == 2){
             leftRearPower = joyStick;
         }
         else if(x == 3){
            rightRearPower = joyStick;
         }
         else if(x == 4){
             gripper = Math.abs(joyStick);
         }

        // Send calculated power to wheels
        if(debug == false) {
            leftfrontDrive.setPower(leftFrontPower);
            rightfrontDrive.setPower(rightFrontPower);
            leftbackDrive.setPower(leftRearPower);
            rightbackDrive.setPower(rightRearPower);
            servo.setPosition(gripper);
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftFrontPower, rightFrontPower);
        //telemetry.addData("TriggerSet", triggerSet);
        telemetry.addData("x", x);
        if(x == 0){
            telemetry.addData("Front left motor activated", nothing);
        }
        else if(x == 1){
            telemetry.addData("Front right motor activated", nothing);
        }
        else if(x == 2){
            telemetry.addData("Rear left motor activated", nothing);
        }
        else if(x == 3){
            telemetry.addData("Rear right motor activated", nothing);
        }
        else if(x == 4) {
            telemetry.addData("Gripper activated", nothing);
        }
        telemetry.addData("leftFrontPower", leftFrontPower);
        telemetry.addData("rightFrontPower", rightFrontPower);
        telemetry.addData("leftRearPower", leftRearPower);
        telemetry.addData("rightRearPower", rightRearPower);
        telemetry.addData("Gripper power", gripper);
        telemetry.addData("joyStick", joyStick);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
