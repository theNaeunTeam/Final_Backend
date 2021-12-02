package com.douzone.final_backend.DAO;

import com.douzone.final_backend.DTO.RecommendListDTO;
import com.douzone.final_backend.DTO.ShopListDTO;
import com.douzone.final_backend.DTO.ShoppingCartDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
public class CommonDAO {

    @Autowired
    private SqlSession sqlSession;

    public ShoppingCartDTO getShoppingCart(String g_code) {
        return sqlSession.selectOne("getShoppingCart", g_code);
    }

    public List<ShopListDTO> getShopList(ShopListDTO s) {
        return sqlSession.selectList("getShopList", s);
    }

    public HashMap<String, Object> getCategory(String g_owner) {
        return sqlSession.selectOne("getCategory", g_owner);
    }

    public List<RecommendListDTO> getRecommendList() {
        return sqlSession.selectList("recommendList");
    }

    public List<ShopListDTO> getLocalList(ShopListDTO s) {
        return sqlSession.selectList("getLocalList",s);
    }
}
