package football.analyze.uiapp;

import com.vaadin.annotations.Theme;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@Theme("football")
@SpringUI
public class ApplicationUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout vertical = new VerticalLayout();
        Label label = new Label("Coming soon");
        label.setStyleName("my-label");
        vertical.setSizeFull();
        vertical.addComponent(label);
        vertical.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        vertical.addLayoutClickListener((LayoutEvents.LayoutClickListener) event -> Notification.show("Be Patient",
                Notification.Type.HUMANIZED_MESSAGE));
        setContent(vertical);
    }
}
