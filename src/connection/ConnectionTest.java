package connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//
public class ConnectionTest {
    @Test
    public void testConnection1() throws SQLException {

        Driver driver = new com.mysql.jdbc.Driver();
        //"jdbc:mysql://ip地址:mysql端口号/demo数据库";
        String url = "jdbc:mysql://localhost:3306/demo";

        //将用户名和密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");
        Connection conn = driver.connect(url, info);
        System.out.println(conn);

    }

    @Test
    public void test2() throws Exception {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/demo";

        //提供用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");

        //获取连接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);

    }

    @Test
    public void testConnection3() throws Exception {
        //1.获取Driver实现类的对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        String url = "jdbc:mysql://localhost:3306/demo";
        String user = "root";
        String password = "123456";

        //注册驱动
        DriverManager.registerDriver(driver);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

    @Test
    //只加载驱动,不用注册驱动
    public void testConnection4() throws Exception {
        String url = "jdbc:mysql://localhost:3306/demo";
        String user = "root";
        String password = "123456";

        //获取Driver实现类的对象
        Class.forName("com.mysql.jdbc.Driver");
//        Driver driver = (Driver) clazz.newInstance();可以省略

        //mysql会自动注册
        //注册驱动
//        DriverManager.registerDriver(driver);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);


    }

    //最终版,将数据库连接需要的4个基本信息放在配置文件中,通过读取配置文件的方式来获取连接
    @Test
    public void getConnection5() throws Exception {
        //1.读取配置文件的基本信息
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
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

//p12


}
