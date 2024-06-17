package com.xiaomi.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.domain.po.Car;
import com.xiaomi.mapper.CarMapper;
import com.xiaomi.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 根据车架id获取汽车,不在缓存就查数据库存入缓存
     * @param carId 车架id
     * @return 汽车信息
     */
    @Override
    public Car getByCarId(Integer carId) {
        String key = "car:" + carId;
        String json = stringRedisTemplate.opsForValue().get(key);
        if(json != null){
            return JSON.parseObject(json, Car.class);
        }
        Car car = getOne(Wrappers.<Car>lambdaQuery().eq(Car::getCarId, carId).eq(Car::getIsDelete, 0));
        if(car == null){
            return null;
        }
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(car));
        return car;
    }
}
