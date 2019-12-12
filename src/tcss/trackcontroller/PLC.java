package tcss.trackcontroller;

import org.apache.commons.jexl2.*;
import tcss.trackmodel.Block;

public class PLC {

    private TrackController trackController;
    private static JexlEngine jexlEvaluator;
    private static String switches;
    private static String RXR;
    private static String maintenance;
    private static String proceed;
    private static String lightslogic;

    public PLC(String switches, String proceed, String maintenance, String RXR, String lights) {
        this.switches = switches;
        this.proceed = proceed;
        this.maintenance = maintenance;
        this.RXR = RXR;
        this.lightslogic = lights;

        jexlEvaluator = new JexlEngine();
    }

    public boolean verifySwitch(Block nextBlock, Block destinationBlock){
        return vitalSwitch(nextBlock, destinationBlock);
    }
    public boolean vitalSwitch(Block nextBlock, Block destinationBlock){
        Expression expression = jexlEvaluator.createExpression(switches);
        JexlContext jexlcontent = new MapContext();
        return vitalSwitchCalculation(nextBlock, destinationBlock, expression, jexlcontent);
    }
    private boolean vitalSwitchCalculation(Block nextBlock, Block destinationBlock, Expression expression, JexlContext jexlcontent){
        boolean result = true;

        for(int i = 0; i < 5; i++){
            jexlcontent.set("currentBlockOccupied", nextBlock.isOccupied());
            jexlcontent.set("nextBlockOccupied", destinationBlock.isOccupied());

            result &= (boolean) expression.evaluate(jexlcontent);

        }

        return result;
    }

    public boolean verifyProceed(Block nextBlock, Block destinationBlock){
        return vitalProceed(nextBlock, destinationBlock);
    }

    private boolean vitalProceed(Block nextBlock, Block destinationBlock){
        boolean result = true;

        Expression expression = jexlEvaluator.createExpression(proceed);

        JexlContext jexlcontent = new MapContext();

        for(int i=0; i < 5; i++){
            jexlcontent.set("nextBlockOccupied", nextBlock.isOccupied());
            jexlcontent.set("nextnextBlockOccupied", destinationBlock.isOccupied());

            result &= (boolean) expression.evaluate(jexlcontent);
        }

        return result;
    }

    public boolean verifyRXR(Block prevBlock, Block RXRBlock, Block nextBlock){
        return vitalRXR(prevBlock, RXRBlock);
    }

    private boolean vitalRXR(Block prevBlock, Block RXRBlock){
        Expression expression = jexlEvaluator.createExpression(RXR);
        JexlContext jexlcontent = new MapContext();
        return vitalRXRCalculation(prevBlock, RXRBlock, expression, jexlcontent);
    }

    private boolean vitalRXRCalculation(Block prevBlock, Block RXRBlock, Expression expression, JexlContext jexlcontent){
        boolean result = true;
        boolean activated = RXRBlock.getRXR().isDown();
        for(int i= 0; i < 7; i++){
            jexlcontent.set("previousBlockOccupied", prevBlock.isOccupied());
            jexlcontent.set("currentBlockOccupied", RXRBlock.isOccupied());
            jexlcontent.set("nextBlockOccupied", activated);

            result &= (boolean) expression.evaluate(jexlcontent);
        }
        return result;
    }

    public boolean verifyMaintenance(Block prevBlock, Block MaintenanceBlock, Block nextBlock){
        return vitalMaintenance(prevBlock, MaintenanceBlock, nextBlock);
    }

    private boolean vitalMaintenance(Block prevBlock, Block MaintenanceBlock, Block nextBlock){
        Expression expression = jexlEvaluator.createExpression(maintenance);
        JexlContext jexlcontent = new MapContext();
        return vitalMaintenanceCalculation(prevBlock, MaintenanceBlock, nextBlock,expression, jexlcontent);
    }

    private boolean vitalMaintenanceCalculation(Block prevBlock, Block MaintenanceBlock, Block nextBlock,Expression expression, JexlContext jexlcontent){
        boolean result = true;

        for(int i= 0; i < 7; i++){
            jexlcontent.set("previousBlockOccupied", prevBlock.isOccupied());
            jexlcontent.set("currentBlockOccupied", MaintenanceBlock.isOccupied());
            jexlcontent.set("nextBlockOccupied", nextBlock.isOccupied());

            result &= (boolean) expression.evaluate(jexlcontent);
        }
        return result;
    }

    public boolean verifyLightBlock(Block normalBlock, Block alternativeBlock){
        return vitalLightBlock(normalBlock, alternativeBlock);
    }

    private boolean vitalLightBlock(Block normalBlock, Block alternativeBlock){
        boolean result = true;
        Expression expression = jexlEvaluator.createExpression(lightslogic);
        JexlContext jexlcontent = new MapContext();
        for(int i=0; i < 7; i++){
            jexlcontent.set("normalBlockOccupied", normalBlock.getSwitch().getStraightDest());
            jexlcontent.set("alternativeBlockOccupied", alternativeBlock.getSwitch().getBranchDest());

            result &= (boolean) expression.evaluate(jexlcontent);
        }

        return result;
    }
   
}





