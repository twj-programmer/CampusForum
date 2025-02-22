package com.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forum.entity.dto.AccountDetails;
import com.forum.entity.vo.request.DetailsSaveVO;
import com.forum.mapper.AccountDetailsMapper;
import com.forum.service.AccountDetailsService;
import com.forum.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService {

    @Resource
    AccountService accountService;

    @Override
    public AccountDetails findAccountDetailsById(int id) {
        return this.getById(id);
    }

    @Override
    public synchronized boolean saveAccountDetails(int id, DetailsSaveVO vo) {
        accountService.update()
                .eq("id", id)
                .set("username", vo.getUsername())
                .update();
        this.saveOrUpdate(new AccountDetails(
                id,
                vo.getGender(),
                vo.getPhone(),
                vo.getQq(),
                vo.getWx(),
                vo.getDesc()
        ));
        return true;
    }
}
