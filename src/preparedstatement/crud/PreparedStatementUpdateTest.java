package preparedstatement.crud;

import connection.ConnectionTest;
import org.junit.Test;
import preparedstatement.util.JDBCUtils;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
//p16
//实现对数据表的增删改查操作
public class PreparedStatementUpdateTest {
    @Test
    public void testUpdate1() {
        String sql = "delete from user where id=?";
        String sql1="insert into user(id,name,sex,age,level) value(5,?,1,13,1)";
        update(sql1, "小红");
    }


    //通用增删改操作
    public void update(String sql, Object... args) {//sql中有几个占位符与可变形参的长度相同


        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            JDBCUtils.closeResource(connection, ps);
        }



    }




    @Test
    public void testUpdate() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //获取连接
            connection = JDBCUtils.getConnection();
            String sql = "update user1 set name=? where id=?";
            ps = connection.prepareStatement(sql);

            ps.setObject(1, "adc");
            ps.setObject(2, 2);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, ps);
        }


    }

    //添加数据
    @Test
    public void testInsert() throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");
            //2.加载驱动
            Class.forName(driverClass);
            //3.获取连接
            connection = DriverManager.getConnection(url, user, password);
            System.out.println(connection);
            //4.获取PreparedStatement实例
            String sql = "insert into user1(name,email)value(?,?)";//?占位符
            ps = connection.prepareStatement(sql);
            //填充占位符
            ps.setString(1, "e");
            ps.setString(2, "2134@wqer");


            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
