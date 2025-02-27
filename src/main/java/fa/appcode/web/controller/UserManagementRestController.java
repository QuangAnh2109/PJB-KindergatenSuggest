package fa.appcode.web.controller;

import fa.appcode.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/api")
public class UserManagementRestController {

    @Autowired
    private AccountService accountService;

    // Delete logic user account (set deleteFlg = true)
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        try {
            accountService.deleteAccount(userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}