package university.accommodation.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.accommodation.management.system.model.University;
import university.accommodation.management.system.repository.UniversityRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/universities")
public class UniversityController {
    @Autowired
    UniversityRepository universityRepository;

    @PreAuthorize("hasAuthority('view_universities')")
    @GetMapping
    public String universities(Model model) {
        model.addAttribute("universities", universityRepository.findAll());
        return "university/universities";
    }
    @PreAuthorize("hasAuthority('add_university')")
    @GetMapping("/new")
    public String showUniversityCreateForm(University university, Model model) {
        return "university/newUniversity";
    }
    @PreAuthorize("hasAuthority('add_university')")
    @PostMapping("/new")
    public String createUniversity(RedirectAttributes redirectAttributes, @Valid University university, BindingResult bindingResult) {
        if(universityRepository.existsByName(university.getName())){
            bindingResult.addError(new FieldError("university", "name", university.getName(), false, null, null, "University with this name already exists !"));
        }
        if(universityRepository.existsByAddress(university.getAddress())){
            bindingResult.addError(new FieldError("university", "address", university.getAddress(), false, null, null, "University with this address already exists !"));
        }
        if(universityRepository.existsByPobox(university.getPobox())){
            bindingResult.addError(new FieldError("university", "pobox", university.getPobox(), false, null, null, "University with this pobox already exists !"));
        }
        if(universityRepository.existsByPhone(university.getPhone())){
            bindingResult.addError(new FieldError("university", "phone", university.getPhone(), false, null, null, "University with this phone already exists !"));
        }
        
        if (bindingResult.hasErrors()) {
            return "university/newUniversity";
        }
        universityRepository.save(university);
        redirectAttributes.addFlashAttribute("success","University saved !");
        return "redirect:/universities";
    }
    @PreAuthorize("hasAuthority('view_university')")
    @GetMapping("/{id}")
    public String getUniversity(Model model, @PathVariable("id") int id) {
        model.addAttribute("university", universityRepository.findById(id).get());
        return "university/getUniversity";
    }
    @PreAuthorize("hasAuthority('edit_university')")
    @GetMapping("/{id}/edit")
    public String showEditUniversityForm(Model model, @PathVariable("id") int id) {
        model.addAttribute("university", universityRepository.findById(id).get());
        return "university/editUniversity";
    }
    @PreAuthorize("hasAuthority('edit_university')")
    @PostMapping("/{id}/edit")
    public String EditUniversity(RedirectAttributes redirectAttributes, @PathVariable("id") int id,@Valid University university, BindingResult bindingResult) {
        if((!universityRepository.findById(id).get().getName().equals(university.getName()))&&(universityRepository.existsByName(university.getName()))){
            bindingResult.addError(new FieldError("university", "name", university.getName(), false, null, null, "University with this name already exists !"));
        }
        if((!universityRepository.findById(id).get().getAddress().equals(university.getAddress()))&&(universityRepository.existsByAddress(university.getAddress()))){
            bindingResult.addError(new FieldError("university", "address", university.getAddress(), false, null, null, "University with this address already exists !"));
        }
        if((!universityRepository.findById(id).get().getPobox().equals(university.getPobox()))&&(universityRepository.existsByPobox(university.getPobox()))){
            bindingResult.addError(new FieldError("university", "pobox", university.getPobox(), false, null, null, "University with this pobox already exists !"));
        }
        if((!universityRepository.findById(id).get().getPhone().equals(university.getName()))&&(universityRepository.existsByPhone(university.getPhone()))){
            bindingResult.addError(new FieldError("university", "phone", university.getPhone(), false, null, null, "University with this phone already exists !"));
        }
        if (bindingResult.hasErrors()) {
            university.setId(id);
            return "university/editUniversity";
        }
        redirectAttributes.addFlashAttribute("success","University updated !");
        universityRepository.save(university);
        return "redirect:/universities";
    }
    @PreAuthorize("hasAuthority('delete_university')")
    @GetMapping("/{id}/delete")
    public String DeleteUniversity(RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
        universityRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success","University deleted !");
        return "redirect:/universities";
    }
}
