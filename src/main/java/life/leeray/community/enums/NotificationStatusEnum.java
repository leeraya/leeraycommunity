package life.leeray.community.enums;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/1 0001 15:37
 */
public enum  NotificationStatusEnum {
    UNREAD(0), READ(1);
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
