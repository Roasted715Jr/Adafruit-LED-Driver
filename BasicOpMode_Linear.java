package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.LEDController;

@TeleOp(name="Basic: Linear OpMode", group="Linear Opmode")
//@Disabled
public class BasicOpMode_Linear extends LinearOpMode {
    LEDController led;

	@Override
	public void runOpMode() {
		led = hardwareMap.get(LEDController.class, "led");

		telemetry.addData("Status", "Initialized");
		telemetry.update();

		//Color a single pixel
		led.drawPixel(0, 0, 255);

		// Wait for the game to start (driver presses PLAY)
		waitForStart();

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {
			//Draw an image on the entire LED board
			led.drawPicture(LEDController.FULL);
			sleep(1000);
			led.drawPicture(LEDController.X);
			sleep(1000);
			led.drawPicture(LEDController.HORIZ_LINES);
			sleep(1000);
			led.drawPicture(LEDController.VERT_LINES);
			sleep(1000);
			led.drawPicture(LEDController.CHECKER);
			sleep(1000);
			led.drawPicture(LEDController.RECTANGLE);
			sleep(1000);
			led.drawPicture(LEDController.CIRCLE);
			sleep(1000);

			telemetry.update();
		}
	}
}
