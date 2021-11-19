package com.douzone.final_backend.DAO;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.UserBean;
import com.douzone.final_backend.DTO.FavoritesDTO;
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
}
