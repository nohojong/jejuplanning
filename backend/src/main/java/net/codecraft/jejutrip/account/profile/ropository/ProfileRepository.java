package net.codecraft.jejutrip.account.profile.ropository;

import net.codecraft.jejutrip.account.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>, CustomProfileRepository {
}