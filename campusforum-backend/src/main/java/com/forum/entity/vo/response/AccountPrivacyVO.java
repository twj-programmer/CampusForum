package com.forum.entity.vo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountPrivacyVO {
    boolean gender;
    boolean phone;
    boolean email;
    boolean qq;
    boolean wx;
}
