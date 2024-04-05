package com.example.foodshare.dto;

import com.example.foodshare.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

public class CustomUserDetails implements UserDetails {

    private  final User user;


    public CustomUserDetails(User user) {
        this.user = user;
    }


    //role 값 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //role이 여러개가 있는데 기존의 컬렉션을 어레이리스트로 만들어서 반환
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority(){
                return user.getRole(); //반드시 이용해서 해줘야 한다. getRole

            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    //계정이 만료되지 않았는지(true: 만료되지 않음을 의미)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있지 않은지 확인 (true: 잠겨있지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정의 패스워드가 만료되지 않았는지(true: 패스워드가 마뇨되지 않음을 의미)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //사용 가능한 계정인지 (true : 사용가닁한 계정을 의미)
    @Override
    public boolean isEnabled() {
        return true;
    }
    //필터를 가지고 재정의 한다

}










































