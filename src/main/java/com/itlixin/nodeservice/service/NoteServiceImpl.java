package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.mapper.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl {

    private final NoteMapper noteMapper;

    public void create(Note note) {
        noteMapper.insert(note);
    }

    public List<Note> list(Long userId) {
        return noteMapper.selectList(
                new LambdaQueryWrapper<Note>()
                        .eq(Note::getUserId, userId)
                        .orderByDesc(Note::getUpdateTime)
        );
    }

    public Note get(Long id, Long userId) {
        Note note = noteMapper.selectById(id);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new RuntimeException("无权限访问");
        }
        return note;
    }

    public void update(Note note) {
        noteMapper.updateById(note);
    }

    public void delete(Long id, Long userId) {
        get(id, userId);
        noteMapper.deleteById(id);
    }
}

