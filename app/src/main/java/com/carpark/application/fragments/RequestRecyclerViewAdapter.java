package com.carpark.application.fragments;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carpark.application.R;
import com.carpark.application.database.RequestItem;
import com.carpark.application.fragments.RequestFragment.OnListFragmentInteractionListener;
import com.carpark.application.fragments.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.ViewHolder> {

    private final List<RequestItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public RequestRecyclerViewAdapter(List<RequestItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        RequestItem item = mValues.get(position);
        holder.mItem = item;
        holder.mCarTitle.setText(item.car.title);
        holder.mCarDescription.setText(item.car.description);
        holder.mCarCost.setText(item.total_str);
        holder.mRequestTitle.setText("Заказ #" + Long.toString(item.id));


        holder.mRequestStatus.setText(item.request_status.title);

        holder.mRequestRegistrationDate.setText(DateUtils.formatDateTime(holder.mView.getContext(),
                item.registration_date,
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME));
        holder.viewBeginDate.setText(DateUtils.formatDateTime(null,
                item.begin_date,
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        holder.viewEndDate.setText(DateUtils.formatDateTime(null,
                item.end_date,
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        holder.viewRentCost.setText(item.car.cost_str.toString());
        holder.viewPledgeCost.setText(item.car.pledge_str.toString());
        holder.viewTotalCost.setText(item.total_str);

        if (item.car.image != null) {
            holder.mCarImage.setImageBitmap(item.car.image);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Представление
         */
        public final View mView;
        /**
         * Поле представления "Название заказа"
         */
        public final TextView mRequestTitle;


        public final TextView mRequestRegistrationDate;

        public final TextView mRequestStatus;

        Button buttonDelete;


        TextView viewBeginDate;
        TextView viewEndDate;
        TextView viewRentCost;
        TextView viewPledgeCost;
        TextView viewTotalCost;
        Button buttonSend;

        /**
         * Поле представления "Название авто"
         */
        public final TextView mCarTitle;
        /**
         * Поле представления "Описание авто"
         */
        public final TextView mCarDescription;
        /**
         * Поле представления "Стоимость аренды"
         */
        public final TextView mCarCost;
        /**
         * Поле представления "Фото автомобиля"
         */
        public final ImageView mCarImage;
        /**
         * Объект данных, связанный с дданным элементом
         */
        public RequestItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCarTitle = (TextView) view.findViewById(R.id.car_title);
            mCarDescription = (TextView) view.findViewById(R.id.car_description);
            mCarCost = (TextView) view.findViewById(R.id.car_cost);
            mCarImage = (ImageView) view.findViewById(R.id.car_image);
            mRequestTitle = (TextView) view.findViewById(R.id.request_title);
            mRequestRegistrationDate = (TextView) view.findViewById(R.id.request_registration_date);
            mRequestStatus = (TextView) view.findViewById(R.id.request_status);
            viewBeginDate = ((TextView) view.findViewById(R.id.begin_date));
            viewEndDate = ((TextView) view.findViewById(R.id.end_date));
            viewRentCost = ((TextView) view.findViewById(R.id.rent_cost));
            viewPledgeCost = ((TextView) view.findViewById(R.id.pledge_cost));
            viewTotalCost = ((TextView) view.findViewById(R.id.total_cost));
            buttonDelete = ((Button) view.findViewById(R.id.button_delete));
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Snackbar snackbar = Snackbar.make(v, "Отменить заказ?", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Да", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewHolder.this.mItem.database.RequestDelete(ViewHolder.this.mItem.id);
                            ViewHolder.this.mView.setVisibility(View.INVISIBLE);

                        }
                    });
                    snackbar.show();
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCarTitle.getText() + "'";
        }
    }
}
