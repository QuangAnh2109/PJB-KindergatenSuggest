package fa.appcode.security;

import fa.appcode.entities.AccountInfo;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private final AccountInfo accountInfo;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(AccountInfo accountInfo, Collection<? extends GrantedAuthority> authorities) {
        this.accountInfo = accountInfo;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return accountInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return accountInfo.getEmail();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountInfo.getDeleteFlg();
    }

    @Override
    public boolean isEnabled() {
        return accountInfo.getStatusId() == 1;
    }
}
