package com.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forum.entity.dto.Account;
import com.forum.entity.vo.request.EmailRegisterVO;
import com.forum.entity.vo.request.EmailResetVO;
import com.forum.entity.vo.request.ForgetConfirmVO;
import com.forum.mapper.AccountMapper;
import com.forum.service.AccountService;
import com.forum.utils.Const;
import com.forum.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    FlowUtils flowUtils;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = this.findAccountByEmail(email);
        if (account == null)
            throw new UsernameNotFoundException("邮箱或密码错误");
        return User
                .withUsername(account.getEmail())
                .password(account.getPassword())
                .authorities(account.getRole())
                .build();
    }

    public Account findAccountByEmail(String email) {
        return this.query()
                .eq("email", email)
                .one();
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip))
                return "请求过于频繁";
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of(
                    "type", type,
                    "email", email,
                    "code", code
            );
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate
                    .opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.email();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);
        String username = vo.username();
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.code())) return "验证码错误";
        if (this.findAccountByEmail(email) != null) return "邮箱已注册";
        String password = passwordEncoder.encode(vo.password());
        Account account = new Account(null, username, password, email, "USER", new Date());
        if (this.save(account)) {
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email);
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    @Override
    public String forgetConfirm(ForgetConfirmVO vo) {
        String email = vo.getEmail();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码错误";
        return null;
    }

    @Override
    public String resetPassword(EmailResetVO vo) {
        String email = vo.getEmail();
        String verify = this.forgetConfirm(new ForgetConfirmVO(email, vo.getCode()));
        if (verify != null) return verify;
        String password = passwordEncoder.encode(vo.getPassword());
        boolean update = this
                .update()
                .eq("email", email)
                .set("password", password)
                .update();
        if (update) {
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email);
        }
        return null;
    }

    @Override
    public Account findAccountById(int id) {
        return this.query().eq("id", id).one();
    }

    private boolean verifyLimit(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }
}
