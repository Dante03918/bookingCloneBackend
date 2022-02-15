package securitybasicauth.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import securitybasicauth.demo.dtos.AccommodationDTO;
import securitybasicauth.demo.models.AccommodationsModel;
import securitybasicauth.demo.models.DatesModel;
import securitybasicauth.demo.models.RegisterUserModel;
import securitybasicauth.demo.repositories.AccommodationRepo;
import securitybasicauth.demo.repositories.RegisterUserRepo;
import securitybasicauth.demo.repositories.ReservationsRepo;
import securitybasicauth.demo.utils.DateUtils;
import securitybasicauth.demo.utils.JwtTokenUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Component
public class Manipulation {

    private final AccommodationRepo accommodationRepo;
    private final RegisterUserRepo registerUserRepo;
    private final JwtTokenUtils jwtTokenUtils;
    private final ReservationsRepo reservationsRepo;
    private final DateUtils dateUtils;
    private final AccommodationDTO accommodationDTO;


    public Manipulation(AccommodationRepo accommodationRepo,
                                  RegisterUserRepo registerUserRepo,
                                  JwtTokenUtils jwtTokenUtils,
                                  ReservationsRepo reservationsRepo,
                                  DateUtils dateUtils,
                                  AccommodationDTO accommodationDTO) {
        this.accommodationRepo = accommodationRepo;
        this.registerUserRepo = registerUserRepo;
        this.jwtTokenUtils = jwtTokenUtils;
        this.reservationsRepo = reservationsRepo;
        this.dateUtils = dateUtils;
        this.accommodationDTO = accommodationDTO;
    }

    public ResponseEntity<?> getAccommodations() {

        List<RegisterUserModel> usersEntities = (List<RegisterUserModel>) registerUserRepo.findAll();

        if(usersEntities.size() == 0)
            return new ResponseEntity<>("Database is empty", HttpStatus.OK);
        else{
            return new ResponseEntity<>(accommodationDTO.getAccommodationsFromUserModel((List<RegisterUserModel>)registerUserRepo.findAll()), HttpStatus.OK);
        }

    }

    public ResponseStatusException addAccommodation(HttpServletRequest request, AccommodationsModel accommodationsModel) {
        String token = request.getHeader("Authorization");

        RegisterUserModel registerUserModel;

        String cleanToken = token.substring(7);
        String username = jwtTokenUtils.extractUsernameFromToken(cleanToken);
        Boolean tokenIsValid = jwtTokenUtils.validateToken(cleanToken, username);

        if (tokenIsValid) {
            registerUserModel = registerUserRepo.findByEmail(username);

            accommodationsModel.setOwnerId(registerUserModel.getId());
            accommodationsModel.setOwnerEmail(username);

            accommodationRepo.save(accommodationsModel);

            registerUserModel.getAccommodations().add(accommodationsModel);

            return new ResponseStatusException(HttpStatus.OK, "Room added");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is not valid");
        }
    }

    public ResponseStatusException removeAccommodation(int id) {

        accommodationRepo.deleteById(id);

        return new ResponseStatusException(HttpStatus.OK, "Advertisement deleted");

    }

    public ResponseStatusException bookAccommodation(DatesModel dates) {

        AccommodationsModel accommodationsModel;

        try {
            accommodationsModel = accommodationRepo
                    .findById(dates
                            .getAccommodationId())
                    .orElse(null);
        } catch (EntityNotFoundException e) {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found. Probably was deleted or never added");
        }
        List<DatesModel> reservations = Objects.requireNonNull(accommodationsModel).getReservations();

        String errorMessage = dateUtils.periodValidation(dates, reservations).getMessage();

        if (errorMessage.equals("toSave")) {
            reservationsRepo.save(dates);
            return new ResponseStatusException(HttpStatus.OK, "Reserved");
        } else
            return new ResponseStatusException(HttpStatus.MULTI_STATUS, errorMessage);
    }
}
