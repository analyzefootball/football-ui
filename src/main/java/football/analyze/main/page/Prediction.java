package football.analyze.main.page;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import football.analyze.main.ApplicationUI;
import football.analyze.main.data.play.PlayedMatch;
import football.analyze.main.data.prediction.PredictionLoader;

import java.util.List;

public class Prediction extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;

    private String jwtToken;

    private PredictionLoader predictionLoader;

    @Override
    public void enter(ViewChangeEvent event) {

        jwtToken = (String) VaadinSession.getCurrent().getAttribute("JWT_TOKEN");
        predictionLoader = ((ApplicationUI) UI.getCurrent()).getPredictionLoader();
        List<PlayedMatch> predictions = predictionLoader.getAllPredictions(jwtToken);
        predictions.forEach(playedMatch -> {
            Panel panel = new Panel();
            panel.setCaptionAsHtml(true);
            panel.setCaption(panelCaptionAsHTML(playedMatch));
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setSpacing(true);
            verticalLayout.setSizeFull();
            playedMatch.getUserMatchPredictions().forEach(userMatchPrediction -> {
                Label label = new Label();
                label.setContentMode(ContentMode.HTML);
                label.setValue("<span>" + userMatchPrediction.getUser() + ":&nbsp;&nbsp;&nbsp;&nbsp;"
                        + playedMatch.getHomeTeam().getName() + "&nbsp;&nbsp;&nbsp;&nbsp;"
                        + userMatchPrediction.getHomeTeamScore() + "&nbsp;&nbsp;&nbsp;&nbsp;"
                        + playedMatch.getAwayTeam().getName() + "&nbsp;&nbsp;&nbsp;&nbsp;"
                        + userMatchPrediction.getAwayTeamScore()
                        + "</span></br>");
                verticalLayout.addComponent(label);
            });
            panel.setContent(verticalLayout);
            panel.setSizeUndefined();
            addComponent(panel);
        });
        setSizeFull();
    }

    private String panelCaptionAsHTML(PlayedMatch playedMatch) {
        String scoreString = "&nbsp;&nbsp;v.&nbsp;&nbsp;";
        if (playedMatch.getHomeTeamScore() != null && playedMatch.getAwayTeamScore() != null) {
            scoreString = "&nbsp;&nbsp;<b>" + playedMatch.getHomeTeamScore() + "&nbsp;-&nbsp;" + playedMatch.getAwayTeamScore() + "</b>&nbsp;&nbsp;";
        }
        String caption = "<img src=\"" + playedMatch.getHomeTeam().getFlagUrl() + "\" alt=\"" + playedMatch.getHomeTeam().getName() + "\"/>" +
                "&nbsp;<b>" + playedMatch.getHomeTeam().getName() + "</b>" +
                scoreString +
                "<img src=\"" + playedMatch.getAwayTeam().getFlagUrl() + "\" alt=\"" + playedMatch.getAwayTeam().getName() + "\"/>" +
                "&nbsp;<b>" + playedMatch.getAwayTeam().getName() + "</b>";
        return caption;
    }
}