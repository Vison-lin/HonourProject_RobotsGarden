package garden.algorithms;

import garden.algorithms.src.gatheringalgorithm.Disc;
import garden.algorithms.src.gatheringalgorithm.SEC;
import garden.algorithms.src.gatheringalgorithm.Vector;
import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GatheringAlgorithm extends Algorithm{

    /**
     *  This list includes visible robots of the current robot
     */
    private List<Robot> state;

    /**
     *  The vision range of the current robot
     */
    private double range;


    SEC sec;


    public GatheringAlgorithm() {

        super();
        System.out.println("!!!!!");
        sec = new SEC();
        sec.setAlgorithm(this);
        registerDisplayAdapter(sec);
    }

    /**
     *  Apply the gathering algorithm (go-to-center)to calculate the next position of the current robot.
     *
     * @param visibles This list includes visible robots of the current robot
     * @param range The vision range of the current robot
     * @return
     */

    public Point2D.Double generateOneRobot (ArrayList<Robot>visibles , double range){
        Point2D.Double result = new Point2D.Double();

        visibles = getUniqueRobots(visibles);

        if (visibles.size()<2){
            result.setLocation(0.0,0.0);
            return result;
        }
        Disc C = miniDisc(visibles);
        System.out.println("r: "+C.getrSquared());
       System.out.println("Robot:"+getRobot().getPosition());
        System.out.println("center:"+ C.getCenter());
        System.out.println("center Global:"+getRobot().getSensor().convertToGlobal(C.getCenter()));
        ArrayList rs = new ArrayList();
        for(Robot p:visibles){
            if(p.getPositionX()!=0.0|| p.getPositionY()!= 0.0){
                rs.add(p);
            }
        }
        Point2D.Double currRobot  = new Point2D.Double();
        currRobot.setLocation(0.0,0.0);
        Point2D.Double connectedCenter = getConnectedCenter(range,C.getCenter(),currRobot,rs);
        System.out.println("Final:" +connectedCenter);
        System.out.println("Final Global:"+getRobot().getSensor().convertToGlobal(connectedCenter));
        return connectedCenter;


    }

















    /**
     * From a given list, only return robots that are at distinct coordinates
     * We realized that if 2 or more robots have the same coordinates and end up in `findDiscWith3Points`, we get a resulting disc that is wrong (infinite disc).
     * Therefore, removing duplicates is necessary.
     *
     * Since then, we restricted even more: duplicates now also include robots that are within a distance of 1e-10
     *
     * @param  robots  a list of all robots
     * @return {Array}
     */
    public ArrayList getUniqueRobots(ArrayList<Robot> robots){
        ArrayList uniques = new ArrayList();

        for(Robot robot:robots){
            ArrayList robotsWithin = findRobotsVisible(robot,uniques,1e-10);
            if (robotsWithin.size()==0){
                uniques.add(robot);

            }

        }
        return uniques;

    }

    /**
     * Retuns the robots visible to a reference robot within a certain range
     *
     * @param  comparedRobot point of reference
     * @param  state current state of robots' positionss
     * @param  range vision v of all robots
     * @return {Array}
     */

    private ArrayList findRobotsVisible(Robot comparedRobot,ArrayList<Robot> state,double range){
        ArrayList visible  = new ArrayList();

        for(Robot robot:state ){
            double distanceSquared = Math.pow((comparedRobot.getPositionX()-robot.getPositionX()),2)+
                    Math.pow((comparedRobot.getPositionY()-robot.getPositionY()),2);
            if(distanceSquared <= range*range){
                visible.add(robot);
            }
        }

      return visible;
    }

    /**
     * Based on `Smallest Enclosing Disc` in "Computational Geometry - Algorithms and Applications - Third Edition"
     *
     * @param  P set of points {x, y}
     * @return {Disc} smallest disc containing all points in `P`
     */

    public Disc miniDisc(ArrayList<Robot> P){
        Collections.shuffle(P);
        Point2D.Double p1 = P.get(0).getPosition();
        Point2D.Double p2 = P.get(1).getPosition();
        Disc D2 = new Disc(p1,p2);
        for(int i= 2;i<P.size();i++){
            Point2D.Double pi = P.get(i).getPosition();
            if(!D2.contains(pi)){
                D2 =miniDiscWithPoint(new ArrayList<Robot>(P.subList(0,i)),pi);
            }
        }
        return D2;
    }


    /**
     * Based on `Smallest Enclosing Disc` in "Computational Geometry - Algorithms and Applications - Third Edition"
     *
     * @param  P set of points {x, y}
     * @param  q point {x, y} on the boundary of the new disc
     * @return {Disc} smallest enclosing disc for P with q on its boundary
     */

    private Disc miniDiscWithPoint(ArrayList<Robot> P, Point2D.Double q){
        Point2D.Double p1 = P.get(0).getPosition();
        Disc D1 = new Disc(p1,q);
//        System.out.println("2:suceess");
        for(int i=1; i<P.size();i++){
            Point2D.Double pi = P.get(i).getPosition();
            if(!D1.contains(pi)){
                D1 = miniDiscWith2Points(new ArrayList<Robot>(P.subList(0,i)),pi,q);
            }

        }
        return D1;

    }



    /**
     * Based on `Smallest Enclosing Disc` in "Computational Geometry - Algorithms and Applications - Third Edition"
     *
     * @param  P set of points {x, y}
     * @param q1 point {x, y} on the boundary of the new disc
     * @param q2 point {x, y} on the boundary of the new disc
     * @return {Disc} smallest enclosing disc for P with q1 and q2 on its boundary
     */
    private Disc miniDiscWith2Points(ArrayList<Robot> P,Point2D.Double q1,Point2D.Double q2){
        Disc D0 = new Disc(q1,q2);
        for (Robot pk : P){
            if(!D0.contains(pk.getPosition())){
                D0 = new Disc(q1,q2,pk.getPosition());
            }
        }


    return D0;
    }


    /**
     * Apply the algorithm to make sure that the next position (ci) of the current robot (Ri) stills maintains connectivity
     * with its neighbours (R)
     *
     * @param  V vision of robot
     * @param  ci next position of robot
     * @param  Ri  current position of robot
     * @param  R visible robots list of current robot
     */

    public Point2D.Double getConnectedCenter(double V,Point2D.Double ci, Point2D.Double Ri,ArrayList<Robot> R){
        Vector Vgoal = new Vector(Ri,ci);
        double D = Vgoal.getNorm();
        System.out.println("D!: "+D);
        System.out.println("Vgoal: "+Vgoal.getNorm());
        if(Vgoal.getNorm()==0){
            return Ri;
        }
        ArrayList<Double> test = new ArrayList<Double>();
        for(Robot Rj:R){
            Vector Vrirj = new Vector(Ri,Rj.getPosition());
            double dj = Vrirj.getNorm();
            ArrayList<Double> cosAndsin = Vrirj.getCosAndSin(Vgoal);
            double lj = (dj/2*cosAndsin.get(0))+Math.sqrt((V/2)*(V/2)-Math.pow((dj/2*cosAndsin.get(1)),2));
            if(!((lj+"").contains("."))){
                System.out.println("===========");
                System.out.println("dj: "+dj);
                System.out.println("cos: "+cosAndsin.get(0));
                System.out.println("v/2*v/2 :"+(V/2)*(V/2) );
                System.out.println("sin: "+cosAndsin.get(1));
                System.out.println("============");
            }
            if((lj+"").contains(".")) {
                test.add(lj);
            }
        }
        if(test.size()!=0) {
            double limit = test.get(0);
            for (double num : test) {
                System.out.println("Num: "+num);
                if (num < limit) {
                    limit = num;
                }
            }


            D = Math.min(Vgoal.getNorm(), limit);
        }
        System.out.println("D: "+D*D);
        return Vgoal.resize(D).getEnd();

    }


    @Override
    public Point2D.Double next(List<Robot> robotList) {
//        System.out.println(getRobot().getSensor().getGlobalRobots().size());
        this.state = new ArrayList<>(this.getRobot().getSensor().getAllVisibleRobotsInLocalScale());
        this.range = getRobot().getVision();
//        System.out.println("=====================================");
//        int count = 1;
////        for (Robot robot : state) {
////            System.out.println("X: " + robot.getPositionX() + ", Y: " + robot.getPositionY()+" ,Point: "+count++);
////        }
////        this.state = robotList;
////        System.out.println("Has visible size: "+state.size());

        Point2D.Double point = generateOneRobot(new ArrayList<>(state),range);
        Point2D.Double secPoint = getRobot().getSensor().convertToGlobal(point);
//        sec.moveTo(secPoint.getX(), secPoint.getY());
        getRobot().getGraphicalDisplay().insertBottomLayer(sec);
      //  System.out.println("rsX: "+ point.getX()+" rsY: "+point.getY());

        return point;
    }

    @Override
    public String algorithmName() {
        return "Gathering(Go-To-Center)";
    }

    @Override
    public String algorithmDescription() {
        return "Robot will try to go to the position that has most other robots";
    }

    @Override
    public boolean timeToTerminate(List<Robot> globalRobotList) {
        Point2D.Double nextPosition = globalRobotList.get(0).getPosition();
        Boolean isTerminate = true;
        for(Robot robot: globalRobotList){
            if(!(robot.getPosition().equals(nextPosition))){
                isTerminate = false;
            }

        }
        return isTerminate;

    }

}
