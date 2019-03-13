package core;


import model.Algorithm;
import model.Robot;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmLoadingHelper {

    /**
     * Read all the files name from the folder /src/garden/garden.algorithms, and return a list of String that represent those garden.algorithms' class name
     * <br/>
     * <br/>
     * In order to be able to display on the control panel, <strong>all the garden.algorithms have to be placed under path: /src/garden/garden.algorithms</strong>.
     * <br/>
     * <br/>
     * In order to prevent any non-algorithm class been displayed on the screen, <strong>all other files, such as the algorithm helpers, have to be placed under /src/garden/garden.algorithms/src</strong>. It is recommended to create a sub package for each algorithm.
     *
     * @return list of algorithms. Note all the algorithm have to be placed under folder /src/garden/garden.algorithms.
     */
    public List<Algorithm> getAlgorithmList() throws IllegalStateException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {

        ArrayList<Algorithm> algorithmList = new ArrayList<>();

        String basePath = new File("").getAbsolutePath();
        String algPath = basePath + "/src/main/java/algorithms";


        //todo FRED: how to place the algorithm??
        /*
        TO RETRIVE FROM JAR FILE
        Things left: how to load an instance from .class file? URLClassLoader?
         */
//        String algPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+"";//"/src/garden/algorithms";//relevant path
//        System.out.println(algPath);
//        try {
//            JarFile jarFile = new JarFile(algPath);
//            final Enumeration<JarEntry> entries = jarFile.entries();
//            while(entries.hasMoreElements()) {
//                String name = entries.nextElement().getName();
//                if (name.startsWith("garden/algorithms")) { //filter according to the path
//                    name = name.substring(18);
//                    System.out.println(name);
//                    if (name.length() > 5 && name.substring(name.length() - 6).equals(".class")) {
//                        System.out.println(name);
//                        String algName = name.substring(0, name.length() - 5);//delete .java postfix
//                        String nameOfAlg = AlgorithmClassLoader.getAlgorithmInstanceByName(algName).algorithmName();
//                        algorithmList.add(new Pair<>(nameOfAlg, algName));
//                    }
//                }
//            }
//            jarFile.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        /*
        END OF TO_DO
         */

        File algFolder = new File(algPath);
        File[] algFiles = algFolder.listFiles();
        if (algFiles != null) {
            for (int i = 0; i < algFiles.length; i++) {
                if (algFiles[i].isFile()) {//load files only
                    String algFileName = algFiles[i].getName();
                    if (algFileName.length() > 5) {
                        String algName = algFileName.substring(0, algFileName.length() - 5);//delete .java postfix
                        Algorithm algorithm = AlgorithmClassLoader.getAlgorithmInstanceByName(algName);
                        algorithmList.add(algorithm);
                    }
                }
            }
        }

//        if (algorithmList.size() == 0) {
//            throw new IllegalStateException("No Algorithm Found: You have to have at least one algorithm to run the program. All the garden.algorithms must extend garden.model.Algorithm and have to be placed under /src/garden/garden.algorithms folder.");
//        }

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
