package tcss.trainmodel;

import tcss.trackmodel.Block;
import tcss.traincontroller.*;

public class TrainModel {

    // Instance variables
    private TrainController controller;
    private Block block;
    private float suggestedSpeed;
    private int authority;
    private int id;
    private float speedLimit;
    private Boolean eBrake;
    private float grade;
    private float cmdSpeed;
    private Boolean underground;
    private float PWRCMD;

    public TrainModel(float suggestedSpeed, int authority, int id, float speedLimit) {
        this.suggestedSpeed = suggestedSpeed;
        this.authority = authority;
        this.id = id;
        this.speedLimit = speedLimit;
        this.eBrake = false;
        this.underground = false;

        controller = new TrainController(this);
        controller.setSpeedLimit(speedLimit);


    }

    public TrainModel(float suggestedSpeed, int authority, int id, Block block) {
        this.block = block;
        this.suggestedSpeed = suggestedSpeed;
        this.authority = authority;
        this.id = id;
        this.speedLimit = block.getSpeedLimit();
        this.eBrake = false;
        this.underground = block.isUnderground();
        this.grade = block.getGrade();

        controller = new TrainController(this);
        controller.setSpeedLimit(speedLimit);
    }

    public void passCommands(float sSpeed, int auth) {
        suggestedSpeed = sSpeed;
        authority = auth;
        controller.passCommands(auth, sSpeed);
    }

    public int getID() {
        return id;
    }

    public void update() {
        this.controller.passCommands(this.authority, this.suggestedSpeed);
    }

    public void setEBrake(Boolean brake) {
        eBrake = brake;
        this.controller.setEBrake(brake);
    }

    public Boolean getEBrake() {
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

    public Boolean getUnderground() {
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

    public void setPWRCMD(float PWRCMD){
        this.PWRCMD = PWRCMD;
    }
}
