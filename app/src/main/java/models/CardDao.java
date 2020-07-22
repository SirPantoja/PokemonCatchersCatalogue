package models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CardDao {

    @Query("SELECT * FROM Card WHERE setCode=:setCode")
    List<Card> getAll(String setCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCard(Card... cards);
}
