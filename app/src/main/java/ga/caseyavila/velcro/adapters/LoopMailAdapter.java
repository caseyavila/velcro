package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailAdapter extends RecyclerView.Adapter<LoopMailAdapter.ViewHolder> {

    private final Context context;

    public LoopMailAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loopmail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (casey.isLoopMailRead(1, position)) {
            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.textMediumEmphasis));
            holder.subject.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.textHighEmphasis));
            holder.subject.setTypeface(null, Typeface.BOLD);
        }
        holder.subject.setText(casey.getLoopMailSubject(1, position));
        holder.sender.setText(casey.getLoopMailSender(1, position));
    }

    @Override
    public int getItemCount() {
        return casey.getNumberOfLoopMails(1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;

        MaterialTextView subject;
        MaterialTextView sender;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.loopmail_card);

            subject = itemView.findViewById(R.id.loopmail_subject);
            sender = itemView.findViewById(R.id.loopmail_sender);
        }
    }
}
