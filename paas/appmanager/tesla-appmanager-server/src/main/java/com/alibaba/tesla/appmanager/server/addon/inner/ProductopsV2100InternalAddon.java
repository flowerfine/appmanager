package com.alibaba.tesla.appmanager.server.addon.inner;

import com.alibaba.tesla.appmanager.common.enums.ComponentTypeEnum;
import com.alibaba.tesla.appmanager.common.util.AddonUtil;
import com.alibaba.tesla.appmanager.domain.schema.ComponentSchema;
import com.alibaba.tesla.appmanager.server.addon.BaseAddon;
import com.alibaba.tesla.appmanager.server.event.loader.AddonLoadedEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ProductOps V2 Addon
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
@Slf4j
@Component("ProductopsV2100InternalAddon")
public class ProductopsV2100InternalAddon extends BaseAddon {

    @Getter
    private final String addonType = ComponentTypeEnum.INTERNAL_ADDON.toString();

    @Getter
    private final String addonId = "productopsv2";

    @Getter
    private final String addonVersion = "1.0.0";

    @Getter
    private final String addonLabel = "Internal-ProductOps V2 Addon";

    @Getter
    private final String addonDescription = "Internal-ProductOps V2 Addon";

    @Getter
    private final ComponentSchema addonSchema = new ComponentSchema();

    @Getter
    private final String addonConfigSchema = null;

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 初始化，注册自身
     */
    @PostConstruct
    public void init() {
        publisher.publishEvent(new AddonLoadedEvent(
                this, AddonUtil.combineAddonKey(getAddonType(), getAddonId()), this.getClass().getSimpleName()));
    }
}
