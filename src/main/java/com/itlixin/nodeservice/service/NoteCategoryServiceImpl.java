package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itlixin.nodeservice.dto.resp.NoteCategoryTree;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.entity.NoteCategory;
import com.itlixin.nodeservice.mapper.NoteCategoryMapper;
import com.itlixin.nodeservice.mapper.NoteMapper;
import com.itlixin.nodeservice.utils.LoginUserUtil;
import com.itlixin.nodeservice.utils.TreeBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteCategoryServiceImpl {
    private static final int MAX_LEVEL = 3;

    private final NoteCategoryMapper noteCategoryMapper;

    private final NoteServiceImpl noteService;

    public List<NoteCategoryTree> getTree() {

        List<NoteCategory> list = noteCategoryMapper.selectList(
                new LambdaQueryWrapper<NoteCategory>()
                        .orderByDesc(NoteCategory::getUpdateTime)
                        .orderByAsc(NoteCategory::getParentId)
        );
        if (list.isEmpty()){
            NoteCategory noteCategory = new NoteCategory();
            noteCategory.setName("我的在线笔记本");
            noteCategory.setLevel(1);
            noteCategory.setUserId(LoginUserUtil.getUserId());
            noteCategory.setSort(1);
            create(noteCategory);
            list.add(noteCategory);
        }
        Map<Long, Long> longIntegerMap = noteService.countByCategory();
        return TreeBuilder.build(list,longIntegerMap);
    }

    public Integer create(NoteCategory category) {

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
       return noteCategoryMapper.insert(category);
    }

    public Integer update(NoteCategory category) {
        NoteCategory noteCategory = noteCategoryMapper.selectById(category.getId());
        if (noteCategory == null){
            category.setId(null);
            category.setUserId(LoginUserUtil.getUserId());
            return create(category);
        }else {
            NoteCategory exist = noteCategoryMapper.selectOne(new LambdaQueryWrapper<NoteCategory>()
                    .eq(NoteCategory::getParentId, category.getParentId())
                    .eq(NoteCategory::getName, category.getName())
            );
            if (exist != null && exist.getId() != category.getId()){
                throw new RuntimeException("目录已存在");
            }
            return noteCategoryMapper.updateById(category);
        }
    }

    public Integer delete(Long id) {
        NoteCategory noteCategory = noteCategoryMapper.selectOne(new LambdaQueryWrapper<NoteCategory>()
                .eq(NoteCategory::getId, id)
                .eq(NoteCategory::getUserId, LoginUserUtil.getUserId())
        );
        if (noteCategory == null){
            return 0;
        }
        List<NoteCategory> noteCategories = noteCategoryMapper.selectList(new LambdaQueryWrapper<NoteCategory>()
                .eq(NoteCategory::getParentId, noteCategory.getId())
        );
        noteCategories.add(noteCategory);
        List<Long> ids = noteCategories.stream()
                .map(NoteCategory::getId).collect(Collectors.toList());
        List<Note> notes = noteService.listByCategory(LoginUserUtil.getUserId(), ids);
        if (!notes.isEmpty()){
            throw new RuntimeException("该目录或子目录下还有笔记,要删除请先清空笔记!");
        }
        return noteCategoryMapper.deleteBatchIds(ids);
    }
}
