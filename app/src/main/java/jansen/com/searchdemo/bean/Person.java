package jansen.com.searchdemo.bean;

import java.io.Serializable;

/**
 * Created Jansen on 2016/6/28.
 */
public class Person implements Cloneable, Serializable {
    private String name;
    private int age;

    /**
     * 除了基本数据类型，其他的类型都要克隆一遍
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            return (Person) super.clone();
        } catch (CloneNotSupportedException mE) {
            mE.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        name = mName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int mAge) {
        age = mAge;
    }
}
