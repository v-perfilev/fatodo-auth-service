package com.persoff68.fatodo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "ftd_activation")
@Data
@EqualsAndHashCode(callSuper = false)
public class Activation extends AbstractAuditingModel {

    @NotNull
    private String userId;

    @NotNull
    @Indexed(unique = true)
    private String code;

    private boolean completed = false;

}
