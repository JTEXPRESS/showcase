package com.jnt.guide;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GuideData {
    private final List<View> listTarget;
    private final List<String> listTitle;
    private final List<String> listContent;
    private final List<String> listLink;
    private final List<Boolean> listIsNew;

    public GuideData() {
        listTarget  = new ArrayList<>();
        listTitle   = new ArrayList<>();
        listContent = new ArrayList<>();
        listLink    = new ArrayList<>();
        listIsNew   = new ArrayList<>();
    }

    public void add(View target, String content) { add(target, null, content); }

    public void add(View target, String title, String content) { add(target, title, content, null); }

    public void add(View target, String title, String content, String link) { add(target, title, content, link, false); }

    public void add(View target, String title, String content, boolean isNew) { add(target, title, content, null, isNew); }

    public void add(View target, String title, String content, String link, boolean isNew) {
        listTarget.add(target);
        listTitle.add(title);
        listContent.add(content);
        listLink.add(link);
        listIsNew.add(isNew);
    }

    public int getCount() { return this.listTarget.size(); }

    public View getTarget(int position) { return this.listTarget.get(position); }

    public String getTitle(int position) { return this.listTitle.get(position); }

    public String getContent(int position) { return this.listContent.get(position); }

    public String getLink(int position) { return this.listLink.get(position); }

    public Boolean isNew(int position) { return this.listIsNew.get(position); }
}
