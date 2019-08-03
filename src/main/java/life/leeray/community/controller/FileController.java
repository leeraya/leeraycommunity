package life.leeray.community.controller;

import life.leeray.community.dto.FileDTO;
import life.leeray.community.provider.UCloudProvider;
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
 */
@Controller
public class FileController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @RequestMapping(value = "/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        FileDTO fileDTO = new FileDTO();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            String imgUrl = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            fileDTO.setSuccess(1);
            fileDTO.setUrl(imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileDTO;
    }
}
