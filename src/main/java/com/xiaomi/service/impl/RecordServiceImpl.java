package com.xiaomi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.domain.po.Record;
import com.xiaomi.mapper.RecordMapper;
import com.xiaomi.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
}
