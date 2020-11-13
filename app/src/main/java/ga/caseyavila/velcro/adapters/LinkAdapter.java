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

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {

    private final int mailBox;
    private final int loopMail;

    public LinkAdapter(int mailBox, int loopMail) {
        this.mailBox = mailBox;
        this.loopMail = loopMail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_link, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // I know it looks confusing, but it just creates a hyperlink using getTitle and getUrl
        holder.link.setText(Html.fromHtml(String.format("<a href='%s'>%s</a>", casey.getMailBox(mailBox).getLoopmail(loopMail).getLink(position).getUrl(), casey.getMailBox(mailBox).getLoopmail(loopMail).getLink(position).getTitle())));
        holder.link.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return casey.getMailBox(mailBox).getLoopmail(loopMail).getNumberOfLinks();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView link;

        public ViewHolder(View itemView) {
            super(itemView);

            link = itemView.findViewById(R.id.link);
        }
    }
}
