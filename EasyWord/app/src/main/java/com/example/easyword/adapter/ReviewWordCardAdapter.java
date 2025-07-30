package com.example.easyword.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyword.R;
import com.example.easyword.enity.LearnWord;
import com.example.easyword.enity.ReviewWord;

import java.util.List;

public class ReviewWordCardAdapter extends RecyclerView.Adapter<ReviewWordCardAdapter.ReviewWordViewHolder>{
    private Context context;
    private List<ReviewWord> wordList;
    private OnReviewWordClickListener listener;

    public ReviewWordCardAdapter(Context context, List<ReviewWord> wordList) {
        this.context = context;
        this.wordList = wordList;
    }

    public void setListener(OnReviewWordClickListener listener) {
        this.listener = listener;
    }

    // ViewHolder 类
    public static class ReviewWordViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_word;
        private TextView tv_meaning;
        private TextView tv_sentence;
        private TextView tv_sentence_meaning;
        private TextView tv_word_count;
        private Button btn_remember;
        private Button btn_little;
        private Button btn_forget;
        public ReviewWordViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_word = itemView.findViewById(R.id.tv_review_menu_word);
            tv_meaning = itemView.findViewById(R.id.tv_review_menu_meaning);
            tv_sentence = itemView.findViewById(R.id.tv_review_menu_sentence);
            tv_sentence_meaning= itemView.findViewById(R.id.tv_review_menu_sentence_meaning);
            tv_word_count =itemView.findViewById(R.id.tv_review_menu_count);
            btn_remember = itemView.findViewById(R.id.btn_review_sure);
            btn_little = itemView.findViewById(R.id.btn_review_little);
            btn_forget = itemView.findViewById(R.id.btn_review_forget);

        }

        // 绑定数据到视图
        public void bind(ReviewWord word,int TotalCount,int position) {
            Log.d("kun1",word.getWord()+TotalCount+position);
            tv_word.setText(word.getWord());
            tv_meaning.setText(word.getMeaning());
            tv_sentence.setText(word.getSentence());
            tv_sentence_meaning.setText(word.getSentence_meaning());
            tv_word_count.setText("第" + String.valueOf(position+1) + "/" + TotalCount + "词");
        }
    }

    @NonNull
    @Override
    public ReviewWordCardAdapter.ReviewWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_word_card_review, parent, false);
        return new ReviewWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewWordCardAdapter.ReviewWordViewHolder holder, int position) {
        ReviewWord word = wordList.get(position);
        holder.bind(word, wordList.size(),position);
        // 设置点击事件
        holder.btn_remember.setOnClickListener(v -> {
            if (listener != null) {
                // 获取当前单词并调用接口方法
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onReviewWordClickRemenber(wordList.get(adapterPosition),position);
                }
            }
        });
        // 设置点击事件
        holder.btn_little.setOnClickListener(v -> {
            if (listener != null) {
                // 获取当前单词并调用接口方法
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onReviewWordClickLittle(wordList.get(adapterPosition),position);
                }
            }
        });
        // 设置点击事件
        holder.btn_forget.setOnClickListener(v -> {
            if (listener != null) {
                // 获取当前单词并调用接口方法
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onReviewWordClickForget(wordList.get(adapterPosition),position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // 返回单词列表的大小
        return wordList.size();
    }

    public interface OnReviewWordClickListener {
        void onReviewWordClickRemenber(ReviewWord word,int position);
        void onReviewWordClickLittle(ReviewWord word,int position);
        void onReviewWordClickForget(ReviewWord word,int position);
    }
}
