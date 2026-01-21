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
    public Result<IPage<Note>> list(@RequestParam(required = false) Long categoryId,
                                   @RequestParam(value = "current", defaultValue = "1") int current,
                                   @RequestParam(value = "size", defaultValue = "10") int size

    ) {
        return Result.ok(noteService.listPage(categoryId,current,size));
    }

    @GetMapping("/{id}")
    public Result<Note> get(@PathVariable Long id) {
        return Result.ok(noteService.getNode(id));
    }

    @PutMapping
    public Result<Long> update(@RequestBody Note req) {
        return Result.ok(noteService.update(req));
    }

    @PutMapping("/move")
    public Result<Integer> move(@RequestBody Note req) {
        return Result.ok(noteService.move(req));
    }

    @DeleteMapping("/{ids}")
    public Result<Integer> delete(@PathVariable List<Long> ids) {
        return Result.ok(noteService.delete(ids));
    }

    @GetMapping("/search")
    public Result<IPage<Note>> search(@RequestParam("keyword") String keyword,
                                      @RequestParam(value = "current", defaultValue = "1") int current,
                                      @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        return Result.ok(noteService.search(keyword,current,size));
    }

    @PostMapping("/import")
    public Result<Long> importNote(@RequestParam("file") MultipartFile file,
                                   @RequestParam("categoryId") Long categoryId) {

        Long noteId = noteService.importFile(file, categoryId);
        return Result.ok(noteId);
    }
}

