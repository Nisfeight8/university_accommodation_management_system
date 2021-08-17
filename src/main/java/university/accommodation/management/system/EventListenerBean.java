package university.accommodation.management.system;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import university.accommodation.management.system.model.Admin;
import university.accommodation.management.system.repository.*;
import university.accommodation.management.system.model.Applicant;
import university.accommodation.management.system.model.auth.Privilege;
import university.accommodation.management.system.model.auth.Role;
import university.accommodation.management.system.model.enums.Gender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class EventListenerBean  {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    PasswordEncoder passwordEncode;

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(userRepository.findAll().isEmpty()){
            List<Privilege> privileges = new ArrayList<Privilege>();
            privileges.add(new Privilege("add_role"));
            privileges.add(new Privilege("edit_role"));
            privileges.add(new Privilege("delete_role"));
            privileges.add(new Privilege("view_role"));
            privileges.add(new Privilege("view_roles"));
            privileges.add(new Privilege("add_department"));
            privileges.add(new Privilege("edit_department"));
            privileges.add(new Privilege("delete_department"));
            privileges.add(new Privilege("view_department"));
            privileges.add(new Privilege("view_departments"));
            privileges.add(new Privilege("add_university"));
            privileges.add(new Privilege("edit_university"));
            privileges.add(new Privilege("delete_university"));
            privileges.add(new Privilege("view_university"));
            privileges.add(new Privilege("view_universities"));
            privileges.add(new Privilege("add_chief"));
            privileges.add(new Privilege("edit_chief"));
            privileges.add(new Privilege("delete_chief"));
            privileges.add(new Privilege("view_chief"));
            privileges.add(new Privilege("view_chiefs"));
            privileges=privilegeRepository.saveAll(privileges);
            Role admin_role = new Role();
            admin_role.setName("ROLE_ADMIN");
            admin_role.setPrivileges(privileges);
            roleRepository.save(admin_role);
            Admin admin = new Admin();
            admin.setRoles(Arrays.asList(roleRepository.findByName("ROLE_ADMIN")));
            admin.setPassword(passwordEncode.encode("admin"));
            admin.setUsername("admin");
            adminRepository.save(admin);

            List<Privilege> chief_privileges = new ArrayList<Privilege>();
            chief_privileges.add(new Privilege("add_call"));
            chief_privileges.add(new Privilege("edit_call"));
            chief_privileges.add(new Privilege("delete_call"));
            chief_privileges.add(new Privilege("view_call"));
            chief_privileges.add(new Privilege("view_calls"));
            chief_privileges.add(new Privilege("add_manager"));
            chief_privileges.add(new Privilege("edit_manager"));
            chief_privileges.add(new Privilege("delete_manager"));
            chief_privileges.add(new Privilege("view_manager"));
            chief_privileges.add(new Privilege("view_managers"));
            chief_privileges.add(new Privilege("view_department"));
            chief_privileges.add(new Privilege("view_university"));
            chief_privileges=privilegeRepository.saveAll(chief_privileges);
            Role chief_role = new Role();
            chief_role.setName("ROLE_CHIEF");
            chief_role.setPrivileges(chief_privileges);
            roleRepository.save(chief_role);

            List<Privilege> manager_privileges = new ArrayList<Privilege>();
            manager_privileges.add(new Privilege("view_call"));
            manager_privileges.add(new Privilege("view_calls"));
            manager_privileges.add(new Privilege("view_application"));
            manager_privileges.add(new Privilege("admit_application"));
            manager_privileges.add(new Privilege("view_applications"));
            manager_privileges.add(new Privilege("view_applicant"));
            manager_privileges.add(new Privilege("view_department"));
            manager_privileges.add(new Privilege("view_university"));
            manager_privileges=privilegeRepository.saveAll(manager_privileges);
            Role manager_role = new Role();
            manager_role.setName("ROLE_MANAGER");
            manager_role.setPrivileges(manager_privileges);
            roleRepository.save(manager_role);

            List<Privilege> applicant_privileges = new ArrayList<Privilege>();
            applicant_privileges.add(new Privilege("view_call"));
            applicant_privileges.add(new Privilege("view_calls"));
            applicant_privileges.add(new Privilege("view_application"));
            applicant_privileges.add(new Privilege("add_application"));
            applicant_privileges.add(new Privilege("edit_application"));
            applicant_privileges.add(new Privilege("delete_application"));
            applicant_privileges.add(new Privilege("view_applications"));
            applicant_privileges.add(new Privilege("view_department"));
            applicant_privileges.add(new Privilege("view_university"));
            applicant_privileges=privilegeRepository.saveAll(applicant_privileges);
            Role applicant_role = new Role();
            applicant_role.setName("ROLE_APPLICANT");
            applicant_role.setPrivileges(applicant_privileges);
            roleRepository.save(applicant_role);
            Applicant applicant = new Applicant();
            applicant.setUsername("applicant");
            applicant.setPassword(passwordEncode.encode("applicant"));
            applicant.setBirth_date(new Date());
            applicant.setFirst_name("Fanis");
            applicant.setLast_name("Papazoglou");
            applicant.setPhone("6976980753");
            applicant.setGender(Gender.MAN);
            applicant.setCountry("Greece");
            applicant.setCity("Mytilene");
            applicant.setAddress("Lafiona");
            applicant.setRoles(Arrays.asList(roleRepository.findByName("ROLE_APPLICANT")));
            applicantRepository.save(applicant);
        }
    }
}