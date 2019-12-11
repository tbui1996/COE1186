package tcss.trainmodel;

import tcss.trackmodel.Block;
import tcss.traincontroller.TrainController;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

/**
 * The TrainModel class represents the physical train itself. It does
 * not handle any of the logic of controlling or navigating the train;
 * it deals only with simulating the train itself. It can be thought of
 * as a sort of physics engine.
 *
 * @author Wesley Miller
 */
public class TrainModel {

	private final int MAX_PASSENGERS = 222;

	// Error State Booleans
	private boolean ENGINE_FAIL = false;
	private boolean SBRAKE_FAIL = false;
	private boolean EBRAKE_FAIL = false;
	private boolean LIGHTS_FAIL = false;
	private boolean ANTENNA_FAIL = false;


	// Static ArrayList of all trains
	private static ArrayList<TrainModel> trains = new ArrayList<TrainModel>();

	// Instance variables
	private TrainController controller;
	private Block block;
	private float suggestedSpeed;
	private int authority;
	private int id;
	private float speedLimit;
	private boolean sBrake;
	private boolean eBrake;
	private float cmdSpeed;
	private boolean underground;
	private int blocksTraveled;
	private String curBeaconSignal;
	private String lastBeaconSignal;
	private boolean lights;
	private boolean[] doors = new boolean[8];
	private int passengers;
	private float temp;

	// Variables for physics
	private float grade;            // Grade of the current block
	private float length;           // Length of the current block
	private float power;            // The current power command
	private float mass;             // in kg
	private float force;            // Force vector for x axis (positive is forward, negative is backward)
	private float mu = 0.002f;      // Coefficient of rolling friction for train wheels on steel rails
	private float lastV;            // Previous velocity
	private float curV;             // New velocity
	private float lastA;            // Previous acceleration
	private float curA;             // New acceleration
	private float x;                // Meters traveled IN THE CURRENT BLOCK
	private float period = 0.2f;    // The sample period for the system in seconds.


	/**
	 * The main TrainModel constructor
	 *
	 * @param suggestedSpeed The initial suggested speed for the train
	 * @param authority The initial authority for the train
	 * @param id An id number for this train
	 * @param block The starting block of the train
	 */
	public TrainModel(float suggestedSpeed, int authority, int id, Block block) {
		this.suggestedSpeed = suggestedSpeed;
		this.authority = authority;
		this.block = block;
		this.id = id;
		this.eBrake = false;

		// Get initial block info
		this.speedLimit = block.getSpeedLimit();
		this.underground = block.isUnderground();
		this.length = block.getLength();
		this.grade = block.getGrade();

		mass = 40900;
		curA = 0;
		curV = 0;

		controller = new TrainController(this);
		controller.setSpeedLimit(speedLimit);
		controller.passCommands(authority, suggestedSpeed);

		// Add to ArrayList
		trains.add(this);
		temp = 68f;

		// TODO Delete this test
		System.out.println("\nAll trains:\n");
		for(TrainModel t: trains) {
			System.out.println(t);
		}
	}

	/**
	 * A TrainModel class constructor
	 * @param suggestedSpeed The initial suggested speed from the CTC via the Track Model
	 * @param authority The initial authority from the CTC via the Track Model
	 * @param id The new train's id number
	 * @param speedLimit Speed limit of the track the train is initiated on
	 * @deprecated
	 */
	public TrainModel(float suggestedSpeed, int authority, int id, float speedLimit) {
		this.suggestedSpeed = suggestedSpeed;
		this.authority = authority;
		this.id = id;
		this.speedLimit = speedLimit;
		this.eBrake = false;
		this.underground = false;

		controller = new TrainController(this);
		//TODO Talk to Pat about speed limit
//        controller.setSpeedLimit(speedLimit);
		mass = 409000;
		curA = 0f;
		curV = 0f;
		temp = 68f;
	}

	/**
	 * For demo only.
	 *
	 * This constructor is used only for demonstration purposes in the
	 * individual submission. Length of a dummy block is passed; that block
	 * simply repeats to demonstrate train functionality.
	 * @param length The length of the dummy "block"
	 */
	public TrainModel(int length) {
//        id = 1;
		eBrake = false;
		sBrake = false;
		underground = false;
		lights = false;
		this.length = length;
		mass = 409000;
		power = 0;
		curV = 0;
		lastV = 0;
		curA = 0;
		lastA = 0;
		trains.add(this);
		id = trains.size();
		controller = new TrainController(this);
		temp = 68f;
	}

