package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.Anvil.drivetrain.WEST_COAST;

/**
 * Created by dcrenshaw on 3/4/18.
 * Clasp is an intelligent teleop designed to interface with Anvil and provide an overhaul to
 * classic teleop.
 */

@TeleOp(name="Bernard", group="Pushbot")
public class ClaspTeleop extends OpMode {
    //Initiates the Anvil object.
    private Anvil anvil = new Anvil();
    boolean aSwap = true;
    @Override
    public void start() {
    }

    @Override
    public void init() {
        //Chooses the drive train from Anvil
        anvil.init(hardwareMap, WEST_COAST, telemetry);
        //Below brings in competition specific teleop code, dealing with the scoring mechanism.
        Competition.init(hardwareMap);
    }

    @Override
    public void loop() {
        //Basic Telemetry to help with controller testing, No longer needed.
        telemetry.addData("R_JoystickX", gamepad1.left_stick_x);
        telemetry.addData("R_JoystickY", gamepad1.left_stick_y);

        //Uses Buttons
        if (gamepad1.a) anvil.customMov(anvil.armMotor, 1);
        else if (gamepad1.y) anvil.customMov(anvil.armMotor, -1);
        else anvil.customMov(anvil.armMotor, 0);

        if (gamepad1.dpad_up){
            anvil.customMov(anvil.slideMotor2, -1);
        } else if (gamepad1.dpad_down){
            anvil.customMov(anvil.slideMotor2, 1);
        } else if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
            anvil.customMov(anvil.slideMotor, 0);
            anvil.customMov(anvil.slideMotor2, 0);
        }
        if (gamepad1.b) anvil.servoMov(0, 1);
        else if(gamepad1.x) anvil.servoMov(6, 3);
        //Deals with controls for drive train.
        if (gamepad1.atRest()) {
            //Ensures that if the gamepad is at rest, then the robot does not move.
            anvil.rest();
        } else {
            //Controls the ability to extend the arm, using the triggers.
            if (gamepad1.right_trigger > 0.1){
                anvil.customMov(anvil.extMotor, gamepad1.right_trigger);
            } else if (gamepad1.left_trigger > 0.1){
                anvil.customMov(anvil.extMotor, -gamepad1.left_trigger);
            } else if (gamepad1.right_trigger < 0.1 && gamepad1.left_trigger < 0.1){
                anvil.customMov(anvil.extMotor,0);
            }
            //controls the lift of the robot, to bring it off the ground
            //Doesnt work, for some reason, says the Dpad is being pushed but no response with the motors.
            //Decides whether the left stick x or the left stick y is farther from the origin
            //This decides whether the robot should move sideways or forward/backward.
            if (Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)) {
                //Turns right when left stick is negative.
                anvil.turnLeft(gamepad1.left_stick_x);
            } else {
                //Uses diff function, which means that the robot goes forward and backward,
                //Also allows robot to turn and move forward simultaneously.
                anvil.diff(gamepad1.left_stick_x, -gamepad1.left_stick_y);
            }
        }
    }
}