package garden.controller.garden;

import garden.model.Robot;

public class RobotSettingHelper {
    public RobotSettingHelper(Robot robot) {
        System.out.println("You've just opened the setting page, the robot is at the position x: " + robot.getPositionX() + " y: " + robot.getPositionY() + "!");
    }
}