package tech.sangdang.invoicer.modules.invoice.infra.s3;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;
import tech.sangdang.invoicer.modules.invoice.domain.ports.FileUploadPort;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUploadPortImpl implements FileUploadPort {
    private final S3Config s3Config;
    private final S3Template s3Template;

    @Override
    public String uploadFile(Invoice invoice) {
        return s3Template.createSignedPutURL(
                s3Config.getBucketName(), 
                s3Config.getKey(invoice.getInvoiceId()), 
                Duration.ofMinutes(s3Config.getDefaultPresignedUploadTtlMins())
        ).toExternalForm();
    }

    @Override
    public String getDownloadUrl(Invoice invoice) {
        return s3Template.createSignedGetURL(
                s3Config.getPermastoreBucketName(),
                invoice.getInvoiceId(),
                Duration.ofMinutes(s3Config.getDefaultPresignedDownloadTtlMins())
        ).toExternalForm();
    }
}
