package com.douzone.final_backend.DAO;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.ReserveBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DTO.FavoritesDTO;
import com.douzone.final_backend.DTO.PushTokenDTO;
import com.douzone.final_backend.DTO.ReserveDTO;
import com.douzone.final_backend.DTO.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class UserDAO {
    @Autowired
    private SqlSession sqlSession;

    public List<UserBean> select() {
        return sqlSession.selectList("selectUserAll");
    }

    // 회원가입  시 아이디 존재여부 확인
    public boolean existsById(String u_id) {
        return sqlSession.selectOne("existsById", u_id);
    }

    // 회원가입 시 아이디 존재하지 않으면 데이터 insert
    public void insertUser(UserBean userBean) {
        sqlSession.insert("insertUser", userBean);
    }

    // 로그인 시 해당 아이디 값 들고옴 비밀번호 비교는 service에서
    public UserBean findByUId(String u_id) {
        return sqlSession.selectOne("findByUId", u_id);
    }

    // 가게 클릭 시 해당 가게 상품 중 판매중인 것만 반환
    public List<GoodsBean> storeGoodsView(String o_sNumber) {
        return sqlSession.selectList("storeGoodsView", o_sNumber);
    }

    // 가게 클릭 시 해당 가게 정보
    public OwnerBean findByStore(String o_sNumber) {
        return sqlSession.selectOne("storeView", o_sNumber);
    }

    // 즐겨찾기 유무 체크
    public boolean favorCheck(FavoritesDTO favoritesDTO) {
        System.out.println(sqlSession.selectOne("favorView", favoritesDTO) + "");
        return sqlSession.selectOne("favorView", favoritesDTO);
    }

    // 유저 즐겨찾기 추가
    public int addFavorDAO(FavoritesDTO favoritesDTO) {
        return sqlSession.insert("addFavor", favoritesDTO);
    }

    // 유저 즐겨찾기 해제
    public int FavorOffDAO(FavoritesDTO favoritesDTO) {
        return sqlSession.delete("favorOff", favoritesDTO);
    }

    public UserBean userData(String u_id) {
        return sqlSession.selectOne("userData", u_id);
    }

    public int userSave(String u_id) {
        return sqlSession.selectOne("userSave", u_id);
    }

    public int userReserve(String u_id) {
        return sqlSession.selectOne("userReserve", u_id);
    }

    public List<ReserveDTO> reserveList(ReserveDTO dto) {
        return sqlSession.selectList("userReserveList", dto);
    }

    public ReserveBean getReserve(ReserveDTO reserveDTO) {
        return sqlSession.selectOne("getReserve", reserveDTO);
    }

    public int changeReserveStatus(ReserveDTO responseDTO) {
        return sqlSession.update("changeReserveStatus", responseDTO);
    }

    // 예약 상태 입력 안됐을 때
    public List<ReserveDTO> searchReserve(ReserveDTO r) {
        return sqlSession.selectList("searchReserveU", r);
    }

    // 예약 상태 입력 됐을 때
    public List<ReserveDTO> userSearchReserve(ReserveDTO r) {
        return sqlSession.selectList("userSearchReserve", r);
    }

    public List<FavoritesDTO> favorList(FavoritesDTO dto) {
        return sqlSession.selectList("favorList", dto);
    }

    public int insertReserve(ReserveDTO reserve) {
        return sqlSession.insert("insertReserve", reserve);
    }

    public int updateGoodsCount(ReserveDTO reserve) {
        return sqlSession.update("updateGoodsCount", reserve);
    }

    public int updateGoodsStatus(ReserveDTO reserve) {
        return sqlSession.update("updateGoodsStatus", reserve);
    }

    public int noShowCount(String u_id) {
        return sqlSession.selectOne("noShowCount", u_id);
    }

    // 유저정보 업데이트
    public int updateUser(UserBean userBean) {
        return sqlSession.update("updateUser", userBean);
    }

    // 유저 정보 삭제
    public int userDelete(final String u_id) {
        log.info("삭제유저 아이디 :" + u_id);
        int res = sqlSession.update("deleteUser", u_id);
        log.info("유저 탈퇴 상태변경 결과:" + res);
        return res;
    }

    public List<String> getOwnerPushToken(int r_g_code) {
        return sqlSession.selectList("getPushTokenList", r_g_code);
    }


    public UserDTO changePWcheck(UserDTO userDTO) {
        return sqlSession.selectOne("changePWcheck",userDTO);
    }

    public int pwUpdate(UserBean user) {
        return sqlSession.update("pwUpdate",user);
    }

    public int tokenDupChk(String token){
        return sqlSession.selectOne("tokenDupChkUser", token);
    }

    public int insertPushToken(PushTokenDTO pushTokenDTO){
        return sqlSession.insert("insertPushTokenUser", pushTokenDTO);
    }

    public List<String> getUserPushToken(String u_id_fk) {
        return sqlSession.selectList("getPushTokenListUser", u_id_fk);
    }
}
