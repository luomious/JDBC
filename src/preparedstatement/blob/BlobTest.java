package preparedstatement.blob;

import org.hamcrest.core.Is;
import org.junit.Test;
import preparedstatement.crud.bean.Customer;
import preparedstatement.util.JDBCUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.time.format.FormatStyle;

//测试preparedStatement操作Blob类型的数据,图片
public class BlobTest {
    //向数据表中添加Blob类型字段
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into user(name ,sex,photo) values(?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setObject(1, "李白");
        ps.setObject(2, 1);
        FileInputStream is = new FileInputStream(new File(""));
        ps.setObject(3,is);
        ps.execute();
        JDBCUtils.closeResource(connection, ps);

    }

    @Test
    public void testQuery() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id,name,sex,photo from user where id=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 6);

            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int sex = rs.getInt(3);

                Customer customer = new Customer(id, name, sex);
                System.out.println(customer);

                Blob photo = rs.getBlob("photo");
                InputStream is = photo.getBinaryStream();
                FileOutputStream fos = new FileOutputStream("tupian.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            is.close();
//            fos.close();
            JDBCUtils.closeResource(connection, ps, rs);
        }




    }

}


