/*
 * Copyright (C)  Justson(https://github.com/Justson/AgentWeb)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.video.vip.player.common;

/**
 * @author cenxiaozhong
 * @date 2018/7/15
 * @since 1.0.0
 */
public class GuideItemEntity {

	private String guideTitle;
	private int guideDictionary;
	private int extra;
	private int image;
	private String url;

	public GuideItemEntity(String guideTitle, int guideDictionary, int image,String url) {
		this.guideTitle = guideTitle;
		this.guideDictionary = guideDictionary;
		this.image = image;
		this.url = url;
	}

	public String getGuideTitle() {
		return guideTitle;
	}

	public void setGuideTitle(String guideTitle) {
		this.guideTitle = guideTitle;
	}

	public int getGuideDictionary() {
		return guideDictionary;
	}

	public void setGuideDictionary(int guideDictionary) {
		this.guideDictionary = guideDictionary;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
