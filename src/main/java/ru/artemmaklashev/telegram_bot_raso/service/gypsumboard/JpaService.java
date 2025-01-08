package ru.artemmaklashev.telegram_bot_raso.service.gypsumboard;



import ru.artemmaklashev.telegram_bot_raso.entity.Shift;
import ru.artemmaklashev.telegram_bot_raso.entity.TradeMark;
import ru.artemmaklashev.telegram_bot_raso.entity.Types;

import java.util.List;

public interface JpaService {
    //Shift
    List<Shift> getAllShifts();
    Shift getShiftById(int id);
    void SaveShift(Shift shift);

    //Types
    List<Types> getAllTypes();
    Types getTypeById(int id);
    void SaveType(Types type);

    //TradeMark
    List<TradeMark> getAllTrademark();
    TradeMark getTradeMarkById(int id);
    void SaveTradeMark(TradeMark tradeMark);

//    //BoardType
//    List<BoardType> getAllBoardTypes();
//    BoardType getBoardTypeById(int id);
//    void SaveBoardType(BoardType boardType);
//
//    //Thickness
//    List<Thickness> getAllThickness();
//    Thickness getThicknessById(int id);
//    void SaveThickness(Thickness thickness);
//
//    //Width
//    List<Width> getAllWidth();
//    Width getWidthById(int id);
//    void SaveWidth(Width width);
//
//    //Length
//    //TO DO
//
//    //GypsumBoard
//    List<GypsumBoard> getAllGypsumBoards();
//
//    List<GypsumBoard>getAllGypsumBoardsByDate(int monthIndex, int year);

}
