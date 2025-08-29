package com.project.dorumdorum.domain.user.domain.entity;

public enum SchoolName {
    // ---------------- 서울 소재 주요 대학 ----------------
    SEOUL_NATIONAL_UNIVERSITY("서울대학교"),
    KOREA_UNIVERSITY("고려대학교"),
    YONSEI_UNIVERSITY("연세대학교"),
    SOGANG_UNIVERSITY("서강대학교"),
    HANYANG_UNIVERSITY("한양대학교"),
    EWHA_WOMANS_UNIVERSITY("이화여자대학교"),
    SUNGKYUNKWAN_UNIVERSITY("성균관대학교"),
    KYUNGHEE_UNIVERSITY("경희대학교"),
    CHUNGANG_UNIVERSITY("중앙대학교"),
    HANKUK_UNIVERSITY_OF_FOREIGN_STUDIES("한국외국어대학교"),
    SEOUL_TECH("서울과학기술대학교"),
    KONKUK_UNIVERSITY("건국대학교"),
    DONGGUK_UNIVERSITY("동국대학교"),
    HONGIK_UNIVERSITY("홍익대학교"),
    MYONGJI_UNIVERSITY("명지대학교(서울)"),
    SOOKMYUNG_WOMENS_UNIVERSITY("숙명여자대학교"),
    SEOUL_WOMENS_UNIVERSITY("서울여자대학교"),
    SEJONG_UNIVERSITY("세종대학교"),
    KANGNAM_UNIVERSITY("강남대학교"),
    KOREA_NATIONAL_UNIVERSITY_OF_ARTS("한국예술종합학교"),

    // ---------------- 경기도 주요 대학 ----------------
    GACHON_UNIVERSITY("가천대학교"),
    GACHON_UNIVERSITY_MEDICAL("가천대학교 메디컬 캠퍼스"),
    AJOU_UNIVERSITY("아주대학교"),
    SUNGKYUNKWAN_UNIVERSITY_SUWON("성균관대학교(수원)"),
    HANYANG_UNIVERSITY_ERICA("한양대학교 ERICA캠퍼스"),
    KYUNGHEE_UNIVERSITY_GLOBAL("경희대학교 국제캠퍼스(수원)"),
    DANKOOK_UNIVERSITY_JUKJEON("단국대학교(죽전)"),
    MYONGJI_UNIVERSITY_YONGIN("명지대학교(용인)"),
    KOREA_AEROSPACE_UNIVERSITY("한국항공대학교"),

    // ---------------- 지방 거점 국립대 ----------------
    PUSAN_NATIONAL_UNIVERSITY("부산대학교"),
    KYUNGPOOK_NATIONAL_UNIVERSITY("경북대학교"),
    CHONNAM_NATIONAL_UNIVERSITY("전남대학교"),
    CHUNGBUK_NATIONAL_UNIVERSITY("충북대학교"),
    CHUNGNAM_NATIONAL_UNIVERSITY("충남대학교"),
    JEONBUK_NATIONAL_UNIVERSITY("전북대학교"),
    KANGWON_NATIONAL_UNIVERSITY("강원대학교"),
    JEJU_NATIONAL_UNIVERSITY("제주대학교"),

    // ---------------- 과학기술원 / 특수 대학 ----------------
    KAIST("KAIST"),
    POSTECH("포항공과대학교"),
    UNIST("UNIST"),
    DGIST("DGIST"),
    GIST("GIST");

    private final String koreanName;

    SchoolName(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}
