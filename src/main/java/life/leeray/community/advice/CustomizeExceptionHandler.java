package life.leeray.community.advice;

import com.alibaba.fastjson.JSON;
import life.leeray.community.dto.ResultDTO;
import life.leeray.community.exception.CustomizeErrorCode;
import life.leeray.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.undo.CannotUndoException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/28 0028 9:07
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Model model,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Throwable ex) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            //返回json
            ResultDTO resultDTO;
            if (ex instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) ex);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(200);
                PrintWriter printWriter = response.getWriter();
                printWriter.write(JSON.toJSONString(resultDTO));
                printWriter.close();//及时关闭流
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            //返回页面
            if (ex instanceof CustomizeException) {
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
