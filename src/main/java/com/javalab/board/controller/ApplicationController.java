package com.javalab.board.controller;

import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.service.ApplicationService;
import com.javalab.board.service.JobPostService;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobPostVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;


    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // 지원 내역(list) 조회
            @GetMapping("/list")
            public String getApplicationsByJobSeekerId(Model model) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                    String jobSeekerId = userDetails.getUsername();

            List<ApplicationDto> applications = applicationService.getApplicationsByJobSeekerId(jobSeekerId);


            // 각 지원서에 연결된 JobPost 정보를 함께 가져옵니다.
            for (ApplicationDto application : applications) {
                JobPostVo jobPost = applicationService.getJobPostByApplicationId(application.getApplicationId());
                application.setJobPost(jobPost);  // JobPostVo를 ApplicationDto에 설정
            }

            model.addAttribute("applications", applications);
        }
        return "application/list";
    }


    // 지원 취소 메서드
    @PostMapping("/delete/{applicationId}")
    public String deleteApplication(@PathVariable("applicationId") Long applicationId) {
        applicationService.deleteApplicationById(applicationId);
        return "redirect:/application/list"; // 삭제 후 리스트 페이지로 리다이렉트
    }

}



//    // 지원하기 기능
//    @PostMapping("/apply")
//    public String applyForJob(@RequestParam("resumeId") int resumeId,
//                              @RequestParam("jobPostId") Long jobPostId) {
//
//        // 현재 인증된 사용자 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            String jobSeekerId = userDetails.getUsername();
//
//            // 서비스 메서드 호출
//            applicationService.applyForJob(resumeId, jobPostId, jobSeekerId);
//
//            return "redirect:/applications"; // 지원이 완료된 후 리다이렉트할 페이지
//        }
//
//        // 인증 정보가 없는 경우 또는 처리 중 오류가 발생한 경우 처리
//        return "redirect:/error"; // 적절한 오류 페이지로 리다이렉트
//    }
