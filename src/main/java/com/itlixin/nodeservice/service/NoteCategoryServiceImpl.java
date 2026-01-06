package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itlixin.nodeservice.dto.resp.NoteCategoryTree;
import com.itlixin.nodeservice.entity.NoteCategory;
import com.itlixin.nodeservice.mapper.NoteCategoryMapper;
import com.itlixin.nodeservice.utils.TreeBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteCategoryServiceImpl {
    private static final int MAX_LEVEL = 3;

    private final NoteCategoryMapper noteCategoryMapper;

    public List<NoteCategoryTree> getTree() {

        List<NoteCategory> list = noteCategoryMapper.selectList(
                new LambdaQueryWrapper<NoteCategory>()
                        .orderByAsc(NoteCategory::getLevel)
                        .orderByAsc(NoteCategory::getParentId)
        );

        return TreeBuilder.build(list);
    }

    public void create(NoteCategory category) {

        if (category.getLevel() < 1 || category.getLevel() > MAX_LEVEL) {
            throw new IllegalArgumentException("分类层级必须是 1~3");
        }

        // 一级分类
        if (category.getLevel() == 1) {
            category.setParentId(0L);
        } else {
            NoteCategory parent = noteCategoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("父分类不存在");
            }
            if (!parent.getLevel().equals(category.getLevel() - 1)) {
                throw new IllegalArgumentException("父分类层级不正确");
            }
        }
        noteCategoryMapper.insert(category);
    }
}
