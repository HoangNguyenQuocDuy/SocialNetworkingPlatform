package api.socialPlatform.ApiForSocialApp.repositories;

import api.socialPlatform.ApiForSocialApp.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICommentRepo extends JpaRepository<Comments, UUID> {

}
