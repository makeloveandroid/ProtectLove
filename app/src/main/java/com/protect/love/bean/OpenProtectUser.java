package com.protect.love.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class OpenProtectUser {
    @Id(autoincrement = true)
    @Property(nameInDb = "id")
    public Long id;
    @Property(nameInDb = "wxId")
    public String wxId;
    @Property(nameInDb = "isOpen")
    public Boolean isOpen;
    @Generated(hash = 144713218)
    public OpenProtectUser(Long id, String wxId, Boolean isOpen) {
        this.id = id;
        this.wxId = wxId;
        this.isOpen = isOpen;
    }
    @Generated(hash = 744824122)
    public OpenProtectUser() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getWxId() {
        return this.wxId;
    }
    public void setWxId(String wxId) {
        this.wxId = wxId;
    }
    public Boolean getIsOpen() {
        return this.isOpen;
    }
    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }
}
