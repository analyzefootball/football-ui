package football.analyze.main.page;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Home extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;

    @Override
    public void enter(ViewChangeEvent event) {
        Label title = new Label();

        title.setCaption("My Predictions");
        title.setValue("Coming Soon");

        addComponent(title);

        Label utcDate = new Label();
        utcDate.setValue(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));


        String zoneId = event.getNavigator().getUI().getPage().getWebBrowser().getTimeZoneId();
        Label bdate = new Label();
        bdate.setValue(LocalDateTime.now(ZoneId.of(zoneId)).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        addComponent(utcDate);
        addComponent(bdate);
    }
}