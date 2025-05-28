package com.alibaba.tesla.appmanager.api.provider;

import java.io.InputStream;

/**
 * Flow 管理服务
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
public interface FlowManagerProvider {

    void upgrade(InputStream inputStream, String operator);
}
