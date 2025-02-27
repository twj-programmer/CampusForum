package com.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forum.entity.dto.AccountPrivacy;
import com.forum.entity.vo.request.PrivacySaveVO;

public interface AccountPrivacyService extends IService<AccountPrivacy> {
    void savePrivacy(int id, PrivacySaveVO vo);
    AccountPrivacy accountPrivacy(int id);
}
