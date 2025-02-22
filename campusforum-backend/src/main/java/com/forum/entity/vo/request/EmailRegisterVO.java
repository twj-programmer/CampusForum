package com.forum.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record EmailRegisterVO(
        @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
        String username,
        @Email
        @NotEmpty
        String email,
        @Length(min = 6, max = 20)
        String password,
        @Length(min = 6, max = 6)
        String code) {
}
