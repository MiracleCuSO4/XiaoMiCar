package com.xiaomi.service;

import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.SignalDto;
import com.xiaomi.domain.vo.WarnVo;

import java.util.List;

public interface WarnService {
    Result<List<WarnVo>> warn(List<SignalDto> dtoList);
}
