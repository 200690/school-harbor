package com.harbor.secondHand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.secondHand.user.domain.dto.LoginFormDTO;
import com.harbor.secondHand.user.domain.dto.UserPageDTO;
import com.harbor.utils.dto.UserInfoDTO;
import com.harbor.secondHand.user.domain.dto.UserRegisterDTO;
import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.domain.vo.UserLoginVO;
import com.harbor.secondHand.user.domain.vo.UserVO;

public interface IUserService extends IService<User> {

    UserLoginVO login(LoginFormDTO loginFormDTO);

    void register(UserRegisterDTO userRegisterDTO);

    UserInfoDTO getUserInfo(Long id);

    void updateUserInfo(UserInfoDTO userInfoDTO);

    PageDTO<UserVO> getUserList(UserPageDTO userPageDTO);

    void banUser(Long id);

    void unbanUser(Long id);

//    void deductMoney(String pw, Integer totalFee);
}
