package football.analyze.main;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
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
import football.analyze.events.LoginEvent;

import javax.annotation.PreDestroy;

@SpringUI(path = "/")
@Theme("football")
@Title("Fifa 2018 World Cup")
@Push
@PushStateNavigation
public class ApplicationUI extends UI implements DetachListener {

    private Menu menu;

    private final EventBus eventBus;

    public ApplicationUI(EventBus eventBus) {
        this.eventBus = eventBus;

    }

    @Override
    protected void init(VaadinRequest request) {
        this.menu = new Menu();
        eventBus.register(this);
        menu.addSignOutListener((Button.ClickListener) event -> {
            getUI().getPage().setLocation("/Login");
            VaadinSession.getCurrent().close();
        });
        setContent(menu.getHybridMenu());

        getNavigator().addViewChangeListener(new ViewChangeFilter(getUI()));


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

    @Subscribe
    void something(LoginEvent event) {
        menu.showAdmin();
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
