package securitybasicauth.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import securitybasicauth.demo.models.DatesModel;

@Repository
public interface ReservationsRepo extends CrudRepository<DatesModel, Integer> {


}
