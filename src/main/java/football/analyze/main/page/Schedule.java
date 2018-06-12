package football.analyze.main.page;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
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

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' HH:mm z");
        ZoneId zoneId = ZoneId.of(event.getNavigator().getUI().getPage().getWebBrowser().getTimeZoneId());

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        matches.forEach(match -> {
            Label matchNumber = new Label();
            matchNumber.setCaption("Match # " + match.getMatchNumber().toString());

            HorizontalLayout matchDetail = new HorizontalLayout();
            Team homeTeam = match.getHomeTeam();
            Team awayTeam = match.getAwayTeam();

            Label homeTeamLabel = new Label();
            homeTeamLabel.setCaption(homeTeam.getName());
            if (homeTeam.getFlagUrl() != null) {
                homeTeamLabel.setIcon(new ExternalResource(homeTeam.getFlagUrl()));
            }
            Label versus = new Label("v");

            Label awayTeamLabel = new Label();
            awayTeamLabel.setCaption(awayTeam.getName());
            if (awayTeam.getFlagUrl() != null) {
                awayTeamLabel.setIcon(new ExternalResource(awayTeam.getFlagUrl()));
            }
            matchDetail.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            matchDetail.addComponents(homeTeamLabel, versus, awayTeamLabel);


            ZonedDateTime actualMatchTime = match.getDateTime().atZone(TimeZone.getDefault().toZoneId());

            Label utcLabel = new Label();
            utcLabel.setCaption("UTC Time: " + dateTimeFormatter.format(actualMatchTime));

            ZonedDateTime userLocaleMatchTime = actualMatchTime.withZoneSameInstant(zoneId);

            Label localTimeLabel = new Label();
            localTimeLabel.setCaption("Your Time: " + dateTimeFormatter.format(userLocaleMatchTime));

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            horizontalLayout.addComponentsAndExpand(matchNumber, matchDetail, localTimeLabel, utcLabel);
            horizontalLayout.setSizeFull();
            content.addComponent(horizontalLayout);
        });

        Panel panel = new Panel("World Cup Schedule");
        panel.setSizeFull();
        panel.setContent(content);
        addComponent(panel);
    }
}