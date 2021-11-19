package preparedstatement.crud;

import org.junit.Test;
import preparedstatement.crud.bean.Customer;
import preparedstatement.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//针对Customer表通用查询操作
public class CustomerForQuery {
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
//p18
