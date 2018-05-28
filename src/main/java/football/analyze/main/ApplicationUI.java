package football.analyze.main;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import elemental.json.JsonArray;
import football.analyze.main.components.TopButton;
import football.analyze.main.page.*;
import football.analyze.main.security.SecurityFilter;
import kaesdingeling.hybridmenu.HybridMenu;
import kaesdingeling.hybridmenu.components.*;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.design.DesignItem;

@SpringUI(path = "/")
@Theme("football")
@Title("Fifa 2018 World Cup")
@Push
@PushStateNavigation
public class ApplicationUI extends UI implements DetachListener {

    private HybridMenu hybridMenu = null;

    @Override
    protected void init(VaadinRequest request) {
        hybridMenu = HybridMenu.get()
                .withNaviContent(new VerticalLayout())
                .withConfig(MenuConfig.get().withDesignItem(DesignItem.getWhiteDesign()))
                .build();

        hybridMenu.getNotificationCenter()
                .setNotiButton(HMButton.get()
                        .withDescription("Notifications"));

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

        getNavigator().addViewChangeListener(new SecurityFilter(getUI()));

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
        topMenu.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        topMenu.add(TopButton.get()
                .withIcon(VaadinIcons.USER)
                .withNavigateTo(MemberPage.class)
                .withDescription("My Profile")
        );

        topMenu.add(TopButton.get()
                .withIcon(VaadinIcons.EXIT)
                .withNavigateTo("/login")
                .withDescription("Logout")
                .withClickListener((Button.ClickListener) event -> VaadinSession.getCurrent().close())
        );

    }

    private void buildLeftMenu() {
        LeftMenu leftMenu = hybridMenu.getLeftMenu();

        leftMenu.add(HMLabel.get()
                .withCaption("<b>Fifa 2018 World Cup</b>")
                .withIcon(new ThemeResource("images/fifa2018.png")));

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

    @Override
    public void detach(DetachEvent event) {
        super.detach();
        getUI().close();
    }
}
