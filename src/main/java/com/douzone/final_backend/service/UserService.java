package com.douzone.final_backend.service;

import com.douzone.final_backend.vo.GoodsVO;
import com.douzone.final_backend.vo.OwnerVO;
import com.douzone.final_backend.vo.ReserveVO;
import com.douzone.final_backend.vo.UserVO;
import com.douzone.final_backend.DTO.FavoritesDTO;
import com.douzone.final_backend.DTO.ReserveDTO;
import com.douzone.final_backend.DTO.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    UserVO create(UserVO userVO);

    // 로그인 시 id로 정보 들고와 입력된 비밀번호화 암호화된 비밀번호 match
    UserVO getByCredentials(String u_id, String u_pw, PasswordEncoder encoder);

    // 비밀번호 찾기 했을 때 유저 정보 가져오기 .
    UserVO findById(String u_id);

    // 가게 눌렀을 때 해당 가게의 판매중인 상품 보기
    List<GoodsVO> storeGoodsView(String o_sNumber);

    // 해당 가게 정보 상세보기
    OwnerVO findByStore(String o_sNumber);

    // 즐겨찾기 유무 체크 하기
    boolean favorCheck(FavoritesDTO favoritesDTO);

    // 유저 즐겨찾기 추가 하기
    int addFavorService(FavoritesDTO favoritesDTO);

    // 유저 즐겨찾기 해제
    int FavorOffService(FavoritesDTO favoritesDTO);

    // 유저 회원정보
    UserVO userData(String u_id);

    int userSave(String u_id);

    int userReserve(String u_id);

    List<ReserveDTO> reserveList(ReserveDTO dto);

    ReserveVO getReserve(ReserveDTO reserveDTO);

    @Transactional
    void changeReserveStatus(ReserveDTO responseDTO);

    List<ReserveDTO> searchReserve(ReserveDTO r);

    List<FavoritesDTO> favorList(FavoritesDTO dto);

    @Transactional
    void insertReserve(ReserveDTO reserve);

    int noShowCount(String u_id);

    // 회원정보 업데이트
    int update(UserVO userVO);

    List<String> getOwnerPushToken(int r_g_code);

    int userDelete(String u_id);

    UserDTO changePWcheck(UserDTO userDTO);

    int pwUpdate(UserVO user);
}
