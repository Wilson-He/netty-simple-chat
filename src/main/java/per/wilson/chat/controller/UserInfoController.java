package per.wilson.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.wilson.chat.service.UserInfoService;
import per.wilson.chat.vo.AddUserVO;

import javax.annotation.Resource;

/**
 * @author Wilson
 * @date 2019/9/5
 **/
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/")
    public ResponseEntity register(@RequestBody AddUserVO vo) {
        userInfoService.register(vo);
        return ResponseEntity.ok("success");
    }
}
