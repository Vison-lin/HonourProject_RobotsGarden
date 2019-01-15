package garden.core;

import garden.model.Robot;

import java.lang.reflect.InvocationTargetException;

public class AlgorithmClassLoader {

    private static String selectedAlgorithm;

    public static Algorithm getAllAlgorithm(Robot robot) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //Concatenate the class reference
        String name = "garden.algorithms." + selectedAlgorithm;

        Algorithm algorithm = (Algorithm) ClassLoader.getSystemClassLoader().loadClass(name).getConstructor(Robot.class).newInstance(robot);

        return algorithm;

    }

    public static String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public static void setSelectedAlgorithm(String selectedAlgorithm) {
        AlgorithmClassLoader.selectedAlgorithm = selectedAlgorithm;
    }
}