	/**
	 * For unit tests only.
	 */
	public TrainModel() {
		id = 1;
		eBrake = false;
		sBrake = false;
		underground = false;
		lights = false;
		mass = 40900;
		curV = 0;
		lastV = 0;
		curA = 0;
		lastA = 0;
	}

	/**
	 * Returns ArrayList containing all existing trains
	 *
	 * @return Reference to ArrayList of all trains
	 */
	public static ArrayList<TrainModel> getAllTrains() {
		return trains;
	}

	/**
	 * Puts train in Yard.
	 *
	 * This method is called when a train returns to the yard. It
	 * removes the train from the ArrayList of active trains.
	 */
	public void toYard() {
		trains.remove(this);
	}

	/**
	 * Update train's state.
	 *
	 * This method uses the current power command and environment of the train
	 * (ie., grade of the track) to calculate the forces acting on the train,
	 * update the acceleration of the train, update the velocity, and finally
	 * update how far in the block it has traveled, moving it to the next block
	 * if necessary.
	 */
	public void update() {

		// If braking, set acceleration manually
		// If emergency brake...
		if(eBrake && !EBRAKE_FAIL) {
			if(curV <= 0) {
				lastA = curA;
				curA = 0;
				curV = 0;
			} else {
				lastA = curA;
				curA = -2.73f;
			}
		} else if(sBrake && !SBRAKE_FAIL) {		// If service brake...
			if(curV <= 0) {
				lastA = curA;
				curA = 0;
				curV = 0;
			} else {
				lastA = curA;
				curA = -1.2f;
			}
		} else {
			calculateForces();
			calculateAccel();
		}

		calculateVelocity();
		// Update distance traveled
		x = x + (curV * period);

		// If the train entered a new block...
		if(x > length) {
			x = x - length;
			if(block != null) {
				block = block.trainGetNextBlock();
				length = block.getLength();
				grade = block.getGrade();
				speedLimit = block.getSpeedLimit();
				controller.setSpeedLimit(speedLimit);
			}

			blocksTraveled = blocksTraveled + 1;
			controller.enterNewBlock();
			if(curBeaconSignal != null) {
				lastBeaconSignal = curBeaconSignal;
				curBeaconSignal = null;
			}

			if(block != null) {
				if(block.getBeacon() != null) {
					curBeaconSignal = new String(block.getBeacon().getData());
				}
			}
		}
		if(LIGHTS_FAIL) {
			lights = false;
		}
	}

	/**
	 * Updates all existing instances of TrainModel
	 */
	public static void updateAll() {
		for(TrainModel t: trains) {
			t.update();
		}
	}

	/**
	 * This method uses the previous and current accelerations to calculate
	 * the train's new velocity.
	 */
	void calculateVelocity() {
		lastV = curV;
		curV = lastV + (period / 2f) * (lastA + curA);
	}

	/**
	 * This method uses the calculated force vector to calculate the train's
	 * new acceleration.
	 */
	void calculateAccel() {
		lastA = curA;
		curA = force / mass;
	}


	/**
	 * This method uses the current state of the train to calculate the
	 * total force vector acting on it in the x dimension.
	 */
	void calculateForces() {
		// Convert grade to angle theta
		// Negate it for ease of calculation when decomposing
		// gravity force vector
		float theta = -(float)atan((double)grade/100);

		// Calculate force due to gravity
		float g = mass*9.8f;

		// Decompose force due to gravity into x and y component vectors
		float gx = g * (float)sin(theta);
		float gy = g * (float)cos(theta);

		// Calculate force due to friction; negate it, as it is always backwards
		float friction = -(mu * gy);

		// Calculate thrust force from engine
		float thrust;
		if(curV == 0) {
			if(power != 0) {
				thrust = 207460;
			} else {
				thrust = 0;
			}
		} else {
			thrust = power / curV;
		}
		// If engine has failed, thrust is zero
		if(ENGINE_FAIL) {
			thrust = 0;
		}
		// Add forces in x dimension
		//NOTE: ONLY CALCULATE FRICTION WHEN THERE IS NO POWER AND NO BRAKE
		if(power == 0) {
			if(curV > 0) {
				force = friction + gx;
			} else if(curV <= 0 && curV > -0.01) {
				force = gx;
				if(force == 0) {
					curV = 0;
				}
			} else {
				force = gx - friction;
			}
		} else {
			force = thrust + gx;
		}
	}

	/**
	 * For passing in a power command
	 * @param p The power command
	 */
	public void powerCmd(float p) {
		power = p;
	}

	/**
	 * Called by the TrackModel; passes speed and authority to the train controller
	 * @param sSpeed Suggested speed
	 * @param auth Authority
	 */
	public void passCommands(float sSpeed, int auth) {
		if(ANTENNA_FAIL) {
			return;
		}
		suggestedSpeed = sSpeed;
		authority = auth;
		controller.passCommands(auth, sSpeed);
	}

