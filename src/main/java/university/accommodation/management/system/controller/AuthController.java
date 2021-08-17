package university.accommodation.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import university.accommodation.management.system.config.CustomUserDetailsService;
import university.accommodation.management.system.repository.ApplicantRepository;
import university.accommodation.management.system.model.Applicant;
import university.accommodation.management.system.model.User;
import university.accommodation.management.system.repository.RoleRepository;
import university.accommodation.management.system.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    ApplicantRepository applicantRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping({"login",""})
    public String login() { return "login"; }

    @GetMapping("home")
    public String home() {
        return "home";
    }

    @GetMapping("logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }
    @GetMapping("register")
    public String showRegisterForm(Applicant applicant) {
        return "signup";
    }

    @PostMapping("register")
    public String register(@Valid Applicant applicant ,HttpServletRequest request, BindingResult bindingResult,Model model) {
        if(!applicant.getPassword().equals(applicant.getMatchingPassword())) {
            bindingResult.addError(new FieldError("applicant", "matchingPassword", applicant.getPassword(), false, null, null, "Passwords doesn't match"));
        }
        if(userRepository.existsByUsername(applicant.getUsername())){
            bindingResult.addError(new FieldError("applicant", "username", applicant.getUsername(), false, null, null, "User with this username already exists !"));
        }
        if(applicantRepository.existsByAddress(applicant.getAddress())){
            bindingResult.addError(new FieldError("applicant", "address", applicant.getAddress(), false, null, null, "User with this address already exists !"));
        }
        if(applicantRepository.existsByPhone(applicant.getPhone())){
            bindingResult.addError(new FieldError("applicant", "phone", applicant.getPhone(), false, null, null, "User with this phone already exists !"));
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        User registered = userDetailsService.registerNewUserAccount(applicant);
        try {
            userDetailsService.autoLogin(request,registered.getUsername(), registered.getPassword());
        } catch (ServletException e) {
            e.printStackTrace();
            return "signup";
        }
        return "redirect:/home";
    }

}
