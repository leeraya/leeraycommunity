package life.leeray.community.dto;

import life.leeray.community.exception.CustomizeErrorCode;
import life.leeray.community.exception.CustomizeException;
import lombok.Data;

import java.io.Serializable;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/28 0028 15:19
 */
@Data
public class ResultDTO<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(errorCode.getCode());
        resultDTO.setMessage(errorCode.getMessage());
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }

    public static ResultDTO okOff() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO okOff(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

    public static ResultDTO captchaError() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3001);
        resultDTO.setMessage("验证码错误");
        return resultDTO;
    }

    public static ResultDTO confirmError() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3002);
        resultDTO.setMessage("两次密码不一致");
        return resultDTO;
    }

    public static ResultDTO duplicateName() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3003);
        resultDTO.setMessage("用户名已经被注册！");
        return resultDTO;
    }

    public static ResultDTO duplicateEmail() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3004);
        resultDTO.setMessage("邮箱已经被注册！");
        return resultDTO;
    }

    public static ResultDTO pwdLengthError() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3005);
        resultDTO.setMessage("请保持密码长度在6-30之间！");
        return resultDTO;
    }

    public static ResultDTO UnknowUser() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3006);
        resultDTO.setMessage("用户名或密码错误！");
        return resultDTO;
    }

    public static ResultDTO test() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(4000);
        resultDTO.setMessage("测试测试！");
        return resultDTO;
    }

    public static ResultDTO NoLogin() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3007);
        resultDTO.setMessage("用户未登录！");
        return resultDTO;
    }

    public static ResultDTO NoSupportAccount() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3008);
        resultDTO.setMessage("不支持的账户！");
        return resultDTO;
    }

    public static ResultDTO OldPwdError() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3009);
        resultDTO.setMessage("旧密码错误！");
        return resultDTO;
    }

    public static ResultDTO NoSuchAccount() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3010);
        resultDTO.setMessage("用户名不存在！");
        return resultDTO;
    }

    public static ResultDTO NoEmail() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3011);
        resultDTO.setMessage("抱歉呢，该用户没有邮箱...请尝试其他方法");
        return resultDTO;
    }

    public static ResultDTO NoVerifyCode() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(3011);
        resultDTO.setMessage("没有邮箱验证！");
        return resultDTO;
    }
}
