package com.example.easyword.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyword.R;
import com.example.easyword.enity.TotalWord;

import java.util.List;

public class SearchWordAdapter extends BaseAdapter {
    private Context mContext;
    private List<TotalWord> wordList;

    public SearchWordAdapter(Context mContext, List<TotalWord> wordList) {
        this.mContext = mContext;
        this.wordList = wordList;
    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search,null);
            holder = new ViewHolder();
            holder.tv_word = convertView.findViewById(R.id.tv_search_word);
            holder.tv_meaning = convertView.findViewById(R.id.tv_search_word_meaning);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();//取出控件
        }
        TotalWord showword = wordList.get(position);
        holder.tv_word.setText(showword.getWord());
        holder.tv_meaning.setText(showword.getMeaning());
        return convertView;
    }
    public final class ViewHolder{
        public TextView tv_word;
        public TextView tv_meaning;
    }
}
