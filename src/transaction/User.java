package transaction;

public class User {
    private String name;
    private int id;
    private int sex;
    private int balance;

    public User() {
    }

    public User(String name, int id, int sex, int balance) {
        this.name = name;
        this.id = id;
        this.sex = sex;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", sex=" + sex +
                ", balance=" + balance +
                '}';
    }
}
