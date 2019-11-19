package tcss.trainmodel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainModelTest {

    static TrainModel t;

    @BeforeAll
    static void setUp() throws Exception{
        t = new TrainModel();
    }

//    TODO Uncomment when TrainController ready for testing
//    @Test
//    void passCommands() {
//    }

    @Test
    void setEBrake() {
        t.setEBrake(true);
        assertTrue(t.getEBrake());
    }

    @Test
    void setSBrake() {
        t.setSBrake(true);
        assertTrue(t.getSBrake());
    }

    @Test
    void calculateVelocity1() {
        t.setSBrake(false);
        t.setEBrake(false);
        t.setLastA(1f);
        t.setCurA(2f);
        t.setCurV(5f);
        t.calculateVelocity();
        assertEquals(5.3f, t.getCurV());
    }

    @Test
    void calcForces1() {
        t.setGrade(0f);
        t.setCurV(0f);
        t.setCurA(0f);
        t.powerCmd(1f);
        t.calculateForces();
        assertEquals(207460f, t.getForce());
    }

    @Test
    void calcAccel1() {

        t.setGrade(0f);
        t.setCurV(0f);
        t.setCurA(0f);
        t.powerCmd(1f);
        t.calculateForces();
        t.calculateAccel();
        System.out.println("Force: " + t.getForce() + "\nMass: " + t.getMass());
        System.out.println("Force/Mass: " + (double)t.getForce() / (double)t.getMass());
        assertEquals(207460.0f/40900.0f, t.getCurA());
    }

    @Test
    void setLights() {
    }

    @Test
    void setBeacon() {
    }

    @Test
    void setDoor() {
    }
}