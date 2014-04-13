package com.coffeeroom.dao.jpa;

import com.coffeeroom.dao.BoardDao;
import com.coffeeroom.entity.Board;
import com.coffeeroom.entity.Category;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class JpaBoardDao implements BoardDao {
    @PersistenceContext(unitName = "CoffeeRoomPU")
    private EntityManager em;

    @Override
    public List<Board> getAllBoards() {
        return em.createNamedQuery("Board.findAll").getResultList();
    }
    
    @Override
    public List<Board> findRange(int[] range) {
        Query q = em.createNamedQuery("Board.findAll");
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    @Override
    public Board findBoardById(int boardId) {
        return em.find(Board.class, boardId);
    }

    @Override
    public Board addBoard(Board board) {
        em.persist(board);
        return board;
    }

    @Override
    public Board updateBoard(Board board) {
        em.merge(board);
        return board;
    }

    @Override
    public void removeBoard(Board board) {
        Board mergedBoard = em.merge(board);
        em.remove(mergedBoard);
    }

    @Override
    public List<Board> findByCategory(Category category) {
        return em.createNamedQuery("Board.findByCategoryId")
                .setParameter("categoryId", category.getId())
                .getResultList();
    }
    
    @Override
    public int count() {
        return em.createNamedQuery("Board.findAll").getResultList().size();
    }
}
