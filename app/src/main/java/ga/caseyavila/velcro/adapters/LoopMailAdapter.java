package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.LoopMailMessageActivity;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailAdapter extends RecyclerView.Adapter<LoopMailAdapter.ViewHolder> {

    private final Context context;
    private final int mailBox;

    public LoopMailAdapter(Context context, int mailBox) {
        this.context = context;
        this.mailBox = mailBox;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loopmail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (casey.getMailBox(mailBox).getLoopmail(position).isRead()) {
            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.textMediumEmphasis));
            holder.subject.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.textHighEmphasis));
            holder.subject.setTypeface(null, Typeface.BOLD);
        }
        holder.subject.setText(casey.getMailBox(mailBox).getLoopmail(position).getSubject());
        holder.sender.setText(casey.getMailBox(mailBox).getLoopmail(position).getSender());
        holder.sendDateTime.setText(casey.getMailBox(mailBox).getLoopmail(position).getSendDateTime());
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, LoopMailMessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("mail_box", mailBox);
            bundle.putInt("index", position);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return casey.getMailBox(mailBox).getNumberOfLoopMails();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        MaterialTextView subject;
        MaterialTextView sender;
        MaterialTextView sendDateTime;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.loopmail_card);
            subject = itemView.findViewById(R.id.loopmail_subject);
            sender = itemView.findViewById(R.id.loopmail_sender);
            sendDateTime = itemView.findViewById(R.id.loopmail_send_date_time);
        }
    }
}
