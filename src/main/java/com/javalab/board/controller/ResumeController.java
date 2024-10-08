package com.javalab.board.controller;

import com.javalab.board.dto.ResumeDto;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.service.ResumeService;
import com.javalab.board.vo.JobSeekerVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;
    private final JobSeekerService jobSeekerService;

    @GetMapping("/save")
    public String save(Model model, Authentication authentication) {
        String loggedInUsername = getLoggedInUsername(authentication);

        if (loggedInUsername != null) {
            // jobSeekerId를 사용하여 JobSeekerVo를 조회
            Optional<JobSeekerVo> optionalJobSeekerVo = jobSeekerService.getJobSeekerDetails(loggedInUsername);

            if (optionalJobSeekerVo.isPresent()) {
                model.addAttribute("jobSeekerVo", optionalJobSeekerVo.get());
                return "/resume/save";
            } else {
                log.error("JobSeekerVo를 찾을 수 없습니다.");
                return "redirect:/error";
            }
        }

        log.error("로그인된 사용자 정보를 가져올 수 없습니다.");
        return "redirect:/login";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("resumeDto") ResumeDto resumeDto,
                       @RequestParam("file") MultipartFile file,
                       Authentication authentication) throws Exception {

        String loggedInUsername = getLoggedInUsername(authentication);
        log.debug("Logged in Username: " + loggedInUsername);

        resumeDto.setJobSeekerId(loggedInUsername);
        log.debug("JobSeekerId 설정됨: " + resumeDto.getJobSeekerId());

        if (resumeDto.getJobSeekerId() == null) {
            log.error("JobSeekerId가 null입니다. 저장할 수 없습니다.");
            return "redirect:/error";
        }

        resumeService.resumeCreate(resumeDto, file);

        return "redirect:/resume/list";
    }

    @GetMapping("/list")
    public String findAll(Model model, Authentication authentication) {
        String loggedInUsername = getLoggedInUsername(authentication);

        if (loggedInUsername != null) {
            List<ResumeDto> resumeDtoList;

            // 사용자 역할에 따라 보여줄 이력서 리스트 결정
            if (isCompanyUser(authentication)) {
                resumeDtoList = resumeService.findPublicResumesForCompany();
            } else {
                resumeDtoList = resumeService.findAll(loggedInUsername);
            }

            model.addAttribute("resumeList", resumeDtoList);

            // 이력서 리스트를 3개씩 묶어 partitionedResumes 리스트로 변환
            List<List<ResumeDto>> partitionedResumes = new ArrayList<>();
            for (int i = 0; i < resumeDtoList.size(); i += 3) {
                partitionedResumes.add(resumeDtoList.subList(i, Math.min(i + 3, resumeDtoList.size())));
            }
            model.addAttribute("partitionedResumes", partitionedResumes);

            Map<String, Long> educationCount = resumeDtoList.stream()
                    .collect(Collectors.groupingBy(ResumeDto::getEducation, Collectors.counting()));
            model.addAttribute("educationCount", educationCount);

            return "/resume/list";
        }

        return "redirect:/login";
    }

    // 기업 사용자 확인 메서드 추가
    private boolean isCompanyUser(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_COMPANY"));
    }




    @GetMapping("/detail/{resumeId}")
    public String getResumeDetail(@PathVariable("resumeId") int resumeId, Model model, Authentication authentication) {

        ResumeDto resumeDto = resumeService.findById(resumeId);
        model.addAttribute("resume", resumeDto);
        return "resume/detail";
    }

    @GetMapping("/update/{resumeId}")
    public String updateResume(@PathVariable("resumeId") int resumeId, Model model, Authentication authentication) {
        String loggedInUsername = getLoggedInUsername(authentication);

        ResumeDto resumeDto = resumeService.findById(resumeId);
        resumeDto.setJobSeekerId(loggedInUsername);

        model.addAttribute("resume", resumeDto);
        return "resume/update";
    }

    @PostMapping("/update/{resumeId}")
    public String update(@PathVariable("resumeId") int resumeId,
                         @ModelAttribute("resume") ResumeDto resumeDto,
                         @RequestParam("file") MultipartFile file,
                         Authentication authentication) throws Exception {

        String loggedInUsername = getLoggedInUsername(authentication);
        resumeDto.setJobSeekerId(loggedInUsername);

        if (resumeDto.getJobSeekerId() == null) {
            log.error("JobSeekerId가 null입니다. 업데이트할 수 없습니다.");
            return "redirect:/error";
        }

        resumeService.updateResume(resumeDto, file);
        return "redirect:/resume/list";
    }

    @PostMapping("/delete/{resumeId}")
    public String deleteResume(@PathVariable("resumeId") int resumeId) {
        resumeService.deleteResume(resumeId);
        return "redirect:/resume/list";
    }


    @PostMapping("/updateVisibility")
    @ResponseBody
    public ResponseEntity<Void> updateVisibility(@RequestParam("resumeId") Long resumeId,
                                                 @RequestParam("visibilityStatus") String visibilityStatus) {
        System.out.println("abdsdfsdf" +visibilityStatus);
        resumeService.updateResumeVisibility(resumeId, visibilityStatus);

        return ResponseEntity.ok().build();
    }



    private String getLoggedInUsername(Authentication authentication) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                return ((OAuth2User) principal).getAttribute("login"); // GitHub의 로그인 정보
            }
        }
        return null;
    }



}
