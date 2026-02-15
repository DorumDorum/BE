package com.project.dorumdorum.domain.user.fixture;

import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;

public class UserFixture {

    public static User createDefaultUser() {
        return User.builder()
                .name("홍길동")
                .nickname("테스터")
                .email("test@university.ac.kr")
                .password("encodedPassword123!")
                .role(Role.USER)
                .studentNo("20210001")
                .major("컴퓨터공학과")
                .grade("3")
                .birth("2000-01-01")
                .age(25)
                .gender(Gender.MALE)
                .firebaseToken("test-firebase-token")
                .build();
    }

    public static User createUserWithEmail(String email) {
        return User.builder()
                .name("김철수")
                .nickname("테스터2")
                .email(email)
                .password("encodedPassword123!")
                .role(Role.USER)
                .studentNo("20210002")
                .major("전자공학과")
                .grade("2")
                .birth("2001-05-15")
                .age(24)
                .gender(Gender.MALE)
                .build();
    }

    public static User createUserWithId(String userNo) {
        return User.builder()
                .userNo(userNo)
                .name("이영희")
                .nickname("테스터3")
                .email("user" + userNo + "@university.ac.kr")
                .password("encodedPassword123!")
                .role(Role.USER)
                .studentNo("20210003")
                .major("경영학과")
                .grade("4")
                .birth("1999-12-25")
                .age(26)
                .gender(Gender.FEMALE)
                .build();
    }

    public static User createUserWithNameAndNickname(String name, String nickname) {
        return User.builder()
                .name(name)
                .nickname(nickname)
                .email("custom@university.ac.kr")
                .password("encodedPassword123!")
                .role(Role.USER)
                .studentNo("20210004")
                .major("디자인학과")
                .grade("1")
                .birth("2003-03-10")
                .age(22)
                .gender(Gender.FEMALE)
                .build();
    }

    public static User createUserWithoutFirebaseToken() {
        return User.builder()
                .name("박민수")
                .nickname("테스터5")
                .email("notoken@university.ac.kr")
                .password("encodedPassword123!")
                .role(Role.USER)
                .studentNo("20210005")
                .major("물리학과")
                .grade("2")
                .birth("2002-07-20")
                .age(23)
                .gender(Gender.MALE)
                .firebaseToken(null)
                .build();
    }
}
