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
        Car car = getOne(Wrappers.<Car>lambdaQuery().eq(Car::getCarId, carId).eq(Car::getIsDelete, 0));
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
        save(car);
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
                .eq(Car::getCarId, carDto.getCarId())
                .eq(Car::getIsDelete, 0));
        String carCacheKey = String.format(RedisConstant.CAR_KEY, carDto.getCarId());
        stringRedisTemplate.delete(carCacheKey);
    }

    @Override
    public void deleteByCarId(Integer carId) {
        update(Wrappers.<Car>lambdaUpdate().eq(Car::getCarId, carId).set(Car::getIsDelete, 1));
        String carCacheKey = String.format(RedisConstant.CAR_KEY, carId);
        stringRedisTemplate.delete(carCacheKey);
    }

    @Override
    public PageResult<CarVo> selectPageList(PageRequest pageQueryDto) {
        IPage<Car> page = new Page<>(pageQueryDto.getPageNumber(), pageQueryDto.getPageSize());
        page = page(page, Wrappers.<Car>lambdaQuery().eq(Car::getIsDelete, 0).orderByAsc(Car::getCarId));
        List<CarVo> carVoList = page.getRecords().stream()
                .map(car -> BeanUtil.copyProperties(car, CarVo.class))
                .collect(Collectors.toList());

        PageResult<CarVo> pageResult = new PageResult<>();
        pageResult.setTotal(pageResult.getTotal());
        pageResult.setRecords(carVoList);
        return pageResult;
    }
}
