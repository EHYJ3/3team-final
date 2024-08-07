package com.javalab.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1; // 페이지 번호 (1부터 시작)

    @Builder.Default
    private int size = 10; // 페이지 크기 (기본값 10)

    private String type; // 검색의 종류 (t,c,w,tc,tw,twc)

    private String searchText; // 검색 키워드

    // 추가된 필드
    private LocalDate from; // 검색 시작 날짜

    private LocalDate to; // 검색 종료 날짜

    private Boolean completed; // 완료 여부

    /**
     * 검색 타입을 배열로 변환
     */
    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    /**
     * 쿼리 문자열로 변환
     */
    private String link;

    public String getLink() {
        if (link == null) {
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }

            if (searchText != null) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(searchText, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            link = builder.toString();
        }

        return link;
    }

    /**
     * MyBatis에서 사용할 Offset 계산
     * - page와 size를 기반으로 offset을 계산하여 페이징 쿼리에 사용
     */
    public int getOffset() {
        return (page - 1) * size;
    }
}
