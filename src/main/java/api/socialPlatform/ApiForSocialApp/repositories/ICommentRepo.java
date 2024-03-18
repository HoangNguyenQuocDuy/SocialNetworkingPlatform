    package api.socialPlatform.ApiForSocialApp.repositories;

    import api.socialPlatform.ApiForSocialApp.model.Comment;
    import api.socialPlatform.ApiForSocialApp.model.Post;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    import java.util.UUID;

    @Repository
    public interface ICommentRepo extends JpaRepository<Comment, UUID> {
        List<Comment> findByPostPostId(UUID postId, Pageable pageable);
    }
