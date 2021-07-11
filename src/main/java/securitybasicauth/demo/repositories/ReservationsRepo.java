package securitybasicauth.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import securitybasicauth.demo.models.DatesModel;

public interface ReservationsRepo extends CrudRepository<DatesModel, Integer> {

}
