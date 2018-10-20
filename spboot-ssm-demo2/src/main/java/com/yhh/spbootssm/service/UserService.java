package com.yhh.spbootssm.service;

import com.yhh.spbootssm.domain.User;
import java.util.List;

/**
 * @Author 【swg】.
 * @Date 2017/11/26 10:01
 * @DESC
 * @CONTACT 317758022@qq.com
 */
public interface UserService {
    User showTest(int id);
    List<User> tests();
    void testAdd(User test);

    void testTra(Integer id1, Integer id2);
}
