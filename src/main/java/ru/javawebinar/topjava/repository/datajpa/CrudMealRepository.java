package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    Optional<Meal> getByIdAndUserId(Integer mealId, Integer userId);

    List<Meal> findAllByUserId(Integer userId, Sort sort);

    List<Meal> findAllByUserIdAndDateTimeAfterAndDateTimeBefore(Integer userid, LocalDateTime before, LocalDateTime after, Sort sort);

    @Transactional
    @Modifying
    Integer deleteByIdAndUserId(Integer mealId, Integer userId);

    @Transactional
    @Modifying
    @Query("update Meal set description = ?1, calories = ?2, date_time = ?3 where (id = ?4 and user_id = ?5)")
    Integer setMealInfo(String description, Integer calories, LocalDateTime dateTime, Integer mealId, Integer userId);
}
