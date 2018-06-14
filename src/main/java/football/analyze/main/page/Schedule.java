package football.analyze.main.page;

import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import football.analyze.main.ApplicationUI;
import football.analyze.main.data.play.Match;
import football.analyze.main.data.play.Team;
import football.analyze.main.data.schedule.ScheduleLoader;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

@NoArgsConstructor
public class Schedule extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeEvent event) {
        String jwtToken = (String) VaadinSession.getCurrent().getAttribute("JWT_TOKEN");
        ScheduleLoader scheduleLoader = ((ApplicationUI) UI.getCurrent()).getScheduleLoader();
        List<Match> matches = scheduleLoader.getMatches(jwtToken);

        ZoneId zoneId = ZoneId.of(event.getNavigator().getUI().getPage().getWebBrowser().getTimeZoneId());

        Grid<Match> grid = new Grid<>();
        grid.setCaption("World Cup Schedule");
        grid.setItems(matches);
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(matches.size());

        grid.addColumn(Match::getMatchNumber).setCaption("Match Number").setWidth(150).setResizable(false);
        grid.addComponentColumn((ValueProvider<Match, Component>) this::matchDetail).setCaption("Match")
                .setResizable(false)
                .setSortable(false).setExpandRatio(1);
        ;
        grid.addColumn((ValueProvider<Match, String>) match -> match.getDateTimeFormatted(zoneId))
                .setCaption("Your Local Time")
                .setResizable(false)
                .setWidth(300);
        grid.addColumn((ValueProvider<Match, String>) match -> match.getDateTimeFormatted(TimeZone.getDefault().toZoneId()))
                .setCaption("UTC Time")
                .setResizable(false)
                .setWidth(300);

        grid.setSelectionMode(Grid.SelectionMode.NONE);
        setSizeFull();
        addComponent(grid);
    }

    private HorizontalLayout matchDetail(Match match) {
        HorizontalLayout matchDetail = new HorizontalLayout();
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        Label homeTeamLabel = new Label();
        homeTeamLabel.setCaption(homeTeam.getName());
        if (homeTeam.getFlagUrl() != null) {
            homeTeamLabel.setIcon(new ExternalResource(homeTeam.getFlagUrl()));
        }
        Label versus = new Label(" v ");

        Label awayTeamLabel = new Label();
        awayTeamLabel.setCaption(awayTeam.getName());
        if (awayTeam.getFlagUrl() != null) {
            awayTeamLabel.setIcon(new ExternalResource(awayTeam.getFlagUrl()));
        }

        matchDetail.addComponents(homeTeamLabel, versus, awayTeamLabel);
        matchDetail.setComponentAlignment(homeTeamLabel, Alignment.MIDDLE_RIGHT);
        matchDetail.setComponentAlignment(versus, Alignment.MIDDLE_CENTER);
        matchDetail.setComponentAlignment(awayTeamLabel, Alignment.MIDDLE_LEFT);
        matchDetail.setSizeFull();
        return matchDetail;
    }
}