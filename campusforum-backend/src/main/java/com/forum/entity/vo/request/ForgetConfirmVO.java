package com.forum.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Data
public class ForgetConfirmVO {
    @Email
    String email;
    @Length(min = 6, max = 6)
    String code;
}
