package api.socialPlatform.ApiForSocialApp.repositories;

import api.socialPlatform.ApiForSocialApp.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IPostRepo extends JpaRepository<Post, UUID> {
    List<Post> findByUserUserId(UUID userId);
    Page<Post> findAll(Pageable pageable);
}
