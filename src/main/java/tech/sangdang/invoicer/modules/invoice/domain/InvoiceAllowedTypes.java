package tech.sangdang.invoicer.modules.invoice.domain;

import lombok.Getter;

@Getter
public enum InvoiceAllowedTypes {
    APPLICATION_PDF,
    APPLICATION_MSWORD,
    IMAGE_JPEG,
    IMAGE_PNG,
    IMAGE_BMP,
    IMAGE_GIF,
    TEXT_PLAIN;

    final String value;

    InvoiceAllowedTypes() {
        this.value = this.name().toLowerCase().replace('_', '/');
    }

    public static InvoiceAllowedTypes fromString(String str) {
        for (InvoiceAllowedTypes type : InvoiceAllowedTypes.values()) {
            if (type.name().equalsIgnoreCase(str) || type.getValue().equalsIgnoreCase(str)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + str);
    }
}
