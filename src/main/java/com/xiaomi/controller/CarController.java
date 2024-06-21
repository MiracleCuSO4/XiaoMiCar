package com.xiaomi.controller;

import com.xiaomi.common.PageResult;
import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.CarDto;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.vo.CarVo;
import com.xiaomi.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
@Api(tags = "汽车接口")
@Validated
public class CarController {

    private final CarService carService;

    @GetMapping("/{id}")
    @ApiOperation("通过carId查询汽车")
    public Result<CarVo> selectByCarId(@PathVariable("id") Integer carId){
        return Result.okResult(carService.selectByCarId(carId));
    }

    @PostMapping("/list")
    @ApiOperation("分页查询汽车列表")
    public Result<PageResult<CarVo>> selectPageList(@RequestBody @Valid PageRequest pageRequest){
        pageRequest.checkParam();
        return Result.okResult(carService.selectPageList(pageRequest));
    }

    @PostMapping
    @ApiOperation("新增汽车")
    public Result<Void> insertCar(@RequestBody @Valid CarDto carDto){
        carService.insertCar(carDto);
        return Result.okResult(null);
    }

    @PutMapping
    @ApiOperation("修改汽车")
    public Result<Void> updateCar(@RequestBody @Valid CarDto carDto){
        carService.updateCar(carDto);
        return Result.okResult(null);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除汽车")
    public Result<Void> deleteByCarId(@PathVariable("id") Integer carId){
        carService.deleteByCarId(carId);
        return Result.okResult(null);
    }


}
