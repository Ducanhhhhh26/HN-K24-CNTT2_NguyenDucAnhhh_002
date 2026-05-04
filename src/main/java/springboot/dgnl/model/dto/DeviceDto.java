package springboot.dgnl.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class DeviceDto {

    private Long id;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(min = 5, max = 150, message = "Tên thiết bị từ 5 đến 150 ký tự")
    private String deviceName;

    @NotBlank(message = "Mã model không được để trống")
    private String modelCode;

    @NotNull(message = "Giá không được để trống")
    private Double price;

    @PastOrPresent(message = "Ngày sản xuất không được trong tương lai")
    private LocalDate manufactureDate;

    private String productImage;

    private MultipartFile imageFile;

    private Boolean isAvailable;

    @NotNull(message = "Thương hiệu không được để trống")
    private Long brandId;

    // Getters and Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}

