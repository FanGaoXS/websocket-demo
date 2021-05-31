package cn.wqk.serverwebsocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 吴青珂
 * @Date: 2021/05/31/13:16
 * @Description:
 */
@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping("/")
    public String index(){
        return "Hello,WebSocket!";
    }
}
