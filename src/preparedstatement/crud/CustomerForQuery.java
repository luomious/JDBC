package preparedstatement.crud;

import org.junit.Test;
import preparedstatement.crud.bean.Customer;
import preparedstatement.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

//针对Customer表通用查询操作
public class CustomerForQuery {
    @Test
    public void testQueryCustomer() {
        //如果数据表的列名和类名不一样,列可以起别名,但必须和类的名一样
        String sql = "select id id,name name,sex sex from user where id=?";
        Customer customer = queryFofCustomer(sql, 6);
        System.out.println(customer);

    }

    //针对Customer表通用查询操作
    public Customer queryFofCustomer(String sql, Object... args)  {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据:ResultSetMetadata
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集的列数
            int columnCount = rsmd.getColumnCount();


            if (rs.next()) {
                Customer customer = new Customer();
                //处理结果集一行数据的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取类的列名
                    //获取类的别名
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名,获取类的列名
                    //获取类的别名getColumnLabel,注意!!!!!!!!!!!!!!!!!!!!!
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //给customer赋予属性
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columnValue);//给customer赋予属性

                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }

        return null;
    }



    @Test
    public void testQuery1()  {


        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id,name,sex from user where id=?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, 5);
            //执行,返回结果集
            resultSet = ps.executeQuery();
            //处理结果集
            if (resultSet.next()) {//判断结果集下一条是否有数据
                //获取当前数据的字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int sex = resultSet.getInt(3);
    //            Object[] data = new Object[]{id, name, sex};

                Customer customer = new Customer(id, name, sex);
                System.out.println(customer);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, resultSet);
        }

    }
}


