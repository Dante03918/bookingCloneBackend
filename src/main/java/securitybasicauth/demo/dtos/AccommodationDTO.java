package securitybasicauth.demo.dtos;

import org.springframework.stereotype.Component;
import securitybasicauth.demo.models.AccommodationsModel;
import securitybasicauth.demo.models.RegisterUserModel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccommodationDTO {

    public List<AccommodationsModel> getAccommodationsFromUserModel(List<RegisterUserModel> registerUserModelList) {

     return   registerUserModelList.stream()
             .map(RegisterUserModel::getAccommodations)
             .flatMap(Collection::stream)
             .collect(Collectors.toList());
    }
}
