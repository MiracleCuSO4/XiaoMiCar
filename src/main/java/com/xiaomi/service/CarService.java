package com.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomi.common.PageResult;
import com.xiaomi.domain.dto.CarDto;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.po.Car;
import com.xiaomi.domain.vo.CarVo;

public interface CarService extends IService<Car> {
    CarVo selectByCarId(Integer carId);

    void insertCar(CarDto carDto);

    void updateCar(CarDto carDto);

    void deleteByCarId(Integer carId);

    PageResult<CarVo> selectPageList(PageRequest pageRequest);

    void deleteByVid(String vid);
}
