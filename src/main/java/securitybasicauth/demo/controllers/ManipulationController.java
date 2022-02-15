package securitybasicauth.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import securitybasicauth.demo.Manipulation;
import securitybasicauth.demo.models.AccommodationsModel;
import securitybasicauth.demo.models.DatesModel;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin(origins = "https://dante03918.github.io/booking-clone-front:443", maxAge = 3600)
@RestController
public class ManipulationController {

    private final Manipulation manipulation;

    public ManipulationController(Manipulation manipulation) {
        this.manipulation = manipulation;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<?> getAccommodations() {

        return manipulation.getAccommodations();

    }

    @PostMapping("/addAccommodation")
    public ResponseStatusException addAccommodation(HttpServletRequest request, @RequestBody AccommodationsModel accommodationsModel) {

        return manipulation.addAccommodation(request, accommodationsModel);
    }


    @DeleteMapping("/removeAccommodation")
    public ResponseStatusException removeAccommodation(@RequestParam(name = "id") int id) {

        return manipulation.removeAccommodation(id);
    }


    @PostMapping("/book")
    public ResponseStatusException bookAccommodation(@RequestBody DatesModel dates) {

        return manipulation.bookAccommodation(dates);
    }

}
