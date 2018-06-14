package football.analyze.main;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;
import elemental.json.JsonArray;
import football.analyze.main.data.prediction.PredictionLoader;
import football.analyze.main.data.schedule.ScheduleLoader;
import football.analyze.security.JWTService;
import football.analyze.security.SecurityService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

@SpringUI(path = "/")
@Theme("football")
@Title("Fifa 2018 World Cup")
@Push
@PushStateNavigation
@Slf4j
public class ApplicationUI extends UI implements DetachListener {

    @Getter
    private final ScheduleLoader scheduleLoader;

    @Getter
    private final PredictionLoader predictionLoader;

    @Getter
    private final SecurityService securityService;

    private final JWTService jwtService;

    private Menu menu;

    public ApplicationUI(ScheduleLoader scheduleLoader, PredictionLoader predictionLoader, SecurityService securityService, JWTService jwtService) {
        this.scheduleLoader = scheduleLoader;
        this.predictionLoader = predictionLoader;
        this.securityService = securityService;
        this.jwtService = jwtService;
    }

    @Override
    protected void init(VaadinRequest request) {
        setPollInterval(5000);
        String jwtToken = (String) VaadinSession.getCurrent().getAttribute("JWT_TOKEN");
        if (jwtToken != null && jwtService.isAdmin(jwtToken)) {
            this.menu = new Menu(true);
        } else {
            this.menu = new Menu(false);
        }

        menu.addSignOutListener((Button.ClickListener) event -> {
            getUI().getPage().setLocation("/Login");
            VaadinSession.getCurrent().close();
        });

        getNavigator().addViewChangeListener(new ViewChangeFilter(getUI()));

        setContent(menu.getHybridMenu());
        setSizeFull();

        JavaScript.getCurrent().addFunction("aboutToClose", new JavaScriptFunction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void call(JsonArray arguments) {
                detach();
            }
        });

        Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e) { var e = e || window.event; aboutToClose(); return; };");

        getNavigator().navigateTo("Home");
    }

    @PreDestroy
    void destroy() {
        super.detach();
        getUI().close();
    }

    @Override
    public void detach(DetachEvent event) {
        destroy();
    }
}
