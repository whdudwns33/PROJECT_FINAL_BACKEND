package com.projectBackend.project.community;

import com.projectBackend.project.utils.CommonService;
import com.projectBackend.project.utils.MultiDto;
import com.projectBackend.project.utils.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final CommonService commonService;

    public CommunityDto savePost(MultiDto multiDto) {
        CommunityDto communityDto = multiDto.getCommunityDto();
        String email = commonService.returnEmail(multiDto);
        log.info("email : {}", email);
        communityDto.setAuthorId(email);

        CommunityEntity communityEntity = communityDto.toEntity();
        return CommunityDto.of(communityRepository.save(communityEntity));
    }

//    public Page<CommunityDto> getAllPosts(Pageable pageable) {
//        Page<CommunityEntity> communityPage = communityRepository.findAll(pageable);
//        return communityPage.map(CommunityDto::of);
//    }

    public boolean incrementViews(Long id) {
        Optional<CommunityEntity> optionalCommunityEntity = communityRepository.findById(id);

        if (optionalCommunityEntity.isPresent()) {
            CommunityEntity communityEntity = optionalCommunityEntity.get();
            communityEntity.setViews(communityEntity.getLikes().size() + communityEntity.getDislikes().size());
//            communityEntity.setViews(communityEntity.getViews() + 1);
            communityRepository.save(communityEntity);
            return true;
        } else {
            return false; // 해당 ID의 토론글이 없을 경우 실패
        }
    }

    public boolean likePost(Long id, MultiDto multiDto) {
        String email = commonService.returnEmail(multiDto);
        Optional<CommunityEntity> optionalCommunityEntity = communityRepository.findById(id);

        if (optionalCommunityEntity.isPresent()) {
            CommunityEntity communityEntity = optionalCommunityEntity.get();
            communityEntity.addLike(email);
            communityRepository.save(communityEntity);
            return true;
        } else {
            return false; // 해당 ID의 토론글이 없을 경우 실패
        }
    }


    public boolean dislikePost(Long id, MultiDto multiDto) {
        String email = commonService.returnEmail(multiDto);
        Optional<CommunityEntity> optionalCommunityEntity = communityRepository.findById(id);

        if (optionalCommunityEntity.isPresent()) {
            CommunityEntity communityEntity = optionalCommunityEntity.get();
            communityEntity.addDislike(email);
            communityRepository.save(communityEntity);
            return true;
        } else {
            return false; // 해당 ID의 토론글이 없을 경우 실패
        }
    }

    // 통합 검색
    public Page<CommunityDto> search(String keyword, String type, Pageable pageable) {
        if ("content".equals(type)) {
            return communityRepository.findByContentContaining(keyword, pageable).map(CommunityDto::of);
        } else if ("author".equals(type)) {
            return communityRepository.findByAuthorIdContaining(keyword, pageable).map(CommunityDto::of);
        } else {
            return Page.empty(); // 빈 페이지 반환 또는 예외 처리 등
        }
    }
}
