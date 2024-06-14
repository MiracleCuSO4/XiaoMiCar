package com.xiaomi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaomi.domain.po.Car;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarMapper extends BaseMapper<Car> {
}
