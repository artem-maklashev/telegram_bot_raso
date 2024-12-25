package ru.artemmaklashev.telegram_bot_raso.repositories.delays;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.artemmaklashev.telegram_bot_raso.entity.Shift;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;

import java.time.LocalDate;
import java.util.List;

public interface BoardsDelaysRepository extends JpaRepository<BoardDelays, Integer> {

    List<BoardDelays> findAllByUnitPartIdIn(List<Integer> unitParts);
    List<BoardDelays> findAllByDelayDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT id FROM BoardDelays WHERE delayDate >= :startDate AND delayDate <= :endDate")
    List<Integer> findIdsInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<BoardDelays> findAllByDelayDateAndShiftAndProduct(LocalDate date, Shift shift, GypsumBoard product);

    BoardDelays findBoardDelaysById(int id);

    List<BoardDelays> findByDelayDateAndShiftAndProduct(LocalDate date, Shift shift, GypsumBoard product);

    void deleteAllByDelayDateAndShiftAndProduct(LocalDate productionDate, Shift shift, GypsumBoard product);
}
