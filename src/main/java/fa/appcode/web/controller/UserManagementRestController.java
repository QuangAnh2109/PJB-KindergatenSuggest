package fa.appcode.web.controller;

import fa.appcode.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
public class UserManagementRestController {

    private final AccountService accountService;

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


//    @PostMapping(value = "/edit-user", consumes = "application/json")
//    public ResponseEntity<Map<String, String>> updateUser(@RequestBody Map<String, String> userData) {
//
//        Integer id = Integer.parseInt(userData.get("id"));  // Lấy ID từ body
//        System.out.println("Received request to update user with ID: " + id);
//
//        Map<String, String> response = new HashMap<>();
//        try {
//            String fullName = userData.get("fullName");
//            String phone = userData.get("phone");
//            String dob = userData.get("dob");
//            Integer roleId = Integer.parseInt(userData.get("roleId"));
//
//            accountService.updateUser(id, fullName, phone, dob, roleId);
//            response.put("message", "Change has been successfully updated.");
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            response.put("error", "User not found.");
//            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//        } catch (NumberFormatException e) {
//            response.put("error", "Invalid role ID.");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            response.put("error", "An error occurred: " + e.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}