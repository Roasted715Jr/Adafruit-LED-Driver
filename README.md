# Adafruit-LED-Driver
Driver for the Adafruit IS31FL3731 LED board. For use in the FIRST Tech Challenge

To use this driver, simply put LEDController.java in your TeamCode folder and then import the package org.firstinspires.ftc.teamcode.LEDController. Then, you can create an LEDController object in your program. After creating the object, call hardwareMap.get() like you would when adding other sensors. Additionally, you must add an "LED Matrix Controller" I2C device in your robot's hardware config, similar to what you would do for other sensors. At that point, everything is set up and ready to go. An example OpMode file is given for reference.

Any pictures you define must be two-dimensional byte arrays. The format looks something like this:

public static final byte[][] EMPTY = {{  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  
	}, {  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  
	}};  
  
  The first nested array must have 80 elements, and the second must have 64. If you don't do so, who knows what'll show up on the screen. Feel free to copy and modify this sample array as well as any of the other preset arrays in LEDController.java.
