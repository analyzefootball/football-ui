package football.analyze.main.page;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Setter;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import football.analyze.main.ApplicationUI;
import football.analyze.main.data.play.Match;
import football.analyze.main.data.play.Prediction;
import football.analyze.main.data.play.Team;
import football.analyze.main.data.prediction.PredictionLoader;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

public class MyPredictions extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;

    private String jwtToken;

    private String username;

    private PredictionLoader predictionLoader;

    @Override
    public void enter(ViewChangeEvent event) {
        jwtToken = (String) VaadinSession.getCurrent().getAttribute("JWT_TOKEN");
        username = (String) VaadinSession.getCurrent().getAttribute("USERNAME");
        predictionLoader = ((ApplicationUI) UI.getCurrent()).getPredictionLoader();
        List<Prediction> predictions = predictionLoader.getPredictions(jwtToken, username);

        Grid<Prediction> grid = new Grid<>();
        grid.setCaption("My Predictions");
        grid.setItems(predictions);
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(predictions.size());

        grid.addColumn((ValueProvider<Prediction, Integer>) prediction -> prediction.getMatch().getMatchNumber())
                .setCaption("Match Number").setWidth(150).setResizable(false);
        grid.addComponentColumn((ValueProvider<Prediction, Component>) this::matchDetail)
                .setCaption("Prediction")
                .setResizable(false)
                .setSortable(false).setExpandRatio(1);
        grid.addColumn((ValueProvider<Prediction, String>) prediction -> prediction.getMatch().getMatchType().name())
                .setResizable(false)
                .setCaption("Stage").setWidth(150);
        grid.addColumn((ValueProvider<Prediction, String>) prediction -> prediction.isLocked() ? "Yes" : "No")
                .setCaption("Locked")
                .setResizable(false)
                .setWidth(100);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.setStyleGenerator((StyleGenerator<Prediction>) item -> {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            if (!item.isLocked() && item.getMatch().getDateTime().isAfter(yesterday.atStartOfDay()) && (item.getMatch().getDateTime().isBefore(tomorrow.atStartOfDay()))) {
                return "locked current";
            }
            return "";
        });

        setSizeFull();
        addComponent(grid);
    }

    private HorizontalLayout matchDetail(Prediction prediction) {
        Match match = prediction.getMatch();
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

        Binder<Prediction> binder = new Binder<>();

        binder.setBean(prediction);

        TextField homeScoreField = new TextField();
        homeScoreField.setWidth("50px");
        homeScoreField.setHeight("25px");
        homeScoreField.setMaxLength(2);
        binder.forField(homeScoreField)
                .withValidator(new PositiveIntegerValidator("Must be a number"))
                .bind(((ValueProvider<Prediction, String>) prediction13 -> prediction13.getMatch().getHomeTeamScore() == null ? "" : prediction13.getMatch().getHomeTeamScore().toString()),
                        (Setter<Prediction, String>) (prediction12, s) -> prediction12.getMatch().setHomeTeamScore(parsetInteger(s)));

        homeScoreField.setReadOnly(prediction.isLocked());

        TextField awayScoreField = new TextField();
        awayScoreField.setWidth("50px");
        awayScoreField.setHeight("25px");
        awayScoreField.setMaxLength(2);
        binder.forField(awayScoreField)
                .withValidator(new PositiveIntegerValidator("Must be a number"))
                .bind(((ValueProvider<Prediction, String>) prediction13 -> prediction13.getMatch().getAwayTeamScore() == null ? "" : prediction13.getMatch().getAwayTeamScore().toString()),
                        (Setter<Prediction, String>) (prediction12, s) -> prediction12.getMatch().setAwayTeamScore(parsetInteger(s)));
        awayScoreField.setReadOnly(prediction.isLocked());

        if (!prediction.isLocked()) {
            binder.addStatusChangeListener(
                    event -> {
                        if (binder.isValid()) {
                            savePrediction(prediction);
                        }
                    });
        }

        matchDetail.addComponents(homeTeamLabel, homeScoreField, versus, awayScoreField, awayTeamLabel);
        matchDetail.setComponentAlignment(homeTeamLabel, Alignment.MIDDLE_RIGHT);
        matchDetail.setComponentAlignment(homeScoreField, Alignment.MIDDLE_LEFT);
        matchDetail.setComponentAlignment(versus, Alignment.MIDDLE_CENTER);
        matchDetail.setComponentAlignment(awayScoreField, Alignment.MIDDLE_LEFT);
        matchDetail.setComponentAlignment(awayTeamLabel, Alignment.MIDDLE_LEFT);
        matchDetail.setSizeFull();
        return matchDetail;
    }

    private void savePrediction(Prediction prediction) {
        if (prediction.getMatch().getHomeTeamScore() != null && prediction.getMatch().getAwayTeamScore() != null) {
            if (predictionLoader.savePrediction(jwtToken, username, prediction)) {
                Notification.show("Prediction saved", Notification.Type.TRAY_NOTIFICATION);
            } else {
                Notification.show("Some error occured, contact admin", Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    private Integer parsetInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e)   {
            return null;
        }
    }
}

class PositiveIntegerValidator extends AbstractValidator<String> {


    protected PositiveIntegerValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        boolean valid = false;
        if (StringUtils.isBlank(value) || StringUtils.isNumeric(value)) {
            valid = true;
        }
        return toResult(value, valid);
    }

}