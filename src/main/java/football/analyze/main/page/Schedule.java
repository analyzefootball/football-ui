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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        grid.setItems(matches);
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(matches.size());

        grid.addColumn(Match::getMatchNumber).setCaption("Match Number");
        grid.addComponentColumn((ValueProvider<Match, Component>) match -> matchDetail(match)).setCaption("Match");
        grid.addColumn((ValueProvider<Match, String>) match -> match.getDateTimeFormatted(zoneId)).setCaption("Your Local Time");
        grid.addColumn((ValueProvider<Match, String>) match -> match.getDateTimeFormatted(TimeZone.getDefault().toZoneId())).setCaption("UTC Time");

        Panel panel = new Panel("World Cup Schedule");
        panel.setSizeFull();
        panel.setContent(grid);
        addComponent(panel);
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
        matchDetail.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        matchDetail.addComponents(homeTeamLabel, versus, awayTeamLabel);
        return matchDetail;
    }
}