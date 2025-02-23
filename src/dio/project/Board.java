package dio.project;

import static dio.project.GameStatusEnum.COMPLETE;
import static dio.project.GameStatusEnum.INCOMPLETE;
import static dio.project.GameStatusEnum.NON_STARTED;
import java.util.Collection;
import java.util.List;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }
    
    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus(){
        if(spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return NON_STARTED;
        }

        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasError(){
        if(getStatus() == NON_STARTED){
            return false;
        }

        return spaces.stream().flatMap(Collection::stream)
        .anyMatch(s -> nonNull(s.getActual()) && s.getActual() != (s.getExpected()));
    }

    public boolean changeValue(final int col, final int row , final int value){
        var space = spaces.get(row).get(col);
        if (space.isFixed()) {
            return false;
        }

        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row){
        var space = spaces.get(row).get(col);
        if (space.isFixed()) {
            return false;
        }

        space.clearSpace();
        return true;
    }

    public void reset(){
        spaces.forEach(s -> s.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished(){
        return !hasError() && getStatus() == COMPLETE;
    }
}
