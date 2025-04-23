package Agriflow.Service;



import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    public void create(T t) throws SQLException;
    public T read(int id) throws SQLException;
    public void update(T t) throws SQLException;
    public void delete(int id) throws SQLException;
    public List<T> readAll() throws SQLException;
}