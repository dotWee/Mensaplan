package de.dotwee.rgb.canteen.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.Constants;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.Item;
import de.dotwee.rgb.canteen.model.Type;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemRecyclerViewAdapter extends SectionedRecyclerViewAdapter {
    private Map<Type, ArrayList<Item>> typeListMap;

    public ItemRecyclerViewAdapter() {
    }

    public ItemRecyclerViewAdapter(List<Item> itemList) {
        setItems(itemList);
    }

    void setItems(List<Item> itemList) {
        typeListMap = new ArrayMap<>();

        for (Item item : itemList) {
            Type itemType = item.getType();

            if (!typeListMap.containsKey(itemType)) {
                typeListMap.put(itemType, new ArrayList<>());
            }

            ArrayList<Item> items = typeListMap.get(itemType);
            items.add(item);
        }

        for (Map.Entry<Type, ArrayList<Item>> entry : typeListMap.entrySet()) {
            TypeSection typeSection = new TypeSection(entry);
            addSection(typeSection);
        }

        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewName)
        TextView textViewName;

        @BindView(R.id.textViewPrice)
        TextView textViewPrice;

        @BindView(R.id.textViewLabel)
        TextView textViewLabel;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public static class TypeSectionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewType)
        TextView textViewType;

        public TypeSectionViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public static class TypeSection extends StatelessSection {
        final ArrayList<Item> itemArrayList;
        final Type type;

        public TypeSection(Map.Entry<Type, ArrayList<Item>> entry) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.list_item)
                    .headerResourceId(R.layout.list_item_section)
                    .build()
            );

            itemArrayList = entry.getValue();
            type = entry.getKey();
        }

        @Override
        public int getContentItemsTotal() {
            return itemArrayList.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            Item item = itemArrayList.get(position);
            itemViewHolder.textViewName.setText(item.getName());
            itemViewHolder.textViewLabel.setText(item.getLabel().toString());
            itemViewHolder.textViewPrice.setText(item.getPrice(Constants.DEFAUTL_PRICE));
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new TypeSectionViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            TypeSectionViewHolder sectionViewHolder = (TypeSectionViewHolder) holder;

            String typeName = sectionViewHolder.textViewType.getContext().getString(type.getTitleId());
            sectionViewHolder.textViewType.setText(typeName);
        }
    }
}
