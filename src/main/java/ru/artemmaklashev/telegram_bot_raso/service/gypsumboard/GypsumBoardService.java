package ru.artemmaklashev.telegram_bot_raso.service.gypsumboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.Plan;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.GypsumBoardProductionData;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;
import ru.artemmaklashev.telegram_bot_raso.repositories.ProductionListRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.ShiftRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.TradeMarkRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.TypesRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class GypsumBoardService extends MyService {
    @Value("${spring.jackson.time-zone}")
    private String timeZone;


    private final BoardTypeRepository boardTypeRepository;
    private final ThicknessRepository thicknessRepository;
    private final WidthRepository widthRepository;
    private final GypsumBoardRepository gypsumBoardRepository;
    private final BoardProductionRepository boardProductionRepository;
    private final ProductionListRepository productionListRepository;
    private final PlanRepository planRepository;
    private final PlanService planService;

    @Autowired
    public GypsumBoardService(ShiftRepository repository, TypesRepository typesRepository,
                              TradeMarkRepository tradeMarkRepository, BoardTypeRepository boardTypeRepository,
                              ThicknessRepository thicknessRepository, WidthRepository widthRepository,
                              GypsumBoardRepository gypsumBoardRepository, BoardProductionRepository boardProductionRepository,
                              ProductionListRepository productionListRepository, PlanRepository planRepository, PlanService planService) {
        super(repository, typesRepository, tradeMarkRepository);
        this.boardTypeRepository = boardTypeRepository;
        this.thicknessRepository = thicknessRepository;
        this.widthRepository = widthRepository;
        this.gypsumBoardRepository = gypsumBoardRepository;
        this.boardProductionRepository = boardProductionRepository;
        this.productionListRepository = productionListRepository;
        this.planRepository = planRepository;

        this.planService = planService;
    }


//    public List<BoardType> getAllBoardTypes() {
//        return boardTypeRepository.findAll();
//    }
//
//
//    public BoardType getBoardTypeById(int id) {
//        Optional<BoardType> optionalBoardType = boardTypeRepository.findById(id);
//        return optionalBoardType.orElse(null);
//    }
//
//
//    public void SaveBoardType(BoardType boardType) {
//        boardTypeRepository.save(boardType);
//    }
//
//
//    public List<Thickness> getAllThickness() {
//        return thicknessRepository.findAll();
//    }
//
//
//    public Thickness getThicknessById(int id) {
//        Optional<Thickness> optionalThickness = thicknessRepository.findById(id);
//        return optionalThickness.orElse(null);
//    }
//
//
//    public void SaveThickness(Thickness thickness) {
//        thicknessRepository.save(thickness);
//    }
//
//
//    public List<Width> getAllWidth() {
//        return widthRepository.findAll();
//    }
//
//
//    public Width getWidthById(int id) {
//        Optional<Width> optionalWidth = widthRepository.findById(id);
//        return optionalWidth.orElse(null);
//    }
//
//
//    public void SaveWidth(Width width) {
//        widthRepository.save(width);
//    }
//
//
//    public List<GypsumBoard> getAllGypsumBoards() {
//        return gypsumBoardRepository.findAll();
//    }


    public List<GypsumBoardProductionData> getAllGypsumBoardsByDate(String startDateValue, String endDateValue) {


        List<BoardProduction> boardProductions = getBoardProductionByDate(startDateValue, endDateValue);
        List<Plan> planList = getPlanByDate(startDateValue, endDateValue);

        if (!boardProductions.isEmpty() || !planList.isEmpty()) {
            return getProductionData(boardProductions, planList);
        }
        return List.of(new GypsumBoardProductionData("Нет данных", 0, 0, 0, 0));
    }

    public List<GypsumBoardProductionData> getProductionData(List<BoardProduction> boardProductions, List<Plan> planList) {
        Map<String, GypsumBoardProductionData> dataMap = new HashMap<>();
        if (!boardProductions.isEmpty() || !planList.isEmpty()) {
            for (BoardProduction bp : boardProductions) {
                float plan = 0, total = 0, fact = 0, defective = 0;
                String name = bp.getProduct().toString();

                dataMap.putIfAbsent(name, new GypsumBoardProductionData(name, 0, 0, 0, 0));

                if (bp.getCategory().getId() == 2
                        || bp.getCategory().getId() == 3
                        || bp.getCategory().getId() == 4) {
                    fact = bp.getValue();
                } else if (bp.getCategory().getId() == 6) {
                    defective = bp.getValue();
                }
                if (bp.getCategory().getId() == 1) {
                    total = bp.getValue();
                }

                GypsumBoardProductionData gypsumBoardProductionData = dataMap.get(name);
                gypsumBoardProductionData.addValues(plan, total, fact, defective);
            }
        }
        if (!planList.isEmpty()) {
            for (Plan p : planList) {
                float plan = p.getPlanValue();
                String name = p.getGypsumBoard().toString();
                dataMap.putIfAbsent(name, new GypsumBoardProductionData(name, 0, 0, 0, 0));
                GypsumBoardProductionData gypsumBoardProductionData = dataMap.get(name);
                gypsumBoardProductionData.addValues(plan, 0, 0, 0);
            }
        }
        System.out.println("Данные для фронтенда - " + dataMap.size() + " записей");
        List<GypsumBoardProductionData> dataMapValues = new ArrayList<>(dataMap.values());
        dataMapValues.sort(Comparator.comparing(GypsumBoardProductionData::getBoardTitle).thenComparing(GypsumBoardProductionData::getPlanValue));
        return dataMapValues;
    }

    public List<BoardProduction> getBoardProductionByDate(String startDateValue, String endDateValue) {
//        LocalDateTime startDate = LocalDate.parse(startDateValue).atStartOfDay();
//        LocalDateTime endDate = startDateValue.equals(endDateValue) ? LocalDate.parse(endDateValue).atStartOfDay() : LocalDate.parse(endDateValue).atStartOfDay().minusDays(1);

        Instant startInstant = Instant.parse(startDateValue);
        Instant endInstant = Instant.parse(endDateValue);
        ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = endInstant.atZone(ZoneId.of(timeZone));

        LocalDateTime startDate = startZonedDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDate = startDateValue.equals(endDateValue) ?
                endZonedDateTime.toLocalDate().atStartOfDay() : endZonedDateTime.toLocalDate().minusDays(1).atStartOfDay();


        System.out.printf("Дата начала: %s, дата конца: %s\n", startDate, endDate);
        List<ProductionList> productionLists = productionListRepository.findProductionListByProductionDateBetween(startDate, endDate);

        System.out.println("Найдено " + productionLists.size() + "записей из productionLog\n");
        if (!productionLists.isEmpty()) {
            List<BoardProduction> boardProductions = boardProductionRepository.findAllByProductionListIn(productionLists);
            System.out.println("Получен список из " + boardProductions.size() + " записей");
            return boardProductions;
        }
        return new ArrayList<>();
    }

    public List<Plan> getPlanByDate(String startDateValue, String endDateValue) {
//        LocalDateTime startDate = Utils.convertStringToDate(startDateValue);//
//        LocalDateTime endDate = Utils.convertStringToDate(endDateValue);
//        List<Integer> planIds = planRepository.findIdsInDateRange(startDate.toLocalDate(), endDate.toLocalDate());
//        if (!planIds.isEmpty()) {
//           return planRepository.findAllById(planIds);
//        }
//        return new ArrayList<>();
        return planService.getPlans(startDateValue, endDateValue);
    }


    public List<GypsumBoard> getGypsumBoard() {
        return gypsumBoardRepository.findAll();
    }

    public List<BoardProduction> getBoardProductionsByGypsumBoard(String startDateValue, String endDateValue, List<GypsumBoard> gypsumBoards) {

        Instant startInstant = Instant.parse(startDateValue);
        Instant endInstant = Instant.parse(endDateValue);
        ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = endInstant.atZone(ZoneId.of(timeZone));

        LocalDateTime startDate = startZonedDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDate = startDateValue.equals(endDateValue) ?
                endZonedDateTime.toLocalDate().atStartOfDay() : endZonedDateTime.toLocalDate().minusDays(1).atStartOfDay();


        System.out.printf("Дата начала: %s, дата конца: %s\n", startDateValue, endDate);
        List<BoardProduction> boardProductions = boardProductionRepository.findByProductInAndProductionListProductionDateBetween(
                gypsumBoards, startDate, endDate);
        List<BoardProduction> result = boardProductions.stream().filter(bp -> bp.getCategory().getId() == 1).toList();
        System.out.println("Найдено " + result.size() + "записей из BoardProduction\n");

        return result;

    }
}


