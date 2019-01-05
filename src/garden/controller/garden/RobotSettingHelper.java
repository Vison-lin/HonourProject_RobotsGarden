package garden.controller.garden;

import garden.model.Robot;

public class RobotSettingHelper {
    public RobotSettingHelper(Robot robot) {
        System.out.println("You've just opened the setting page, the robot is: " + robot.getPositionX());
    }
}
