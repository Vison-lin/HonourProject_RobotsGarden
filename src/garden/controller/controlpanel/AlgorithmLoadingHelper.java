package garden.controller.controlpanel;

import garden.core.Algorithm;
import garden.core.AlgorithmClassLoader;
import garden.model.Robot;
import javafx.util.Pair;

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
     * @return list of Pairs with type <String, String> where the first one (Key) represents the algorithm's displaying name, and the second one (Value) is the file name of the algorithm. Note all the algorithm have to be placed under folder /src/garden/algorithms.
     */
    public List<Pair<String, String>> getAlgorithmList() throws IllegalStateException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {

        ArrayList<Pair<String, String>> algorithmList = new ArrayList<>();

        String basePath = new File("").getAbsolutePath();
        String algPath = basePath + "/src/garden/algorithms";

        File algFolder = new File(algPath);
        File[] algFiles = algFolder.listFiles();

        for (int i = 0; i < algFiles.length; i++) {
            if (algFiles[i].isFile()) {//load files only
                String algFileName = algFiles[i].getName();
                if (algFileName.length() > 5) {
                    String algName = algFileName.substring(0, algFileName.length() - 5);//delete .java postfix
                    String nameOfAlg = AlgorithmClassLoader.getAlgorithmInstanceByName(algName).algorithmName();
                    algorithmList.add(new Pair<>(nameOfAlg, algName));
                }
            }
        }

        if (algorithmList.size() == 0) {
            throw new IllegalStateException("No Algorithm Found: You have to have at least one algorithm to run the program. All the algorithms must extend garden.core.Algorithm and have to be placed under /src/garden/algorithms folder.");
        }

        return algorithmList;
    }

    /**
     * This method will assign the given robot with the given algorithm in String
     *
     * @param robot     the robot instance that needs to apply the algorithm
     * @param algorithmName the name of the algorithm that needs to apply to the robot
     */
    public void assignAlgorithmToRobot(Robot robot, String algorithmName) {
        try {
            Algorithm algorithm = AlgorithmClassLoader.getAlgorithmInstanceByName(algorithmName);
            algorithm.setRobot(robot);
            robot.setAlgorithm(algorithm);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
