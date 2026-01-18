package com.itlixin.nodeservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itlixin.nodeservice.dto.resp.Result;
import com.itlixin.nodeservice.entity.NoteShare;
import com.itlixin.nodeservice.service.NoteShareServiceImpl;
import com.itlixin.nodeservice.utils.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/note/share")
@RequiredArgsConstructor
public class NoteShareController {

    private final NoteShareServiceImpl noteShareService;

    // 生成分享链接
    @GetMapping()
    public Result<String> getLink(@RequestParam Long noteId,
                                      @RequestParam long expireDay,@RequestParam String prefix) {
        return Result.ok(noteShareService.createShare(noteId, LoginUserUtil.getUserId(), Duration.ofDays(expireDay),prefix));
    }

    // 访问分享（无需登录）
    @GetMapping("/public/{shareCode}")
    public Result<NoteShare> accessShare(@PathVariable String shareCode) {
        NoteShare share = noteShareService.accessShare(shareCode);
        return Result.ok(share);
    }
    @GetMapping("/page")
    public Result<IPage<NoteShare>> page(@RequestParam int current, @RequestParam int size) {
        return Result.ok(noteShareService.pageShare(current, size));
    }
    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable Long id){
        return Result.ok(noteShareService.delete(id));
    }
}

