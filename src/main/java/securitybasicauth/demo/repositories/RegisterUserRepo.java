package securitybasicauth.demo.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import securitybasicauth.demo.RegisterUserModel;


@Repository
public interface RegisterUserRepo extends CrudRepository<RegisterUserModel, Integer> {

    @Override
    <S extends RegisterUserModel> S save(S s);

    boolean existsByEmail(String email);

    RegisterUserModel findByEmail(String email);


}
