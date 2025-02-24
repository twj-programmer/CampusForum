package com.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forum.entity.dto.Account;
import com.forum.entity.vo.request.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByEmail(String email);
    String registerEmailVerifyCode(String type, String email, String ip);
    String registerEmailAccount(EmailRegisterVO vo);
    String forgetConfirm(ForgetConfirmVO vo);
    String resetPassword(EmailResetVO vo);
    Account findAccountById(int id);
    String modifyEmail(int id, ModifyEmailVO vo);
    String changePassword(int id, ChangePasswordVO vo);
}
