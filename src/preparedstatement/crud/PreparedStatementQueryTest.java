package preparedstatement.crud;

import org.junit.Test;
import preparedstatement.crud.bean.Customer;
import preparedstatement.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

//使用PreparedStatement实现针对于不同表的通用查询操作,
public class PreparedStatementQueryTest {
    @Test
    public void testGetForList() {
        String sql = "select id,name,sex from user where id=?";
        List<Customer> list = getForList(Customer.class, sql, 7);

        list.forEach(System.out::println);

    }


    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据:ResultSetMetadata
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集的列数
            int columnCount = rsmd.getColumnCount();


            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.newInstance();

                //处理结果集一行数据的每一列
                for (int i = 0; i < columnCount; i++) {

                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名,获取类的列名
                    //获取类的别名getColumnLabel,注意!!!!!!!!!!!!!!!!!!!!!
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //给t赋予属性
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);//给customer赋予属性

                }
                list.add(t);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }

        return null;


    }


    @Test
    public void testGetInstance() {
        String sql="select id id,name name,sex sex from user where id=?";
        Customer instance = getInstance(Customer.class, sql, 6);
        System.out.println(instance);

        String sql1="select id , name, sex from student where id=?";
        Customer instance1 = getInstance(Customer.class, sql, 6);
        System.out.println(instance1);

    }

    //返回表中的一条记录
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据:ResultSetMetadata
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集的列数
            int columnCount = rsmd.getColumnCount();


            if (rs.next()) {
                T t = clazz.newInstance();

                //处理结果集一行数据的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取类的列 名
                    //获取类的别 名
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名,获取类的列名
                    //获取类的别名getColumnLabel,注意!!!!!!!!!!!!!!!!!!!!!
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //给t赋予属性
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);//给customer赋予属性

                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }

        return null;

    }

}


