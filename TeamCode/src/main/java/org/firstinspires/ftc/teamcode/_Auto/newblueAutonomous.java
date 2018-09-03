package org.firstinspires.ftc.teamcode._Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="newblueAutomousOp", group="Autonomous")
public class newblueAutonomous extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();
    // Declare the motor matrix
    private DcMotor[][] motors = new DcMotor[2][2];
    // Set default power to 100%
    private float motorPower = 1.f;

    @Override
    public void runOpMode(){
        // Set up the motor matrix (that sounds cool)
        motors[0][0] = hardwareMap.dcMotor.get("frontLeft");
        motors[0][1] = hardwareMap.dcMotor.get("frontRight");
        motors[1][0] = hardwareMap.dcMotor.get("backLeft");
        motors[1][1] = hardwareMap.dcMotor.get("backRight");
        // The motors on the left side of the robot need to be in reverse mode
        for(DcMotor[] motor : motors){
            motor[0].setDirection(DcMotor.Direction.REVERSE);
        }
        // Being explicit never hurt anyone, right?
        for(DcMotor[] motor : motors){
            motor[1].setDirection(DcMotor.Direction.FORWARD);
        }

        waitForStart();

        //Kill ten seconds
        runtime.reset();
        while(runtime.seconds()<10); runtime.reset();

        while(runtime.milliseconds()<700){
            // Loop through front and back motors
            for(DcMotor[] motor : motors){
                // Set left motor power
                motor[0].setPower(100);
                // Set right motor power
                motor[1].setPower(100);
            }
        }

        while(runtime.milliseconds()<200){
            // Loop through front and back motors
            for(DcMotor[] motor : motors){
                // Set left motor power
                motor[0].setPower(-100);
                // Set right motor power
                motor[1].setPower(-100);
            }
        }

        runtime.reset();
        // Loop through front and back motors
        for(DcMotor[] motor : motors){
            // Set left motor power
            motor[0].setPower(0);
            // Set right motor power
            motor[1].setPower(0);
        }
    }
}
