package com.zs.handler;

import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.pojo.User;
import com.zs.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 上传文件处理器
 * @Created by zs on 2022/3/6.
 */
public class UploadUtils {

    // TODO 处理图片时，如果是编辑的情况，则需删除以前保存的图片，以减少空间占用

    /**
     * 上传图片处理器
     * @param uploadFile 上传文件对象
     * @param userName 用户名
     * @param picSize 限定文件大小
     * @return serverSaveFileName == null  表示图片大小、格式有误
     *         serverSaveFileName == ""    表示未上传图片
     *         serverSaveFileName == xxx   表示图片处理成功，返回在本地保存的文件名
     * @throws Exception
     */
    public static String uploadPictureHandler(MultipartFile uploadFile, String userName, Long picSize) throws Exception {
        String serverSaveFileName = null;
        // 上传文件不为 null / 大小不为 0
        if(uploadFile != null && uploadFile.getSize() != 0) {
            // 文件是否超过规定大小
            if (uploadFile.getSize() < picSize) {
                String originalFilename = uploadFile.getOriginalFilename();
                String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
                if (Const.PICTURE_SUPPORT_FORMAT.contains(ext)) {
                        // 文件处理
                        serverSaveFileName = userName + System.currentTimeMillis() + ext;
                        String parentDir = System.getProperty("user.dir");
                        String childDir = Const.BLOG_FIRST_PICTURE_SAVE_DIR + serverSaveFileName;
                        uploadFile.transferTo(new File(parentDir + childDir));
                } else {
                    serverSaveFileName = null;
                    throw new UniversalException("上传图片格式有误");
                }
            } else {
                serverSaveFileName = null;
                throw new UniversalException("上传图片大小有误");
            }
        } else {
            serverSaveFileName = "";
        }
        return serverSaveFileName;
    }

    /**
     * 文件上传处理器（2.0）
     * @param uploadFile 上传文件
     * @param userMail 用户邮箱
     * @param fileSize 允许的文件大小
     * @param ext 允许的文件扩展名
     * @param type 上传类型，因为不同类型的文件存储在不同的文件加
     *             0 文章封面/文章插图
     *             1 文章类别图片
     *             2 用户头像
     * @return ResultVO.code = 533 上传文件对象为空
     *         ResultVO.code = 531 上传文件类型不支持
     *         ResultVO.code = 535 上传文件大小不支持
     *         ResultVO.code = 538 上传文件成功，返回文件在服务器中保存的文件名
     * @throws Exception 获取操作系统信息失败
     */
    public static ResultVO uploadFileHandler(MultipartFile uploadFile, String userMail, Long fileSize, String ext, Integer type) throws Exception {
        if (uploadFile == null) {
            return new ResultVO(Const2.UPLOAD_FILE_FAIL_NULL, "fail: file is null", null);
        }
        String originalFilename = uploadFile.getOriginalFilename();
        String originalExt = originalFilename.substring(originalFilename.lastIndexOf('.'));
        if (!ext.contains(originalExt)) {
            return new ResultVO(Const2.UPLOAD_FILE_FAIL_EXT, "fail: file type not support", null);
        }
        if (uploadFile.getSize() > fileSize) {
            return new ResultVO(Const2.UPLOAD_FILE_FAIL_SIZE, "fail: file size too big", null);
        }
        String fileSaveName = userMail + System.currentTimeMillis() + originalExt;
        String parentDir = "";
        String os = System.getProperty("os.name");
        if (os.contains("windows") || os.contains("Windows")) {
            if (type == 0) {
                parentDir = Const2.ARTICLE_PICTURE_SAVE_DIR_WINDOWS;
            } else if (type == 1) {
                // TODO 后台管理上线时处理
                parentDir = "";
            } else {
                parentDir = Const2.USER_AVATAR_SAVE_DIR_WINDOWS;
            }
        } else if (os.contains("linux") || os.contains("Linux")) {
            if (type == 0) {
                parentDir = Const2.ARTICLE_PICTURE_SAVE_DIR_LINUX;
            } else if (type == 1) {
                // TODO 后台管理上线时处理
                parentDir = "";
            } else {
                parentDir = Const2.USER_AVATAR_SAVE_DIR_LINUX;
            }
        }
        if (Objects.equals("", parentDir)) {
            return new ResultVO(Const2.UPLOAD_FILE_FAIL_EXCEPTION, "cannot get os msg", null);
        }
        uploadFile.transferTo(new File(parentDir + fileSaveName));
        return new ResultVO(Const2.UPLOAD_FILE_SUCCESS, "success", fileSaveName);
    }

}
