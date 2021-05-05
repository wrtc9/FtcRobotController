package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
//Copy pasted for now because I don't know what I need to import

@TeleOp (name = "DKTeleOpV1", group = "TeleOp")
public class DLKTeleOpV1 extends OpMode {
	public MovementHandler movementHandler;

	private double rotation;
	private double xCheck;
	private double yCheck;
	private double rt;
	private double lt;
	
	private DcMotor ramp;
	private DcMotor flywheel;
	
	@Override
	public void init() {
	//TODO Go back and relearn objects and inheritence
		movementHandler = new MovementHandler(hardwareMap);
		
		//probably not absolutely needed to reset variables, but it doesn't hurt
		rotation = 0;
		rt = 0;
		lt = 0;
		xCheck = 0;
		yCheck = 0;
		
		ramp = hardwareMap.dcMotor.get("ramp");
		flywheel = hardwareMap.dcMotor.get("flywheel");
	}

	@Override
	public void loop() {
		xCheck = deadzone(gamepad1.left_stick_x, .1);
		yCheck = deadzone(gamepad1.left_stick_y, .1);
		rt = deadzone(gamepad1.right_trigger, .1);
		lt = deadzone(gamepad1.left_trigger, .1);
		rotation = (rt - lt);
		double speed = Collections.max(Arrays.asList(Math.abs(yCheck), Math.abs(xCheck), Math.abs(rotation)));
		movementHandler.move(yCheck, xCheck, rotation, Math.pow(speed, 4)); // speed: Math.most(Math.abs(yCheck), Math.abs(xCheck), Math.abs(rotation))

		telemetry.addData("X", String.valueOf(xCheck));
		telemetry.addData("Y", String.valueOf(yCheck));
		telemetry.addData("R", String.valueOf(rotation));

		if (gamepad1.left_bumper) {
			ramp.setPower(1);
		}
		else {
			ramp.setPower(0);
		}
		if (gamepad1.right_bumper) {
			flywheel.setPower(1);
		}
		else {
			flywheel.setPower(0);
		}
	}

	//Checks to see if the value is above a certain threshold so that we can have a deadzone on the joystick and triggers
	private double deadzone(double variable, double deadzone){
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
