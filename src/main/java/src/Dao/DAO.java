package src.Dao;

import java.util.HashSet;
import java.util.List;

public interface DAO <T>{
     void save(List<T> list);
     List<T> List();
}
