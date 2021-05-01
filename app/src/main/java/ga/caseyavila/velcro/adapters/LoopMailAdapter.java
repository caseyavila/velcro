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
import ga.caseyavila.velcro.fragments.LoopMailFragment;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailAdapter extends RecyclerView.Adapter<LoopMailAdapter.ViewHolder> {

    private final Context context;
    private final int mailBox;
    private final LoopMailFragment fragment;

    public LoopMailAdapter(Context context, int mailBox, LoopMailFragment fragment) {
        this.context = context;
        this.mailBox = mailBox;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == LOOP_MAIL_VIEW_TYPES.NORMAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loopmail, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_load_more, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == LOOP_MAIL_VIEW_TYPES.NORMAL) {
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
        } else {
            holder.cardView.setOnClickListener(view -> {
                fragment.loadMore();
            });
        }
    }

    @Override
    public int getItemCount() {
        // Add one for footer card (Load more mail)
        return casey.getMailBox(mailBox).getNumberOfLoopMails() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // Position starts at 0, so subtract one from the number of loop mails
        if (position <= casey.getMailBox(mailBox).getNumberOfLoopMails() - 1) {
            return LOOP_MAIL_VIEW_TYPES.NORMAL;
        } else {
            return LOOP_MAIL_VIEW_TYPES.FOOTER;
        }
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

    private static class LOOP_MAIL_VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int FOOTER = 1;
    }
}
