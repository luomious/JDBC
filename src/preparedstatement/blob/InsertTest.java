package preparedstatement.blob;

import org.junit.Test;
import preparedstatement.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

//实现批量数据的操作
public class InsertTest {

    @Test
    public void testInsert1() {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            long start = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name )values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <= 20000; i++) {
                ps.setObject(1, "name_" + 1);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费时间" + (end - start));//80秒

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }

    }

    //addBatch(),executeBatch(),clearBatch()
    //mysql服务器默认关闭批处理,需要手动开启,  demo?rewriteBatchedStatements=true,导入新的jar包
    @Test
    public void testInsert2() {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            long start = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name )values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <= 20000; i++) {
                ps.setObject(1, "name_" + 1);
                //1.攒sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //执行
                    ps.executeBatch();
                    //清空
                    ps.clearBatch();

                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费时间" + (end - start));//80秒

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
    }

    @Test
    public void testinsert3(){
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            long start = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name )values(?)";
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(sql);
            for (int i = 0; i <= 20000; i++) {
                ps.setObject(1, "name_" + 1);
                //1.攒sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //执行
                    ps.executeBatch();
                    //清空
                    ps.clearBatch();

                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费时间" + (end - start));//80秒

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
    }

}
