package life.leeray.community.enums;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/28 0028 15:33
 */
public enum ContentTypeEnum {
    QUESTION(1),
    CONMENT(2);
    private Integer type;

    public static boolean isExits(Integer type) {
        for (ContentTypeEnum typeEnum : ContentTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    ContentTypeEnum(Integer type) {
        this.type = type;
    }
}
