package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeFilter {
    private static final Logger log = LoggerFactory.getLogger(DateTimeFilter.class);

    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    public LocalDate getDateStart() {
        return dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public boolean hasDates() {
        return dateStart != null && dateEnd != null;
    }

    public boolean hasTimes() {
        return timeStart != null && timeEnd != null;
    }

    // return NULL if the request doesn't contain filters
    public static DateTimeFilter parseRequest(HttpServletRequest request) {
        log.info("parseRequest");
        String dStart = request.getParameter("dateStart");
        String dEnd   = request.getParameter("dateEnd");
        String tStart = request.getParameter("timeStart");
        String tEnd   = request.getParameter("timeEnd");
        if (dStart == null && dEnd == null && tStart == null  && tEnd == null) {
            log.info("no found filter");
            return new DateTimeFilter();
        }

        DateTimeFilter filter = new DateTimeFilter();
        filter.dateStart = dStart == null || dStart.isEmpty() ? null : LocalDate.parse(dStart);
        filter.dateEnd   = dEnd   == null || dEnd.isEmpty()  ? null : LocalDate.parse(dEnd);
        filter.timeStart = tStart == null || tStart.isEmpty()  ? null : LocalTime.parse(tStart);
        filter.timeEnd   = tEnd   == null || tEnd.isEmpty()  ? null : LocalTime.parse(tEnd);
        return filter;
    }

    public boolean mealBetweenDates(Meal meal) {
        LocalDate mealDate = meal.getDate();
        return mealDate.compareTo(dateStart) >= 0 && mealDate.compareTo(dateEnd) < 0;
    }

    public boolean mealBetweenTimes(Meal meal) {
        LocalTime mealTime = meal.getTime();
        return mealTime.compareTo(timeStart) >= 0 && mealTime.compareTo(timeEnd) < 0;
    }

    @Override
    public String toString() {
        return "[filter:[DS_" + dateStart + "]" +
                "[DE_" + dateEnd + "]" +
                "[TS_" + timeStart + "]" +
                "[TE_" + timeEnd + "]]";
    }
}
