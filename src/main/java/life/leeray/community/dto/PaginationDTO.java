package life.leeray.community.dto;

import life.leeray.community.dto.QuestionDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/25 0025 15:48
 */
@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirst;
    private boolean showNext;
    private boolean showEnd;

    private Integer page;
    private List<Integer> pages = new ArrayList();

    private Integer totalPage;

    public void setPagination(Integer count, Integer page, Integer size) {
        this.totalPage = count % size == 0 ? count / size : count / size + 1;
        this.page = page;
        if (count != 0) {
            pages.add(page);//先把当前页加入
            for (int i = 1; i <= 3; i++) {
                if (page - i > 0) {
                    pages.add(0, page - i);//每次从头插入
                }
                if (page + i <= totalPage) {
                    pages.add(page + i);
                }
            }
            if (page == 1) {
                this.showPrevious = false;
            } else {
                this.showPrevious = true;
            }
            if (page == totalPage) {
                this.showNext = false;
            } else {
                this.showNext = true;
            }
            //是否展示第一页
            if (pages.contains(1)) {
                this.showFirst = false;
            } else {
                this.showFirst = true;
            }
            //是否展示最后一页
            if (pages.contains(totalPage)) {
                this.showEnd = false;
            } else {
                this.showEnd = true;
            }
        }
    }
}
