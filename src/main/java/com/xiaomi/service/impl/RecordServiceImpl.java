package com.xiaomi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.common.PageResult;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.po.Record;
import com.xiaomi.domain.vo.RecordVo;
import com.xiaomi.exception.DataNotExistException;
import com.xiaomi.mapper.RecordMapper;
import com.xiaomi.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Override
    public RecordVo selectById(Integer id) {
        Record record = getById(id);
        if(record == null){
            throw new DataNotExistException("不存在id为" + id + "的预警记录");
        }
        return BeanUtil.copyProperties(record, RecordVo.class);
    }

    @Override
    public PageResult<RecordVo> selectPageList(PageRequest pageRequest) {
        IPage<Record> page = new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize());
        page = page(page);
        List<RecordVo> recordVoList = page.getRecords().stream()
                .map(record -> BeanUtil.copyProperties(record, RecordVo.class)).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), recordVoList);
    }

    @Override
    public void updateMarkAsRead(Integer id) {
        update(Wrappers.<Record>lambdaUpdate().eq(Record::getId, id).set(Record::getDealTime, Timestamp.from(Instant.now())));
    }

    @Override
    public void deleteById(Integer id) {
        removeById(id);
    }
}
