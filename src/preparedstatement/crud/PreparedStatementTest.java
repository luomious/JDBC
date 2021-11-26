package preparedstatement.crud;

import org.junit.Test;
import preparedstatement.crud.bean.Customer;
import preparedstatement.util.JDBCUtils;
import statement.crud.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

//使用PreparedStatement替换Statement,解决SQL注入的问题
public class PreparedStatementTest {

    @Test
    public void testLogin() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("用户名：");
        String user = scanner.next();
        System.out.print("密   码：");
        String password = scanner.next();

        String sql = "SELECT id,name from user where user=? and sex=? and id=?";


        Customer user1 = getInstance(Customer.class, sql, user, password,5);
        if (user1 != null) {
            System.out.println("登陆成功");

        } else {
            System.out.println("用户名或密码错误");
        }

    }




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
