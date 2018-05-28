package football.analyze.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import elemental.json.JsonArray;
import football.analyze.ui.page.*;
import kaesdingeling.hybridmenu.HybridMenu;
import kaesdingeling.hybridmenu.components.*;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.design.DesignItem;

@Theme("football")
@Title("Fifa 2018 World Cup")
@Push
@PushStateNavigation
@SpringUI
public class ApplicationUI extends UI {

    private HybridMenu hybridMenu = null;

    @Override
    protected void init(VaadinRequest request) {
        hybridMenu = HybridMenu.get()
                .withNaviContent(new VerticalLayout())
                .withConfig(MenuConfig.get().withDesignItem(DesignItem.getDarkDesign()))
                .build();

        buildTopOnlyMenu();
        buildLeftMenu();

        getNavigator().addViewChangeListener(new ViewChangeListener() {
            private static final long serialVersionUID = -1840309356612297980L;

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                if (event.getOldView() != null && event.getOldView().getClass().getSimpleName().equals(ThemeBuilderPage.class.getSimpleName())) {
                    hybridMenu.switchTheme(DesignItem.getDarkDesign());
                }
                return true;
            }
        });

        setContent(hybridMenu);

        JavaScript.getCurrent().addFunction("aboutToClose", new JavaScriptFunction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void call(JsonArray arguments) {
                detach();
            }
        });

        Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e) { var e = e || window.event; aboutToClose(); return; };");
    }

    private void buildTopOnlyMenu() {
        TopMenu topMenu = hybridMenu.getTopMenu();

        topMenu.add(HMTextField.get(VaadinIcons.SEARCH, "Search ..."));

        topMenu.add(HMButton.get()
                .withIcon(VaadinIcons.HOME)
                .withDescription("Home")
                .withNavigateTo(HomePage.class));

        hybridMenu.getNotificationCenter()
                .setNotiButton(topMenu.add(HMButton.get()
                        .withDescription("Notifications")));
    }

    private void buildLeftMenu() {
        LeftMenu leftMenu = hybridMenu.getLeftMenu();

        leftMenu.add(HMLabel.get()
                .withCaption("<b>Hybrid</b> Menu")
                .withIcon(new ThemeResource("images/hybridmenu-Logo.png")));

        hybridMenu.getBreadCrumbs().setRoot(leftMenu.add(HMButton.get()
                .withCaption("Home")
                .withIcon(VaadinIcons.HOME)
                .withNavigateTo(HomePage.class)));

        leftMenu.add(HMButton.get()
                .withCaption("Notification Builder")
                .withIcon(VaadinIcons.BELL)
                .withNavigateTo(NotificationBuilderPage.class));

        leftMenu.add(HMButton.get()
                .withCaption("Theme Builder")
                .withIcon(VaadinIcons.WRENCH)
                .withNavigateTo(ThemeBuilderPage.class));

        HMSubMenu memberList = leftMenu.add(HMSubMenu.get()
                .withCaption("Member")
                .withIcon(VaadinIcons.USERS));

        memberList.add(HMButton.get()
                .withCaption("Settings")
                .withIcon(VaadinIcons.COGS)
                .withNavigateTo(SettingsPage.class));

        memberList.add(HMButton.get()
                .withCaption("Member")
                .withIcon(VaadinIcons.USERS)
                .withNavigateTo(MemberPage.class));

        memberList.add(HMButton.get()
                .withCaption("Group")
                .withIcon(VaadinIcons.USERS)
                .withNavigateTo(GroupPage.class));

        HMSubMenu memberListTwo = memberList.add(HMSubMenu.get()
                .withCaption("Member")
                .withIcon(VaadinIcons.USERS));

        memberListTwo.add(HMButton.get()
                .withCaption("Settings")
                .withIcon(VaadinIcons.COGS)
                .withNavigateTo(SettingsPage.class));

        memberListTwo.add(HMButton.get()
                .withCaption("Member")
                .withIcon(VaadinIcons.USERS)
                .withNavigateTo(MemberPage.class));


        HMSubMenu demoSettings = leftMenu.add(HMSubMenu.get()
                .withCaption("Settings")
                .withIcon(VaadinIcons.COGS));

        demoSettings.add(HMButton.get()
                .withCaption("White Theme")
                .withIcon(VaadinIcons.PALETE)
                .withClickListener(e -> hybridMenu.switchTheme(DesignItem.getWhiteDesign())));

        demoSettings.add(HMButton.get()
                .withCaption("Dark Theme")
                .withIcon(VaadinIcons.PALETE)
                .withClickListener(e -> hybridMenu.switchTheme(DesignItem.getDarkDesign())));

        demoSettings.add(HMButton.get()
                .withCaption("Minimal")
                .withIcon(VaadinIcons.COG)
                .withClickListener(e -> hybridMenu.getLeftMenu().toggleSize()));
    }

    public HybridMenu getHybridMenu() {
        return hybridMenu;
    }
}
