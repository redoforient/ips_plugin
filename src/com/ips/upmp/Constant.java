package com.ips.upmp;

/**
 * Created in July 20, 2016 9:25:18 AM.
 *
 * @author Zhang Leilei.
 */
public class Constant {
    /**
     *RELEASE_MODE:是否为发布模式
     *true:连接生产环境(银联组件为生产模式)
     *false:连接非生产环境(银联组件为生产模式)
     */
    //public static final boolean RELEASE_MODE = true;
     public static final boolean RELEASE_MODE = false;

    // 银联环境设置
    public static final String mMode;
    // 服务器地址
    public static final String SERVER_URL;
    static {
        if (RELEASE_MODE) {
            // "00" - 启动银联正式环境(生产)
            mMode = "00";
            // 生产环境地址
            SERVER_URL = "https://mobilegw.ips.com.cn/psfp-mgw/mgw.htm";
        } else {
            // "01" - 连接银联测试环境
            mMode = "01";

            // 内网环境 sit测试环境
            // SERVER_URL ="http://192.168.12.62:8006/psfp-mgw/mgw.htm";
            // 开发环境
            // SERVER_URL = "http://192.168.12.68:8001/psfp-mgw/mgw.htm";
            // 陈苗苗本机
            // SERVER_URL = "http://192.168.12.113:8080/psfp-mgw/mgw.htm";
            // 预测试地址
            // SERVER_URL = "http://192.168.23.35:8002/psfp-mgw/mgw.htm";

            // 外网可访问(映射内网地址为：192.168.24.12:8002)
            SERVER_URL = "http://180.168.26.114:20012/psfp-mgw/mgw.htm";
        }
    }

    /**
     * 结果码定义
     * 
     * @author IH847
     */
    public static final class ResultCode {
        public static final String 操作成功 = "000000";
        public static final String 操作类型不存在 = "000001";
        public static final String 登录超时 = "200000";// 登录超时，需重新登录
        public static final String 操作失败 = "999999";
    }

    /**
     * 操作类型定义
     * 
     * @author IH847
     *
     */
    public static final class OperationType {
        public static final String 移动无卡支付 = "ips.mobile.trade.mPay";// 移动无卡支付
    }

}
