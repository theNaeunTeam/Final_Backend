package com.douzone.final_backend.service;

import com.douzone.final_backend.DTO.RecommendListDTO;
import com.douzone.final_backend.DTO.ShopListDTO;
import com.douzone.final_backend.DTO.ShoppingCartDTO;

import java.util.HashMap;
import java.util.List;

public interface CommonService {
    ShoppingCartDTO getShoppingCart(String g_code);

    List<ShopListDTO> getShopList(ShopListDTO s);

    HashMap<String, Object> getCategory(String g_owner);

    List<RecommendListDTO> getRecommendList();

    // getShopList 랑 동일하지만 sql 문이 다름
    List<ShopListDTO> getLocalList(ShopListDTO s);

    List<ShopListDTO> selectRedisUpdate();
}
