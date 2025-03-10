package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.ValidateUtils;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.services.AccountService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/api")
public class UserManagementRestController {
    @Autowired
    private AccountService accountService;

    /**
     * Delete account
     *
     * @param userId
     * @return ResponseEntity<String>
     */
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        accountService.deleteAccount(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Edit or Add user account
     *
     * @param accountVo
     * @param principal
     * @param result
     * @return
     */
    @PostMapping("/save-user")
    public ResponseEntity<?> saveUser(@RequestBody @Valid AccountVo accountVo, BindingResult result, Principal principal) throws Exception {
        Map<String, String> errors = new HashMap<>();

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                if (!error.getField().equals("password") && !error.getField().equals("confirmPassword")) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            });
        }
            if (!ValidateUtils.isValidFullName(accountVo.getFullName())) {
                errors.put("fullName", "Full name cannot be empty");
            }
            if (!ValidateUtils.isValidEmail(accountVo.getEmail())) {
                errors.put("email", "Invalid email format");
            }
            if (!ValidateUtils.isValidDob(LocalDate.parse(accountVo.getDob()))) {
                errors.put("dob", "Date of birth must be in the past");
            }
            if (!ValidateUtils.validatePhone(accountVo.getPhone())) {
                errors.put("phone", "Invalid phone number format");
            }
            if (!ValidateUtils.isValidRole(accountVo.getRole())) {
                errors.put("role", "Role cannot be empty");
            }
            if (!ValidateUtils.isValidStatus(accountVo.getStatus())) {
                errors.put("status", "Status cannot be empty");
            }

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }

        Log4jUtils.getLogger().info("AccountID :" + accountVo.getId());

        boolean isAdding = accountVo.getId() == null;

        try {
            if (isAdding) {
                accountService.addUserFromAdmin(accountVo, principal);
                return ResponseEntity.ok(Map.of("message", "User added successfully."));
            } else {
                int newRecordNo = accountService.updateAccount(accountVo); // Trả về recordNo mới
                return ResponseEntity.ok(Map.of(
                        "message", "User updated successfully.",
                        "recordNo", newRecordNo // Trả về recordNo mới
                ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }

    }
}