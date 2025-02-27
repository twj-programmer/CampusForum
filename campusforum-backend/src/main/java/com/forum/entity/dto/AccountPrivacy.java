package com.forum.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.forum.entity.BaseData;
import lombok.Data;

@Data
@TableName("db_account_privacy")
public class AccountPrivacy implements BaseData {
    @TableId
    final Integer id;
    boolean gender = false;
    boolean phone = false;
    boolean email = false;
    boolean qq = false;
    boolean wx = false;
}
