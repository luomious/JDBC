package preparedstatement.crud.bean;

import java.util.Date;

public class Customer {
    private int id;
    private String name;
    private int sex;
    private int age;
    private int level;
    private int balance;
    private Date birth;

    public Customer(int id, String name, int sex, Date birth) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birth = birth;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void Customer(String name, int sex, Date birth) {
        this.name = name;
        this.sex = sex;
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", level=" + level +
                ", balance=" + balance +
                ", birth=" + birth +
                '}';
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Customer(int id, String name, int sex, int age, int level) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.level = level;
    }

    public Customer(int id, String name, int sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

}
