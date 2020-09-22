package com.offcn.user.controller;

import com.offcn.user.po.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "helloController 控制类")
public class HelloController {

    @ApiOperation("测试方法hello")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="name",value = "姓名",required = true),
            @ApiImplicitParam(name="age",value="年龄")
    })
    @GetMapping("/hello")
    public String hello(String name,int age){
        return "  OK : " + name + "  -- 年龄 ： " + age;
    }

    @ApiOperation("保存方法")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="name",value = "姓名",required = true),
            @ApiImplicitParam(name="email",value="邮箱")
    })
    @PostMapping("/save")
    public User save(String name,String email){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

}
