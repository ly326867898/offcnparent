package com.offcn.user.controller;

import com.offcn.dycommon.enums.AppResponse;
import com.offcn.user.component.SmsTemplate;
import com.offcn.user.po.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Api(tags = "用户登录/注册模块（包括忘记密码等）")
public class UserLoginController {



    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserService userService;

    Logger log = LoggerFactory.getLogger(getClass());

    @ApiOperation("用户注册")
    @PostMapping("/regist")
    public AppResponse<Object> regist(UserRespVo registVo) {
        //1、校验验证码
        String code = stringRedisTemplate.opsForValue().get(registVo.getLoginacct());
        if (!StringUtils.isEmpty(code)) {
            //redis中有验证码
            boolean b = code.equalsIgnoreCase(registVo.getCode());
            if (b) {
                //2、将vo转业务能用的数据对象
                TMember member = new TMember();
                BeanUtils.copyProperties(registVo, member);
                //3、将用户信息注册到数据库
                try {
                    userService.registeUser(member);
                    log.debug("用户信息注册成功：{}", member.getLoginacct());
                    //4、注册成功后，删除验证码
                    stringRedisTemplate.delete(registVo.getLoginacct());
                    return AppResponse.ok("注册成功...");
                } catch (Exception e) {
                    log.error("用户信息注册失败：{}", member.getLoginacct());
                    return AppResponse.fail(e.getMessage());
                }
            } else {
                return AppResponse.fail("验证码错误");
            }
        } else {
            return AppResponse.fail("验证码过期，请重新获取");
        }
    }

    @ApiOperation("短信验证码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "phone",value = "手机号码",required = true)
    })
    @GetMapping("/sendCode")
    public AppResponse sendCode(String phone){

        //1、生成验证码
        String code = UUID.randomUUID().toString().substring(0, 4);

        System.out.println("code: "+code);

        //2、redis存短信验证码
        stringRedisTemplate.opsForValue().set(phone,code,60000, TimeUnit.MILLISECONDS);

        //3、发送短信
        Map<String,String> map = new HashMap<String,String>();
        map.put("mobile",phone);
        map.put("param","code:"+code);
        map.put("tpl_id","TP1711063");

        String str = smsTemplate.sendCode(map);

        if(str.equals("")||str.equals("fail")){
            return AppResponse.fail("短信发送失败");
        }
        return AppResponse.ok("短信发送成功");

    }

    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })//@ApiImplicitParams：描述所有参数；@ApiImplicitParam描述某个参数
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String username, String password) {

        //1、尝试登录
        TMember member = userService.login(username, password);
        if (member == null) {
            //登录失败
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMeg("用户名密码错误");
            return fail;
        }
        //2、登录成功;生成令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(member, vo);
        vo.setLoginacct(token);

        //3、经常根据令牌查询用户的id信息
        stringRedisTemplate.opsForValue().set(token,member.getId()+"",2,TimeUnit.HOURS);
        return AppResponse.ok(vo);
    }

    //根据用户编号获取用户信息
    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id){
        TMember tmember = userService.findTmemberById(id);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(tmember,userRespVo);

        return AppResponse.ok(userRespVo);
    }
}
