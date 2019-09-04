package com.accenture.huaweigroup.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api(value = "测试接口", tags = "测试调用接口用接口")
public class TestController {

}
