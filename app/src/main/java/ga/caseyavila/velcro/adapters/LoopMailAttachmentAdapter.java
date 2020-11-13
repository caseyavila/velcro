package ga.caseyavila.velcro.adapters;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailAttachmentAdapter extends RecyclerView.Adapter<LoopMailAttachmentAdapter.ViewHolder> {

    private final int mailBox;
    private final int loopMail;

    public LoopMailAttachmentAdapter(int mailBox, int loopMail) {
        this.mailBox = mailBox;
        this.loopMail = loopMail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_attachment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // I know it looks confusing, but it just creates a hyperlink using getTitle and getUrl
        holder.attachment.setText(Html.fromHtml(String.format("<a href='%s'>%s</a>", casey.getMailBox(mailBox).getLoopmail(loopMail).getAttachment(position).getUrl(), casey.getMailBox(mailBox).getLoopmail(loopMail).getAttachment(position).getTitle())));
        holder.attachment.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return casey.getMailBox(mailBox).getLoopmail(loopMail).getNumberOfAttachments();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView attachment;

        public ViewHolder(View itemView) {
            super(itemView);

            attachment= itemView.findViewById(R.id.attachment);
        }
    }
}
