package com.rocketkids.science;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.Collections;
import java.util.List;

public class RocketStageAdapter extends RecyclerView.Adapter<RocketStageAdapter.StageViewHolder> {

    private final List<RocketStage> dataset;
    private boolean isValidationState = false;

    public RocketStageAdapter(List<RocketStage> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public StageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rocket_stage, parent, false);
        return new StageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StageViewHolder holder, int position) {
        RocketStage stage = dataset.get(position);
        holder.txtTitle.setText(stage.getTitle());
        holder.txtDesc.setText(stage.getDescription());
        holder.txtIndex.setText("#" + (position + 1));

        GradientDrawable dotShape = new GradientDrawable();
        dotShape.setShape(GradientDrawable.OVAL);
        dotShape.setColor(stage.getDotColor());
        holder.viewDot.setBackground(dotShape);

        if (isValidationState) {
            boolean isCorrect = stage.getCorrectIdPosition().equals("POS_" + position);
            if (isCorrect) {
                holder.parentCard.setCardBackgroundColor(Color.parseColor("#0C211A"));
                holder.parentCard.setStrokeColor(Color.parseColor("#10B981"));
                holder.txtIndex.setText("✓");
                holder.txtIndex.setTextColor(Color.parseColor("#10B981"));
            } else {
                holder.parentCard.setCardBackgroundColor(Color.parseColor("#211313"));
                holder.parentCard.setStrokeColor(Color.parseColor("#F85149"));
                holder.txtIndex.setText("❌");
                holder.txtIndex.setTextColor(Color.parseColor("#F85149"));
            }
        } else {
            holder.parentCard.setCardBackgroundColor(Color.parseColor("#161B22"));
            holder.parentCard.setStrokeColor(Color.parseColor("#21262D"));
            holder.txtIndex.setTextColor(Color.parseColor("#58A6FF"));
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public List<RocketStage> getDataset() {
        return dataset;
    }

    public void setValidationState(boolean enabled) {
        this.isValidationState = enabled;
        notifyDataSetChanged();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(dataset, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(Math.min(fromPosition, toPosition), Math.abs(fromPosition - toPosition) + 1);
    }

    static class StageViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView parentCard;
        TextView txtTitle, txtDesc, txtIndex;
        View viewDot;

        StageViewHolder(@NonNull View itemView) {
            super(itemView);
            parentCard = itemView.findViewById(R.id.card_stage);
            txtTitle = itemView.findViewById(R.id.txt_stage_title);
            txtDesc = itemView.findViewById(R.id.txt_stage_desc);
            txtIndex = itemView.findViewById(R.id.txt_position_index);
            viewDot = itemView.findViewById(R.id.view_color_dot);
        }
    }
}