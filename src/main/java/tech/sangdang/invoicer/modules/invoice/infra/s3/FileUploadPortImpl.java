package tech.sangdang.invoicer.modules.invoice.infra.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;
import tech.sangdang.invoicer.modules.invoice.domain.ports.FileUploadPort;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUploadPortImpl implements FileUploadPort {
    private final S3Config s3Config;

    @Override
    public String uploadFile(Invoice invoice) {
        try (S3Presigner presigner = S3Presigner.create()) {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(invoice.getInvoiceId())
                    .metadata(new HashMap<>())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.of(
                            s3Config.getDefaultPresignedUploadTtlMins(),
                            ChronoUnit.MINUTES
                    ))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toExternalForm();
        }
    }
}
