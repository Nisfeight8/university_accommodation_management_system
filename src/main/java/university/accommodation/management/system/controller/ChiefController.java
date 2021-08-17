package university.accommodation.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import university.accommodation.management.system.model.Chief;
import university.accommodation.management.system.repository.RoleRepository;
import university.accommodation.management.system.repository.ChiefRepository;
import university.accommodation.management.system.repository.UniversityRepository;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/chiefs")
public class ChiefController {
    @Autowired
    ChiefRepository chiefRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    PasswordEncoder passwordEncode;
    @PreAuthorize("hasAuthority('view_chiefs')")
    @GetMapping
    public String chiefs(Model model) {
        model.addAttribute("chiefs", chiefRepository.findAll());
        return "chief/chiefs";
    }
    @PreAuthorize("hasAuthority('add_chief')")
    @GetMapping("/new")
    public String showChiefCreateForm(Chief chief, Model model) {
        model.addAttribute("universities", universityRepository.findByChiefIsNull());
        return "chief/newChief";
    }
    @PreAuthorize("hasAuthority('add_chief')")
    @PostMapping("/new")
    public String createChief(Model model,@Valid Chief chief, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(chiefRepository.existsByPhone(chief.getPhone())){
            bindingResult.addError(new FieldError("chief", "phone", chief.getPhone(), false, null, null, "Chief with this phone already exists !"));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("universities", universityRepository.findByChiefIsNull());
            return "chief/newChief";
        }
        chief.setRoles(Arrays.asList(roleRepository.findByName("ROLE_CHIEF")));
        chief.setPassword(passwordEncode.encode(chief.getPassword()));
        chiefRepository.save(chief);
        redirectAttributes.addFlashAttribute("success","Chief saved !");
        return "redirect:/chiefs";
    }
    @PreAuthorize("hasAuthority('view_chief')")
    @GetMapping("/{id}")
    public String getChief(Model model, @PathVariable("id") int id) {
        model.addAttribute("chief", chiefRepository.findById(id).get());
        return "chief/getChief";
    }
    @PreAuthorize("hasAuthority('edit_chief')")
    @GetMapping("/{id}/edit")
    public String showEditChiefForm(Model model, @PathVariable("id") int id) {
        model.addAttribute("chief", chiefRepository.findById(id).get());
        model.addAttribute("universities", universityRepository.findByChiefIsNull());
        return "chief/editChief";
    }
    @PreAuthorize("hasAuthority('edit_chief')")
    @PostMapping("/{id}/edit")
    public String editChief(Model model, @PathVariable("id") int id,@Valid Chief chief, BindingResult bindingResult,RedirectAttributes redirectAttributes) {
        if((!chiefRepository.findById(id).get().getPhone().equals(chief.getPhone()))&&(chiefRepository.existsByPhone(chief.getPhone()))){
            bindingResult.addError(new FieldError("chief", "phone", chief.getPhone(), false, null, null, "Chief with this phone already exists !"));
        }
        if (bindingResult.hasErrors()) {
            chief.setId(id);
            model.addAttribute("universities", universityRepository.findByChiefIsNull());
            return "chief/editChief";
        }
        chiefRepository.save(chief);
        redirectAttributes.addFlashAttribute("success","Chief updated !");

        return "redirect:/chiefs";
    }
    @PreAuthorize("hasAuthority('delete_chief')")
    @GetMapping("/{id}/delete")
    public String deleteChief(RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
        chiefRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success","Chief deleted !");
        return "redirect:/chiefs";
    }
}
