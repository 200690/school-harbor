package com.harbor.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.user.domain.dto.LoginFormDTO;
import com.harbor.user.domain.dto.UserRegisterDTO;
import com.harbor.user.domain.po.RechargeRecord;
import com.harbor.user.domain.vo.UserLoginVO;

public interface IUserService extends IService<RechargeRecord> {

    UserLoginVO login(LoginFormDTO loginFormDTO);

    void register(UserRegisterDTO userRegisterDTO);


//    void deductMoney(String pw, Integer totalFee);
}
