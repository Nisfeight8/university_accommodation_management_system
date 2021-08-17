package university.accommodation.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.accommodation.management.system.model.Department;
import university.accommodation.management.system.repository.DepartmentRepository;
import university.accommodation.management.system.repository.UniversityRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @PreAuthorize("hasAuthority('view_departments')")
    @GetMapping
    public String Departments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "department/departments";
    }
    @PreAuthorize("hasAuthority('add_department')")
    @GetMapping("/new")
    public String showDepartmentCreateForm(Department department, Model model) {
        model.addAttribute("universities", universityRepository.findAll());
        return "department/newDepartment";
    }

    @PreAuthorize("hasAuthority('add_department')")
    @PostMapping("/new")
    public String createDepartment(Model model,RedirectAttributes redirectAttributes,@Valid Department department, BindingResult bindingResult) {
        if(departmentRepository.existsByName(department.getName())){
            bindingResult.addError(new FieldError("department", "name", department.getName(), false, null, null, "Department with this name already exists !"));
        }
        if(departmentRepository.existsByAddress(department.getAddress())){
            bindingResult.addError(new FieldError("department", "address", department.getAddress(), false, null, null, "Department with this address already exists !"));
        }
        if(departmentRepository.existsByPobox(department.getPobox())){
            bindingResult.addError(new FieldError("department", "pobox", department.getPobox(), false, null, null, "Department with this pobox already exists !"));
        }
        if(departmentRepository.existsByPhone(department.getPhone())){
            bindingResult.addError(new FieldError("department", "phone", department.getPhone(), false, null, null, "Department with this phone already exists !"));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("universities", universityRepository.findAll());
            return "department/newDepartment";
        }
        departmentRepository.save(department);
        redirectAttributes.addFlashAttribute("success","Department saved !");
        return "redirect:/departments";
    }

    @PreAuthorize("hasAuthority('view_department')")
    @GetMapping("/{id}")
    public String getDepartment(Model model, @PathVariable("id") int id) {
        model.addAttribute("department", departmentRepository.findById(id).get());
        return "department/getDepartment";
    }
    @PreAuthorize("hasAuthority('edit_department')")
    @GetMapping("/{id}/edit")
    public String showEditDepartmentForm(Model model, @PathVariable("id") int id) {
        model.addAttribute("department", departmentRepository.findById(id).get());
        model.addAttribute("universities", universityRepository.findAll());
        return "department/editDepartment";
    }
    @PreAuthorize("hasAuthority('edit_department')")
    @PostMapping("/{id}/edit")
    public String editDepartment(Model model,RedirectAttributes redirectAttributes,@PathVariable("id") int id, @Valid Department department, BindingResult bindingResult) {
        if((!departmentRepository.findById(id).get().getName().equals(department.getName()))&&(departmentRepository.existsByName(department.getName()))){
            bindingResult.addError(new FieldError("department", "name", department.getName(), false, null, null, "Department with this name already exists !"));
        }
        if((!departmentRepository.findById(id).get().getAddress().equals(department.getAddress()))&&(departmentRepository.existsByAddress(department.getAddress()))){
            bindingResult.addError(new FieldError("department", "address", department.getAddress(), false, null, null, "Department with this address already exists !"));
        }
        if((!departmentRepository.findById(id).get().getPobox().equals(department.getPobox()))&&(departmentRepository.existsByPobox(department.getPobox()))){
            bindingResult.addError(new FieldError("department", "pobox", department.getPobox(), false, null, null, "Department with this pobox already exists !"));
        }
        if((!departmentRepository.findById(id).get().getPhone().equals(department.getPhone()))&&(departmentRepository.existsByPhone(department.getPhone()))){
            bindingResult.addError(new FieldError("department", "phone", department.getPhone(), false, null, null, "Department with this phone already exists !"));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("universities", universityRepository.findAll());
            department.setId(id);
            return "department/editDepartment";
        }
        departmentRepository.save(department);
        redirectAttributes.addFlashAttribute("success","Department updated !");
        return "redirect:/departments";
    }
    @PreAuthorize("hasAuthority('delete_department')")
    @GetMapping("/{id}/delete")
    public String deleteDepartment(RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
        departmentRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success","Department deleted !");
        return "redirect:/departments";
    }
}