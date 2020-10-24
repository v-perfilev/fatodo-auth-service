package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import lombok.Builder;

public class TestCaptchaResponseDTO extends CaptchaResponseDTO {

    @Builder
    TestCaptchaResponseDTO(boolean success) {
        super(success);
    }

    public static TestCaptchaResponseDTOBuilder defaultBuilder() {
        return TestCaptchaResponseDTO.builder()
                .success(true);
    }


}
