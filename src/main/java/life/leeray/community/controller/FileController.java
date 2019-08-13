package life.leeray.community.controller;

import life.leeray.community.dto.FileDTO;
import life.leeray.community.dto.ResultDTO;
import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.User;
import life.leeray.community.provider.UCloudProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/3 0003 10:28
 * 上传图片控制器
 */
@Controller
@Slf4j
public class FileController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;


    /**
     * @param request
     * @return 返回一个FileDTO对象，自动转为json。对象属性根据ufile的api设定。
     */
    @RequestMapping(value = "/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        FileDTO fileDTO = new FileDTO();
        //转为复杂类型request,包含图片内容
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //根据前端给图片的name属性拿到图片file
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            String imgUrl = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            fileDTO.setSuccess(1);
            fileDTO.setUrl(imgUrl);
            return fileDTO;
        } catch (Exception e) {
            log.error("upload error", e);
            fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            return fileDTO;
        }
    }

    /**
     * 用户修改头像
     *
     * @param img
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO uploadImg(@RequestParam(value = "uploadImg") MultipartFile img,
                               HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        //上传
        try {
            String imgUrl = uCloudProvider.upload(img.getInputStream(), img.getContentType(), img.getOriginalFilename());
            user.setAvatarUrl(imgUrl);
            userMapper.updateByPrimaryKeySelective(user);
            if (redisTemplate.hasKey("user" + user.getToken())) {
                redisTemplate.opsForValue().set("user" + user.getToken(), user, 600, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultDTO.okOff();
    }

}
