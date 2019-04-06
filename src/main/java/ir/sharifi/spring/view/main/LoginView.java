package ir.sharifi.spring.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.*;

import java.util.Locale;

@Route("login")
@PageTitle("MyApp Login")
@HtmlImport("styles/shared-styles.html")
public class LoginView extends VerticalLayout implements LocaleChangeObserver, AfterNavigationObserver, BeforeEnterObserver {

    private LoginOverlay login = new LoginOverlay();
    Button button ;

    private Locale getNewLocale() {
        if (getLocale().getLanguage().equals("en")) {
            button.setText("English");
            return new Locale("fa", "IR");
        }
        else {
            button.setText("Persian");
            return Locale.ENGLISH;
        }
    }

    public LoginView(){
        login.setI18n(createTranslatedI18N());
        login.getElement().setAttribute("no-forgot-password", true);
        login.setAction("login");
        login.setOpened(true);


        button = new Button("Persian",
                event -> getUI().get().setLocale(getNewLocale()));
//        login.getElement().appendChild(button.getElement());
//        login.getElement().appendVirtualChild(button.getElement());

//        getElement().appendChild(login.getElement(),button.getElement());
        add(login,button);
    }

    private LoginI18n createTranslatedI18N() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.setForm(new LoginI18n.Form());


//        i18n.getHeader().setTitle(getTranslation(MY_APP_NAME.getKey());
//        //i18n.getHeader().setDescription("");
//        i18n.getForm().setSubmit(getTranslation(LOGIN.getKey()));
//        i18n.getForm().setTitle(getTranslation(LOGIN.getKey()));
//        i18n.getForm().setUsername(getTranslation(USERNAME.getKey()));
//        i18n.getForm().setPassword(getTranslation(PASSWORD.getKey()));
//        i18n.getErrorMessage().setTitle(getTranslation(LOGIN_ERROR_TITLE.getKey()));
//        i18n.getErrorMessage().setMessage(getTranslation(LOGIN_ERROR.getKey()));
//        i18n.setAdditionalInformation(getTranslation(LOGIN_INFO.getKey()));
        i18n.getHeader().setTitle(getTranslation("app.title"));


        //i18n.getHeader().setDescription("");
        i18n.getForm().setSubmit(getTranslation("login.login"));
        i18n.getForm().setTitle(getTranslation("login.login"));
        i18n.getForm().setUsername(getTranslation("login.username"));
        i18n.getForm().setPassword(getTranslation("login.password"));
        i18n.getErrorMessage().setTitle(getTranslation("login.login_error_title"));
        i18n.getErrorMessage().setMessage(getTranslation("login.login_error"));
        i18n.setAdditionalInformation(getTranslation("login.login_info"));

        return i18n;
    }


    @Override
    public void localeChange(LocaleChangeEvent event) {
        login.setI18n(createTranslatedI18N());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
//        Notification.show("afterNavigation");
//        getNewLocale();
        login.setError(
                event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    }
}
