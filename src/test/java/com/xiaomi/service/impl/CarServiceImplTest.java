package com.xiaomi.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaomi.WarnApplication;
import com.xiaomi.domain.po.Car;
import com.xiaomi.mapper.CarMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = WarnApplication.class)
@RunWith(SpringRunner.class)
public class CarServiceImplTest {

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testInsertCar(){
        Car car = new Car();
        car.setVid(RandomUtil.randomString(16));
        car.setCarId(1);
        car.setBatteryType("三元电池");
        car.setTotalDistance(100);
        car.setBatteryHealth(100);
        carMapper.insert(car);

        car.setVid(RandomUtil.randomString(16));
        car.setCarId(2);
        car.setBatteryType("铁锂电池");
        car.setTotalDistance(600);
        car.setBatteryHealth(95);
        carMapper.insert(car);

        car.setVid(RandomUtil.randomString(16));
        car.setCarId(3);
        car.setBatteryType("三元电池");
        car.setTotalDistance(300);
        car.setBatteryHealth(98);
        carMapper.insert(car);

    }

    @Test
    public void testGetCarById(){
        Car car = carMapper.selectOne(Wrappers.<Car>lambdaQuery().eq(Car::getCarId, 1));
        System.out.println(car);
    }

    @Test
    public void testCacheCar(){
        List<Car> carList = carMapper.selectList(Wrappers.<Car>lambdaQuery().eq(Car::getIsDelete, 0));
        for (Car car : carList) {
            stringRedisTemplate.opsForValue().set("car:" + car.getCarId(), JSON.toJSONString(car), 10, TimeUnit.MINUTES);
        }
        String json = stringRedisTemplate.opsForValue().get("car:1");
        Car car = JSON.parseObject(json, Car.class);
        System.out.println(car);
    }
}