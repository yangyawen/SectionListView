package com.example.youngjasmine.sectionlistview;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngjasmine on 16/3/14.
 */
public abstract class SimpleSectionAdapter<T> extends BaseAdapter implements AdapterView.OnItemClickListener {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private LayoutInflater layoutInflater;
    private int headerResource;
    private int itemResource;

    /* 所有节的唯一集合 */
    private List<SectionItem<T>> sections;
    /* 节的分组，按其初始位置设置键 */
    private SparseArray<SectionItem<T>> keyedSections;

    public SimpleSectionAdapter(ListView parent, int headerResId, int itemResId){
        layoutInflater = LayoutInflater.from(parent.getContext());
        headerResource = headerResId;
        itemResource = itemResId;

        // 创建包含自动排序键的集合
        sections = new ArrayList<SectionItem<T>>();
        keyedSections = new SparseArray<SectionItem<T>>();

        // 将自身附加为列表的单击处理程序
        parent.setOnItemClickListener(this);
        
    }

    /* 向列表添加新的带标题的节，
    或者更新现有的节
     */
    public void addSection(String title, T[] items){
        SectionItem<T> sectionItem = new SectionItem<T>(title, items);
        // 添加节，替换具有相同标题的现有节
        int currentIndex = sections.indexOf(sectionItem);
        if(currentIndex >= 0){
            sections.remove(sectionItem);
            sections.add(currentIndex, sectionItem);

        }else{
            sections.add(sectionItem);
        }
        // 对最新的集合排序
        reorderSections();
        // 表明视图数据已改变
        notifyDataSetChanged();
    }

    /*
    将带有初始全局位置的节标记为可引用的键
     */
    private void reorderSections(){
        keyedSections.clear();
        int startPosition = 0;
        for(SectionItem<T> item:sections){
            keyedSections.put(startPosition, item);
            startPosition += item.getCount();
        }
    }

    @Override
    public int getCount(){
        int count = 0;
        for(SectionItem<T> item:sections){
            count += item.getCount();
        }

        return count;
    }

    @Override
    public int getViewTypeCount(){
        // 两种视图类型，头部和项
        return 2;
    }

    @Override
    public T getItem(int position){
        return findSectionItemAtPosition(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public boolean areAllItemsEnabled(){
        return false;
    }

    @Override
    public boolean isEnabled(int position){
        return !isHeaderAtPosition(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        switch (getItemViewType(position)){
            case TYPE_HEADER:
                return getHeaderView(position, convertView, parent);
            case TYPE_ITEM:
                return getItemView(position, convertView, parent);
            default:
                return convertView;
        }
    }

    private View getHeaderView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = layoutInflater.inflate(headerResource, parent, false);

        }

        SectionItem<T> item = keyedSections.get(position);
        TextView textView = (TextView)convertView.findViewById(android.R.id.text1);

        textView.setText(item.getTitle());
        return convertView;
    }

    private View getItemView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = layoutInflater.inflate(itemResource, parent, false);

        }

        T item = findSectionItemAtPosition(position);
        TextView textView = (TextView)convertView.findViewById(android.R.id.text1);

        textView.setText(item.toString());
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        T item = findSectionItemAtPosition(position);
        if(item!=null){
            onSectionItemClick(item);
        }
    }

    public abstract void onSectionItemClick(T item);

    private boolean isHeaderAtPosition(int position){
        for(int i=0; i < keyedSections.size(); i++){
            if(position == keyedSections.keyAt(i)){
                return true;
            }
        }
        return false;
    }

    private T findSectionItemAtPosition(int position){
        int firstIndex, lastIndex;
        for(int i=0; i<keyedSections.size(); i++){
            firstIndex = keyedSections.keyAt(i);
            lastIndex =firstIndex + keyedSections.valueAt(i).getCount();
            if(position >= firstIndex && position < lastIndex){
                int sectionPosition = position - firstIndex - 1;
                return keyedSections.valueAt(i).getItem(sectionPosition);
            }
        }
        return null;
    }
}
