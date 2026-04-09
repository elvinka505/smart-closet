package ru.isgaij.smartcloset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.isgaij.smartcloset.entity.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i WHERE i.user.id = :userId AND i.season = :season")
    List<Item> findByUserIdAndSeason(@Param("userId") Long userId, @Param("season") String season);

    @Query("SELECT i FROM Item i WHERE i.user.id = :userId AND i.price > (SELECT AVG(i2.price) FROM Item i2 WHERE i2.user.id = :userId)")
    List<Item> findItemsAboveAveragePrice(@Param("userId") Long userId);
}
