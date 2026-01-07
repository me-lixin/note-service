package com.itlixin.nodeservice.controller;

import com.itlixin.nodeservice.dto.resp.Result;
import com.itlixin.nodeservice.entity.NoteShare;
import com.itlixin.nodeservice.service.NoteShareServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/note/share")
@RequiredArgsConstructor
public class NoteShareController {

    private final NoteShareServiceImpl noteShareService;

    // 生成分享链接
    @PostMapping
    public Result<String> createShare(@RequestParam Long noteId,
                                      @RequestParam(defaultValue = "7") long expireSeconds,
                                      @RequestParam Long userId) {
        NoteShare share = noteShareService.createShare(noteId, userId, Duration.ofDays(expireSeconds));
        return Result.ok("http://yourdomain.com/share/" + share.getShareCode());
    }

    // 访问分享（无需登录）
    @GetMapping("/{shareCode}")
    public Result<NoteShare> accessShare(@PathVariable String shareCode) {
        NoteShare share = noteShareService.accessShare(shareCode);
        return Result.ok(share);
    }
}

