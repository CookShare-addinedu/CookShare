package com.example.foodshare.security.dto;

import com.example.foodshare.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 여기서는 User 엔티티의 getAuthorities 메소드가 필요합니다.
        // User 클래스에 그런 메소드가 없다면, 적절한 권한을 반환하도록 수정해야 합니다.
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                    return user.getRole();
                }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getMobileNumber(); // 또는 user.getUsername() 사용자 이름을 반환하는 것으로 가정
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정이 만료되었는지 여부를 반환합니다.
        // 만료 기능을 사용하지 않는 경우 true를 반환합니다.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠겨 있는지 여부를 반환합니다.
        // 잠금 기능을 사용하지 않는 경우 true를 반환합니다.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명(비밀번호)이 만료되었는지 여부를 반환합니다.
        // 만료 기능을 사용하지 않는 경우 true를 반환합니다.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정이 활성화(사용 가능) 상태인지 여부를 반환합니다.
        // 사용 가능 상태를 나타내려면 true를 반환합니다.
        //return user.isActive(); // User 클래스에 isActive() 메서드가 있다고 가정
        return true;
    }
}
