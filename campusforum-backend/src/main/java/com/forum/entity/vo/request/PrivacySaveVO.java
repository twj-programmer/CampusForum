package com.forum.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PrivacySaveVO {
    @Pattern(regexp = "(gender|phone|email|qq|wx)")
    String type;
    boolean status;
}
