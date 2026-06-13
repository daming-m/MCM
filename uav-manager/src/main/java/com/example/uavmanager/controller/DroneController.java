package com.example.uavmanager.controller;

import com.example.uavmanager.domain.Drone;
import com.example.uavmanager.domain.DroneForm;
import com.example.uavmanager.exception.DuplicateSerialNoException;
import com.example.uavmanager.exception.NotFoundException;
import com.example.uavmanager.service.DroneService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        List<Drone> drones = droneService.list(q);
        model.addAttribute("q", q);
        model.addAttribute("drones", drones);
        return "drones/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new DroneForm());
        }
        model.addAttribute("mode", "create");
        return "drones/form";
    }

    @PostMapping
    public String create(@Valid DroneForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "drones/form";
        }
        try {
            long id = droneService.create(form);
            redirectAttributes.addFlashAttribute("success", "创建成功，ID=" + id);
            return "redirect:/drones";
        } catch (DuplicateSerialNoException ex) {
            bindingResult.rejectValue("serialNo", "duplicate", "序列号已存在");
            model.addAttribute("mode", "create");
            return "drones/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        return droneService.getById(id).map(drone -> {
            DroneForm form = new DroneForm();
            form.setName(drone.getName());
            form.setModel(drone.getModel());
            form.setSerialNo(drone.getSerialNo());
            model.addAttribute("form", form);
            model.addAttribute("drone", drone);
            model.addAttribute("mode", "edit");
            return "drones/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "无人机不存在");
            return "redirect:/drones";
        });
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") long id,
                         @Valid DroneForm form,
                         BindingResult bindingResult,
                         @RequestParam(value = "regenAi", required = false, defaultValue = "false") boolean regenAi,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("droneId", id);
            return "drones/form";
        }
        try {
            droneService.update(id, form, regenAi);
            redirectAttributes.addFlashAttribute("success", "更新成功");
            return "redirect:/drones";
        } catch (DuplicateSerialNoException ex) {
            bindingResult.rejectValue("serialNo", "duplicate", "序列号已存在");
            model.addAttribute("mode", "edit");
            model.addAttribute("droneId", id);
            return "drones/form";
        } catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", "无人机不存在");
            return "redirect:/drones";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            droneService.delete(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", "无人机不存在");
        }
        return "redirect:/drones";
    }
}

