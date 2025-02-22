package com.forum.controller;

import com.forum.entity.RestBean;
import com.forum.entity.vo.request.EmailRegisterVO;
import com.forum.entity.vo.request.EmailResetVO;
import com.forum.entity.vo.request.ForgetConfirmVO;
import com.forum.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    AccountService service;

    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset)") String type,
                                        HttpServletRequest request) {
        return this.messageHandle(() -> service.registerEmailVerifyCode(type, email, request.getRemoteAddr()));
    }

    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return messageHandle(vo, service::registerEmailAccount);
    }

    @PostMapping("/forget-confirm")
    public RestBean<Void> forgetConfirm(@RequestBody @Valid ForgetConfirmVO vo) {
        return messageHandle(vo, service::forgetConfirm);
    }

    @PostMapping("/forget-password")
    public RestBean<Void> forgetPassword(@RequestBody @Valid EmailResetVO vo) {
        return messageHandle(vo, service::resetPassword);
    }

    private <T> RestBean<Void> messageHandle(T vo, Function<T, String> function) {
        return this.messageHandle(() -> function.apply(vo));
    }

    private RestBean<Void> messageHandle(Supplier<String> action) {
        String message = action.get();
        if (message == null)
            return RestBean.success();
        return RestBean.failure(400, message);
    }
}
