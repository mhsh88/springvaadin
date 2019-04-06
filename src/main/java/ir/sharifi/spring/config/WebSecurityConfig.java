package ir.sharifi.spring.config;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource(name = "securityService")
    private UserDetailsService userDetailsService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/VAADIN/**", "/HEARTBEAT/**", "/UIDL/**", "/resources/**"
                        , "/login", "/login**", "/login/**", "/manifest.json", "/icons/**", "/images/**",
                        // (development mode) static resources
                        "/frontend/**",
                        // (development mode) webjars
                        "/webjars/**",
                        // (development mode) H2 debugging console
                        "/h2-console/**",
                        // (production mode) static resources
                        "/frontend-es5/**", "/frontend-es6/**","/sw.js").permitAll()
                .requestMatchers(WebSecurityConfig::isFrameworkInternalRequest).permitAll()
//                .antMatchers("/**").fullyAuthenticated()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/login")
////                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/"))
//                .and()
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
////                .anyRequest().permitAll();
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successForwardUrl("/")
//                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                // Configure logout
                .and().logout().logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                //.invalidateHttpSession(true)
                .and()
                .sessionManagement()
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
//                .sessionRegistry(sessionRegistry())
                .expiredSessionStrategy(e -> {
                    final String redirectUrl = e.getRequest().getContextPath() + "/login";
                    if(WebSecurityConfig.isFrameworkInternalRequest(e.getRequest())) {
                        e.getResponse().setHeader("Content-Type", "text/plain");
                        e.getResponse().getWriter().write("Vaadin-Refresh: " + redirectUrl);
                    } else {
                        e.getResponse().sendRedirect(redirectUrl);
                    }
                })
                .expiredUrl("/login");
    }


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers(
//                // Vaadin Flow static resources
//                "/VAADIN/**",
//
//                // the standard favicon URI
//                "/favicon.ico",
//
//                // web application manifest
//                "/manifest.json",
//
//                // icons and images
//                "/icons/**",
//                "/images/**",
//
//                // (development mode) static resources
//                "/frontend/**",
//
//                // (development mode) webjars
//                "/webjars/**",
//
//                // (development mode) H2 debugging console
//                "/h2-console/**",
//
//                // (production mode) static resources
//                "/frontend-es5/**", "/frontend-es6/**");
//    }


    private static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(ServletHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    @Bean
    public DaoAuthenticationProvider createDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
