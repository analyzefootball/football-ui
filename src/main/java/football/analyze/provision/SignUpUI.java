package football.analyze.provision;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import football.analyze.security.SecurityService;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Hassan Mushtaq
 * @since 6/12/18
 */
@SpringUI(path = "/signup")
@Theme("football")
@Title("Fifa 2018 World Cup")
public class SignUpUI extends UI {

    private TextField username;

    private TextField fullName;

    private PasswordField password;

    private Button signUp;


    private final SecurityService securityService;

    public SignUpUI(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        String email = fetchEmail(request);
        if (StringUtils.isBlank(email)) {
            Label invalid = new Label("Invalid Invitation ID, please ask admin to send you an email again");
            invalid.addStyleName(ValoTheme.LABEL_FAILURE);
            rootLayout.addComponent(invalid);
        } else {
            username = new TextField("Username");
            username.setValue(email);
            username.setEnabled(false);
            username.setIcon(VaadinIcons.USER);
            username.setWidth("300px");
            username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

            password = new PasswordField("Password");
            password.setIcon(VaadinIcons.LOCK);
            password.setWidth("300px");
            password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
            rootLayout.addComponent(username);
        }
        super.setContent(rootLayout);
        super.setSizeFull();
    }

    private String fetchEmail(VaadinRequest request) {
        String invitationId = request.getParameter("invitationId");
        if (invitationId == null) {
            return null;
        }
        return securityService.fetchInviteEmail(invitationId);
    }
}
