package cn.makerknz.product.server.utils;

/**
 * 物联网主题工具类
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/15 8:12
 */

public class TopicUtils {

    static public String replaceToDevice(String topic) {
        return topic.replaceAll("%c", "{device}");
    }

    static public String replaceToDevice(String topic, String device) {
        return topic.replaceAll("%c", device);
    }

    static public String subTopic(String productEnName) {
        return "/" + productEnName + "/%c/command/#";
    }

    static public String pubTopic(String productEnName) {
        return "/" + productEnName + "/%c/sensor/+/+/#";
    }

    static public String thingsTopic(String productEnName) {
        return "/" + productEnName + "/%c/things/#";
    }

    static public String createTopic(String productEnName, String topic) {
        return "/" + productEnName + "/%c/" + topic;
    }

}
