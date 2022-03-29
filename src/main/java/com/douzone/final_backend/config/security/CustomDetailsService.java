package com.douzone.final_backend.config.security;

import com.douzone.final_backend.vo.UserVO;
import com.douzone.final_backend.DAO.MasterDAO;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DAO.UserDAO;
import com.douzone.final_backend.DTO.MasterDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomDetailsService implements UserDetailsService {

    private final UserDAO userDAO;
    private final MasterDAO masterDAO;
    private final OwnerDAO ownerDAO;


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        log.info("CustomDetailServie 들어옴 !@!@!@!");
        SecurityUser securityUser = new SecurityUser();

        if(userDAO.findByUId(id) != null){
            UserVO result = userDAO.findByUId(id);

            securityUser.setId(result.getU_id());
            securityUser.setPw(result.getU_pw());

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            securityUser.setAuthorities(authorities);
            return securityUser;
        }else if(ownerDAO.findByOwner(id)!= null) {
            OwnerDTO result = ownerDAO.findByOwner(id);

            securityUser.setId(result.getO_sNumber());
            securityUser.setPw(result.getO_pw());

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));

            securityUser.setAuthorities(authorities);

            log.info("authorities : " + authorities);

            return securityUser;
        } else{
            MasterDTO result = masterDAO.findByMaster(id);

            securityUser.setId(result.getM_id());
            securityUser.setPw(result.getM_pw());

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_MASTER"));

            securityUser.setAuthorities(authorities);

            log.info("authorities : " + authorities);
            log.info("security : " + securityUser.getAuthorities());

            return securityUser;
        }
    }


}
