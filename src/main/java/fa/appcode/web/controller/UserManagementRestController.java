package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.AccountService;

import fa.appcode.services.ValidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin/api")
public class UserManagementRestController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private GlobalConfig globalConfig;

    /**
     * Delete account
     *
     * @param userId
     * @return ResponseEntity<String>
     */
    @GetMapping(value = "/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {

        Log4jUtils.getLogger().info("Received request to delete user with ID: {}", userId);
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

        Log4jUtils.getLogger().info("Received request to save user: {}", accountVo);

        Map<String, String> errors = new HashMap<>();
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                if (!error.getField().equals("password") && !error.getField().equals("confirmPassword")) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            });
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        boolean isAdding = accountVo.getId() == null;
        Log4jUtils.getLogger().info("Is new user: {}", isAdding);

        try {
            if (isAdding) {
                accountService.addUserFromAdmin(accountVo, principal);
                return ResponseEntity.ok(Map.of("message", globalConfig.getUserAddSucess()));
            } else {
                int newRecordNo = accountService.updateAccount(accountVo);
                return ResponseEntity.ok(Map.of(
                        "message", globalConfig.getUserUpdateSucess(),
                        "recordNo", newRecordNo
                ));
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }

    }
}