package com.douzone.final_backend.Service;

import com.douzone.final_backend.DAO.CommonDAO;
import com.douzone.final_backend.DTO.RecommendListDTO;
import com.douzone.final_backend.DTO.ShopListDTO;
import com.douzone.final_backend.DTO.ShoppingCartDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class CommonService {

    @Autowired
    private CommonDAO commonDAO;

    public ShoppingCartDTO getShoppingCart(String g_code) {
        return commonDAO.getShoppingCart(g_code);
    }

    public List<ShopListDTO> getShopList(ShopListDTO s) {
        return commonDAO.getShopList(s);
    }

    public HashMap<String, Object> getCategory(String g_owner) {
        return commonDAO.getCategory(g_owner);
    }

    public List<RecommendListDTO> getRecommendList(){
        return commonDAO.getRecommendList();
    }


    // getShopList 랑 동일하지만 sql 문이 다름
    public List<ShopListDTO> getLocalList(ShopListDTO s) {
        return commonDAO.getLocalList(s);
    }
}
