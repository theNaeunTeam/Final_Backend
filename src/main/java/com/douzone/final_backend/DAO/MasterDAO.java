package com.douzone.final_backend.DAO;

import com.douzone.final_backend.vo.MasterVO;
import com.douzone.final_backend.vo.OwnerVO;
import com.douzone.final_backend.vo.UserVO;
import com.douzone.final_backend.DTO.BannerDTO;
import com.douzone.final_backend.DTO.LocalDTO;
import com.douzone.final_backend.DTO.MasterDTO;
import com.douzone.final_backend.DTO.SaleDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MasterDAO {

    @Autowired
    private SqlSession sqlSession;


    public List<OwnerVO> findAll() {
        return sqlSession.selectList("findAll");
    }

    public int requestOK(String o_sNumber) {
        log.info("master : " + sqlSession.update("requestOK", o_sNumber));
        return sqlSession.update("requestOK", o_sNumber);
    }

    public int requestNO(String o_sNumber) {
        return sqlSession.update("requestNO", o_sNumber);
    }

    public MasterDTO findByMaster(String id) {
        return sqlSession.selectOne("findByMaster", id);
    }

    public MasterVO masterLogin(MasterDTO masterDTO) {
        log.info(masterDTO.getM_id() + "/" + masterDTO.getM_pw());
        log.info("MasterBean  : " + sqlSession.selectOne("masterlogin", masterDTO));
        return sqlSession.selectOne("masterlogin", masterDTO);
    }

    public List<UserVO> userAll() {
        return sqlSession.selectList("userAll");
    }

    public List<OwnerVO> findApproval() {
        return sqlSession.selectList("findApproval");
    }

    public List<OwnerVO> approvalCompletion() {
        return sqlSession.selectList("approvalCompletion");
    }

    public List<OwnerVO> terminationWaiting() {
        return sqlSession.selectList("terminationWaiting");
    }

    public int terminationOK(String o_sNumber) {
        log.info("master : " + sqlSession.update("terminationOK", o_sNumber));
        return sqlSession.update("terminationOK", o_sNumber);
    }

    public List<OwnerVO> terminationCompletion() {
        return sqlSession.selectList("terminationCompletion");
    }

    public int terminationCancle(String o_sNumber) {
        log.info("master : " + sqlSession.update("terminationCancle", o_sNumber));
        return sqlSession.update("terminationCancle", o_sNumber);
    }

    public List<BannerDTO> getBanner() {
        return sqlSession.selectList("getBanner");
    }

    public int deleteBannerTable() {
        return sqlSession.delete("deleteBannerTable");
    }

    public int insertBannerTable(BannerDTO bannerDTO) {
        return sqlSession.insert("insertBannerTable", bannerDTO);
    }
    public List<SaleDTO> masterMonth(int dal){
        return sqlSession.selectList("masterMonth", dal);
    }

    public List<SaleDTO> masterYear(int nowYear){
        return sqlSession.selectList("masterYear", nowYear);
    }

    public List<SaleDTO> userMonth(int dal){
        return sqlSession.selectList("userMonth", dal);
    }

    public List<SaleDTO> userYear(int nowYear){
        return sqlSession.selectList("userYear", nowYear);
    }

    public List<SaleDTO> ownerUser(int dal){
        return sqlSession.selectList("ownerUser", dal);
    }

    public List<SaleDTO> onnerUserYear(int nowYear){
        return sqlSession.selectList("ownerUserYear", nowYear);
    }

    public List<SaleDTO> ownerUser2(int dal){
        return sqlSession.selectList("ownerUser2", dal);
    }

    public List<SaleDTO> onnerUserYear2(int nowYear){
        return sqlSession.selectList("ownerUserYear2", nowYear);
    }

    public LocalDTO OwnerUserChart3(){
        return sqlSession.selectOne("OwnerUserChart3");
    }
}
