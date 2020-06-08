package com.persoff68.fatodo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "ftd_reset_password")
@Data
@EqualsAndHashCode(callSuper = false)
public class ResetPassword extends AbstractAuditingModel {

    @NotNull
    private String userId;

    @NotNull
    @Indexed(unique = true)
    private String code;

    private boolean completed = false;

}
