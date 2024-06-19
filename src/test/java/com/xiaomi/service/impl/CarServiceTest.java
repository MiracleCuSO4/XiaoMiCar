package com.xiaomi.service.impl;

import com.xiaomi.WarnApplication;
import com.xiaomi.common.PageResult;
import com.xiaomi.domain.dto.CarDto;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.vo.CarVo;
import com.xiaomi.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = WarnApplication.class)
@RunWith(SpringRunner.class)
public class CarServiceTest {

    @Autowired
    private CarService carService;

    @Test
    public void testInsertCar(){
        CarDto carDto = new CarDto();
        carDto.setCarId(1);
        carDto.setBatteryType("三元电池");
        carDto.setTotalDistance(100);
        carDto.setBatteryHealth(100);
        carService.insertCar(carDto);

        carDto.setCarId(2);
        carDto.setBatteryType("铁锂电池");
        carDto.setTotalDistance(600);
        carDto.setBatteryHealth(95);
        carService.insertCar(carDto);

        carDto.setCarId(3);
        carDto.setBatteryType("三元电池");
        carDto.setTotalDistance(300);
        carDto.setBatteryHealth(98);
        carService.insertCar(carDto);
    }

    @Test
    public void testSelectByCarId(){
        CarVo carVo = carService.selectByCarId(1);
        System.out.println(carVo);
    }

    @Test
    public void testSelectPageList(){
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNumber(1);
        pageRequest.setPageSize(10);
        PageResult<CarVo> pageResult = carService.selectPageList(pageRequest);
        System.out.println(pageResult);
    }

    @Test
    public void testUpdateCar(){
        CarDto carDto = new CarDto();
        carDto.setVid("429s4i1v67mplcit");
        carDto.setCarId(1);
        carDto.setTotalDistance(210);
        carDto.setBatteryHealth(98);
        carService.updateCar(carDto);
    }

    @Test
    public void testDeleteByCarId(){
        carService.deleteByCarId(1);
    }

}
