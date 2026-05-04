package springboot.dgnl.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springboot.dgnl.model.dto.DeviceDto;
import springboot.dgnl.model.entity.Brand;
import springboot.dgnl.model.entity.Device;
import springboot.dgnl.repository.BrandRepository;
import springboot.dgnl.repository.DeviceRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final BrandRepository brandRepository;

    // Configure path
    private final String UPLOAD_DIR = "uploads/";

    public DeviceController(DeviceRepository deviceRepository, BrandRepository brandRepository) {
        this.deviceRepository = deviceRepository;
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Long brandId) {

        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

        Page<Device> devicePage;
        if (searchKeyword == null && brandId == null) {
            devicePage = deviceRepository.findAll(PageRequest.of(page, 5));
        } else {
            devicePage = deviceRepository.searchAndFilter(searchKeyword, brandId, PageRequest.of(page, 5));
        }

        model.addAttribute("devicePage", devicePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("brandId", brandId);
        model.addAttribute("brands", brandRepository.findAll());
        return "devices/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("deviceDto", new DeviceDto());
        model.addAttribute("brands", brandRepository.findAll());
        return "devices/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("deviceDto") DeviceDto deviceDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("brands", brandRepository.findAll());
            return "devices/form";
        }

        Device device = new Device();
        mapDtoToEntity(deviceDto, device);

        deviceRepository.save(device);
        return "redirect:/devices";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid device Id:" + id));
        DeviceDto dto = new DeviceDto();
        dto.setId(device.getId());
        dto.setDeviceName(device.getDeviceName());
        dto.setModelCode(device.getModelCode());
        dto.setPrice(device.getPrice());
        dto.setManufactureDate(device.getManufactureDate());
        dto.setProductImage(device.getProductImage());
        dto.setIsAvailable(device.getIsAvailable());
        if(device.getBrand() != null) dto.setBrandId(device.getBrand().getId());

        model.addAttribute("deviceDto", dto);
        model.addAttribute("brands", brandRepository.findAll());
        return "devices/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @Valid @ModelAttribute("deviceDto") DeviceDto deviceDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("brands", brandRepository.findAll());
            return "devices/form";
        }

        Device device = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid device Id:" + id));
        mapDtoToEntity(deviceDto, device);

        deviceRepository.save(device);
        return "redirect:/devices";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid device Id:" + id));
        deviceRepository.delete(device);
        return "redirect:/devices";
    }

    private void mapDtoToEntity(DeviceDto dto, Device entity) {
        entity.setDeviceName(dto.getDeviceName());
        entity.setModelCode(dto.getModelCode());
        entity.setPrice(dto.getPrice());
        entity.setManufactureDate(dto.getManufactureDate());
        entity.setIsAvailable(dto.getIsAvailable());
        if(dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId()).orElse(null);
            entity.setBrand(brand);
        }

        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdir();

                String fileName = UUID.randomUUID().toString() + "_" + dto.getImageFile().getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                Files.write(filePath, dto.getImageFile().getBytes());
                entity.setProductImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (entity.getId() == null) {
            entity.setProductImage(null); // Just handle the case
        }
    }
}

