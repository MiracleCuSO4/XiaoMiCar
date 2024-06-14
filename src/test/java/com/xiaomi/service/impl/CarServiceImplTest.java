package com.xiaomi.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaomi.WarnApplication;
import com.xiaomi.domain.po.Car;
import com.xiaomi.mapper.CarMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = WarnApplication.class)
@RunWith(SpringRunner.class)
public class CarServiceImplTest {

    @Autowired
    private CarMapper carMapper;

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
}