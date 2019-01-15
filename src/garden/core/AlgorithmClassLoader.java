package garden.core;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import garden.model.Robot;

import java.lang.reflect.InvocationTargetException;

public class AlgorithmClassLoader {

    private static String selectedAlgorithm;

    public static Algorithm assignRobotWithSelectedAlgorithm(Robot robot) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {

        //Concatenate the class reference
        String name = "garden.algorithms." + selectedAlgorithm;

        Object newObject = null;
        try {
            return (Algorithm) ClassLoader.getSystemClassLoader().loadClass(name).getConstructor(Robot.class).newInstance(robot);
        } catch (NoSuchMethodException | ClassCastException e) {
            throw new ClassFormatException("The class " + getSelectedAlgorithm() + " cannot be cast to garden.core.Algorithm. All the algorithms must extends garden.core.Algorithm.");
        }
    }

    public static String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public static void setSelectedAlgorithm(String selectedAlgorithm) {
        AlgorithmClassLoader.selectedAlgorithm = selectedAlgorithm;
    }
}
