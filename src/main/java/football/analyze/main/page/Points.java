package football.analyze.main.page;

import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import football.analyze.main.ApplicationUI;
import football.analyze.main.data.play.UserPoints;
import football.analyze.main.data.points.PointsLoader;

import java.util.List;

public class Points extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;

    @Override
    public void enter(ViewChangeEvent event) {
        String jwtToken = (String) VaadinSession.getCurrent().getAttribute("JWT_TOKEN");
        PointsLoader pointsLoader = ((ApplicationUI) UI.getCurrent()).getPointsLoader();
        List<UserPoints> userPoints = pointsLoader.getPoints(jwtToken);

        Grid<UserPoints> grid = new Grid<>();
        grid.setCaption("Points Table");
        grid.setItems(userPoints);
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(userPoints.size());

        grid.addColumn((ValueProvider<UserPoints, String>) UserPoints::getUser).setCaption("Name").setResizable(false).setSortable(false).setExpandRatio(1);
        grid.addColumn((ValueProvider<UserPoints, Integer>) UserPoints::getTotal).setCaption("Total Points").setWidth(200).setResizable(false);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        setSizeUndefined();
        addComponent(grid);
    }
}