package garden.algorithms;

import garden.algorithms.src.gatheringalgorithm.SEC;
import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class FakedAlgorithm_UpdateColor extends Algorithm {

    SEC sec;

    public FakedAlgorithm_UpdateColor() {

    }

    @Override
    public Point next(List<Robot> robots) {
        Point point = new Point();
        point.setLocation(1, 0);

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
