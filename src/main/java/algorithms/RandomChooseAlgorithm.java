package algorithms;


import model.Algorithm;
import model.Robot;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *   This algorithm is to randomly assgin a algorithm between algorithm A and algorithm B to the robot before each step of moving, and let the robot
 *   follow the rule of assinged algorithm to move.
 * <br/>
 * <br/>
 *
 */
public class RandomChooseAlgorithm extends Algorithm {

    /**
     *  Algorithm A.
     */
    private GatheringAlgorithm gatheringAlgorithm;

    /**
     *  Algorithm B
     */
    private GatheringGoToSec gatheringGoToSec;


    /**
     * This boolean type ensures which algorithm is choosed.
     */
    private boolean isAlgorithmA = true ;


//    private RandomChooseAlgorithm(){
//        gatheringAlgorithm = new GatheringAlgorithm();
//        gatheringGoToSec = new GatheringGoToSec();
//        isAlgorithmA = true;
//
//    }


    /**
     *  This method is to decide which algorithm will be assgined through a calculate probability.
     *
     */
    private void calculateP(){

        Random random = new Random();
        int num = random.nextInt(100);
        if(num>=50){
            this.isAlgorithmA = false;
        }else{
            this.isAlgorithmA = true;
        }

    }




    @Override
    public Point2D.Double next(List<Robot> localRobotList) {
        gatheringAlgorithm = new GatheringAlgorithm();
        gatheringGoToSec = new GatheringGoToSec();
//        System.out.println("==============" + gatheringAlgorithm.getRobot());
//        System.out.println("==============" + gatheringGoToSec.getRobot());

        ArrayList state = new ArrayList<>(this.getRobot().getSensor().getAllVisibleRobotsInLocalScale());
//        System.out.println("--RandomChoose");
//        System.out.println("Para"+localRobotList.size());
//        System.out.println("Sensor" +this.getRobot().getSensor().getAllVisibleRobotsInLocalScale().size() );
//        if (this.getRobot().getSensor().getAllVisibleRobotsInLocalScale().size() != localRobotList.size()){
//            throw new IllegalStateException("Size mismatched");
//        }
//        System.out.println("---end of RandomChoose");
        double range = getRobot().getVision();
        calculateP();
        if(isAlgorithmA){
//            System.out.println("A: "+gatheringAlgorithm.generateOneRobot(state,range));
            return gatheringAlgorithm.generateOneRobot(state,range);
        }else{
//            System.out.println("B: "+gatheringAlgorithm.generateOneRobot(state,range));
            return gatheringGoToSec.generateOneRobot(state,range);
        }

    }

    @Override
    public String algorithmName() {
        return "Random Choose Algorithm";
    }

    @Override
    public String algorithmDescription() {
        return "Robot will perform the Gathering or Go-to-Sec algorithm.";
    }
}
