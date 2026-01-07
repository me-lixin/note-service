package com.itlixin.nodeservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.entity.NoteShare;
import com.itlixin.nodeservice.mapper.NoteShareMapper;
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
    public NoteShare createShare(Long noteId, Long userId, Duration expire) {
        Note note = noteService.getNode(noteId,userId);
        if (!note.getUserId().equals(userId)) {
            throw new RuntimeException("无权分享此笔记");
        }

        String shareCode = UUID.randomUUID().toString().replace("-", "");

        NoteShare share = new NoteShare();
        share.setNoteId(noteId);
        share.setShareCode(shareCode);
        share.setTitleSnapshot(note.getTitle());
        share.setContentSnapshot(note.getContent());
        share.setExpireTime(LocalDateTime.now().plus(expire));
        share.setCreateTime(LocalDateTime.now());
        share.setViewCount(0);

        noteShareMapper.insert(share);
        return share;
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
}

