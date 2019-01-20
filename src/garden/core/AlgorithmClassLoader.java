package garden.core;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import garden.model.Robot;

import java.lang.reflect.InvocationTargetException;

public class AlgorithmClassLoader {

    private static String selectedAlgorithm;

    public static Algorithm getSelectedAssignedAlgorithm(Robot robot) throws InvocationTargetException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return getSelectedAssignedAlgorithm(selectedAlgorithm, robot);
    }

    public static Algorithm getSelectedAssignedAlgorithm(String algName, Robot robot) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {

        //Concatenate the class reference
        String name = "garden.algorithms." + algName;
        try {
            return (Algorithm) ClassLoader.getSystemClassLoader().loadClass(name).getConstructor(Robot.class).newInstance(robot);
        } catch (NoSuchMethodException | ClassCastException e) {
            throw new ClassFormatException("The class " + getSelectedAlgorithmName() + " cannot be cast to garden.core.Algorithm. All the algorithms must extends garden.core.Algorithm.");
        }
    }

    public static String getSelectedAlgorithmName() {
        return selectedAlgorithm;
    }

    public static void setSelectedAlgorithm(String selectedAlgorithm) {
        AlgorithmClassLoader.selectedAlgorithm = selectedAlgorithm;
    }
}
