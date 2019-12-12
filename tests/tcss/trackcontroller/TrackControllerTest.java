package tcss.trackmodel;
package tcss.trackcontroller;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TrackControllerTest {
    private PLC plc;
    private TrackController redTrC;
    private Track redTrack;
    private Track greenTrack;
    @BeforeAll
    static void setUp() throws Exception{
        TrackController tc = new TrackController();
    }
    @Test
    void verifyUploadPLC() {
        boolean atttempt = tc.loadPLC("resources/plctest.plc")
        assertTrue(atttempt);
    }

    @Test
    void maintenanceRequest(){
        boolean attempt = tc.maintenanceRequest(1,1);
        assertTrue(attempt);
    }

    @Test
    void railroadRequestTest(){
        boolean attempt = tc.railroadCrossingRequest(1,1);
        assertTrue(attempt);
    }

    @Test
    void switchRequestTest(){
        boolean switchrequest = tc.switchRequest(1,1);
        assertTrue(switchrequest);
    }

}