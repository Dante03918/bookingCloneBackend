package securitybasicauth.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import securitybasicauth.demo.models.RegisterUserModel;


@Repository
public interface RegisterUserRepo extends JpaRepository<RegisterUserModel, Integer> {

    @Override
    <S extends RegisterUserModel> S save(S s);

    boolean existsByEmail(String email);

    RegisterUserModel findByEmail(String email);


}
