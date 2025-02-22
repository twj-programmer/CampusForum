package com.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forum.entity.dto.AccountDetails;
import com.forum.entity.vo.request.DetailsSaveVO;

public interface AccountDetailsService extends IService<AccountDetails> {
    AccountDetails findAccountDetailsById(int id);
    boolean saveAccountDetails(int id, DetailsSaveVO vo);
}
