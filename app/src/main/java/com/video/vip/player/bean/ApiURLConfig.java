package com.video.vip.player.bean;

import java.io.Serializable;
import java.util.Objects;

public class ApiURLConfig implements Serializable {
    private int id;
    private String code;
    private String title;
    private int is_default;

    public ApiURLConfig() {
    }

    public ApiURLConfig(String code, String title, int is_default) {
        this.code = code;
        this.title = title;
        this.is_default = is_default;
    }

    public ApiURLConfig(int id, String code, String title, int is_default) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.is_default = is_default;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiURLConfig that = (ApiURLConfig) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, title);
    }
}
