package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.AccountService;

import fa.appcode.services.ValidateService;
import fa.appcode.services.impl.ValidateServiceImpl;
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
     * @return
     */
    @PostMapping("/save-user")
    public ResponseEntity<?> saveUser(@RequestBody  AccountVo accountVo, Principal principal) throws Exception {

        Log4jUtils.getLogger().info("Received request to save user: {}", accountVo);

        // Determine add or edit
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
            // check
            HttpStatus status = e.getMessage().contains(globalConfig.getInvalidRecordNo())
                    ? HttpStatus.CONFLICT  // 409: data has bên modified by another one
                    : HttpStatus.NOT_MODIFIED;  // 304: data has no change

            return ResponseEntity.status(status).body(Map.of("message", e.getMessage()));
        }

    }
}
