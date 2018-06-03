package football.analyze.main;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import football.analyze.main.page.*;
import kaesdingeling.hybridmenu.HybridMenu;
import kaesdingeling.hybridmenu.components.HMButton;
import kaesdingeling.hybridmenu.components.HMLabel;
import kaesdingeling.hybridmenu.components.LeftMenu;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.design.DesignItem;
import lombok.Getter;

/**
 * @author Hassan Mushtaq
 * @since 6/1/18
 */
public class Menu {

    private HMButton adminButton;

    private HMButton signOutButton;

    @Getter
    private HybridMenu hybridMenu;

    public Menu(boolean admin) {
        hybridMenu = HybridMenu.get()
                .withNaviContent(new Home())
                .withConfig(MenuConfig.get().withDesignItem(DesignItem.getWhiteDesign()))
                .build();

        hybridMenu.getNotificationCenter()
                .setNotiButton(HMButton.get()
                        .withDescription("Notifications"));

        LeftMenu leftMenu = hybridMenu.getLeftMenu();

        leftMenu.add(HMLabel.get()
                .withCaption("<b>Fifa 2018 World Cup</b>")
                .withIcon(new ThemeResource("images/fifa2018.png")));

        hybridMenu.getBreadCrumbs().setRoot(leftMenu.add(HMButton.get()
                .withCaption("Home")
                .withIcon(VaadinIcons.HOME)
                .withNavigateTo(Home.class)));

        leftMenu.add(HMButton.get()
                .withCaption("Predictions")
                .withIcon(VaadinIcons.MEGAFONE)
                .withNavigateTo(Prediction.class));

        leftMenu.add(HMButton.get()
                .withCaption("Points")
                .withIcon(VaadinIcons.TABLE)
                .withNavigateTo(Points.class));

        leftMenu.add(HMButton.get()
                .withCaption("Schedule")
                .withIcon(VaadinIcons.CALENDAR)
                .withNavigateTo(Schedule.class));

        leftMenu.add(HMButton.get()
                .withCaption("Group")
                .withIcon(VaadinIcons.BAR_CHART)
                .withNavigateTo(Group.class));

        leftMenu.add(HMButton.get()
                .withCaption("Profile")
                .withIcon(VaadinIcons.USER)
                .withNavigateTo(Profile.class));
        if (admin) {
            leftMenu.add(HMButton.get()
                    .withCaption("Administration")
                    .withIcon(VaadinIcons.USERS)
                    .withNavigateTo(Admin.class));
        }
        signOutButton = HMButton.get()
                .withCaption("Sign Out")
                .withIcon(VaadinIcons.SIGN_OUT);

        leftMenu.add(signOutButton);

    }

    public void addSignOutListener(Button.ClickListener listener) {
        signOutButton.addClickListener(listener);
    }
}
