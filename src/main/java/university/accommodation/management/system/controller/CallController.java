package university.accommodation.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.accommodation.management.system.model.*;
import university.accommodation.management.system.repository.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/calls")
public class CallController {

    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    CallRepository callRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    ChiefRepository chiefRepository;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    ApplicantRepository applicantRepository;

    @PreAuthorize("hasAuthority('view_calls')")
    @GetMapping
    public String Calls(Model model, @AuthenticationPrincipal User user) {
        if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CHIEF"))){
            Chief chief = chiefRepository.findById(user.getId()).get();
            model.addAttribute("calls", callRepository.findByDepartmentUniversity(chief.getUniversity()));
        }
        if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))){
            Manager manager = managerRepository.findById(user.getId()).get();
            model.addAttribute("calls", callRepository.findById(manager.getCall().getId()).get());
        }
        if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_APPLICANT"))){
            model.addAttribute("calls", callRepository.findAllBetweenDates());
        }
        return "call/calls";
    }
    @PreAuthorize("hasAuthority('add_call')")
    @GetMapping("/new")
    public String showCallCreateForm(Call call, Model model, @AuthenticationPrincipal User user) {
        Chief chief =chiefRepository.findById(user.getId()).get();
        model.addAttribute("departments", departmentRepository.findByUniversity(chief.getUniversity()));
        return "call/newCall";
    }
    @PreAuthorize("hasAuthority('add_call')")
    @PostMapping("/new")
    public String createCall(Model model,@Valid Call call,@AuthenticationPrincipal User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(callRepository.existsByTitle(call.getTitle())){
            bindingResult.addError(new FieldError("call", "title", call.getTitle(), false, null, null, "Call with this title already exists !"));

        }
        if (bindingResult.hasErrors()) {
            Chief chief =chiefRepository.findById(user.getId()).get();
            model.addAttribute("departments", departmentRepository.findByUniversity(chief.getUniversity()));
            return "call/newCall";
        }

        callRepository.save(call);
        redirectAttributes.addFlashAttribute("success","Call saved !");
        return "redirect:/calls";
    }
    @PreAuthorize("hasAuthority('view_call')")
    @GetMapping("/{id}")
    public String getCall(Model model, @PathVariable("id") int id) {
        model.addAttribute("call", callRepository.findById(id).get());
        return "call/getCall";
    }
    @PreAuthorize("hasAuthority('add_application')")
    @GetMapping("/{id}/applications/new")
    public String showCreateApplicationForm(Model model, Application application, @PathVariable("id") int id) {
        model.addAttribute("call", callRepository.findById(id).get());
        return "application/newApplication";
    }
    @PreAuthorize("hasAuthority('add_application')")
    @PostMapping("/{id}/applications/new")
    public String newApplication(RedirectAttributes redirectAttributes, @PathVariable("id") int id ,@AuthenticationPrincipal User user,@Valid Application application, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "application/newApplication";
        }
        application.setCall(callRepository.findById(id).get());
        application.setApplicant(applicantRepository.findById(user.getId()).get());
        applicationRepository.save(application);
        redirectAttributes.addFlashAttribute("success","Application saved !");
        return "redirect:/applications";
    }
    @PreAuthorize("hasAuthority('edit_call')")
    @GetMapping("/{id}/edit")
    public String showEditCallForm(Model model, @PathVariable("id") int id,@AuthenticationPrincipal User user) {
        model.addAttribute("call", callRepository.findById(id).get());
        Chief chief =chiefRepository.findById(user.getId()).get();
        model.addAttribute("departments", departmentRepository.findByUniversity(chief.getUniversity()));
        return "call/editCall";
    }
    @PreAuthorize("hasAuthority('edit_call')")
    @PostMapping("/{id}/edit")
    public String editCall(Model model,@AuthenticationPrincipal User user,RedirectAttributes redirectAttributes,@PathVariable("id") int id, @Valid Call call, BindingResult bindingResult) {
        if((!callRepository.findById(call.getId()).get().getTitle().equals(call.getTitle()))&&(callRepository.existsByTitle(call.getTitle()))){
            bindingResult.addError(new FieldError("call", "title", call.getTitle(), false, null, null, "Call with this title already exists !"));

        }
        if (bindingResult.hasErrors()) {
            call.setId(id);
            Chief chief =chiefRepository.findById(user.getId()).get();
            model.addAttribute("departments", departmentRepository.findByUniversity(chief.getUniversity()));
            return "call/editCall";
        }
        callRepository.save(call);
        redirectAttributes.addFlashAttribute("success","Call updated !");

        return "redirect:/calls";
    }
    @PreAuthorize("hasAuthority('delete_call')")
    @GetMapping("/{id}/delete")
    public String deleteCall( RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
        callRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success","Call deleted !");
        return "redirect:/calls";
    }
}
