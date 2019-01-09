package garden.controller.garden;

import garden.Algorithm.caculate2D;
import garden.core.llibrary.FakedAlgorithm_UpdateColor;
import garden.model.Robot;

public class RobotSettingHelper {
    public RobotSettingHelper(Robot robot) {

        robot.setAlgorithm(new caculate2D(robot));
        robot.setTag(" -> " + Math.round(Math.random() * 100) + " <- ");
        robot.getLog().addToLog("This robot has been assigned the algorithm: FAKRD ALG");
        robot.getLog().addToLog("You've just opened the setting page, the robot with tag: " + robot.getTag() + "is at the position x: " + robot.getPositionX() + " y: " + robot.getPositionY() + "! The Gathering Alg will be assigned to this robot");

    }
}