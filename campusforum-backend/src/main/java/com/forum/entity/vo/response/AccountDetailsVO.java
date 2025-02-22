package com.forum.entity.vo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDetailsVO {
    public int gender;
    public String phone;
    public String qq;
    public String wx;
    public String desc;
}
