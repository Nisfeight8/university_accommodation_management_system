package university.accommodation.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.accommodation.management.system.model.Applicant;
import university.accommodation.management.system.model.Application;
import university.accommodation.management.system.model.Manager;
import university.accommodation.management.system.model.User;
import university.accommodation.management.system.model.enums.Status;
import university.accommodation.management.system.repository.ApplicantRepository;
import university.accommodation.management.system.repository.ApplicationRepository;
import university.accommodation.management.system.repository.ManagerRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    ApplicantRepository applicantRepository;

    @PreAuthorize("hasAuthority('view_applications')")
    @GetMapping
    public String applications(Model model, @AuthenticationPrincipal User user) {
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            Manager manager = managerRepository.findById(user.getId()).get();
            model.addAttribute("applications", applicationRepository.findByCall(manager.getCall()));
        }
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_APPLICANT"))) {
            Applicant applicant = applicantRepository.findById(user.getId()).get();
            model.addAttribute("applications", applicationRepository.findByApplicant(applicant));
        }
        return "application/applications";
    }
    @PreAuthorize("hasAuthority('view_application')")
    @GetMapping("/{id}")
    public String getApplication(Model model, @PathVariable("id") int id) {
        model.addAttribute("app", applicationRepository.findById(id).get());
        return "application/getApplication";
    }

    @PreAuthorize("hasAuthority('edit_application')")
    @GetMapping("/{id}/edit")
    public String showEditApplicationForm(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
        Application app = applicationRepository.findById(id).get();
        if( ! app.getStatus().equals(Status.Pending)){
            redirectAttributes.addFlashAttribute("warning","Application has already reviewed by manager !");
            return "redirect:/applications/"+id;

        }
        model.addAttribute("application",app );
        return "application/editApplication";
    }
    @PreAuthorize("hasAuthority('edit_application')")
    @PostMapping("/{id}/edit")
    public String EditApplication(RedirectAttributes redirectAttributes,@PathVariable("id") int id, @Valid Application application, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            application.setId(id);
            return "application/editApplication";
        }
        applicationRepository.save(application);
        redirectAttributes.addFlashAttribute("success","Application updated !");
        return "redirect:/applications";
    }
    @PreAuthorize("hasAuthority('delete_application')")
    @GetMapping("/{id}/delete")
    public String DeleteApplication(RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
        Application app = applicationRepository.findById(id).get();
        if( ! app.getStatus().equals(Status.Pending)){
            redirectAttributes.addFlashAttribute("warning","Application has already reviewed by manager !");
            return "redirect:/applications/"+id;

        }
        redirectAttributes.addFlashAttribute("success","Application deleted !");
        applicationRepository.delete(app);
        return "redirect:/applications";
    }
    @PreAuthorize("hasAuthority('admit_application')")
    @PostMapping("/{id}/accept")
    public String AcceptApplication(RedirectAttributes redirectAttributes,@PathVariable("id") int id) {
        Application application=applicationRepository.findById(id).get();
        application.setStatus(Status.Accepted);
        applicationRepository.save(application);
        redirectAttributes.addFlashAttribute("success","Application accepted !");
        return "redirect:/applications";
    }
    @PreAuthorize("hasAuthority('admit_application')")
    @PostMapping("/{id}/refuse")
    public String RefuseApplication(RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
        Application application=applicationRepository.findById(id).get();
        application.setStatus(Status.Refused);
        applicationRepository.save(application);
        redirectAttributes.addFlashAttribute("success","Application refused !");
        return "redirect:/applications";
    }
}
