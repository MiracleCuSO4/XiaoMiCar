package com.xiaomi.service.impl;

import com.xiaomi.common.AppHttpCodeEnum;
import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.SignalDto;
import com.xiaomi.domain.vo.WarnVo;
import com.xiaomi.exception.BaseException;
import com.xiaomi.service.WarnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarnServiceImpl implements WarnService {
    @Override
    public Result<List<WarnVo>> warn(List<SignalDto> dtoList) {
        throw new BaseException("接口待实现");
    }
}
