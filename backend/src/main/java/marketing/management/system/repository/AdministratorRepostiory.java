package marketing.management.system.repository;

import marketing.management.system.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepostiory extends JpaRepository<Administrator, Long> {
}
