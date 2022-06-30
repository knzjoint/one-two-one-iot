package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.service.IEMQXClientServer;
import io.github.gcdd1993.emqx.sdk.http.api.EmqxClientApi;
import io.github.gcdd1993.emqx.sdk.http.api.EmqxSubscriptionApi;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/15 17:06
 */

public class EMQXClientServerImpl implements IEMQXClientServer {

    @Autowired
    private EmqxClientApi emqxClientApi;

    @Autowired
    private EmqxSubscriptionApi emqxSubscriptionApi;

}
