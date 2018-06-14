package football.analyze.main.page;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import football.analyze.main.ApplicationUI;
import football.analyze.provision.Invitation;
import football.analyze.security.Role;
import football.analyze.security.SecurityService;

import java.util.Arrays;

public class Admin extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

        FormLayout layout = new FormLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setCaption("Send Invites");
        layout.setIcon(VaadinIcons.MAILBOX);

        TextField emailField = new TextField("username/email");
        emailField.setWidth("300px");


        NativeSelect<Role> roleSelect = new NativeSelect<>("Select role", Arrays.asList(Role.ADMIN, Role.REGULAR));

        roleSelect.setEmptySelectionAllowed(false);

        Binder<Invitation> binder = new Binder<>();

        binder.forField(emailField)
                .asRequired("Email may not be empty")
                .withValidator(new EmailValidator("Not a valid email address"))
                .bind(Invitation::getEmail, Invitation::setEmail);

        binder.forField(roleSelect)
                .asRequired("Role cannot be empty")
                .bind(Invitation::getRole, Invitation::setRole);


        Label validationStatus = new Label();
        binder.setStatusLabel(validationStatus);

        binder.setBean(new Invitation("", Role.REGULAR));

        Button sendInvite = new Button("Send Invite");
        sendInvite.setEnabled(false);
        sendInvite.addClickListener(
                event -> {
                    sendInvite.setEnabled(false);
                    boolean success = sendInvite(binder.getBean());
                    if (success)    {
                        Notification.show("Invitation Sent Successfully", Notification.Type.HUMANIZED_MESSAGE);
                    } else  {
                        Notification.show("Failed to send notification", Notification.Type.ERROR_MESSAGE);
                    }
                    binder.setBean(new Invitation("", Role.REGULAR));
                });

        binder.addStatusChangeListener(
                event -> sendInvite.setEnabled(binder.isValid()));

        layout.addComponents(emailField, roleSelect, sendInvite);
        layout.setSizeUndefined();
        addComponent(layout);
        setComponentAlignment(layout, Alignment.TOP_LEFT);

    }

    private boolean sendInvite(Invitation invitation) {
        String jwtToken = (String) VaadinSession.getCurrent().getAttribute("JWT_TOKEN");
        SecurityService securityService = ((ApplicationUI) UI.getCurrent()).getSecurityService();
        return securityService.sendInvite(invitation, jwtToken);
    }
}