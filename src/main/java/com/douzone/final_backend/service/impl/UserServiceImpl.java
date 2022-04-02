package com.douzone.final_backend.service.impl;

import com.douzone.final_backend.vo.GoodsVO;
import com.douzone.final_backend.vo.OwnerVO;
import com.douzone.final_backend.vo.ReserveVO;
import com.douzone.final_backend.vo.UserVO;
import com.douzone.final_backend.DAO.MasterDAO;
import com.douzone.final_backend.DAO.OwnerDAO;
import com.douzone.final_backend.DAO.UserDAO;
import com.douzone.final_backend.DTO.FavoritesDTO;
import com.douzone.final_backend.DTO.ReserveDTO;
import com.douzone.final_backend.DTO.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements com.douzone.final_backend.service.UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OwnerDAO ownerDAO;

    @Autowired
    private MasterDAO masterDAO;

    @Override
    public UserVO create(final UserVO userVO) {
        if (userVO == null || userVO.getU_id() == null || userVO.getU_pw() == null || userVO.getU_cellPhone() == null || userVO.getU_email() == null || userVO.getU_gender() == null || userVO.getU_age() == 0) {
            log.warn("데이터 누락");
            throw new RuntimeException("필수 데이터가 누락되었습니다.");
        }
        final String u_id = userVO.getU_id();

        if (userDAO.existsById(u_id)||ownerDAO.existsBySNum(u_id)||u_id.equals("master")) {
            log.warn("이미 존재하는 아이디");
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        userDAO.insertUser(userVO);

        return userVO;
    }

    // 로그인 시 id로 정보 들고와 입력된 비밀번호화 암호화된 비밀번호 match
    @Override
    public UserVO getByCredentials(final String u_id, final String u_pw, final PasswordEncoder encoder) {
        log.info("login service");
        if(u_id == null || u_pw == null){
            throw new RuntimeException("필수 데이터가 누락되었습니다.");
        }
        final UserVO originalUser = userDAO.findByUId(u_id);
        log.info("originalUser" + originalUser);

        if (originalUser == null) {
            log.info("아이디가 존재하지 않습니다.");
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        } else if (encoder.matches(u_pw, originalUser.getU_pw()) == false) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        return originalUser;

    }

    // 비밀번호 찾기 했을 때 유저 정보 가져오기 .
    @Override
    public UserVO findById(String u_id) {

        if (userDAO.findByUId(u_id) == null) {
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }

        return userDAO.findByUId(u_id);
    }

    // 가게 눌렀을 때 해당 가게의 판매중인 상품 보기
    @Override
    public List<GoodsVO> storeGoodsView(String o_sNumber) {
        return userDAO.storeGoodsView(o_sNumber);
    }

    // 해당 가게 정보 상세보기
    @Override
    public OwnerVO findByStore(String o_sNumber) {
        return userDAO.findByStore(o_sNumber);
    }

    // 즐겨찾기 유무 체크 하기
    @Override
    public boolean favorCheck(FavoritesDTO favoritesDTO) {
        return userDAO.favorCheck(favoritesDTO);
    }

    // 유저 즐겨찾기 추가 하기
    @Override
    public int addFavorService(FavoritesDTO favoritesDTO) {
        int r = userDAO.addFavorDAO(favoritesDTO);
        if (r == 0) {
            throw new RuntimeException("즐겨찾기 추가 실패하였습니다.");
        }
        return r;
    }

    // 유저 즐겨찾기 해제
    @Override
    public int FavorOffService(FavoritesDTO favoritesDTO) {
        int r = userDAO.FavorOffDAO(favoritesDTO);
        if (r == 0) {
            throw new RuntimeException("즐겨찾기 해제 실패하였습니다.");
        }
        return r;
    }

    // 유저 회원정보
    @Override
    public UserVO userData(String u_id) {

        if (userDAO.userData(u_id) == null) {
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }
        return userDAO.userData(u_id);
    }

    @Override
    public int userSave(String u_id) {
        return userDAO.userSave(u_id);
    }

    @Override
    public int userReserve(String u_id) {
        return userDAO.userReserve(u_id);
    }

    @Override
    public List<ReserveDTO> reserveList(ReserveDTO dto) {
        return userDAO.reserveList(dto);
    }

    @Override
    public ReserveVO getReserve(ReserveDTO reserveDTO) {
        return userDAO.getReserve(reserveDTO);
    }

    @Override
    @Transactional
    public void changeReserveStatus(ReserveDTO responseDTO) {
        if (responseDTO.getR_status() == 1 || responseDTO.getR_status() == 0) {
            // 예약상태 5 취소로 변경
            int r = userDAO.changeReserveStatus(responseDTO);
            // goods에 남은 수량 더하기
            int r1 = ownerDAO.resNoCount(responseDTO);
            // goods 수량 확인하고 판매중으로 변경
            int r2 = ownerDAO.reNoSt(responseDTO);

            if (r == 0 || r1 == 0 || r2 == 0) {
                throw new RuntimeException("예약 취소 실패하였습니다.");
            }
        } else {
            throw new RuntimeException("예약 승인대기/승인 상태에서만 취소할 수 있습니다.");
        }
    }

    @Override
    public List<ReserveDTO> searchReserve(ReserveDTO r) {
        // 상태가 입력안됐을 때
        if (r.getR_status() == 9999) {
            return userDAO.searchReserve(r);
        } else { // 상태 입력 됐을때
            return userDAO.userSearchReserve(r);
        }
    }

    @Override
    public List<FavoritesDTO> favorList(FavoritesDTO dto) {
        return userDAO.favorList(dto);
    }

    @Override
    @Transactional
    public void insertReserve(ReserveDTO reserve) {
        log.info("insertReserve 안");
        // 해당가게 상태가 1 또는 3일 때만 예약가능.
        OwnerVO owner = ownerDAO.findBySNum(reserve.getR_owner());
        if (owner == null) {
            throw new RuntimeException("현재 예약이 불가능한 가게입니다.");
        }
        // 방문요청 한 시간이 영업시간인지 확인.


        int r = userDAO.insertReserve(reserve); // 데이터 삽입
        log.info("insert" + r);

        int r1 = userDAO.updateGoodsCount(reserve); // goods 테이블 상품수 빼기. 수량이 0이상이여야함
        log.info("update1" + r1);

        int r2 = userDAO.updateGoodsStatus(reserve); //수량 확인하고 수량 0이면 판매완료로 바꾸기

        if (r != 1 || r1 != 1 || r2 != 1) {
            throw new RuntimeException("예약 데이터 삽입 또는 수량 줄이기 실패하였습니다.");
        }
        log.info("삽입/상품수량빼기/상태 변화" + r + "/" + r1);
    }

    @Override
    public int noShowCount(String u_id) {
        return userDAO.noShowCount(u_id);
    }

    // 회원정보 업데이트
    @Override
    public int update(final UserVO userVO) {
        if (userVO == null || userVO.getU_id() == null) {
            log.warn("데이터 누락");
            throw new RuntimeException("필수 데이터가 데이터 누락되었습니다.");
        }
        return userDAO.updateUser(userVO);
    }


    @Override
    public List<String> getOwnerPushToken(int r_g_code) {
        return userDAO.getOwnerPushToken(r_g_code);
    }

    @Override
    public int userDelete(String u_id) {
        return userDAO.userDelete(u_id);
    }


    @Override
    public UserDTO changePWcheck(UserDTO userDTO) {
        // common mapper에 있음
        return userDAO.changePWcheck(userDTO);
    }

    @Override
    public int pwUpdate(UserVO user) {
        if (userDAO.pwUpdate(user) == 0) {
            throw new RuntimeException("비밀번호 변경 실패하였습니다.");
        }
        return userDAO.pwUpdate(user);
    }
}
