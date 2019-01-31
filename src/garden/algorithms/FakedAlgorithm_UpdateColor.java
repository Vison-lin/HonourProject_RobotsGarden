package garden.algorithms;

import garden.algorithms.src.gatheringalgorithm.SEC;
import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

public class FakedAlgorithm_UpdateColor extends Algorithm {

    SEC sec;

    private int ctr = 0;

    public FakedAlgorithm_UpdateColor() {

    }

    @Override
    public Point2D.Double next(List<Robot> robots) {
        Point2D.Double point = new Point2D.Double();
        point.setLocation(1, 0);
        ctr++;
        System.out.println(this.ctr);
        sec = new SEC();
        getRobot().getGraphicalDisplay().insertBottomLayer(sec);
        Random random = new Random();
        sec.moveTo(random.nextInt(500), random.nextInt(500));
        System.out.println("# of BottomLayer: " + getRobot().getGraphicalDisplay().getBottomLayers().size());
        return point;
    }

    @Override
    public String algorithmName() {
        return "FAKED";
    }

    @Override
    public String algorithmDescription() {
        return "???!!!___";
    }
}
