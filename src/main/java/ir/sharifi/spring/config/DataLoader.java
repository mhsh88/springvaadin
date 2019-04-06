package ir.sharifi.spring.config;

import ir.sharifi.spring.model.model.security.Permission;
import ir.sharifi.spring.model.model.security.Role;
import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.repository.security.RoleRepository;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    SecurityUserRepository securityUserRepository;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    private List<Permission> permissions;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SecurityUser admin = securityUserRepository.getActiveUser("admin");
//        if(Objects.nonNull(admin)){
//            securityUserRepository.delete(admin);
//        }
        if(Objects.isNull(admin)) {
            admin = new SecurityUser();
            admin.setEnabled(true);
            admin.setUsername("admin");
            admin.setEmail("admin@dadehpardaz.ir");
            admin.setPassword(encoder.encode("admin"));
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setDeleted(false);
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setTitle("admin");
            permissions =
                    Stream.of("USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
                            "ROLE_READ", "ROLE_CREATE", "ROLE_UPDATE", "ROLE_DELETE",
                            "PERMISSION_CREATE", "PERMISSION_READ", "PERMISSION_UPDATE", "PERMISSION_DELETE").map(str -> {
                        Permission permission = new Permission();
                        permission.setName(str);
                        permission.setTitle(str.toLowerCase());
                        return permission;

                    }).collect(Collectors.toList());
            adminRole.setPermissions(permissions);
            Role finalRole1 = adminRole;
            admin.setRoles(new ArrayList<Role>() {{
                add(finalRole1);
            }});
            securityUserRepository.save(admin);
        }


        SecurityUser manager = securityUserRepository.getActiveUser("manager");
//        if(Objects.nonNull(manager)){
//            securityUserRepository.delete(manager);
//        }
        if(Objects.isNull(manager)) {
            manager = new SecurityUser();
            manager.setEnabled(true);
            manager.setUsername("manager");
            manager.setEmail("manager@dadehpardaz.ir");
            manager.setPassword(encoder.encode("manager"));
            manager.setFirstName("manager");
            manager.setLastName("manager");
            manager.setDeleted(false);
            Role managerRole = new Role();
            managerRole.setName("MANGER");
            managerRole.setTitle("manger");
            permissions =
                    Stream.of("WORK_TIME_MANAGE").map(str -> {
                        Permission permission = new Permission();
                        permission.setName(str);
                        permission.setTitle(str.toLowerCase());
                        return permission;

                    }).collect(Collectors.toList());
            managerRole.setPermissions(permissions);

            manager.setRoles(new ArrayList<Role>() {{
                add(managerRole);
            }});
            securityUserRepository.save(manager);
        }

        SecurityUser employee = securityUserRepository.getActiveUser("employee");
//        if(Objects.nonNull(employee)){
//            securityUserRepository.delete(employee);
//        }
        if(Objects.isNull(employee)) {
            employee = new SecurityUser();
            employee.setEnabled(true);
            employee.setUsername("employee");
            employee.setEmail("employee@dadehpardaz.ir");
            employee.setPassword(encoder.encode("employee"));
            employee.setFirstName("employee");
            employee.setLastName("employee");
            employee.setDeleted(false);
            Role employeeRole = new Role();
            employeeRole.setName("EMPLOYEE");
            employeeRole.setTitle("employee");
            permissions =
                    Stream.of("LEAVE_TIME_REQUEST_DELETE", "LEAVE_TIME_REQUEST_CREATE", "LEAVE_TIME_REQUEST_UPDATE", "LEAVE_TIME_REQUEST_READ",
                            "LEAVE_TIME_DELETE", "LEAVE_TIME_CREATE", "LEAVE_TIME_UPDATE", "LEAVE_TIME_READ",
                            "WORK_TIME_DELETE", "WORK_TIME_UPDATE", "WORK_TIME_CREATE", "WORK_TIME_READ"
                    ).map(str -> {
                        Permission permission = new Permission();
                        permission.setName(str);
                        permission.setTitle(str.toLowerCase());
                        return permission;

                    }).collect(Collectors.toList());
            employeeRole.setPermissions(permissions);
            employee.setRoles(new ArrayList<Role>() {{
                add(employeeRole);
            }});
            securityUserRepository.save(employee);
        }




    }

}

