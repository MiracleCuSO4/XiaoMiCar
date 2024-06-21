package com.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomi.common.PageResult;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.po.Record;
import com.xiaomi.domain.vo.RecordVo;

public interface RecordService extends IService<Record> {
    RecordVo selectById(Integer id);

    PageResult<RecordVo> selectPageList(PageRequest pageRequest);

    void markAsRead(Integer id);

    void deleteById(Integer id);
}
