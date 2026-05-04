package springboot.dgnl.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springboot.dgnl.model.entity.Brand;
import springboot.dgnl.repository.BrandRepository;
import springboot.dgnl.repository.DeviceRepository;

@Controller
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepository brandRepository;
    private final DeviceRepository deviceRepository;

    public BrandController(BrandRepository brandRepository, DeviceRepository deviceRepository) {
        this.brandRepository = brandRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("brands", brandRepository.findAll());
        return "brands/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "brands/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("brand") Brand brand, BindingResult result) {
        if (result.hasErrors()) {
            return "brands/form";
        }
        brandRepository.save(brand);
        return "redirect:/brands";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid brand Id:" + id));
        model.addAttribute("brand", brand);
        return "brands/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @Valid @ModelAttribute("brand") Brand brand, BindingResult result) {
        if (result.hasErrors()) {
            return "brands/form";
        }
        brand.setId(id);
        brandRepository.save(brand);
        return "redirect:/brands";
    }

    @PostMapping("/delete/{id}")
    @Transactional
    public String delete(@PathVariable("id") Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid brand Id:" + id));
        // Remove cascading effects by clearing current linked devices in context
        brand.getDevices().forEach(device -> device.setBrand(null));
        brand.getDevices().clear();
        
        // Execute explicit query to force database update immediately
        deviceRepository.unlinkDevicesFromBrand(id);
        
        brandRepository.delete(brand);
        return "redirect:/brands";
    }
}
