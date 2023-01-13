package com.video.vip.player.bean;

import java.util.Objects;

public class SelectItem {
    public SelectItem() {

    }

    public SelectItem(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String code;
    public String title;

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

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectItem that = (SelectItem) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, title);
    }
}
