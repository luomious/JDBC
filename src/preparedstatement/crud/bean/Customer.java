package preparedstatement.crud.bean;

public class Customer {
    private int id;
    private String name;
    private int sex;
    int age;
    int level;

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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