	public int getID() {
		return id;
	}

	public void setEBrake(boolean brake) {
		if(EBRAKE_FAIL) {
			eBrake = false;
			return;
		}
		eBrake = brake;
		if(controller != null) {
			this.controller.setEBrake(brake);
		}
	}

	public boolean getEBrake() {
		return eBrake;
	}

	public float getGrade() {
		return grade;
	}

	public float getCmdSpeed() {
		return cmdSpeed;
	}

	public void setCmdSpeed(float s) {
		cmdSpeed = s;
	}

	public boolean getUnderground() {
		return underground;
	}

	public float getSSpeed() {
		return suggestedSpeed;
	}

	public int getAuthority() {
		return authority;
	}

	public float getSpeedLimit() {
		return speedLimit;
	}

	public TrainController getTControl() {
		return this.controller;
	}

	public String getCurBeaconSignal() {
		if(curBeaconSignal != null){
			return this.curBeaconSignal;
		} else {
			return "NULL";
		}
	}

	public String getLastBeaconSignal() {
		if(lastBeaconSignal != null) {
			return this.lastBeaconSignal;
		}
		return "NULL";
	}

	public boolean getLights() {
		return lights;
	}

	public void setLights(boolean l) {
		lights = l;
	}

	public float getCurV() {
		return curV;
	}

	public float getLastV() {
		return lastV;
	}

	public float getCurA() {
		return curA;
	}

	public float getLastA() {
		return lastA;
	}

	public float getPower() {
		return power;
	}

	public float getMass() {
		return mass;
	}

	public int getPassengers() {
		return passengers;
	}

	public void setBeacon(char[] c) {
		if(curBeaconSignal != null) {
			lastBeaconSignal = curBeaconSignal;
		}
		if(c != null) {
			curBeaconSignal = new String(c);
		} else {
			curBeaconSignal = null;
		}
	}

	public void addPassengers(int p) {
		passengers += p;
		mass = 409000 + 80*passengers;
	}

	/**
	 * Removes a random number of passengers.
	 *
	 * @return free space on train.
	 */
	public int removePassengers() {
		Random r = new Random();
		passengers -= r.nextInt(passengers+1);
		mass = 409000 + 80*passengers;
		return MAX_PASSENGERS - passengers;
	}

	public void setLength(float l) {
		length = l;
	}

	public void setDoor(int i, boolean b) {
		doors[i] = b;
	}

	public boolean getDoor(int i) {
		return doors[i];
	}

	public void setSBrake(boolean b) {
		if(SBRAKE_FAIL) {
			sBrake = false;
			return;
		}
		sBrake = b;
	}

	public boolean getSBrake() {
		return sBrake;
	}

	public float getX() {
		return x;
	}

	public int getBlocksTraveled() {
		return blocksTraveled;
	}

	public void setID(int id) {
		this.id = id;
	}

	/**
	 * For testing purposes only
	 * @param v New curV
	 */
	public void setCurV(float v) {
		curV = v;
	}

	/**
	 * For testing purposes only
	 * @param v New lastV
	 */
	public void setLastV(float v) {
		lastV = v;
	}

	/**
	 * For testing purposes only
	 * @param a New curA
	 */
	public void setCurA(float a) {
		curA = a;
	}

	/**
	 * For testing purposes only
	 * @param a New lastA
	 */
	public void setLastA(float a) {
		lastA = a;
	}

	/**
	 * For testing purposes only
	 * @return Returns train's current force vector
	 */
	public float getForce() {
		return force;
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public void setPWRCMD(float PWRCMD){
		this.power = PWRCMD;
	}

	public void setTemp(float temp) {
		this.temp = temp;
	}

	public float getTemp() {
		return temp;
	}

	public boolean[] getFailures() {
		return new boolean[]{EBRAKE_FAIL, SBRAKE_FAIL, ENGINE_FAIL, LIGHTS_FAIL, ANTENNA_FAIL};
	}

	public void setSBrakeFail(boolean f) {
		SBRAKE_FAIL = f;
		if(f)
			sBrake = false;
	}

	public void setEBrakeFail(boolean f) {
		EBRAKE_FAIL = f;
		if(f)
			eBrake = false;
	}

	public void setEngineFail(boolean f) {
		ENGINE_FAIL = f;
	}

	public void setAntennaFail(boolean f) {
		ANTENNA_FAIL = f;
	}

	public void setLightsFail(boolean f) {
		LIGHTS_FAIL = f;
	}

}
