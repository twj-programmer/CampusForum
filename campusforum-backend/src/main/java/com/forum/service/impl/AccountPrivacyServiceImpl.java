package com.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forum.entity.dto.AccountPrivacy;
import com.forum.entity.vo.request.PrivacySaveVO;
import com.forum.mapper.AccountPrivacyMapper;
import com.forum.service.AccountPrivacyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountPrivacyServiceImpl extends ServiceImpl<AccountPrivacyMapper, AccountPrivacy> implements AccountPrivacyService {
    @Override
    @Transactional
    public void savePrivacy(int id, PrivacySaveVO vo) {
        AccountPrivacy privacy = Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
        boolean status = vo.isStatus();
        switch (vo.getType()) {
            case "gender" -> privacy.setGender(status);
            case "phone" -> privacy.setPhone(status);
            case "email" -> privacy.setEmail(status);
            case "qq" -> privacy.setQq(status);
            case "wx" -> privacy.setWx(status);
            default -> throw new IllegalArgumentException("Invalid type: " + vo.getType());
        }
        this.saveOrUpdate(privacy);
    }

    public AccountPrivacy accountPrivacy(int id) {
        return Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
    }
}
