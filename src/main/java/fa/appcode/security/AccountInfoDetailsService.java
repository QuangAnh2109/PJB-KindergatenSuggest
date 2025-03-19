package fa.appcode.security;

import fa.appcode.config.GlobalConfig;
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
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new UsernameNotFoundException("Not Found"));
        List<String> roles = accountRepository.findRolesByEmail(email);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new CustomUserDetails(account, authorities);
    }
}
