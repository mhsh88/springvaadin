package ir.sharifi.spring.service.security;

import ir.sharifi.spring.model.model.security.Permission;
import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.repository.security.PermissionRepository;
import ir.sharifi.spring.repository.security.RoleRepository;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("securityService")
public class BackOfficeUserDetailsService implements UserDetailsService {
    final static String ERROR_LOGIN_BAD_CREDENTIALS = "error.login.bad.credentials";
    final static String ERROR_USERNAME_NOT_FOUND = "error.username.not.found";

    private final SecurityUserRepository securityUserRepository;

    @Autowired
    public BackOfficeUserDetailsService(SecurityUserRepository securityUserRepository) {
        this.securityUserRepository = securityUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        SecurityUser user;
        try {
            user = securityUserRepository.getActiveUser(username);
        }
        catch (Exception e) {
            throw new BadCredentialsException(ERROR_LOGIN_BAD_CREDENTIALS);
        }
        if (user == null) {
            throw new UsernameNotFoundException(ERROR_USERNAME_NOT_FOUND);
        }
        return org.springframework.security.core.userdetails.User.withUsername(username)
                .password(user.getPassword()).authorities(convertToAuthorities(user.getPermissions()))
                .accountExpired(!user.getEnabled()).accountLocked(false).credentialsExpired(false)
                .disabled(user.getDeleted()).build();
    }

    private List<GrantedAuthority> convertToAuthorities(List<Permission> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Permission permission : permissions) {
            if (authorities.stream().noneMatch(a -> a.getAuthority().equals(permission.getName()))) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        return authorities;
    }
}
