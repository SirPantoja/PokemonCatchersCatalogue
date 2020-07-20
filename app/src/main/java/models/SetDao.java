package models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SetDao {

    @Query("SELECT * FROM [Set]")
    List<Set> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSet(Set... sets);
}
