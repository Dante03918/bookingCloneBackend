package securitybasicauth.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securitybasicauth.demo.models.AccommodationsModel;
import securitybasicauth.demo.models.DatesModel;
import securitybasicauth.demo.models.RegisterUserModel;
import securitybasicauth.demo.repositories.AccommodationRepo;
import securitybasicauth.demo.repositories.RegisterUserRepo;
import securitybasicauth.demo.repositories.ReservationsRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
public class ManipulationController {

    @Autowired
    AccommodationRepo accommodationRepo;

    @Autowired
    RegisterUserRepo registerUserRepo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ReservationsRepo reservationsRepo;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<?> getAccommodations() {

        return new ResponseEntity(registerUserRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("/addAccommodation")
    public ResponseEntity addAccommodation(HttpServletRequest request, @RequestBody AccommodationsModel accommodationsModel) {
        String token = request.getHeader("Authorization");

        RegisterUserModel registerUserModel;

        String cleanToken = token.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(cleanToken);
        Boolean tokenIsValid = jwtTokenUtil.validateToken(cleanToken, username);

        if (tokenIsValid) {
            registerUserModel = registerUserRepo.findByEmail(username);

            accommodationsModel.setOwnerId(registerUserModel.getId());
            accommodationsModel.setOwnerEmail(username);

            accommodationRepo.save(accommodationsModel);

            registerUserModel.getAccommodations().add(accommodationsModel);

            return ResponseEntity.ok("Ad added");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/removeAccommodation")
    public ResponseEntity removeAccommodation(@RequestParam(name = "email") String email, @RequestParam(name = "id") int id) {

        accommodationRepo.deleteById(id);

        System.out.println(id);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/book")
    public ResponseEntity bookAccommodation(@RequestBody DatesModel dates) {

        AccommodationsModel accommodationsModel = new AccommodationsModel();

        try {
            accommodationsModel = accommodationRepo.findById(dates.getAccommodationId()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<DatesModel> reservations = accommodationsModel.getReservations();


        if (reservations.size() == 0) {

            reservationsRepo.save(dates);

            reservations.add(dates);
            return new ResponseEntity("Reserved", HttpStatus.OK);
        } else {
            for (DatesModel model : reservations) {
                Date startDate = model.getStartDate();
                Date endDate = model.getEndDate();

                if ((dates.getStartDate().after(startDate)) &&
                        (dates.getStartDate().before(endDate)) ||
                        (dates.getEndDate().after(startDate)) &&
                                (dates.getEndDate().before(endDate))) {
                    return new ResponseEntity("Term already booked", HttpStatus.OK);
                } else {
                    reservationsRepo.save(dates);
                    reservations.add(dates);
                    return new ResponseEntity("Reserved", HttpStatus.OK);
                }
            }
        }
        return null;
    }
}