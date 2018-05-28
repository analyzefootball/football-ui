package football.analyze.login;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import football.analyze.main.security.SecurityService;
import football.analyze.main.security.User;

@SpringUI(path = "/login")
@Theme("football")
@Title("Fifa 2018 World Cup")
public class LoginUI extends UI {

    private TextField username;

    private PasswordField password;


    private Button login;

    private Label loginFailedLabel;
    private Label loggedOutLabel;

    private final SecurityService securityService;

    public LoginUI(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void init(VaadinRequest request) {
        Component loginLayout = buildLoginLayout(request);
        VerticalLayout rootLayout = new VerticalLayout(loginLayout);
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
        super.setContent(rootLayout);
        super.setSizeFull();
    }

    private Component buildLoginLayout(VaadinRequest request) {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
        loginPanel.addStyleName("login-panel");
        Responsive.makeResponsive(loginPanel);

        if (request.getParameter("logout") != null) {
            loggedOutLabel = new Label("You have been logged out!");
            loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
            loggedOutLabel.setWidth(100, Unit.PERCENTAGE);
            loginPanel.addComponent(loggedOutLabel);
        }
        loginFailedLabel = new Label();
        loginFailedLabel.setWidth(100, Unit.PERCENTAGE);
        loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        loginFailedLabel.setVisible(false);

        loginPanel.addComponent(loginFailedLabel);

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        return loginPanel;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Fifa 2018 World Cup");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);
        return labels;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.addStyleName("fields");

        username = new TextField("Username");
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password = new PasswordField("Password");
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        login = new Button("Login");
        login.addStyleName(ValoTheme.BUTTON_PRIMARY);
        login.setDisableOnClick(true);
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.focus();

        fields.addComponents(username, password, login);
        fields.setComponentAlignment(login, Alignment.BOTTOM_LEFT);
        login.addClickListener(e -> login());
        return fields;
    }

    private void login() {
        User user = securityService.authenticate(username.getValue(), password.getValue());
        if (user!=null && user.getCredentials()!=null && user.getCredentials().getJwtToken()!=null) {
            VaadinSession.getCurrent().setAttribute("JWT_TOKEN", "something");
            getUI().getPage().setLocation("/");
        }
        else    {
            login.setEnabled(true);
            Notification.show("Invalid Username/Password", Notification.Type.ERROR_MESSAGE);
        }
    }
}
