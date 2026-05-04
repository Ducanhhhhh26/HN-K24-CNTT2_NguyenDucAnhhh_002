package springboot.dgnl.config;

import springboot.dgnl.model.entity.Brand;
import springboot.dgnl.model.entity.Device;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import springboot.dgnl.repository.BrandRepository;
import springboot.dgnl.repository.DeviceRepository;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final DeviceRepository deviceRepository;

    public DataSeeder(BrandRepository brandRepository, DeviceRepository deviceRepository) {
        this.brandRepository = brandRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (brandRepository.count() == 0 && deviceRepository.count() == 0) {
            String[] brandNames = {"Apple", "Samsung", "Xiaomi"};

            for (int i = 0; i < brandNames.length; i++) {
                Brand brand = new Brand(null, brandNames[i], "Description for " + brandNames[i], new ArrayList<>());
                brandRepository.save(brand);

                for (int j = 1; j <= 4; j++) {
                    Device device = new Device(
                        null,
                        brandNames[i] + " Device " + j,
                        brandNames[i].substring(0, 2).toUpperCase() + "-" + j + "00",
                        500.0 + (j * 100),
                        LocalDate.now().minusDays(j * 10),
                        "image" + j + ".jpg",
                        true,
                        brand
                    );
                    deviceRepository.save(device);
                }
            }
        }
    }
}
