package com.zhuxiaoxue.controller;

import com.zhuxiaoxue.dto.FlashMessage;
import com.zhuxiaoxue.pojo.UserLog;
import com.zhuxiaoxue.service.UserService;
import com.zhuxiaoxue.util.ServletUtil;
import com.zhuxiaoxue.util.ShiroUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Inject
    private UserService userService;

    /**
     * 到登录界面
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "/login";
    }

    /**
     * 登录验证用户名和密码是否正确
     * @param username
     * @param password
     * @param redirectAttributes
     * @param request
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String login(String username, String password,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {

        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, DigestUtils.md5Hex(password));
            subject.login(usernamePasswordToken);

            userService.saveUserLogin(ServletUtil.getRemoteIp(request));
            return "redirect:/home";
        } catch (LockedAccountException ex) {
            redirectAttributes.addFlashAttribute("message", new FlashMessage(FlashMessage.STATE_ERROR,"账号已被禁用"));
        } catch (AuthenticationException exception) {
            redirectAttributes.addFlashAttribute("message", new FlashMessage(FlashMessage.STATE_ERROR,"账号或密码错误"));
        }
        return "redirect:/";
    }

    /**
     * 重定向到home
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home() {
        return "/home";
    }

    /**
     * 安全退出
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes) {
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message",new FlashMessage(FlashMessage.STATE_SUCCESS,"安全退出"));
        return "redirect:/";
    }

    /**
     * 添加403错误页
     * @return
     */
    @RequestMapping(value = "/403",method = RequestMethod.GET)
    public String error403(){
        return "error/403";
    }
}
