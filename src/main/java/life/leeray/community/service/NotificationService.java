package life.leeray.community.service;

import life.leeray.community.dto.NotificationDTO;
import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.enums.NotificationStatusEnum;
import life.leeray.community.enums.NotificationTypeEnum;
import life.leeray.community.exception.CustomizeErrorCode;
import life.leeray.community.exception.CustomizeException;
import life.leeray.community.mapper.NotificationMapper;
import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.Notification;
import life.leeray.community.model.NotificationExample;
import life.leeray.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/1 0001 16:40
 */
@Service
public class NotificationService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    public static Integer validator(Integer page, Integer size, Integer count) {
        //边界验证，容错处理
        Integer totalPage = count % size == 0 ? count / size : count / size + 1;
        page = page < 1 ? 1 : page;
        page = page > totalPage ? totalPage : page;
        return page;
    }

    public PaginationDTO list(Long id, Integer page, Integer size) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        Integer count = notificationMapper.countByExample(notificationExample);
        page = validator(page, size, count);
        //计算用于limit m,n所需的m值
        Integer offset = size * (page - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id);
        example.setOrderByClause("gmt_create DESC");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        //未读的消息总是排前面，如果未读状态相同，那才按时间顺序倒序排。使用lambda表达式。
        Collections.sort(notifications, (o1, o2) -> {
            if (o1.getStatus() == 0 && o2.getStatus() == 1) {
                return -1;
            } else if (o1.getStatus() == 1 && o2.getStatus() == 0) {
                return 1;
            } else {
                return (int) (o2.getGmtCreate() - o1.getGmtCreate());
            }
        });
        PaginationDTO paginationDTO = new PaginationDTO();

        if (notifications == null || notifications.size() == 0) {
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList();
        /*for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }*/
        notifications.stream().forEach(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        });
        paginationDTO.setData(notificationDTOS);
        paginationDTO.setPagination(count, page, size);
        return paginationDTO;
    }

    public int unreadCount(Long id) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!notification.getReceiver().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

    /**
     * 删除通知的方法
     *
     * @param id 通知的id
     * @param user
     */
    public void delete(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //如果通知确实存在并且通知确实是当前用户的，那么删除它。
        if (notification != null && notification.getReceiver().equals(user.getId())){
            notificationMapper.deleteByPrimaryKey(id);
        }
    }
}
