package com.jnt.showcase;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GuideData {
    private final List<View> listTarget;
    private final List<String> listTitle;
    private final List<String> listContent;
    private final List<String> listLink;

    public GuideData() {
        listTarget  = new ArrayList<>();
        listTitle   = new ArrayList<>();
        listContent = new ArrayList<>();
        listLink    = new ArrayList<>();
    }

    public void add(View target, String content) { add(target, null, content); }

    public void add(View target, String title, String content) { add(target, title, content, null); }

    public void add(View target, String title, String content, String link) {
        listTarget.add(target);
        listTitle.add(title);
        listContent.add(content);
        listLink.add(link);
    }

    public int getCount() { return this.listTarget.size(); }

    public View getTarget(int position) { return this.listTarget.get(position); }

    public String getTitle(int position) { return this.listTitle.get(position); }

    public String getContent(int position) { return this.listContent.get(position); }

    public String getLink(int position) { return this.listLink.get(position); }
}
