package com.xiaomi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaomi.domain.po.Record;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
