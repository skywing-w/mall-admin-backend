package com.mall.controller;

import com.mall.common.Result;
import com.mall.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口。
 * <p>
 * 仅接受图片与文档类文件，保存到配置的上传目录并返回可访问路径
 * （完整地址需加上 context-path，即 {@code /api/uploads/...}）。
 *
 * @author wwty
 */
@Slf4j
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileUtil fileUtil;

    /**
     * 上传单个文件。
     *
     * @param file 表单字段名为 {@code file} 的文件
     * @return 可访问的相对路径
     */
    @PostMapping
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String path = fileUtil.save(file);
        log.info("文件上传成功 name={} size={} path={}", file.getOriginalFilename(), file.getSize(), path);
        return Result.success(path);
    }
}