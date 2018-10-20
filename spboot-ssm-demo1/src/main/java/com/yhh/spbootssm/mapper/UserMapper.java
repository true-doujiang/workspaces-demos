package com.yhh.spbootssm.mapper;

import com.yhh.spbootssm.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;


//@Mapper //声明是一个Mapper,与springbootApplication中的@MapperScan二选一写上即可
public interface UserMapper {


    /**
     * 根据姓名查询数据
     * @param
     * @return 实体数据
     */
    //@Select("SELECT * FROM user WHERE username = #{username}")
    //@ResultType(User.class)
    List<User> findAll();

    //int inser(User user);

}
