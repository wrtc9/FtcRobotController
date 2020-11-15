package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.depricated.CalibrationInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
//Copy pasted for now because I don't know what I need to import

@Teleop(name = "DKTeleOpV1", group = "TeleOp")
public class DLKTeleOpV1 extends OpMode {
	public MovementHandler movementHandler;
	
	private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;

	private double rotation;
	private double xCheck;
	private double yCheck;
	private double rt;
	
	@Override
	public void init() {
		//TODO Go back and relearn objects and inheritence
		movementHandler = new MovementHandler();
		
		leftFront = movementHandler.getDcMotor("leftFront");
        rightFront = movementHandler.getDcMotor("rightFront");
        leftRear = movementHandler.getDcMotor("leftRear");
        rightRear = movementHandler.getDcMotor("rightRear");
		
		//probably not absolutely needed to reset variables, but it doesn't hurt
		rotation = 0;
		rt = 0;
		lt = 0;
		xCheck = 0;
		yCheck = 0;
	}
	
	@Override
	public void loop() {
		xCheck = deadzone(gamepad1.left_stick_x, .1);
		yCheck = deadzone(gamepad1.left_stick_y, .1);
		rt = deadzone(gamepad1.right_trigger, .1);
		lt = deadzone(gamepad1.left_trigger, .1);
		rotation = (rt - lt);
		movementHandler.move(xCheck, yCheck, rotation);
	}

	//Checks to see if the value is above a certain threshold so that we can have a deadzone on the joystick and triggers
	private double deadzone(variable, deadzone){
		if(Math.abs(variable) < deadzone) {
			return 0;
		} else {
			return variable;
		}
	}
	/* Just in case movement handler doesn't work I'll leave this here
	private void move(x,y){
		leftFront.setPower();
		leftRear.setPower();
		rightFront.setPower();
		rightRear.setPower()
	}
	*/
}
