package life.leeray.community.controller;

import life.leeray.community.dto.FileDTO;
import life.leeray.community.provider.UCloudProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

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
}
