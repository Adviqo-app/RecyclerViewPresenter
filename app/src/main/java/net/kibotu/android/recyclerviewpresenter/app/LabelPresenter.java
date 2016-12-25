package net.kibotu.android.recyclerviewpresenter.app;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kibotu.android.recyclerviewpresenter.BaseViewHolder;
import net.kibotu.android.recyclerviewpresenter.Presenter;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */

public class LabelPresenter extends Presenter<String, LabelPresenter.ViewHolder> {

    public LabelPresenter(@NonNull PresenterAdapter<String> presenterAdapter) {
        super(presenterAdapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.label_presenter_item;
    }

    @NonNull
    protected ViewHolder createViewHolder(@LayoutRes int layout, @NonNull ViewGroup parent) {
        return new ViewHolder(layout, parent);
    }

    public void bindViewHolder(@NonNull ViewHolder viewHolder, @NonNull String item, int position) {
        viewHolder.label.setText(item);

        viewHolder.itemView.setOnClickListener(v -> {
            if (presenterAdapter.getOnItemClickListener() != null)
                presenterAdapter.getOnItemClickListener().onItemClick(item, v, position);
        });
    }

    @Override
    public <VH extends RecyclerView.ViewHolder> void bindViewHolder(@NotNull VH viewHolder, String item, int position) {

    }

    public static class ViewHolder extends BaseViewHolder {

        @NonNull
        @BindView(R.id.label)
        TextView label;

        ViewHolder(@LayoutRes int layout, @NonNull ViewGroup parent) {
            super(layout, parent);
        }
    }
}
