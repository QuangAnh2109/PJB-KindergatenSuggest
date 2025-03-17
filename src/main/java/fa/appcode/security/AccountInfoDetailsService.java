package fa.appcode.security;

import fa.appcode.entities.AccountInfo;
import fa.appcode.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountInfoDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * Loads user details by email from the database.
     *
     * @param email The email of the user.
     * @return UserDetails object containing user credentials and roles.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        AccountInfo account = accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        // Assigns the role to the user based on their role ID.
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(mapRole(account.getRoleId()))
        );

        return new CustomUserDetails(account, authorities);
    }

    /**
     * Maps role ID to a specific role name.
     *
     * @param roleId The ID representing the user's role.
     * @return The corresponding role name as a string.
     */
    private String mapRole(int roleId) {
        switch (roleId) {
            case 1:
                return "Admin";
            case 2:
                return "School owner";
            case 3:
                return "Parent";
            default:
                return "Parent";
        }
    }
}
