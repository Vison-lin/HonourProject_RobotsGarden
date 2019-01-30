package garden.algorithms.src.gatheringalgorithm;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This class defines behaviour related to 2D vectors properties (like norms, products, and resizing)
 *
 */
public class Vector {
    /**
     * The start point of the vector
     */
    private Point2D.Double start;
    /**
     * The end point of the vector
     */
    private Point2D.Double end;
    /**
     * The delta coordinate of x
     */
    private double deltaX;
    /**
     * The delta coordinate of y
     */
    private double deltaY;
    /**
     *  The length of the vector
     */
    private double norm;


    public Vector(Point2D.Double p1,Point2D.Double p2){
        this.start = p1;
        this.end  = p2;
        this.deltaX = p2.getX() - p1.getX();
        this.deltaY = p2.getY() - p1.getY();
        this.norm = Math.sqrt(Math.pow(this.deltaX,2)+Math.pow(this.deltaY,2));

    }
    /**
     * Returns the scalar product of 2 vectors with components `x` and `y`
     *
     * @param otherVec the other vector
     */
    public double scalarProduct(Vector otherVec){
        return this.deltaX*otherVec.deltaX+this.deltaY*otherVec.deltaY;

    }

    /**
     * Returns the cosθ and sinθ of two vectors
     *
     * @param  otherVec the other vector
     */
    public ArrayList<Double> getCosAndSin(Vector otherVec){
        double cos = scalarProduct(otherVec)/(this.norm*otherVec.norm);
        double sin;
        if ((Math.sqrt(1-cos*cos))==Double.NaN){
          sin = 0;

        }else{  sin = Math.sqrt(1-cos*cos);}

        ArrayList rs = new ArrayList();
        rs.add(cos);
        rs.add(sin);
        return rs;
    }

    /**
     * Resizes a Vector of norm "x" to a norm of "newNorm" (keeping the same orientation)
     *
     * @param newNorm
     */
    public Vector resize(double newNorm){
        Vector vs = new Vector(this.start,this.end);
        if(this.norm!=newNorm){
            double newDeltaX = this.deltaX*newNorm/this.norm;
            double newDeltaY = this.deltaY*newNorm/this.norm;
            Point2D.Double newEnd= new Point2D.Double();
            newEnd.setLocation(this.start.getX()+newDeltaX,this.start.getY()+newDeltaY);
           vs = new Vector(this.start,newEnd);
            return vs;
        }
        return vs;

    }

    public double getNorm(){
        return this.norm;
    }


    public Point2D.Double getEnd(){
        return this.end;
    }


}
