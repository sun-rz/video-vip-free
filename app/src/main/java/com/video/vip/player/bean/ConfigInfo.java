package com.video.vip.player.bean;

import com.video.vip.player.common.GuideItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ConfigInfo {

    private List<ApiURLConfig> api = new ArrayList<>();
    private List<GuideItemEntity> sites = new ArrayList<>();

    public ConfigInfo() {
    }

    public List<ApiURLConfig> getApi() {
        return api;
    }

    public void setApi(List<ApiURLConfig> api) {
        this.api = api;
    }

    public List<GuideItemEntity> getSites() {
        return sites;
    }

    public void setSites(List<GuideItemEntity> sites) {
        this.sites = sites;
    }
}
