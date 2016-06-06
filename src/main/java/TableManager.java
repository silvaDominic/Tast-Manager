/**
 * Created by reclaimer on 6/5/16.
 */
public interface TableManager {

    public void updateTable(String sql, int id);

    public void deleteElementFromTable(String sql, int id);

    public void insertElementIntoTable(String sql, int id);

    public void createTable(String sql);
}
