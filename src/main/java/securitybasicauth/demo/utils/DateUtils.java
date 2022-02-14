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

    public DateUtils(ReservationsRepo reservationsRepo) {
        this.reservationsRepo = reservationsRepo;
    }

    public Error periodValidation(DatesModel datesModel, List<DatesModel> datesModelList) {

        if ((datesModel.getStartDate().compareTo(new Date(System.currentTimeMillis())) > 0) &&
                (datesModel.getStartDate().compareTo(datesModel.getEndDate()) <= 0)) {
            if (datesModelList.size() == 0) {

                return new Error("toSave");

            } else {

                for (DatesModel model : datesModelList) {
                    Date startDate = model.getStartDate();
                    Date endDate = model.getEndDate();


                    if ((datesModel.getStartDate().compareTo(startDate) >= 0) &&
                            (datesModel.getStartDate().compareTo(endDate) <= 0) ||
                            (datesModel.getEndDate().compareTo(startDate) <= 0) &&
                                    (datesModel.getEndDate().compareTo(endDate) <= 0)) {
                        return new Error("Occupied term");
                    } else {
                        return new Error("toSave");
                    }
                }
                return new Error("Unknown error");
            }
        } else
            return new Error("First date after second or first date before now");
    }
}
