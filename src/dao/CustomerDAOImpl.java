package dao;

import preparedstatement.crud.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {


    @Override
    public void insert(Connection conn, Customer cust) {
        String sql = "insert into user(name,sex,birth)values(?,?,?)";
        update(conn, sql, cust.getName(), cust.getSex(), cust.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from user where id = ?";
        update(conn, sql, id);
    }

    @Override
    public void update(Connection conn, Customer cust) {
        String sql = "update user set name = ?,sex= ?,birth = ? where id = ?";
        update(conn, sql,cust.getName(),cust.getSex(),cust.getBirth(),cust.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,birth from user where id = ?";
        Customer customer = getInstance(conn,Customer.class, sql,id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,sex,birth from user";
        List<Customer> list = getForList(conn, Customer.class, sql);
        return list;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from user";
        return getValue(conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from user";
        return getValue(conn, sql);
    }
}
