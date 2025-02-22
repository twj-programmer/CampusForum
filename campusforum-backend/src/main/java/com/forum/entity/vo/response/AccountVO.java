package com.forum.entity.vo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AccountVO {
    public String username;
    public String email;
    public String role;
    public Date registerTime;
}
