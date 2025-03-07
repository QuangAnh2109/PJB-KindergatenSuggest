package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/admin/api")
public class UserManagementRestController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

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


    @PostMapping("/save-user")
    public ResponseEntity<?> saveUser(@RequestBody @Valid AccountVo accountVo, BindingResult result, Principal principal) {
        Map<String, String> errors = new HashMap<>();

        boolean isAdding = accountVo.getId() == null ; // Kiểm tra thêm mới hay chỉnh sửa

        Log4jUtils.getLogger().info("AccountID :" + accountVo.getId());
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                // Bỏ qua kiểm tra password và confirmPassword
                if (!error.getField().equals("password") && !error.getField().equals("confirmPassword")) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            });

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }
        }

        try {
            if (isAdding) {
                String randomPassword = UUID.randomUUID().toString();
                accountVo.setPassword(randomPassword);
                accountVo.setConfirmPassword(randomPassword);
                accountService.addUserFromAdmin(accountVo);

                // Gửi email
                emailService.sendEmailToMany(SendMailInfo.builder()
                        .toMail(List.of(accountVo.getEmail()))
                        .ccMail(List.of())
                        .mailId(2)
                        .detail(Map.of(Placeholder.USER_NAME, accountVo.getEmail(),
                                Placeholder.EMAIL, accountVo.getEmail(),
                                Placeholder.PASSWORD, randomPassword,
                                Placeholder.OWNER_ACCOUNT, accountService.getAccountInfo(principal).getFullName()))
                        .build());
            } else {
                accountService.updateUser(accountVo);
            }

            return ResponseEntity.ok(Map.of("message", isAdding ? "User added successfully." : "User updated successfully."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred: " + e.getMessage()));
        }
    }
}