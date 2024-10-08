package com.javalab.board.repository;

import com.javalab.board.dto.BlacklistDto;
import com.javalab.board.vo.AdminVo;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminMapper {

    // Insert a new Admin
    void insertAdmin(AdminVo adminVo);

    // Select an Admin by ID
    Optional<AdminVo> getAdminById(String adminId);

    // Select an Admin by email
    Optional<AdminVo> getAdminByEmail(String email);

    // Select an Admin by ID and password for login
    Optional<AdminVo> getAdminByIdAndPassword(String adminId, String password);

    // Update Admin details
    void updateAdmin(AdminVo adminVo);

    // Delete an Admin by ID
    void deleteAdmin(String adminId);

    /**
     * 구직자 목록 가져옴
     */
    List<JobSeekerVo> selectAllJobSeekers();


    /**
     * 기업 목록 가져옴
     */
    List<CompanyVo> selectAllCompany();


    // BlacklistMapper의 메서드를 재사용
    List<BlacklistDto> getAllBlacklist();
    List<BlacklistDto> getActiveBlacklist();
    BlacklistDto getBlacklistStatus(@Param("id") String id, @Param("type") String type);
    int addBlacklist(BlacklistDto blacklist);
    void updateBlacklist(BlacklistDto blacklist);

}