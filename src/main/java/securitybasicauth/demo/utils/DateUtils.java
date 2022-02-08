package securitybasicauth.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import securitybasicauth.demo.models.DatesModel;
import securitybasicauth.demo.repositories.ReservationsRepo;

import java.util.Date;
import java.util.List;

@Component
public class DateUtils {
    private final ReservationsRepo reservationsRepo;

    @Autowired
    public DateUtils(ReservationsRepo reservationsRepo) {
        this.reservationsRepo = reservationsRepo;
    }

    public ResponseEntity<?> periodValidation(DatesModel datesModel, List<DatesModel> datesModelList) {

        if ((datesModel.getStartDate().compareTo(new Date(System.currentTimeMillis())) > 0) &&
                (datesModel.getStartDate().compareTo(datesModel.getEndDate()) <= 0)) {
            if (datesModelList.size() == 0) {

                return new ResponseEntity<>("toSave", HttpStatus.OK);

            } else {

                for (DatesModel model : datesModelList) {
                    Date startDate = model.getStartDate();
                    Date endDate = model.getEndDate();


                    if ((datesModel.getStartDate().compareTo(startDate) >= 0) &&
                            (datesModel.getStartDate().compareTo(endDate) <= 0) ||
                            (datesModel.getEndDate().compareTo(startDate) <= 0) &&
                                    (datesModel.getEndDate().compareTo(endDate) <= 0)) {
                        return new ResponseEntity<>("Term already booked", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("toSave", HttpStatus.OK);

                    }
                }
                return new ResponseEntity<>("Unknown error", HttpStatus.NOT_FOUND);
            }
        } else
            return new ResponseEntity<>("First date after second or first date before now", HttpStatus.TOO_EARLY);
    }
}
