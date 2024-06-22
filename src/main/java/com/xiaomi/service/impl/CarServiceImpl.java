package com.xiaomi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.common.PageResult;
import com.xiaomi.common.RedisConstant;
import com.xiaomi.domain.dto.CarDto;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.po.Car;
import com.xiaomi.domain.vo.CarVo;
import com.xiaomi.exception.DataNotExistException;
import com.xiaomi.mapper.CarMapper;
import com.xiaomi.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public CarVo selectByCarId(Integer carId) {
        String carCacheKey = String.format(RedisConstant.CAR_KEY, carId);
        String json = stringRedisTemplate.opsForValue().get(carCacheKey);
        if(StrUtil.isNotBlank(json)){
            return JSON.parseObject(json, CarVo.class);
        }
        Car car = getOne(Wrappers.<Car>lambdaQuery().eq(Car::getCarId, carId));
        if(car == null){
            throw new DataNotExistException("不存在" + carId + "的车架id");
        }
        CarVo carVo = BeanUtil.copyProperties(car, CarVo.class);
        stringRedisTemplate.opsForValue().set(carCacheKey, JSON.toJSONString(carVo), RedisConstant.TTL, TimeUnit.MILLISECONDS);
        return carVo;
    }

    @Override
    public void insertCar(CarDto carDto) {
        Car car = BeanUtil.copyProperties(carDto, Car.class);
        car.setVid(RandomUtil.randomString(16));
        try {
            save(car);
        } catch (Exception e) {
            // 因为车架id重复时可能有多个电池类型,这样来自一辆车的信号数据用多个电池类型的规则来判断不合理
            throw new IllegalArgumentException("新增失败,不支持carId重复的情况");
        }
    }

    @Override
    public void updateCar(CarDto carDto) {
        String vid = carDto.getVid();
        if(StrUtil.isBlank(vid)){
            log.warn("没有指定汽车vid,将更新所有车架id为{}的汽车信息", carDto.getCarId());
        }
        Car car = BeanUtil.copyProperties(carDto, Car.class);
        update(car, Wrappers.<Car>lambdaUpdate()
                .eq(StrUtil.isNotBlank(vid), Car::getVid, vid)
                .eq(Car::getCarId, carDto.getCarId()));
        String carCacheKey = String.format(RedisConstant.CAR_KEY, carDto.getCarId());
        stringRedisTemplate.delete(carCacheKey);
    }

    @Override
    public void deleteByCarId(Integer carId) {
        remove(Wrappers.<Car>lambdaQuery().eq(Car::getCarId, carId));
        String carCacheKey = String.format(RedisConstant.CAR_KEY, carId);
        stringRedisTemplate.delete(carCacheKey);
    }

    @Override
    public PageResult<CarVo> selectPageList(PageRequest pageRequest) {
        IPage<Car> page = new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize());
        page = page(page, Wrappers.<Car>lambdaQuery().orderByAsc(Car::getCarId));
        List<CarVo> carVoList = page.getRecords().stream()
                .map(car -> BeanUtil.copyProperties(car, CarVo.class))
                .collect(Collectors.toList());

        return new PageResult<>(page.getTotal(), carVoList);
    }

    @Override
    public void deleteByVid(String vid) {
        Car car = getById(vid);
        if(car == null){
            throw new DataNotExistException("vid为" + vid + "的汽车不存在");
        }
        removeById(vid);
        String carCacheKey = String.format(RedisConstant.CAR_KEY, car.getCarId());
        stringRedisTemplate.delete(carCacheKey);
    }
}
