package com.alibaba.tesla.appmanager.dynamicscript.controller;

import com.alibaba.tesla.appmanager.common.BaseRequest;
import com.alibaba.tesla.appmanager.dynamicscript.service.DynamicScriptService;
import com.alibaba.tesla.common.base.TeslaBaseResult;
import com.alibaba.tesla.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dynamic-script")
@Tag(name = "Dynamic Script")
public class DynamicScriptController extends BaseController {

    @Autowired
    private DynamicScriptService dynamicScriptService;

    @GetMapping
    @Operation(summary = "查询 Dynamic Script 列表")
    public TeslaBaseResult list(
            @ParameterObject @ModelAttribute BaseRequest request) {
        return buildSucceedResult(dynamicScriptService.list(request));
    }
}
