package football.analyze.main.page;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Setter;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import football.analyze.main.ApplicationUI;
import football.analyze.main.data.play.Match;
import football.analyze.main.data.play.Team;
import football.analyze.main.data.schedule.ScheduleLoader;
import football.analyze.security.JWTService;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

@NoArgsConstructor
public class Schedule extends VerticalLayout implements View {

    private String jwtToken;

    private ScheduleLoader scheduleLoader;

    @Override
    public void enter(ViewChangeEvent event) {
        jwtToken = (String) VaadinSession.getCurrent().getAttribute("JWT_TOKEN");
        scheduleLoader = ((ApplicationUI) UI.getCurrent()).getScheduleLoader();
        JWTService jwtService = ((ApplicationUI) UI.getCurrent()).getJwtService();
        boolean isAdmin = jwtService.isAdmin(jwtToken);
        List<Match> matches = scheduleLoader.getMatches(jwtToken);

        ZoneId zoneId = ZoneId.of(event.getNavigator().getUI().getPage().getWebBrowser().getTimeZoneId());

        Grid<Match> grid = new Grid<>();
        grid.setCaption("World Cup Schedule");
        grid.setItems(matches);
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(matches.size());

        grid.addColumn(Match::getMatchNumber).setCaption("Match Number").setWidth(150).setResizable(false);
        grid.addComponentColumn((ValueProvider<Match, Component>) match -> matchDetail(match, isAdmin))
                .setCaption("Match")
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


    private HorizontalLayout matchDetail(Match match, boolean isAdmin) {
        HorizontalLayout matchDetail = new HorizontalLayout();
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        Label homeTeamLabel = new Label();
        homeTeamLabel.setCaption(homeTeam.getName());
        if (homeTeam.getFlagUrl() != null) {
            homeTeamLabel.setIcon(new ExternalResource(homeTeam.getFlagUrl()));
        }

        Label versus = new Label(" vs. ");
        versus.setWidth("50px");


        Label awayTeamLabel = new Label();
        awayTeamLabel.setCaption(awayTeam.getName());
        if (awayTeam.getFlagUrl() != null) {
            awayTeamLabel.setIcon(new ExternalResource(awayTeam.getFlagUrl()));
        }

        if (isAdmin) {


            Binder<Match> binder = new Binder<>();

            binder.setBean(match);

            TextField homeScoreField = new TextField();
            homeScoreField.setWidth("50px");
            homeScoreField.setHeight("25px");
            homeScoreField.setMaxLength(2);
            binder.forField(homeScoreField)
                    .withValidator(new PositiveIntegerValidator("Must be a number"))
                    .bind(((ValueProvider<Match, String>) match13 -> match13.getHomeTeamScore() == null ? "" : match13.getHomeTeamScore().toString()),
                            (Setter<Match, String>) (match12, s) -> match12.setHomeTeamScore(parsetInteger(s)));


            homeScoreField.setReadOnly(!match.isMatchStarted());

            TextField awayScoreField = new TextField();
            awayScoreField.setWidth("50px");
            awayScoreField.setHeight("25px");
            awayScoreField.setMaxLength(2);
            binder.forField(awayScoreField)
                    .withValidator(new PositiveIntegerValidator("Must be a number"))
                    .bind(((ValueProvider<Match, String>) match13 -> match13.getAwayTeamScore() == null ? "" : match13.getAwayTeamScore().toString()),
                            (Setter<Match, String>) (match12, s) -> match12.setAwayTeamScore(parsetInteger(s)));

            awayScoreField.setReadOnly(!match.isMatchStarted());

            if (match.isMatchStarted()) {
                binder.addStatusChangeListener(
                        event -> {
                            if (binder.isValid()) {
                                saveMatch(match);
                            }
                        });
            }
            matchDetail.addComponents(homeTeamLabel, homeScoreField, versus, awayScoreField, awayTeamLabel);
            matchDetail.setComponentAlignment(homeTeamLabel, Alignment.MIDDLE_RIGHT);
            matchDetail.setComponentAlignment(homeScoreField, Alignment.MIDDLE_LEFT);
            matchDetail.setComponentAlignment(versus, Alignment.MIDDLE_CENTER);
            matchDetail.setComponentAlignment(awayScoreField, Alignment.MIDDLE_LEFT);
            matchDetail.setComponentAlignment(awayTeamLabel, Alignment.MIDDLE_LEFT);
        } else {
            matchDetail.addComponents(homeTeamLabel, versus, awayTeamLabel);
            matchDetail.setComponentAlignment(homeTeamLabel, Alignment.MIDDLE_RIGHT);
            matchDetail.setComponentAlignment(versus, Alignment.MIDDLE_CENTER);
            matchDetail.setComponentAlignment(awayTeamLabel, Alignment.MIDDLE_LEFT);
        }
        matchDetail.setSizeFull();
        return matchDetail;
    }

    private Integer parsetInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    private void saveMatch(Match match) {
        if (match.getHomeTeamScore() != null && match.getAwayTeamScore() != null) {
            try {
                scheduleLoader.saveMatch(jwtToken, match);
                Notification.show("Match saved", Notification.Type.TRAY_NOTIFICATION);
            } catch (Exception e)   {
                Notification.show("Some error occured, contact admin", Notification.Type.ERROR_MESSAGE);
            }
        }
    }
}