package com.forum.controller;

import com.forum.entity.RestBean;
import com.forum.entity.dto.Account;
import com.forum.entity.dto.AccountDetails;
import com.forum.entity.vo.request.ChangePasswordVO;
import com.forum.entity.vo.request.DetailsSaveVO;
import com.forum.entity.vo.request.ModifyEmailVO;
import com.forum.entity.vo.response.AccountDetailsVO;
import com.forum.entity.vo.response.AccountVO;
import com.forum.service.AccountDetailsService;
import com.forum.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Resource
    AccountService accountService;

    @Resource
    AccountDetailsService accountDetailsService;

    @GetMapping("/info")
    public RestBean<AccountVO> info(@RequestAttribute("id") int id) {
        Account account = accountService.findAccountById(id);
        return RestBean.success(account.asViewObject(AccountVO.class));
    }

    @GetMapping("/details")
    public RestBean<AccountDetailsVO> details(@RequestAttribute("id") int id) {
        AccountDetails details = Optional
                .ofNullable(accountDetailsService.findAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBean.success(details.asViewObject(AccountDetailsVO.class));
    }

    @PostMapping("/save-details")
    public RestBean<Void> saveDetails(@RequestAttribute("id") int id,
                                      @RequestBody @Valid DetailsSaveVO vo) {
        boolean success = accountDetailsService.saveAccountDetails(id, vo);
        return success ? RestBean.success() : RestBean.failure(400, "保存失败");
    }

    @PostMapping("/modify-email")
    public RestBean<Void> modifyEmail(@RequestAttribute("id") int id,
                                      @RequestBody @Valid ModifyEmailVO vo) {
        return this.messageHandle(() -> accountService.modifyEmail(id, vo));
    }

    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestAttribute("id") int id,
                                         @RequestBody @Valid ChangePasswordVO vo) {
        return this.messageHandle(() -> accountService.changePassword(id, vo));
    }

    private <T> RestBean<T> messageHandle(Supplier<String> action) {
        String message = action.get();
        if (message == null)
            return RestBean.success();
        return RestBean.failure(400, message);
    }
}
