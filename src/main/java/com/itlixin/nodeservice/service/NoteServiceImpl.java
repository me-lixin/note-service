package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.mapper.NoteMapper;
import com.itlixin.nodeservice.utils.FileParseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl {

    private final NoteMapper noteMapper;

    public Integer create(Note note) {
        return noteMapper.insert(note);
    }

    public IPage<Note> listPage(Long userId, Long categoryId, int current, int size) {
        Page<Note> page = new Page<>(current, size);

        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<Note>()
                .eq(Note::getUserId, userId)
                .eq(Note::getCategoryId, categoryId)
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

    public Note getNode(Long id, Long userId) {
        return noteMapper.selectOne(new LambdaQueryWrapper<Note>()
                .eq(Note::getId,id)
                .eq(Note::getUserId,userId)
        );
    }

    public Integer update(Note note) {
       return noteMapper.updateById(note);
    }

    public Integer delete(List<Long> ids) {
        return noteMapper.deleteByIds(ids);
    }

    public IPage<Note> search(String keyword, Long userId, int current, int size){
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new RuntimeException("搜索关键词不能为空");
        }
        Page<Note> page = new Page<>(current, size);
        return noteMapper.search(page, userId, keyword);
    }

    public Long importFile(MultipartFile file, Long categoryId, Long userId) {
        String content = FileParseUtil.parse(file);

        Note note = new Note();
        note.setTitle(getTitle(file.getOriginalFilename()));
        note.setContent(content);
        note.setCreateTime(LocalDateTime.now());
        note.setCategoryId(categoryId);
        note.setUserId(userId);
        noteMapper.insert(note);
        return note.getId();
    }

    private String getTitle(String filename) {
        int index = filename.lastIndexOf(".");
        return index == -1 ? filename : filename.substring(0, index);
    }
}

