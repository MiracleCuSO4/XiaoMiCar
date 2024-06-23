package com.xiaomi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ResetMapper {

    // 查询表格的数据行数
    @Select("SELECT COUNT(*) FROM car")
    int countCarTable();

    @Select("SELECT COUNT(*) FROM rule")
    int countRuleTable();

    @Select("SELECT COUNT(*) FROM record")
    int countRecordTable();

    // 删除表格的数据
    @Update("TRUNCATE TABLE car")
    void truncateCarTable();

    @Update("TRUNCATE TABLE rule")
    void truncateRuleTable();

    @Update("TRUNCATE TABLE record")
    void truncateRecordTable();

    // 从原始数据表复制数据
    @Insert("INSERT INTO car SELECT * FROM original_car")
    int copyDataFromOriginalCarTable();

    @Insert("INSERT INTO rule SELECT * FROM original_rule")
    int copyDataFromOriginalRuleTable();

    @Insert("INSERT INTO record SELECT * FROM original_record")
    int copyDataFromOriginalRecordTable();
}
