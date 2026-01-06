package com.itlixin.nodeservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itlixin.nodeservice.entity.NoteCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoteCategoryMapper extends BaseMapper<NoteCategory> {
}
