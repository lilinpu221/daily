package com.linzi.daily.satoken.base;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/satoken/base/")
public class LoginController {

    private static final Integer USER_ID = 10001;
    @RequestMapping("/doLogin")
    public SaResult doLogin(@RequestParam(name="username") String username, @RequestParam(name="password") String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(USER_ID);
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    @RequestMapping("/isLogin")
    public SaResult isLogin(@RequestParam(name="id") Integer id) {
        return SaResult.ok("用户是否登录：" + StpUtil.isLogin(id));
    }

    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout(USER_ID);
        return SaResult.ok();
    }
}
