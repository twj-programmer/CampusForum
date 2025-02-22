package com.forum.entity.vo.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DetailsSaveVO {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    String username;
    @Min(0) @Max(1)
    int gender;
    @Length(max=11)
    String phone;
    String qq;
    String wx;
    String desc;
}
