package garden.algorithms.src.gatheringalogrithm;

import java.awt.*;

/**
 * This class defines simple behaviours and minimal data of a disc.
 * Discs are used in the algorithm to find the smallest enclosing disc, which is necessary
 * to determine the next position of robots in a 2D environment.
 */
public class Disc {

    /**
     * The middle coordinate of the disc.
     */
    private Point center;


    /**
     * The half length of the disc
     */
    private double rSquared;


    /**
     * This inner class help to save result in a tuple for method 'findDiscWith3Points'
     */
    private class discResult{
        private double x;
        private double y;
        private double rSquared;
        public discResult(double x,double y,double rSquared){
            this.x = x;
            this.y = y;
            this.rSquared = rSquared;
        }
    }

    public Disc(Point p1,Point p2){
        center = new Point();
        this.center.setLocation((p1.getX()+p2.getX())/2,(p1.getY()+p2.getY())/2);
        this.rSquared = Math.pow((p1.getX()-this.center.getX()),2)+Math.pow((p1.getY()-this.center.getY()),2);

    }

    public Disc(Point p1,Point p2,Point p3){
        center = new Point();

            discResult rs = findDiscWith3Points2(p1,p2,p3);
            this.center.setLocation(rs.x,rs.y);
            this.rSquared = rs.rSquared;

    }

    /**
     * Determines if the given point is contained within the current disc
     *
     * @param p coordinates of a given point
     */
    public Boolean contains (Point p){
        double rSquared = Math.pow((p.getX()-this.center.getX()),2)+Math.pow((p.getY()-this.center.getY()),2);
        boolean result = rSquared <= this.rSquared;
        return  result;
    }

    /**
     * Finds the unique disc provided 3 points on its boundary
     * Calculations are the simplified state of the computation determinant of a special matrix
     * Based on this answer: https://math.stackexchange.com/a/1460096
     *
     * @param  p1 first point
     * @param  p2 second point
     * @param  p3 third point
     * @return return a tuple contains coordinates of center and the length of radius
     */
    private discResult findDiscWith3Points(Point p1,Point p2,Point p3){
        double a21 = Math.pow(p1.getX(),2)+Math.pow(p1.getY(),2);
        double a31 = Math.pow(p2.getX(),2)+Math.pow(p2.getY(),2);
        double a41 = Math.pow(p3.getX(),2)+Math.pow(p3.getY(),2);

        // compute the minors
        double M11 = p1.getX()*(p2.getY()-p3.getY())-p1.getY()*(p2.getX()-p3.getX())+p2.getX()*p3.getX();
        double M12 = a21*(p2.getY()-p3.getY())-p1.getY()*(a31-a41)+a31*p3.getY()-p2.getY()*a41;
        double M13 = a21*(p2.getX()-p3.getX())-p1.getX()*(a31-a41)+a31*p3.getX()-p2.getX()*a41;
        double M14 = a21*(p2.getX()*p3.getY()-p2.getY()*p3.getX())-p1.getX()*(a31*p3.getY()-p2.getY()*a41)+
                p1.getY()*(a31*p3.getX()-p2.getX()*a41);

        // compute the center (x,y) and the radius of the disc
        double X = M12/M11/2;
        double Y = -M13/M11/2;
        double rSquared  = M14/M11+Math.pow(X,2)+Math.pow(Y,2);

        discResult rs = new discResult(X,Y,rSquared);

        return rs;
    }


    private discResult findDiscWith3Points2(Point p1,Point p2,Point p3){
        double a = p1.getX()-p2.getX();
        double b = p1.getY()-p2.getY();
        double c = p1.getX()-p3.getX();
        double d = p1.getY()-p3.getY();
        double a1 = ((Math.pow(p1.getX(),2)-Math.pow(p2.getX(),2))+(Math.pow(p1.getY(),2)-Math.pow(p2.getY(),2)))/2;
        double a2 = ((Math.pow(p1.getX(),2)-Math.pow(p3.getX(),2))+(Math.pow(p1.getY(),2)-Math.pow(p3.getY(),2)))/2;
        double theta = b*c-a*d;
        double x0 = (b*a2-d*a1)/theta;
        double y0 = (c*a1-a*a2)/theta;
        double rSquared = Math.pow(p1.getX()-x0, 2)+Math.pow(p1.getY()-y0, 2);








        discResult rs = new discResult(x0,y0,rSquared);

        return rs;
    }






    public Point getCenter(){
        return center;
    }


}
