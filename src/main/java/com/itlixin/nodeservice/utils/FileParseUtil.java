package com.itlixin.nodeservice.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileParseUtil {

    private FileParseUtil() {
    }

    /**
     * 解析上传文件为文本内容
     */
    public static String parse(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("文件名非法");
        }

        String suffix = getSuffix(filename);

        try {
            switch (suffix) {
                case "md":
                case "txt":
                    return readText(file);
                case "docx":
                    return readDocx(file);
                default:
                    throw new IllegalArgumentException("不支持的文件类型: " + suffix);
            }
        } catch (IOException e) {
            throw new RuntimeException("文件解析失败", e);
        }
    }

    private static String readText(MultipartFile file) throws IOException {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }

    private static String readDocx(MultipartFile file) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph p : doc.getParagraphs()) {
                sb.append(p.getText()).append("\n");
            }
            return sb.toString();
        }
    }

    private static String getSuffix(String filename) {
        int index = filename.lastIndexOf(".");
        return index == -1 ? "" : filename.substring(index + 1).toLowerCase();
    }
}
