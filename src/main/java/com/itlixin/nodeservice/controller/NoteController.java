package com.itlixin.nodeservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itlixin.nodeservice.dto.resp.Result;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.service.NoteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Note 分类API
 */
@RestController
@RequestMapping("/api/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteServiceImpl noteService;

    @PostMapping
    public Result<Integer> create(@RequestBody Note req) {
        return Result.ok(noteService.create(req));
    }

    @GetMapping("/list")
    public Result<IPage<Note>> list(@RequestParam Long categoryId,
                                   @RequestAttribute("userId") Long userId,
                                   @RequestParam(value = "current", defaultValue = "1") int current,
                                   @RequestParam(value = "size", defaultValue = "10") int size

    ) {
        return Result.ok(noteService.listPage(userId,categoryId,current,size));
    }

    @GetMapping("/{id}")
    public Result<Note> get(@PathVariable Long id,
                            @RequestAttribute("userId") Long userId) {
        return Result.ok(noteService.getNode(id, userId));
    }

    @PutMapping
    public Result<Integer> update(@RequestBody Note req) {
        return Result.ok(noteService.update(req));
    }

    @DeleteMapping("/{ids}")
    public Result<Integer> delete(@PathVariable List<Long> ids) {
        return Result.ok(noteService.delete(ids));
    }

    @GetMapping("/search")
    public Result<IPage<Note>> search(@RequestParam("keyword") String keyword,
                                      @RequestAttribute("userId") Long userId,
                                      @RequestParam(value = "current", defaultValue = "1") int current,
                                      @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        return Result.ok(noteService.search(keyword, userId,current,size));
    }

    @PostMapping("/import")
    public Result<Long> importNote(@RequestParam("file") MultipartFile file,
                                   @RequestParam("categoryId") Long categoryId,
                                   @RequestAttribute("userId") Long userId) {

        Long noteId = noteService.importFile(file, categoryId, userId);
        return Result.ok(noteId);
    }
}

