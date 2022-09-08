package org.firstinspires.ftc.teamcode;

//I2C Stuff
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteBuffer;

//@SuppressWarnings({"WeakerAccess", "unused"})
@I2cDeviceType
@DeviceProperties(name = "LED Matrix Controller", description = "Adafruit LED matrix controller", xmlTag = "IS31FL3731")
public class LEDController extends I2cDeviceSynchDevice<I2cDeviceSynch> {
	private static final short ISSI_ADDR_DEFAULT = 0x74;

	private static final short ISSI_REG_CONFIG = 0x00;
	private static final short ISSI_REG_CONFIG_PICTUREMODE = 0x00;

	private static final short ISSI_CONF_PICTUREMODE = 0x00;

	private static final short ISSI_REG_PICTUREFRAME = 0x01;

	private static final short ISSI_REG_SHUTDOWN = 0x0a;
	private static final short ISSI_REG_AUDIOSYNC = 0x06;

	private static final short ISSI_COMMANDREGISTER = 0xfd;
	private static final short ISSI_BANK_FUNCTIONREG = 0x0b;

	////////////////////////////////////////////////////////////////////////////////////////////////
	// Preset drawings
	////////////////////////////////////////////////////////////////////////////////////////////////

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

	//Java is dumb. A java byte is a signed 8-bit value, but there is no such thing as an unsigned
	// byte in java. Anything above 0x7f requires casting to a byte, which is stupid. The solution
	// is to instead give the two's complement of the value you want (if you want 0xff, that is the
	// two's complement for 1 so the value you need is -1).
	// Refer to https://stackoverflow.com/questions/18812864/why-will-byte-not-take-0xff-in-java
	public static final byte[][] FULL = {{
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	}, {
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	}};

	public static final byte[][] BACKSLASH = {{
			-1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, -1, -1, -1, 0, 0, 0, 0, 0, 0
	}, {
			0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1
	}};

	public static final byte[][] X = {{
			-1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1,
			0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0,
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0
	}, {
			0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0,
			0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0,
			-1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1
	}};

	public static final byte[][] HORIZ_LINES = {{
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	}, {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	}};

	public static final byte[][] VERT_LINES = {{
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0
	}, {
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0
	}};

	public static final byte[][] CHECKER = {{
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0
	}, {
			0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0,
			0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1,
			-1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0, -1, 0
	}};

	public static final byte[][] RECTANGLE = {{
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1
	}, {
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	}};

	public static final byte[][] CIRCLE = {{
			0, 0, 0, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, -1, -1, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0,
			0, -1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0,
			-1, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0,
			-1, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0,
	}, {
			-1, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0,
			0, -1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0,
			0, -1, -1, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}};

	////////////////////////////////////////////////////////////////////////////////////////////////
	// User Methods
	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Used for displaying content for the entire LED matrix at once.
	 * @param drawing	Two-dimensional array that contains the pixel brightnesses from 0-255,
	 *                  inclusive.
	 */
	public void drawPicture(byte[][] drawing) {
		selectBank(frame);

		deviceClient.write(0x24, drawing[0]);
		deviceClient.write(0x74, drawing[1]);
	}

	/**
	* Draw a single pixel on the display. Only use if drawing a small amount of pixels.
	* Trying to update the whole screen using this will incur a SIGNIFICANT performance impact
	*
	* @param x				x position of LED
	* @param y				y position of LED
	* @param brightness		desired brightness of LED on scale from 0-255 inclusive
	*/
	public void drawPixel(int x, int y, int brightness) {
		if (brightness > 255)
			brightness = 255;

		setLEDPWM((short) (x + y * 16), brightness, frame);
	}

	/**
	 * Turn off all the LEDs on the board by setting their brightnesses to 0
	 */
	public void clear() {
		drawPicture(EMPTY);
	}

	private void setLEDPWM(int ledNum, int pwm, int frame) {
		if (ledNum >= 144)
			return;
		writeRegister8(frame, (short) (0x24 + ledNum), pwm);
	}

	private void setLEDPWM(int ledNum, int pwm) {
		setLEDPWM(ledNum, pwm, frame);
	}

	private void audioSync(boolean sync) {
		if (sync)
			writeRegister8(ISSI_BANK_FUNCTIONREG, ISSI_REG_AUDIOSYNC, (short) 1);
		else
			writeRegister8(ISSI_BANK_FUNCTIONREG, ISSI_REG_AUDIOSYNC, (short) 0);
	}

	/**
	 * The chip can store 8 different "picture frames" at a time, useful for quickly swapping
	 * between pictures that you'll be using often. Use this function to swap between them
	 * @param frame		frame to select, 0-7 inclusive
	 */
	public void displayFrame(int frame) {
		if (frame > 7)
				return;
		this.frame = frame;
		writeRegister8(ISSI_BANK_FUNCTIONREG, ISSI_REG_PICTUREFRAME, frame);
	}

	public int getFrame() {
		return frame;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// Base comamnds
	////////////////////////////////////////////////////////////////////////////////////////////////

	private void selectBank(int bank) {
		deviceClient.write8(ISSI_COMMANDREGISTER, bank);
	}

	private void writeRegister8(int bank, int reg, int data) {
		selectBank(bank);

		deviceClient.write8(reg, data);
	}

	private int frame;

	////////////////////////////////////////////////////////////////////////////////////////////////
	// Construction and Initialization
	////////////////////////////////////////////////////////////////////////////////////////////////

	private static final I2cAddr ADDRESS = I2cAddr.create7bit(ISSI_ADDR_DEFAULT);

	public LEDController(I2cDeviceSynch deviceClient) {
		super(deviceClient, true);

		this.deviceClient.setI2cAddress(ADDRESS);

		super.registerArmingStateCallback(false); // Deals with USB cables getting unplugged
		// Sensor starts off disengaged so we can change things like I2C address. Need to engage
		this.deviceClient.engage();
	}

	@Override
	protected synchronized boolean doInitialize() {
		frame = 0;

		//Shutdown
		writeRegister8(ISSI_BANK_FUNCTIONREG, ISSI_REG_SHUTDOWN, 0x00);

		try {
			wait(10);
		} catch (InterruptedException e) {}

		//Exit shutdown
		writeRegister8(ISSI_BANK_FUNCTIONREG, ISSI_REG_SHUTDOWN, 0x01);

		//Picture mode
		writeRegister8(ISSI_BANK_FUNCTIONREG, ISSI_REG_CONFIG, ISSI_REG_CONFIG_PICTUREMODE);

		displayFrame(frame);

		//All LEDs on and 0 PWM
		clear(); //Set each LED to 0 PWM

		for (int f = 0; f < 8; f++) {
			selectBank(f);
			byte[] arr = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
			deviceClient.write(0, arr);
		}

		audioSync(false);

		return true;
	}

	@Override
	public Manufacturer getManufacturer() {
		return Manufacturer.Adafruit;
	}

	@Override
	public String getDeviceName() {
		return "Adafruit IS31FL3731 LED Controller";
	}
}
