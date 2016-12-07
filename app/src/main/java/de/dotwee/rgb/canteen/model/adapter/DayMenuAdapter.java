package de.dotwee.rgb.canteen.model.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.DayMenu;
import de.dotwee.rgb.canteen.model.Item;
import de.dotwee.rgb.canteen.model.constant.Price;
import de.dotwee.rgb.canteen.model.constant.Type;
import de.dotwee.rgb.canteen.model.helper.PreferencesHelper;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import timber.log.Timber;

import static de.dotwee.rgb.canteen.model.helper.PreferencesHelper.isColorSeparationEnabled;

/**
 * Created by lukas on 19.11.2016.
 */
public class DayMenuAdapter extends SectionedRecyclerViewAdapter {
    private static final String TAG = DayMenuAdapter.class.getSimpleName();
    private DayMenu dayMenu;

    public DayMenuAdapter() {

    }

    public void setDayMenu(@NonNull DayMenu dayMenu) {
        this.dayMenu = dayMenu;
        setSections();
    }

    private void setSections() {
        removeAllSections();

        for (Type type : dayMenu.getContainedTypes()) {
            TypeSection typeSection = new TypeSection(type, dayMenu.get(type));
            addSection(typeSection);
        }
    }

    private static class TypeSection extends StatelessSection {
        final List<Item> items;
        final Type type;

        TypeSection(Type type, List<Item> items) {
            super(R.layout.list_daymenu_type, R.layout.list_daymenu_item);
            this.items = items;
            this.type = type;
        }

        @Override
        public int getContentItemsTotal() {
            return items.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;
            final Item item = items.get(position);

            viewHolder.textViewName.setText(item.getName());
            viewHolder.textViewInfo.setText(item.getInfo());

            viewHolder.cardView.setRadius(0);
            viewHolder.viewDivider.setVisibility(position == getSectionItemsTotal() - 2 ? View.GONE : View.VISIBLE);

            setItemPrice(viewHolder, item);
            setColorState(viewHolder);
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new TypeViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            TypeViewHolder typeViewHolder = (TypeViewHolder) holder;

            String typeString = typeViewHolder.textViewName
                    .getResources().getString(type.getTitleId())
                    .toUpperCase();

            typeViewHolder.textViewName.setText(typeString);
            setColorState(typeViewHolder);
        }

        private void setItemPrice(@NonNull ItemViewHolder itemViewHolder, @NonNull Item item) {
            Price price = PreferencesHelper.getPriceAppearance(itemViewHolder.cardView.getContext());
            String itemPrice;
            switch (price) {

                case STUDENT:
                    itemPrice = item.getPriceStudent();
                    break;

                case EMPLOYEE:
                    itemPrice = item.getPriceEmployee();
                    break;

                case GUEST:
                    itemPrice = item.getPriceGuest();
                    break;

                default:
                    itemPrice = item.getPriceAll();
                    break;
            }

            String formatted = itemViewHolder.textViewPrice.getResources().getString(R.string.price_format, itemPrice);
            itemViewHolder.textViewPrice.setText(formatted);
        }

        private void setColorState(@NonNull TypeViewHolder typeViewHolder) {
            int backgroundColor;

            Resources.Theme theme = typeViewHolder.itemView.getContext().getTheme();
            Resources resources = typeViewHolder.itemView.getResources();

            backgroundColor = ResourcesCompat.getColor(resources, isColorSeparationEnabled(typeViewHolder.itemView.getContext()) ? type.getDarkColorId() : R.color.background_dark_900, theme);
            typeViewHolder.itemView.setBackgroundColor(backgroundColor);
        }

        private void setColorState(@NonNull ItemViewHolder itemViewHolder) {
            int cardBackgroundColor, dividerColor;

            Resources.Theme theme = itemViewHolder.cardView.getContext().getTheme();
            Resources resources = itemViewHolder.cardView.getResources();

            if (isColorSeparationEnabled(itemViewHolder.cardView.getContext())) {
                cardBackgroundColor = ResourcesCompat.getColor(resources, type.getColorId(), theme);
                dividerColor = ResourcesCompat.getColor(resources, type.getDarkColorId(), theme);
            } else {
                cardBackgroundColor = ResourcesCompat.getColor(resources, R.color.background_dark_800, theme);
                dividerColor = ResourcesCompat.getColor(resources, R.color.background_dark_900, theme);
            }

            itemViewHolder.cardView.setCardBackgroundColor(cardBackgroundColor);
            itemViewHolder.viewDivider.setBackgroundColor(dividerColor);
        }
    }

    static class TypeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewName)
        TextView textViewName;

        View itemView;

        TypeViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.linearLayoutWrapper)
        LinearLayout linearLayoutWrapper;

        @BindView(R.id.textViewName)
        TextView textViewName;

        @BindView(R.id.textViewInfo)
        TextView textViewInfo;

        @BindView(R.id.textViewPrice)
        TextView textViewPrice;

        @BindView(R.id.viewDivider)
        View viewDivider;

        CardView cardView;

        ItemViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.linearLayoutWrapper)
        @Override
        public void onClick(View v) {
            Timber.i("OnClick item on position=%d", getAdapterPosition());
        }
    }
}
