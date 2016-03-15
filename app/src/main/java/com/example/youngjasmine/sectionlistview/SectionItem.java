package com.example.youngjasmine.sectionlistview;

/**
 * Created by youngjasmine on 16/3/14.
 */
public class SectionItem<T> {

    private String title;
    private T[] items;

    public SectionItem(String title, T[] items){
        if(title == null) title = "";
        this.title = title;
        this.items = items;
    }

    public int getCount(){
        return (items == null ? 1:1+items.length);
    }

    @Override
    public boolean equals(Object object){
        //如果两个节有相同的标题，则它们相等
        if(object != null && object instanceof SectionItem){
            return ((SectionItem)object).getTitle().equals(title);

        }
        return false;
    }

    public String getTitle(){
        return title;
    }

    public T getItem(int index){
        return items[index];
    }
}
