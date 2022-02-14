package securitybasicauth.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import securitybasicauth.demo.utils.JwtTokenUtils;
import securitybasicauth.demo.models.AccommodationsModel;
import securitybasicauth.demo.models.DatesModel;
import securitybasicauth.demo.models.RegisterUserModel;
import securitybasicauth.demo.repositories.AccommodationRepo;
import securitybasicauth.demo.repositories.RegisterUserRepo;
import securitybasicauth.demo.repositories.ReservationsRepo;
import securitybasicauth.demo.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "https://dante03918.github.io/booking-clone-front:443", maxAge = 3600)
@RestController
public class ManipulationController {

    private final DataSource dataSource;
    private final AccommodationRepo accommodationRepo;
    private final RegisterUserRepo registerUserRepo;
    private final JwtTokenUtils jwtTokenUtils;
    private final ReservationsRepo reservationsRepo;
    private final DateUtils dateUtils;


    public ManipulationController(AccommodationRepo accommodationRepo,
                                  RegisterUserRepo registerUserRepo,
                                  JwtTokenUtils jwtTokenUtils,
                                  ReservationsRepo reservationsRepo,
                                  DateUtils dateUtils,
                                  DataSource dataSource) {
        this.accommodationRepo = accommodationRepo;
        this.registerUserRepo = registerUserRepo;
        this.jwtTokenUtils = jwtTokenUtils;
        this.reservationsRepo = reservationsRepo;
        this.dateUtils = dateUtils;
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<?> getAccommodations() {

        return new ResponseEntity<>(registerUserRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("/addAccommodation")
    public ResponseStatusException addAccommodation(HttpServletRequest request, @RequestBody AccommodationsModel accommodationsModel) {
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


    @DeleteMapping("/removeAccommodation")
    public ResponseStatusException removeAccommodation(@RequestParam(name = "id") int id) {

        accommodationRepo.deleteById(id);

        return new ResponseStatusException(HttpStatus.OK, "Advertisement deleted");

    }


    @PostMapping("/book")
    public ResponseEntity<?> bookAccommodation(@RequestBody DatesModel dates) {

        AccommodationsModel accommodationsModel = new AccommodationsModel();

        try {
            accommodationsModel = accommodationRepo
                    .findById(dates
                            .getAccommodationId())
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<DatesModel> reservations = Objects.requireNonNull(accommodationsModel).getReservations();

        ResponseEntity<?> responseEntity = dateUtils.periodValidation(dates, reservations);

        if (Objects.requireNonNull(responseEntity.getBody()).toString().equals("toSave")) {
            reservationsRepo.save(dates);
            return new ResponseEntity<>("Reserved", HttpStatus.OK);
        } else
            return responseEntity;
    }

}
