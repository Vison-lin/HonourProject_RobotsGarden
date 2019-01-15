package garden.controller.controlpanel;

import garden.core.AlgorithmClassLoader;
import garden.model.Robot;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmLoadingHelper {

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
            robot.setAlgorithm(AlgorithmClassLoader.getAllAlgorithm(robot));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
