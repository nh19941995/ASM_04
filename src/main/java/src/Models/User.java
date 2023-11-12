package src.Models;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String name;  //  Chứa thông tin tên của người dùng.
    private CCCD customerId;  // Mã căn cước công dân định danh của khách hàng

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CCCD getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CCCD customerId) {
        this.customerId = customerId;
    }

    public User(String name, CCCD customerId) {
        this.name = name;
        this.customerId = customerId;
    }

    public User() {
    }


    // phương thức so sánh chỉ so sánh trường id

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;

        return this.getCustomerId().getId() == user.getCustomerId().getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId().getId());
    }




}

