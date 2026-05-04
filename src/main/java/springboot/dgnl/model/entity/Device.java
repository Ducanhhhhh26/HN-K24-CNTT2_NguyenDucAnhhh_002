package springboot.dgnl.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(min = 5, max = 150, message = "Tên thiết bị từ 5 đến 150 ký tự")
    private String deviceName;

    @NotBlank(message = "Mã model không được để trống")
    private String modelCode;

    private Double price;

    @PastOrPresent(message = "Ngày sản xuất không được trong tương lai")
    private LocalDate manufactureDate;
    private String productImage;
    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public Device() {
    }

    public Device(Long id, String deviceName, String modelCode, Double price, LocalDate manufactureDate, String productImage, Boolean isAvailable, Brand brand) {
        this.id = id;
        this.deviceName = deviceName;
        this.modelCode = modelCode;
        this.price = price;
        this.manufactureDate = manufactureDate;
        this.productImage = productImage;
        this.isAvailable = isAvailable;
        this.brand = brand;
    }

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

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}
