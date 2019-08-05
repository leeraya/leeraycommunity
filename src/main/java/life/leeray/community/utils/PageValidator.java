package life.leeray.community.utils;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/5 0005 20:05
 */
public class PageValidator {
    public static Integer validator(Integer page, Integer size, Integer count) {
        //边界验证，容错处理
        Integer totalPage = count % size == 0 ? count / size : count / size + 1;
        page = page < 1 ? 1 : page;
        page = page > totalPage ? totalPage : page;
        return page;
    }
}
