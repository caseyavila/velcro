package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.NewsActivity;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(casey.getNews(position).getTitle());
        holder.author.setText(casey.getNews(position).getAuthor());
        holder.date.setText(casey.getNews(position).getDate());
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, NewsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index", position);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return casey.getNumberOfNews();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        MaterialTextView title;
        MaterialTextView author;
        MaterialTextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.news_card);
            title = itemView.findViewById(R.id.news_title);
            author = itemView.findViewById(R.id.news_author);
            date = itemView.findViewById(R.id.news_date);
        }
    }
}
