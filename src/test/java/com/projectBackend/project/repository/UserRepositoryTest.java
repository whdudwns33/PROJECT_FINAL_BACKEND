package com.projectBackend.project.repository;

import com.projectBackend.project.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

//    @Test()
//    public void testFindByUserNickname() {
//        // 가짜 Member 객체 생성
//        Member fakeMember = new Member();
//        fakeMember.setId(1L);
//        fakeMember.setUserNickname("testUser");
//
//        // Mocking을 이용한 MemberRepository 객체 생성
//        UserRepository memberRepositoryMock = mock(UserRepository.class);
//
//        // 닉네임으로 회원을 찾는 테스트용 닉네임
//        String testNickname = "testUser";
//
//        // 닉네임으로 찾는 경우에 가짜 Member 객체를 Optional로 감싸서 반환하도록 설정
//        when(memberRepositoryMock.findByUserNickname(testNickname)).thenReturn(Optional.of(fakeMember));
//
//        // findByUserNickname 메서드 호출
//        Optional<Member> result = memberRepositoryMock.findByUserNickname(testNickname);
//
//        // 결과 확인
//        assertEquals(fakeMember, result.orElse(null), "Expected Member not found");
//    }
//
//    @Test
//    public void testFindByEmail() {
//        // 테스트 데이터 생성
//        Member member = new Member();
//        member.setUserEmail("test@example.com");
//        member.setUserNickname("testUser");
//        // 데이터 저장
//        userRepository.save(member);
//        // 회원 정보 조회
////        Member foundMember = userRepository.findByEmail("test@example.com");
//
//
//        System.out.println("member : " + foundMember);
//        System.out.println("test@example.com : " + foundMember.getUserEmail());
//        assertEquals("test@example.com", foundMember.getUserEmail());
//        // Add more assertions based on your Member entity and expected data
//    }





}