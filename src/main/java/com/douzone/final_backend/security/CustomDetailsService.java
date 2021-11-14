package com.douzone.final_backend.security;

import com.douzone.final_backend.Master.MasterDAO;
import com.douzone.final_backend.Master.MasterDTO;
import com.douzone.final_backend.Owner.OwnerDAO;
import com.douzone.final_backend.Owner.OwnerDTO;
import com.douzone.final_backend.User.UserDAO;
import com.douzone.final_backend.User.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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

        if (role.equals("USER")) {
            UserDTO result = userDAO.findByUIdDTO(id);
            if (result == null) throw new BadCredentialsException("UserID not Found");

            List<String> list = new ArrayList<>();
            list.add("ROLE_USER");
            result.setRoles(list);

            log.info("DetailsService : " + list);

            return result;
        } else if (role.equals("MASTER")) {
            MasterDTO result = masterDAO.findByMaster(id);
            if (result == null) throw new BadCredentialsException("MasterID not Found");
            List<String> list = new ArrayList<>();
            list.add("ROLE_MASTER");
            list.add("ROLE_OWNER");
            result.setRoles(list);
            return result;
        } else if (role.equals("OWNER")) {
            OwnerDTO result = ownerDAO.findByOwner(id);
            if (result == null) throw new BadCredentialsException("OwnerID not Found");
            List<String> list = new ArrayList<>();
            list.add("ROLE_OWNER");
            result.setRoles(list);
            return result;
        } else {
            return null;
        }
    }


}
