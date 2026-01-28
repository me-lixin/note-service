package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.entity.NoteShare;
import com.itlixin.nodeservice.mapper.NoteShareMapper;
import com.itlixin.nodeservice.utils.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteShareServiceImpl {

    private final NoteShareMapper noteShareMapper;
    private final NoteServiceImpl noteService;

    // 创建分享
    public String createShare(Long noteId, Long userId, Duration expire, String prefix) {
        Note note = noteService.getNode(noteId);
        if (note == null){
            throw new RuntimeException("请先保存,再分享!");
        }
        if (!note.getUserId().equals(userId)) {
            throw new RuntimeException("无权分享此笔记");
        }

        String shareCode = UUID.randomUUID().toString().replace("-", "");
        NoteShare noteShare = noteShareMapper.selectOne(new LambdaQueryWrapper<NoteShare>()
                .eq(NoteShare::getNoteId, note.getId())
                .eq(NoteShare::getUserId, LoginUserUtil.getUserId())
        );
        if (noteShare == null){
            NoteShare share = new NoteShare();
            share.setNoteId(noteId);
            share.setTitleSnapshot(note.getTitle());
            share.setUrl(prefix+shareCode);
            share.setUserId(LoginUserUtil.getUserId());
            share.setShareCode(shareCode);
            share.setContentSnapshot(note.getContent());
            share.setExpireTime(LocalDateTime.now().plus(expire));
            share.setCreateTime(LocalDateTime.now());
            share.setViewCount(0);
            noteShareMapper.insert(share);
        }else {
            noteShare.setUrl(prefix+shareCode);
            noteShare.setShareCode(shareCode);
            noteShare.setContentSnapshot(note.getContent());
            noteShare.setExpireTime(LocalDateTime.now().plus(expire));
            noteShareMapper.updateById(noteShare);
        }
        return prefix+shareCode;
    }

    public NoteShare accessShare(String shareCode) {
        NoteShare share = noteShareMapper.selectOne(new LambdaQueryWrapper<NoteShare>()
                .eq(NoteShare::getShareCode,shareCode)
                .gt(NoteShare::getExpireTime,LocalDateTime.now())
        );
        if (share == null) {
            throw new RuntimeException("分享不存在");
        }
        noteShareMapper.incrementViewCount(share.getId());
        return share;
    }

    public IPage<NoteShare> pageShare(int current, int size) {
        Page<NoteShare> page = new Page<>(current, size);
        LambdaQueryWrapper<NoteShare> queryWrapper = new LambdaQueryWrapper<NoteShare>()
                .eq(NoteShare::getUserId, LoginUserUtil.getUserId())
                .orderByDesc(NoteShare::getCreateTime);
        return noteShareMapper.selectPage(page, queryWrapper);

    }

    public Integer delete(Long id) {
        return noteShareMapper.deleteById(id);
    }
}

