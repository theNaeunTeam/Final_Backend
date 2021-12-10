package com.douzone.final_backend.security;

import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DAO.MasterDAO;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DAO.UserDAO;
import com.douzone.final_backend.DTO.MasterDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {


        log.info(s);
        String id = s.substring(0, s.indexOf("&"));
        String role = s.substring(s.indexOf("&") + 1);

        log.info(s.substring(0, s.indexOf("&")));
        log.info(s.substring(s.indexOf("&") + 1));

        log.info("CustomDetailServie 들어옴 !@!@!@!");
        SecurityUser securityUser = new SecurityUser();
        if (role.equals("USER")) {
            UserBean result = userDAO.findByUId(id);
            if (result == null) throw new BadCredentialsException("UserID not Found");

            securityUser.setId(result.getU_id());
            securityUser.setPw(result.getU_pw());

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            log.info("authorities : " + authorities);

            securityUser.setAuthorities(authorities);

            log.info("security : " + securityUser.getAuthorities());

            return securityUser;
        } else if (role.equals("MASTER")) {
            MasterDTO result = masterDAO.findByMaster(id);
            if (result == null) throw new BadCredentialsException("MasterID not Found");

            securityUser.setId(result.getM_id());
            securityUser.setPw(result.getM_pw());

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_MASTER"));

            securityUser.setAuthorities(authorities);

            log.info("authorities : " + authorities);
            log.info("security : " + securityUser.getAuthorities());

            return securityUser;
        } else if (role.equals("OWNER")) {
            OwnerDTO result = ownerDAO.findByOwner(id);
            if (result == null) throw new BadCredentialsException("OwnerID not Found");

            securityUser.setId(result.getO_sNumber());
            securityUser.setPw(result.getO_pw());

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));

            securityUser.setAuthorities(authorities);

            log.info("authorities : " + authorities);

            return securityUser;
        } else {
            return null;
        }
    }


}
