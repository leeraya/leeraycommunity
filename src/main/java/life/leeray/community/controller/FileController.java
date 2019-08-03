package life.leeray.community.controller;

import life.leeray.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/3 0003 10:28
 */
@Controller
public class FileController {

    @RequestMapping(value = "/file/upload")
    public FileDTO upload() {
        FileDTO fileDTO = new FileDTO();

        return fileDTO;
    }
}
