package com.itlixin.nodeservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itlixin.nodeservice.dto.resp.Result;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.service.NoteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @PostMapping("/upload")
    public Result<Map> upload(@RequestParam("file") MultipartFile file) throws IOException {

        // 1. 保存文件（本地 / OSS / MinIO）
        String url = saveFile(file);

        // 2. 返回 Vditor 规范格式
        return Result.ok(Map.of(
                "errFiles", List.of(),
                "succMap", Map.of("filename.jpg","/img/"+url)
        ));
    }
    private static String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件为空");
        }

        // 1. 获取 jar 所在目录（本地运行 / jar 运行 都适用）
        String rootPath = System.getProperty("user.dir");

        // 2. 设定保存目录（如：oldImg）
        String saveDirPath = rootPath + File.separator + "img/";
        File saveDir = new File(saveDirPath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        // 3. 获取原始文件名后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 4. 生成唯一文件名
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + suffix;

        // 5. 保存文件
        File dest = new File(saveDir, fileName);
        file.transferTo(dest);

        // 6. 返回文件名（或你也可以返回相对路径）
        return fileName;
    }
}

