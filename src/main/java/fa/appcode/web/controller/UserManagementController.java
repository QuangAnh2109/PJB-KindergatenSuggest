package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManagementController {
    @GetMapping("/admin/userlist")
    public String admin_userManagement() {return "admin_side/UserList";}
    @GetMapping("/admin/adduser")
    public String admin_addUser() {return "admin_side/AddUser";}
    @GetMapping("/admin/edituser")
    public String admin_editUser() {return "admin_side/EditUser";}
    @GetMapping("/admin/userdetail")
    public String admin_userDetail() {return "admin_side/UserDetails";}
}
