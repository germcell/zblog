package com.zs.service.impl;

import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.handler.QQMailUtils;
import com.zs.handler.RandomUtils;
import com.zs.handler.UploadUtils;
import com.zs.mapper.BlogOutlineMapper;
import com.zs.mapper.WriterMapper;
import com.zs.pojo.BlogOutline;
import com.zs.pojo.Writer;
import com.zs.service.WriterService;
import com.zs.vo.ResultVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Created by zs on 2022/4/22.
 */
@Service
public class writerServiceImpl implements WriterService {

    private Logger logger = LoggerFactory.getLogger(writerServiceImpl.class);

    @Resource
    private HashMap<String, String>  validateCodeMap;
    @Resource
    private WriterMapper writerMapper;
    @Resource
    private BlogOutlineMapper blogOutlineMapper;

    /**
     * 判断用户名\邮箱是否已存在
     * @param name
     * @param mail
     * @return code ==  505 参数全部为空
     *         code ==  602 用户名可用
     *         code ==  601 用户名重复
     *         code ==  604 邮箱可用
     *         code ==  603 邮箱重复
     *         code ==  525 异常
     */
    @Override
    public ResultVO repeatName(String name, String mail) {
        if ((name == null || Objects.equals("", name)) && (mail == null || Objects.equals("", mail))) {
            return new ResultVO(Const2.PARAMETERS_IS_NULL, "fail: please provide parameters", null);
        }
        try {
            Writer condition = new Writer();
            if (mail == null || Objects.equals("", mail)) {
                condition.setWriterName(name);
                List<Writer> list = writerMapper.listWriterByCondition(condition);
                if (list.size() == 0) {
                    return new ResultVO(Const2.WRITER_NAME_AVAILABLE, "success: writer name is available", null);
                }
                return new ResultVO(Const2.WRITER_NAME_REPEAT, "fail: writer name is repeat", null);
            } else {
                condition.setMail(mail);
                List<Writer> list = writerMapper.listWriterByCondition(condition);
                if (list.size() == 0) {
                    return new ResultVO(Const2.MAIL_AVAILABLE, "success: mail is available", null);
                }
                return new ResultVO(Const2.MAIL_REPEAT, "fail: mail is repeat", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("用户名/邮箱判重接口异常");
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        }
    }

    /**
     * 给指定邮箱发送验证码
     * @param mail
     * @return
     */
    @Override
    public ResultVO sendValidateCode(String mail) {
        if (mail == null || Objects.equals("", mail)) {
            return new ResultVO(Const2.PARAMETERS_IS_NULL, "fail: please provide parameters", null);
        }
        try {
            String validateCode = RandomUtils.generateRandomNum();
            QQMailUtils.sendMail(mail, "你本次的验证码为: " + validateCode, "【ZBLOG】注册验证码，请勿提供给他人");
            validateCodeMap.put(mail, validateCode);
            return new ResultVO(Const2.SEND_MAIL_SUCCESS, "success", validateCodeMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("QQ邮箱发送接口异常");
            return new ResultVO(Const2.SEND_MAIL_FAIL, "fail: send mail fail", null);
        }
    }

    /**
     * 用户注册
     * @param writer
     * @param code
     * @return
     */
    @Transactional
    @Override
    public ResultVO register(Writer writer, String code) {
        synchronized (this) {
            if (Objects.equals("", writer.getWriterName()) ||
                Objects.equals("", writer.getPwd()) ||
                Objects.equals("", writer.getMail()) ||
                Objects.equals("", code)) {

             return new ResultVO(Const2.PARAMETERS_IS_NULL, "fail: please provide parameters", null);
            }
            if (Objects.equals(code, validateCodeMap.get(writer.getMail()))) {
                if (repeatName(writer.getWriterName(), null).getCode() == 601) {
                    return new ResultVO(Const2.WRITER_NAME_REPEAT, "fail: writer name is repeat", null);
                }
                if (repeatName(null, writer.getMail()).getCode() == 603) {
                    return new ResultVO(Const2.MAIL_REPEAT, "fail: mail is repeat", null);
                }
                try {
                    writer.setWriterName(writer.getWriterName());
                    writer.setWriterStatus(1);
                    writer.setWriterAvatar(Const2.DEFAULT_WRITER_AVATAR);
                    writer.setArticleNum(0);
                    writer.setFans(0L);
                    writer.setRegisterTime(new Date());
                    writer.setPwd(DigestUtils.md5DigestAsHex(writer.getPwd().getBytes()));
                    writer.setMail(writer.getMail());
                    int row = writerMapper.insert(writer);
                    if (row == 1) {
                        validateCodeMap.remove(writer.getMail(), code);
                        return new ResultVO(Const2.REGISTER_SUCCESS, "success", null);
                    } else {
                        return new ResultVO(Const2.REGISTER_FAIL, "fail: insert", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("用户注册失败");
                    return new ResultVO(Const2.REGISTER_FAIL, "fail: database", null);
                }
            }
            return new ResultVO(Const2.REGISTER_FAIL_VALIDATE_CODE, "fail: validate code mismatch", null);
        }

    }

    /**
     * 用户登录
     * @param writer
     * @return
     */
    @Override
    public ResultVO login(Writer writer) {
        if (Objects.equals("", writer.getMail()) || Objects.equals("", writer.getPwd()) ||
            null == writer.getMail() || null == writer.getPwd()) {
            return new ResultVO(Const2.PARAMETERS_IS_NULL, "fail: please provide parameters", null);
        }
        try {
            List<Writer> writers = writerMapper.listWriterByCondition(writer);
            if (writers.size() == 0) {
              return new ResultVO(Const2.LOGIN_FAIL, "fail: username or pwd mismatch", writer);
            }
            if (Objects.equals(writers.get(0).getPwd(), DigestUtils.md5DigestAsHex(writer.getPwd().getBytes()))) {
                // 生成token
                String token = Jwts.builder()
                        .setIssuedAt(new Date())
                        .setId(writers.get(0).getUid() + "")
                        .setExpiration(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000))
                        .signWith(SignatureAlgorithm.HS256, Const2.TOKEN_PWD)
                        .compact();
                writers.get(0).setPwd(token);
                return new ResultVO(Const2.LOGIN_SUCCESS, "success", writers.get(0));
            }
            return new ResultVO(Const2.LOGIN_FAIL, "fail: username or pwd mismatch", writer);
        } catch(Exception e) {
            e.printStackTrace();
            logger.info("登录接口异常");
            return new ResultVO(Const2.LOGIN_FAIL_TOKEN, "fail", writer);
        }
    }

    /**
     * 根据用户id查询用户信息
     * @param uid 用户id
     * @param whose 谁的信息
     *        whose == 0 查询自己的信息
     *        whose == 1 查询别人的信息
     */
    @Override
    public ResultVO getWriterByUid(String uid, Integer whose) {
        Integer writerId = null;
        try {
            writerId = Integer.parseInt(uid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询用户接口异常,用户不存在");
            return new ResultVO(Const2.NOT_FOUND, "not found", null);
        }
        try {
            // 1.查询用户基本信息
            Writer condition = new Writer();
            condition.setUid(writerId);
            List<Writer> writers = writerMapper.listWriterByCondition(condition);
            if (writers.size() == 0) {
                return new ResultVO(Const2.NOT_FOUND, "not found", null);
            }
            writers.get(0).setPwd(null);
            // 2.查询用户发布文章， 查询自己的信息时可查询置为草稿状态的文章；反之只能查询已发布的文章
            // TODO 分页
            List<BlogOutline> blogOutlines = null;
            if (Objects.equals(1, whose)) {
                blogOutlines = blogOutlineMapper.listBlogOutlinesByUid(writerId, 1);
            } else if (Objects.equals(0, whose)) {
                blogOutlines = blogOutlineMapper.listBlogOutlinesByUid(writerId, null);
            }
            Map<String,Object> map = new HashMap<>();
            map.put("writer", writers.get(0));
            map.put("articles", blogOutlines);
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询用户接口异常");
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        }
    }

    /**
     * 更新指定用户信息
     * @param uid
     * @param writer
     * @return code = 404 用户id不存在
     *         code = 500 未登录
     *         code = 501 token过期
     *         code = 525 更新失败
     *         code = 200 更新成功
     */
    @Transactional
    @Override
    public ResultVO updateWriterByUid(String uid, Writer writer) {
        Integer writerId = null;
        try {
            writerId = Integer.parseInt(uid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("更新用户信息接口异常,用户不存在");
            return new ResultVO(Const2.NOT_FOUND, "not found", null);
        }
        try {
            writer.setUid(writerId);
            writer.setUpdateTime(new Date());
            writerMapper.updateWriterByUid(writer);
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("更新用户信息接口异常");
            return new ResultVO(Const2.SERVICE_FAIL, "exception", null);
        }
    }

    /**
     * 更新指定用户头像
     * @param uid
     * @param avatarFile
     * @return code == 404 用户不存在
     *         code == 525 处理上传文件异常
     */
    @Transactional
    @Override
    public ResultVO updateAvatarByUid(String uid, String mail, MultipartFile avatarFile) {
        Integer writerId = null;
        try {
            writerId = Integer.parseInt(uid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("更新用户头像接口异常,用户不存在");
            return new ResultVO(Const2.NOT_FOUND, "not found", null);
        }
        try {
            ResultVO uploadResultVO = UploadUtils.uploadFileHandler(avatarFile, mail,
                    Const.USER_AVATAR_PICTURE_SIZE,
                    Const.PICTURE_SUPPORT_FORMAT, 2);
            if (uploadResultVO.getCode() == 538) {
                Writer writer = new Writer();
                writer.setUid(writerId);
                writer.setWriterAvatar((String) uploadResultVO.getData());
                writer.setUpdateTime(new Date());
                writerMapper.updateWriterByUid(writer);
            }
            return uploadResultVO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("处理上传头像异常");
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        }
    }
}
