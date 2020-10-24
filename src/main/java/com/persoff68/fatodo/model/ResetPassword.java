package com.persoff68.fatodo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Document(collection = "ftd_reset_password")
@Data
@EqualsAndHashCode(callSuper = true)
public class ResetPassword extends AbstractAuditingModel {

    @NotNull
    private UUID userId;

    @NotNull
    @Indexed(unique = true)
    private String code;

    private boolean completed = false;

}
