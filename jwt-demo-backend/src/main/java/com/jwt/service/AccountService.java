package com.jwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jwt.entity.dto.Account;
import com.jwt.entity.vo.request.EmailRegisterVO;
import com.jwt.entity.vo.request.EmailResetVO;
import com.jwt.entity.vo.request.ForgetConfirmVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByEmail(String email);
    String registerEmailVerifyCode(String type, String email, String ip);
    String registerEmailAccount(EmailRegisterVO vo);
    String forgetConfirm(ForgetConfirmVO vo);
    String resetPassword(EmailResetVO vo);
}
