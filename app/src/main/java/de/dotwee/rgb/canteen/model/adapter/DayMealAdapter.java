package de.dotwee.rgb.canteen.model.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.specs.DayMeal;
import de.dotwee.rgb.canteen.model.api.specs.Item;
import de.dotwee.rgb.canteen.model.constant.Label;
import de.dotwee.rgb.canteen.model.constant.Price;
import de.dotwee.rgb.canteen.model.constant.Type;
import de.dotwee.rgb.canteen.model.events.OnItemClickEvent;
import de.dotwee.rgb.canteen.model.helper.PreferencesHelper;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static de.dotwee.rgb.canteen.model.helper.PreferencesHelper.isColorSeparationEnabled;

/**
 * Created by lukas on 19.11.2016.
 */
public class DayMealAdapter extends SectionedRecyclerViewAdapter {
    private static final String TAG = DayMealAdapter.class.getSimpleName();
    private DayMeal dayMeal;

    public DayMealAdapter() {

    }

    public void setDayMeal(@NonNull DayMeal dayMeal) {
        this.dayMeal = dayMeal;
        setSections();
    }

    private void setSections() {
        removeAllSections();

        for (Type type : dayMeal.getContainedTypes()) {
            TypeSection typeSection = new TypeSection(type, dayMeal.get(type));
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

            // set item object as tag
            viewHolder.cardView.setTag(item);

            viewHolder.textViewName.setText(item.getName());
            viewHolder.textViewInfo.setText(item.getInfo());

            viewHolder.cardView.setRadius(0);
            viewHolder.viewDivider.setVisibility(position == getSectionItemsTotal() - 2 ? View.INVISIBLE : View.VISIBLE);

            setItemLabels(viewHolder, item.getLabels());
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

        private void setItemLabels(@NonNull ItemViewHolder itemViewHolder, @NonNull Label[] labels) {

            // For each label
            ImageView imageView;
            Iterator<ImageView> viewIterator = itemViewHolder.imageViews.iterator();
            for (Label label : labels) {
                imageView = viewIterator.next();
                if (label.getDrawableId() != 0) {
                    Picasso.with(imageView.getContext())
                            .load(label.getDrawableId())
                            .into(imageView);
                }
            }

            // If there are unused imageviews, set their bitmap to null
            while (viewIterator.hasNext()) {
                imageView = viewIterator.next();
                imageView.setImageBitmap(null);
            }
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

        final View itemView;
        @BindView(R.id.textViewName)
        TextView textViewName;

        TypeViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final List<ImageView> imageViews;
        final CardView cardView;
        @BindView(R.id.constraintLayoutWrapper)
        ConstraintLayout constraintLayoutWrapper;
        @BindView(R.id.textViewName)
        TextView textViewName;
        @BindView(R.id.textViewInfo)
        TextView textViewInfo;
        @BindView(R.id.textViewPrice)
        TextView textViewPrice;
        @BindView(R.id.imageViewLabel)
        ImageView imageViewLabel;
        @BindView(R.id.imageViewLabel2)
        ImageView imageViewLabel2;
        @BindView(R.id.imageViewLabel3)
        ImageView imageViewLabel3;
        @BindView(R.id.viewDivider)
        View viewDivider;

        ItemViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView;
            ButterKnife.bind(this, itemView);

            imageViews = new ArrayList<>();
            imageViews.add(imageViewLabel);
            imageViews.add(imageViewLabel2);
            imageViews.add(imageViewLabel3);
        }

        @OnClick(R.id.constraintLayoutWrapper)
        @Override
        public void onClick(View v) {
            Item item = (Item) cardView.getTag();

            OnItemClickEvent onItemClickEvent = new OnItemClickEvent(item);
            EventBus.getDefault().post(onItemClickEvent);
        }
    }
}
