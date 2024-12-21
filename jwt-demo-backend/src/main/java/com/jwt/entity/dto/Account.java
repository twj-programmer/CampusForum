package com.jwt.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@TableName("db_account")
public class Account {
        @TableId(type = IdType.AUTO)
        Integer id;
        @TableField("username")
        String username;
        @TableField("password")
        String password;
        @TableField("email")
        String email;
        @TableField("role")
        String role;
        @TableField("register_time")
        Date registerTime;
}
