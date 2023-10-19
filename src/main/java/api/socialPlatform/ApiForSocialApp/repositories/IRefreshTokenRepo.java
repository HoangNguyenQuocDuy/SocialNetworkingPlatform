package api.socialPlatform.ApiForSocialApp.repositories;

import api.socialPlatform.ApiForSocialApp.model.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IRefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {
    @Transactional
    void deleteByToken(String token);
    RefreshToken findByToken(String token);
    RefreshToken findByUserUsername(String username);
}
