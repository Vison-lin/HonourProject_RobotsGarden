package controller.controlpanel;

import core.Statistic;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import statistics.LongestDistance;
import statistics.LongestRun;
import statistics.SumOfDistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatisticPageController extends VBox {

    private static List<Statistic> statistics = new ArrayList<>();
    @FXML
    private Text statisticDisplay;
    private ControlPanelFacade controlPanelFacade;

    public StatisticPageController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/control_panel_component/statistic.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            initStatisticDisplay();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public Text getStatisticDisplay() {
        return statisticDisplay;
    }

    private void initStatisticDisplay() {
        statisticDisplay.setText(ControlPanelFacade.STATISTIC_UNAVAILABLE_DISPLAY);
        //todo faked file scan for Statistic
        statistics.add(new LongestRun());
        statistics.add(new LongestDistance());
        statistics.add(new SumOfDistance());

        statisticDisplay.setText(ControlPanelFacade.STATISTIC_DEFAULT_DISPLAY);
    }

    List<Statistic> getStatistics() {
        return statistics;
    }

    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
    }
}
