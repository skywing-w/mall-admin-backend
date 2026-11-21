package com.mall.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.mall.exception.BusinessException;
import com.mall.properties.MallProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * 本地文件上传工具：把 MultipartFile 写入配置的上传目录，返回可访问的 URL 路径。
 * <p>
 * 上传根目录来自配置 {@code mall.upload-dir}，便于不同环境切换；生产环境可替换为 OSS 实现。
 *
 * @author wwty
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

    /** 按天分目录，避免单目录文件过多 */
    private static final DateTimeFormatter DATE_DIR_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /** 允许上传的扩展名白名单：上传目录是静态可访问的，必须限制类型，避免写入可执行脚本 */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "webp", "bmp",
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            "txt", "csv", "zip"
    );

    /** 单文件大小上限 10MB，与 application.yml 的 multipart 配置保持一致 */
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024L;

    private final MallProperties properties;

    /**
     * 保存文件。
     *
     * @param file 上传的文件
     * @return 形如 {@code /uploads/2026/11/21/xxx.jpg} 的可访问路径
     * @throws BusinessException 文件为空、类型不允许、超出大小上限或写入失败
     */
    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件为空");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("文件大小不能超过 10MB");
        }

        String original = file.getOriginalFilename();
        String ext = StrUtil.subAfter(original, '.', true);
        if (StrUtil.isBlank(ext)) {
            throw new BusinessException("文件缺少扩展名，无法识别类型");
        }
        ext = ext.toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new BusinessException("不支持的文件类型：" + ext);
        }

        String dateDir = LocalDate.now().format(DATE_DIR_FORMAT);
        String fileName = IdUtil.simpleUUID() + "." + ext;
        String relative = "/uploads/" + dateDir + "/" + fileName;

        String uploadDir = properties.getUploadDir();
        // 配置项可能带或不带结尾斜杠，统一处理，避免出现 // 或拼错路径
        if (!uploadDir.endsWith("/") && !uploadDir.endsWith("\\")) {
            uploadDir = uploadDir + "/";
        }
        File target = new File(uploadDir + dateDir + "/" + fileName).getAbsoluteFile();
        // 注意：MultipartFile.transferTo(File) 对相对路径是按 Tomcat 临时目录解析的，
        // 直接传 "./uploads/..." 会落到 tomcat/work 下并报「系统找不到指定的路径」，
        // 必须先转成绝对路径（相对启动目录）。
        cn.hutool.core.io.FileUtil.mkParentDirs(target);
        try {
            file.transferTo(target);
        } catch (IOException e) {
            log.error("文件保存失败 path={}", target.getAbsolutePath(), e);
            throw new BusinessException("文件保存失败：" + e.getMessage());
        }
        return relative;
    }
}