package garden.controller.controlpanel;

import garden.core.AlgorithmClassLoader;
import garden.model.Robot;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmLoadingHelper {

    /**
     * Read all the files name from the folder /src/garden/algorithms, and return a list of String that represent those algorithms' class name
     * <br/>
     * <br/>
     * In order to be able to display on the control panel, <strong>all the algorithms have to be placed under path: /src/garden/algorithms</strong>.
     * <br/>
     * <br/>
     * In order to prevent any non-algorithm class been displayed on the screen, <strong>all other files, such as the algorithm helpers, have to be placed under /src/garden/algorithms/src</strong>. It is recommended to create a sub package for each algorithm.
     *
     * @return list of string that represents all the algorithms placed in the folder /src/garden/algorithms
     */
    public List<String> getAlgorithmList() {

        ArrayList<String> algorithmList = new ArrayList<>();

        String basePath = new File("").getAbsolutePath();
        basePath = basePath + "/src/garden/algorithms";

        File folder = new File(basePath);
        File[] files = folder.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {//load files only
                String algFileName = files[i].getName();
                if (algFileName.length() > 5) {
                    String algName = algFileName.substring(0, algFileName.length() - 5);//delete .java postfix
                    algorithmList.add(algName);
                }
            }
        }
        return algorithmList;
    }

    /**
     * This method will assign the given robot with the given algorithm in String
     *
     * @param robot     the robot instance that needs to apply the algorithm
     * @param algorithm the name of the algorithm that needs to apply to the robot
     */
    public void assignAlgorithmToRobot(Robot robot, String algorithm) {
        AlgorithmClassLoader.setSelectedAlgorithm(algorithm);
        try {
            robot.setAlgorithm(AlgorithmClassLoader.assignRobotWithSelectedAlgorithm(robot));
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}
