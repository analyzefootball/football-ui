package football.analyze.provision;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import football.analyze.security.SecurityService;
import football.analyze.security.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Hassan Mushtaq
 * @since 6/12/18
 */
@SpringUI(path = "/signup")
@Theme("football")
@Title("Fifa 2018 World Cup")
public class SignUpUI extends UI {

    private final SecurityService securityService;

    public SignUpUI(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void init(VaadinRequest request) {
        Invitation invitation = fetchEmail(request);
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        if (invitation == null || StringUtils.isBlank(invitation.getEmail())) {
            Label invalid = new Label("Invalid Invitation ID, please ask admin to send you an email again");
            invalid.addStyleName(ValoTheme.LABEL_FAILURE);
            rootLayout.addComponent(invalid);

        } else {
            FormLayout layout = new FormLayout();
            layout.setSpacing(true);
            layout.setMargin(true);
            layout.setCaption("Sign up for Fifa 2018 World Cup Prediction Contest");
            layout.setIcon(VaadinIcons.USER);

            TextField emailField = new TextField("username/email");
            emailField.setReadOnly(true);
            emailField.setWidth("300px");

            TextField fullNameField = new TextField("Full Name");
            fullNameField.setWidth("300px");

            PasswordField passwordField = new PasswordField("Password");
            passwordField.setWidth("300px");

            PasswordField confirmPasswordField = new PasswordField("Confirm Password");
            confirmPasswordField.setWidth("300px");

            Binder<User> binder = new Binder<>();

            binder.forField(emailField)
                    .asRequired("Email may not be empty")
                    .withValidator(new EmailValidator("Not a valid email address"))
                    .bind(User::getUsername, User::setUsername);

            binder.forField(fullNameField)
                    .asRequired("Name may not be empty")
                    .bind(User::getDisplayName, User::setDisplayName);

            binder.forField(passwordField)
                    .asRequired("Password may not be empty")
                    .withValidator(new StringLengthValidator(
                            "Password must be at least 8 characters long", 8, null))
                    .bind(User::getPassword, User::setPassword);

            binder.forField(confirmPasswordField)
                    .asRequired("Must confirm password")
                    .bind(User::getPassword, (person, password) -> {
                    });

            binder.withValidator(Validator.from(account -> {
                if (passwordField.isEmpty() || confirmPasswordField.isEmpty()) {
                    return true;
                } else {
                    return Objects.equals(passwordField.getValue(),
                            confirmPasswordField.getValue());
                }
            }, "Entered password and confirmation password must match"));

            Label validationStatus = new Label();
            binder.setStatusLabel(validationStatus);

            binder.setBean(new User("", invitation.getRole(), invitation.getEmail(), "", null));

            Button registerButton = new Button("Register");
            registerButton.setEnabled(false);
            registerButton.addClickListener(
                    event -> registerNewAccount(invitation.getId(), binder.getBean()));

            binder.addStatusChangeListener(
                    event -> registerButton.setEnabled(binder.isValid()));

            layout.addComponents(emailField, fullNameField, passwordField, confirmPasswordField, registerButton);
            layout.setSizeUndefined();
            rootLayout.addComponent(layout);
            rootLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        }
        super.setContent(rootLayout);
        super.setSizeFull();
    }

    private void registerNewAccount(String invitationId, User user) {
        try {
            if (securityService.registerUser(invitationId, user)) {
                Notification.show("Registration Successful, please login", Notification.Type.HUMANIZED_MESSAGE);
                getUI().getPage().setLocation("/");
            } else {
                Notification.show("Failed to register, please contact site admin", Notification.Type.ERROR_MESSAGE);
            }
        }
        catch (Exception e) {
            Notification.show("Failed to register, please contact site admin", Notification.Type.ERROR_MESSAGE);
        }

    }

    private Invitation fetchEmail(VaadinRequest request) {
        String invitationId = request.getParameter("invitationId");
        if (invitationId == null) {
            return null;
        }
        return securityService.fetchInviteEmail(invitationId);
    }
}