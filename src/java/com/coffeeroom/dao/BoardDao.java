package com.coffeeroom.dao;

import com.coffeeroom.entity.Board;
import com.coffeeroom.entity.Category;
import java.util.List;
import javax.ejb.Local;

@Local
public interface BoardDao {
    public List<Board> getAllBoards();
    
    public List<Board> findRange(int[] range);

    public Board findBoardById(int boardId);
    
    public Board addBoard(Board board);

    public Board updateBoard(Board board);
    
    public void removeBoard(Board board);

    public List<Board> findByCategory(Category category);
    
    public int count();
}
