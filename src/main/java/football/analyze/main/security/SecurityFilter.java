package football.analyze.main.security;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Hassan Mushtaq
 * @since 5/28/18
 */
public class SecurityFilter implements ViewChangeListener {

    private final UI ui;

    public SecurityFilter(UI ui) {
        this.ui = ui;
    }


    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        boolean loggedOut = StringUtils.isBlank((String) VaadinSession.getCurrent().getAttribute("JWT_TOKEN"));
        if (loggedOut) {
            ui.getPage().setLocation("/Login");
        }
        return true;
    }
}
