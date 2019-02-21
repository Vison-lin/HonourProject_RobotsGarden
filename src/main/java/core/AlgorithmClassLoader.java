package core;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AlgorithmClassLoader {

    public static final String PATH_TO_ALGORITHMS = "algorithms.";

    public static List<Class<? extends Algorithm>> algorithmClasses;

    /**
     * Give the name of the algorithm, return a new instance of that algorithm.
     * <br/>
     * <br/>Note that all the algorithms must:
     * <br/><li>extends garden.core.Algorithm</li>
     * <br/><li><strong>be placed under garden.algorithms. path</strong></li>
     * <br/><li>have a accessible default constructor</li>
     * <br/><li>not be an abstract class</li>
     *
     * @param algName the name of the algorithm class
     * @return the new instance of the algorithm
     * @throws ClassNotFoundException if there is no such algorithm class under the directory
     * @deprecated Not encourage to use this method as this method requires all the algorithm been placed under garden.algorithm path and is a little bit hard for users to add. Use getAlgorithmInstanceByClass instead.
     */
    @Deprecated
    public static Algorithm getAlgorithmInstanceByName(String algName) throws ClassNotFoundException {

        //Concatenate the class reference
        String name = PATH_TO_ALGORITHMS + algName;
        try {
            return (Algorithm) ClassLoader.getSystemClassLoader().loadClass(name).getConstructor().newInstance();
            //runtime exceptions: nothing to do with it: programmer must handle the incorrect implementation.
        } catch (ClassCastException e) {
            throw new ClassCastException("The class " + algName + " cannot be cast to garden.core.Algorithm. All the algorithms must extends garden.core.Algorithm");
        } catch (ClassNotFoundException e) {
            //checked exception
            throw new ClassNotFoundException("Algorithm: " + name + " not founded. All the Algorithms must be placed under " + PATH_TO_ALGORITHMS);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException("The Algorithm: " + algName + " does not have default constructor or the given algorithm is enforcing Java language access control and the underlying constructor is inaccessible.");
        } catch (InstantiationException e) {
            throw new IllegalStateException("The given algorithm is an abstract class");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(0);//because of the exceptions exist, terminate the program
            return null;
        }
    }

    /**
     * Give the algorithm's class, return a new instance of that algorithm.
     * <br/>
     * <br/>Note that all the algorithms must:
     * <br/><li>extends garden.core.Algorithm</li>
     * <br/><li><strong>be regisitered</strong></li>//todo ???
     * <br/><li>have a accessible default constructor</li>
     * <br/><li>not be an abstract class</li>
     *
     * @param algorithmClass the Class instance of the algorithm class
     * @return the new instance of the algorithm
     * @deprecated Not encourage to use this method as this method requires all the algorithm been placed under garden.algorithm path and is a little bit hard for users to add. Use getAlgorithmInstanceByClass instead.
     */
    public static Algorithm getAlgorithmInstanceByClass(Class<? extends Algorithm> algorithmClass) {
        try {
            return algorithmClass.getConstructor().newInstance();
            //runtime exceptions: nothing to do with it: programmer must handle the incorrect implementation.
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException("The Algorithm class: " + algorithmClass.getName() + " does not have default constructor or the given algorithm is enforcing Java language access control and the underlying constructor is inaccessible.");
        } catch (InstantiationException e) {
            throw new IllegalStateException("The given algorithm is an abstract class");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(0);//because of the exceptions exist, terminate the program
            return null;
        }
    }
}
