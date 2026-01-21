package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itlixin.nodeservice.dto.CategoryCountDTO;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.entity.NoteCategory;
import com.itlixin.nodeservice.mapper.NoteCategoryMapper;
import com.itlixin.nodeservice.mapper.NoteMapper;
import com.itlixin.nodeservice.utils.FileParseUtil;
import com.itlixin.nodeservice.utils.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl {

    private final NoteMapper noteMapper;
    private final NoteCategoryMapper categoryMapper;

    public Integer create(Note note) {
        return noteMapper.insert(note);
    }
    Map<Long, Long> countByCategory(){
        List<CategoryCountDTO> count = noteMapper.countByCategory();
        Map<Long, Long> countMap = count.stream()
                .collect(Collectors.toMap(
                        CategoryCountDTO::getCategoryId,
                        CategoryCountDTO::getCnt
                ));

        return countMap;
    }
    public IPage<Note> listPage(Long categoryId, int current, int size) {
        Page<Note> page = new Page<>(current, size);
        NoteCategory noteCategory = categoryMapper.selectById(categoryId);
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<Note>()
                .select(
                        Note::getId,Note::getCategoryId,Note::getUserId,
                        Note::getTitle,Note::getCreateTime,Note::getSummary,
                        Note::getUpdateTime
                )
                .eq(Note::getUserId, LoginUserUtil.getUserId())
                .eq(noteCategory.getLevel() != 1,Note::getCategoryId, categoryId)
                .orderByDesc(Note::getUpdateTime);

        return noteMapper.selectPage(page, queryWrapper);
    }

    public List<Note> listByCategory(Long userId,List<Long> categoryIds) {
        return noteMapper.selectList(
                new LambdaQueryWrapper<Note>()
                        .eq(Note::getUserId, userId)
                        .in(Note::getCategoryId, categoryIds)
        );
    }

    public Note getNode(Long id) {
        return noteMapper.selectOne(new LambdaQueryWrapper<Note>()
                .eq(Note::getId,id)
                .eq(Note::getUserId,LoginUserUtil.getUserId())
        );
    }

    public Long update(Note note) {
        if(note.getId() == null){
            note.setUserId(LoginUserUtil.getUserId());
            create(note);
        }else {
            note.setUpdateTime(LocalDateTime.now());
            noteMapper.updateById(note);
        }
        return note.getId();
    }

    public Integer delete(List<Long> ids) {
        return noteMapper.deleteBatchIds(ids);
    }

    public IPage<Note> search(String keyword, int current, int size){
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new RuntimeException("搜索关键词不能为空");
        }
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<Note>()
                .select(
                        Note::getId,Note::getCategoryId,Note::getUserId,
                        Note::getTitle,Note::getCreateTime,Note::getSummary,
                        Note::getUpdateTime
                )
                .eq(Note::getUserId, LoginUserUtil.getUserId())
                .like(Note::getContent,keyword)
                .orderByDesc(Note::getUpdateTime);
        Page<Note> page = new Page<>(current, size);
        return noteMapper.selectPage(page, queryWrapper);
    }

    public Long importFile(MultipartFile file, Long categoryId) {
        String content = FileParseUtil.parse(file);

        Note note = new Note();
        note.setTitle(getTitle(file.getOriginalFilename()));
        note.setContent(content);
        note.setCreateTime(LocalDateTime.now());
        note.setCategoryId(categoryId);
        note.setUserId(LoginUserUtil.getUserId());
        noteMapper.insert(note);
        return note.getId();
    }

    private String getTitle(String filename) {
        int index = filename.lastIndexOf(".");
        return index == -1 ? filename : filename.substring(0, index);
    }

    public int move(Note req) {
        Note note = noteMapper.selectById(req.getId());
        note.setCategoryId(req.getCategoryId());
        return noteMapper.updateById(note);
    }
}

