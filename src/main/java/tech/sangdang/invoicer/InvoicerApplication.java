package tech.sangdang.invoicer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import tech.sangdang.invoicer.modules.invoice.infra.s3.S3Config;
import tech.sangdang.invoicer.modules.system.SystemConfig;

@ConfigurationPropertiesScan(basePackageClasses = {S3Config.class, SystemConfig.class})
@SpringBootApplication
public class InvoicerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoicerApplication.class, args);
    }

}
