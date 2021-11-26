package transaction;

import org.junit.Test;
import preparedstatement.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

//数据库事务问题

/**
 *    1.什么叫数据库事务？
 *  * 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
 *  * 		> 一组逻辑操作单元：一个或多个DML操作。
 *  *
 *  * 2.事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 *  * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存
 *  * 下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *  *
 *  * 3.数据一旦提交，就不可回滚
 *  *
 *  * 4.哪些操作会导致数据的自动提交？
 *  * 		>DDL操作一旦执行，都会自动提交。
 *  * 			>set autocommit = false 对DDL操作失效
 *  * 		>DML默认情况下，一旦执行，就会自动提交。
 *  * 			>我们可以通过set autocommit = false的方式取消DML操作的自动提交。
 *  * 		>默认在关闭连接时，会自动的提交数据
 *  */
public class TransactionTest {

    @Test
    public void testupdate() {
        String sql1 = "update user set balance = balance-100 where id=?";
        update(sql1, 5);


        //模拟网络异常
        System.out.println(10 / 0);

        String sql2 = "update user set balance = balance+100 where id=?";
        update(sql2, 7);

        System.out.println("转账成功");

    }

    public int update(String sql, Object... args) {//sql中有几个占位符与可变形参的长度相同

        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            //资源关闭
            JDBCUtils.closeResource(connection, ps);
        }

        return 0;
    }


    //********************考虑数据库事务后的转账操作*********************
    @Test
    public void testUpdateWithTx() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            System.out.println(conn.getAutoCommit());//true
            //1.取消数据的自动提交
            conn.setAutoCommit(false);

            String sql1 = "update user set balance = balance - 100 where id = ?";
            update(conn, sql1, 5);

            //模拟网络异常
            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where id= ?";
            update(conn, sql2, 7);

            System.out.println("转账成功");

            //2.提交数据
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            //3.回滚数据
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {

            JDBCUtils.closeResource(conn, null);
        }

    }

    //version2.0,考虑上事务
    public int update(Connection conn, String sql, Object... args) {//sql中有几个占位符与可变形参的长度相同

        PreparedStatement ps = null;
        try {
            // 1.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //修改其为自动提交数据,加在操作方法里
            //主要针对于使用数据库连接池的使用
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //资源关闭
            JDBCUtils.closeResource(conn, ps);
        }

        return 0;
    }


    @Test
    public void testTransactionSelect() throws Exception {

        Connection conn = JDBCUtils.getConnection();
        //获取当前连接的隔离级别
        System.out.println(conn.getTransactionIsolation());//默认为4
        //设置数据库的隔离级别：
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消自动提交数据
        conn.setAutoCommit(false);

        String sql = "select name,sex,balance from user where id = ?";

        User user = getInstance(conn, User.class, sql, 7);

        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        //取消自动提交数据
        conn.setAutoCommit(false);
        String sql = "update user set balance = ? where id = ?";
        update(conn, sql, 5000, 7);

        Thread.sleep(15000);
        System.out.println("修改结束");
    }

    //**********************************************************************************************
    //通用的查询操作，用于返回数据表中的一条记录（version 2.0：考虑上事务）
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columValue = rs.getObject(i + 1);

                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);

        }

        return null;

    }
}
