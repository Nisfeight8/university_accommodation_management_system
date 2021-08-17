package university.accommodation.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.accommodation.management.system.repository.*;
import university.accommodation.management.system.model.Chief;
import university.accommodation.management.system.model.Manager;
import university.accommodation.management.system.model.User;
import university.accommodation.management.system.repository.ManagerRepository;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/managers")
public class ManagerController {
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    CallRepository callRepository;
    @Autowired
    ChiefRepository chiefRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncode;

    @PreAuthorize("hasAuthority('view_managers')")
    @GetMapping
    public String managers(Model model, @AuthenticationPrincipal User user) {
        Chief chief =chiefRepository.findById(user.getId()).get();
        model.addAttribute("managers", managerRepository.findByUniversity(chief.getUniversity()));
        return "manager/managers";
    }
    @PreAuthorize("hasAuthority('add_manager')")
    @GetMapping("/new")
    public String showManagerCreateForm(Manager manager, Model model, @AuthenticationPrincipal User user) {
        Chief chief =chiefRepository.findById(user.getId()).get();
        model.addAttribute("calls", callRepository.findByDepartmentUniversity(chief.getUniversity()));
        return "manager/newManager";
    }
    @PreAuthorize("hasAuthority('add_manager')")
    @PostMapping("/new")
    public String createManager(Model model,RedirectAttributes redirectAttributes, @Valid Manager manager, BindingResult bindingResult,@AuthenticationPrincipal User user) {
        if(managerRepository.existsByPhone(manager.getPhone())){
            bindingResult.addError(new FieldError("manager", "phone", manager.getPhone(), false, null, null, "Manager with this phone already exists !"));
        }
        if (bindingResult.hasErrors()) {
            Chief chief =chiefRepository.findById(user.getId()).get();
            model.addAttribute("calls", callRepository.findByDepartmentUniversity(chief.getUniversity()));
            return "manager/newManager";
        }
        Chief chief =chiefRepository.findById(user.getId()).get();
        manager.setUniversity(chief.getUniversity());
        manager.setRoles(Arrays.asList(roleRepository.findByName("ROLE_MANAGER")));
        manager.setPassword(passwordEncode.encode(manager.getPassword()));
        managerRepository.save(manager);
        redirectAttributes.addFlashAttribute("success","Manager saved !");
        return "redirect:/managers";
    }
    @PreAuthorize("hasAuthority('view_manager')")
    @GetMapping("/{id}")
    public String getManager(Model model, @PathVariable("id") int id) {
        model.addAttribute("manager", managerRepository.findById(id).get());
        return "manager/getManager";
    }
    @PreAuthorize("hasAuthority('edit_manager')")
    @GetMapping("/{id}/edit")
    public String showEditManagerForm(Model model, @PathVariable("id") int id,@AuthenticationPrincipal User user) {
        model.addAttribute("manager", managerRepository.findById(id).get());
        Chief chief =chiefRepository.findById(user.getId()).get();
        model.addAttribute("calls", callRepository.findByDepartmentUniversity(chief.getUniversity()));
        return "manager/editManager";
    }
    @PreAuthorize("hasAuthority('edit_manager')")
    @PostMapping("/{id}/edit")
    public String editManager(RedirectAttributes redirectAttributes,Model model, @PathVariable("id") int id,@AuthenticationPrincipal User user,@Valid Manager manager, BindingResult bindingResult) {
        if((!managerRepository.findById(id).get().getPhone().equals(manager.getPhone()))&&(managerRepository.existsByPhone(manager.getPhone()))){
            bindingResult.addError(new FieldError("manager", "phone", manager.getPhone(), false, null, null, "Manager with this phone already exists !"));
        }
        if (bindingResult.hasErrors()) {
            manager.setId(id);
            Chief chief =chiefRepository.findById(user.getId()).get();
            model.addAttribute("calls", callRepository.findByDepartmentUniversity(chief.getUniversity()));
            return "manager/editManager";
        }
        managerRepository.save(manager);
        redirectAttributes.addFlashAttribute("success","Manager updated !");
        return "redirect:/managers";
    }
    @PreAuthorize("hasAuthority('delete_manager')")
    @GetMapping("/{id}/delete")
    public String deleteManager(RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
        managerRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success","Manager deleted !");
        return "redirect:/managers";
    }
}
