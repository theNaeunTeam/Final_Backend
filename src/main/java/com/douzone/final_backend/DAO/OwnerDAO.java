package com.douzone.final_backend.DAO;

import com.douzone.final_backend.Bean.OwnerBean;
import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.DTO.ReserveDTO;
import com.douzone.final_backend.DTO.OwnerDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class OwnerDAO {
    @Autowired
    private SqlSession sqlSession;

    public OwnerBean findBySNum(String o_sNumber) {
        return sqlSession.selectOne("findBySNum", o_sNumber);
    }

    public boolean existsBySNum(String o_sNumber) {
        return sqlSession.selectOne("existsBySNum", o_sNumber);
    }

    public void insertOwner(OwnerBean owner) {
        sqlSession.insert("insertOwner", owner);
    }

    public OwnerDTO findByOwner(String id) {
        return sqlSession.selectOne("findByOwner", id);
    }

    public int addGoods(GoodsBean goodsBean) {
        return sqlSession.insert("addGoods", goodsBean);
    }

    public int updateGoods(GoodsBean goodsBean) {
        return sqlSession.update("updateGoods", goodsBean);
    }

    public List<GoodsBean> goodsList(String o_sNumber) {
        return sqlSession.selectList("goodsList", o_sNumber);
    }

    public int reserveCheck(ReserveDTO reserveDTO) {
        String check = reserveDTO.getCheck();
        // 예약 승인하면 예약한 상품(where r_code) 수량만큼 goods 테이블에서 - 하고
        // 예약 테이블에서 status 를 1바꾼다.
        int result = 0;
        if (check.equals("승인")) {
            // 예약 테이블 statue 1로 변경
            sqlSession.update("resOK", reserveDTO);
            log.info("승인 실행");
        } else if (check.equals("거절")) {
            // goods 테이블에서 수량 더하기
            sqlSession.update("resNoCount", reserveDTO);

            // 예약 테이블 status 2로 변경
            sqlSession.update("resNo", reserveDTO);
            // 상품 상태 count != 0 and g_status != 2 이면 g_status = 0

        } else if (check.equals("판매완료")) {
            // 예약 테이블 status 3으로 변경
            sqlSession.update("resSu", reserveDTO);
        } else if (check.equals("노쇼")) {
            // goods 테이블 수량 더하기
            sqlSession.update("resNoCount", reserveDTO);

            // 예약 테이블 status 4로 변경
            sqlSession.update("reseNoShowStatus", reserveDTO);

            // 예약한 user id 의 u_noShowCnt 1 증가
            sqlSession.update("resNoShowCount", reserveDTO);

            // 상품상태 g_count != 0 and g_status != 2 g_status = 0;
        }

        return result;
    }


}
