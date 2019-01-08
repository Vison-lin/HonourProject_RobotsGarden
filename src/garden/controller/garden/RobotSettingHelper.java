package garden.controller.garden;

import garden.Algorithm.caculate2D;
import garden.core.llibrary.FakedAlgorithm_UpdateColor;
import garden.model.Robot;

import java.util.List;

public class RobotSettingHelper {
    public RobotSettingHelper(Robot robot) {
        System.out.println("You've just opened the setting page, the robot is at the position x: " + robot.getPositionX() + " y: " + robot.getPositionY() + "! The Gathering Alg will be assigned to this robot");
        robot.setAlgorithm(new caculate2D(robot));
    }
}