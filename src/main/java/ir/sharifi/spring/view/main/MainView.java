package ir.sharifi.spring.view.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import ir.sharifi.spring.repository.security.PermissionRepository;
import ir.sharifi.spring.repository.security.RoleRepository;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import ir.sharifi.spring.service.security.SecurityUtils;
import ir.sharifi.spring.view.main.component.security.permission.PermissionEdit;
import ir.sharifi.spring.view.main.component.security.permission.PermissionLayout;
import ir.sharifi.spring.view.main.component.security.role.RoleEdit;
import ir.sharifi.spring.view.main.component.security.role.RoleLayout;
import ir.sharifi.spring.view.main.component.security.user.UserEdit;
import ir.sharifi.spring.view.main.component.security.user.UserLayout;
import ir.sharifi.spring.view.main.component.test.leaveTime.LeaveTimeLayout;
import ir.sharifi.spring.view.main.component.test.leaveTimeRequestManage.LeaveTimeRequestManageLayout;
import ir.sharifi.spring.view.main.component.test.leaveTimeRequests.LeaveTimeRequestLayout;
import ir.sharifi.spring.view.main.component.test.memberWorkTime.WorkTimeLayout;
import ir.sharifi.spring.view.main.component.test.memberWorkTimeManage.WorkTimeManageLayout;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Route
public class MainView extends VerticalLayout implements LocaleChangeObserver {

    private final SecurityUserRepository repo;
    private final ModelMapper modelMapper;
    private final UserEdit userEdit;

    private final RoleRepository roleRepository;
    private final RoleEdit roleEdit;

    private final PermissionRepository permissionRepository;
    private final PermissionEdit permissionEdit;


    private final WorkTimeLayout workTimeLayout;

    private final WorkTimeManageLayout workTimeManageLayout;
    private final LeaveTimeRequestManageLayout leaveTimeRequestManageLayout;

    private final LeaveTimeLayout leaveTimeLayout;

    private final LeaveTimeRequestLayout leaveTimeRequestLayout;



    private final ApplicationContext context;

    private AppLayoutMenu menu;
    private AppLayout appLayout;
    private List<AppLayoutMenuItem> menuItems = new ArrayList<>();






    @Autowired
    public MainView(SecurityUserRepository repo,
                    ModelMapper modelMapper,
                    UserEdit userEdit,
                    RoleRepository roleRepository,
                    RoleEdit roleEdit,
                    PermissionRepository permissionRepository,
                    PermissionEdit permissionEdit,
                    WorkTimeLayout workTimeLayout,
                    WorkTimeManageLayout workTimeManageLayout,
                    LeaveTimeRequestManageLayout leaveTimeRequestManageLayout,
                    LeaveTimeLayout leaveTimeLayout,
                    LeaveTimeRequestLayout leaveTimeRequestLayout, ApplicationContext context) throws IOException {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.userEdit = userEdit;
        this.roleRepository = roleRepository;
        this.roleEdit = roleEdit;
        this.permissionRepository = permissionRepository;
        this.permissionEdit = permissionEdit;
        this.workTimeLayout = workTimeLayout;
        this.workTimeManageLayout = workTimeManageLayout;
        this.leaveTimeRequestManageLayout = leaveTimeRequestManageLayout;
        this.leaveTimeLayout = leaveTimeLayout;
        this.leaveTimeRequestLayout = leaveTimeRequestLayout;
        this.context = context;


        init();
    }

    private void init() throws IOException {

        UserLayout userLayout = new UserLayout(repo, userEdit);
        RoleLayout roleLayout = new RoleLayout(roleRepository,roleEdit);
        PermissionLayout permissionLayout = new PermissionLayout(permissionRepository,permissionEdit);
         appLayout = new AppLayout();
         menu = appLayout.createMenu();

        Image img = new Image("classpath:/29010360.png", "Dadehpardaz Logo");
        img.setHeight("55px");
        appLayout.setBranding(img);
        if (SecurityUtils.isUserLoggedIn()) {

            if(SecurityUtils.isAccessGranted(UserLayout.class)){
                setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.USERS.create(), getTranslation("main.users",getLocale()),e->appLayout.setContent(userLayout)));

            }
            if(SecurityUtils.isAccessGranted(RoleLayout.class)){
                setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.ROAD_BRANCHES.create(), getTranslation("main.users.roles",getLocale()), e->appLayout.setContent(roleLayout)));
            }
            if(SecurityUtils.isAccessGranted(PermissionLayout.class)){
                setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.EYE_SLASH.create(), getTranslation("main.users.permissions",getLocale()), e->appLayout.setContent(permissionLayout)));
            }
            if(SecurityUtils.isAccessGranted(WorkTimeLayout.class)){
                setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.TIMER.create(), getTranslation("main.users.work.time",getLocale()), e->appLayout.setContent(workTimeLayout)));
            }
            if(SecurityUtils.isAccessGranted(WorkTimeManageLayout.class)){
                setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.TIMER.create(), getTranslation("main.users.work.time",getLocale()), e->appLayout.setContent(workTimeManageLayout)));
            }
            if(SecurityUtils.isAccessGranted(LeaveTimeRequestManageLayout.class)){
                setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.TIMER.create(), getTranslation("main.users.leave.time.request.manage",getLocale()), e->appLayout.setContent(leaveTimeRequestManageLayout)));
            }

            if(SecurityUtils.isAccessGranted(LeaveTimeLayout.class)){
                setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.LEVEL_UP.create(), getTranslation("main.users.leave.time",getLocale()), e->appLayout.setContent(leaveTimeLayout)));
            }
            if(SecurityUtils.isAccessGranted(LeaveTimeRequestLayout.class)){
                setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.LEVEL_DOWN.create(), getTranslation("main.users.leave.time.requests",getLocale()), e->appLayout.setContent(leaveTimeRequestLayout)));
            }



        }



        setMenuItem(menu,new AppLayoutMenuItem(VaadinIcon.ARROW_RIGHT.create(), getTranslation("app.logout",getLocale()), e ->
                UI.getCurrent().getPage().executeJavaScript("location.assign('logout')")));

//        setMenuItem(menu, languageMenuItem );

        Component content = new H3("DadehPardaz Sharif");
//
//        Component content = new Span(new H3("Page title"),
//                new Span("Page content"));
        appLayout.setContent(content);

        add(appLayout);

    }
    private void setMenuItem(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menuItems.add(menuItem);
        menu.addMenuItem(menuItem);
    }


    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

   }
}
