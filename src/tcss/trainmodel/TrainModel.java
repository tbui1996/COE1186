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

    public TrainModel(float suggestedSpeed, int authority, int id, float speedLimit) {
        controller = new TrainController(this);
        this.suggestedSpeed = suggestedSpeed;
        this.authority = authority;
        this.id = id;
        this.speedLimit = speedLimit;
        controller.setSpeedLimit(speedLimit);

        this.eBrake = false;
        this.underground = false;
    }

    public TrainModel(float suggestedSpeed, int authority, int id, Block block) {
        controller = new TrainController(this);
        this.block = block;
        this.suggestedSpeed = suggestedSpeed;
        this.authority = authority;
        this.id = id;
        this.speedLimit = block.getSpeedLimit();
        controller.setSpeedLimit(speedLimit);

        this.eBrake = false;
        this.underground = block.isUnderground();
        this.grade = block.getGrade();
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

    }

    public void setEBrake(Boolean brake) {
        eBrake = brake;
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
}
