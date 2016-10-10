package com.ctsi.push.message;

/**
 * Created by doulala on 16/9/28.
 *
 * 通知内容,主要负责notice相关的内容
 */

public class NoticeContent {

    private String title;
    private String subTitle;

    public NoticeContent(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}





