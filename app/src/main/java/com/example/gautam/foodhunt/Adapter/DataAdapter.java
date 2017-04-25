package com.example.gautam.foodhunt.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gautam.foodhunt.ActProductInfo;
import com.example.gautam.foodhunt.Constants;
import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ProductVersion;
import com.example.gautam.foodhunt.Modal.ServerRequest;
import com.example.gautam.foodhunt.Modal.User;
import com.example.gautam.foodhunt.R;
import com.example.gautam.foodhunt.RequestInterface;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<ProductVersion> products;
    Context context;

    public DataAdapter(ArrayList<ProductVersion> products, Context context) {

        this.products = products;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.p_name.setText(products.get(position).getP_name());
        holder.p_sold.setText(String.format("Rs %s",products.get(position).getP_sold()));

        Picasso.with(context).load(products.get(position).getP_image()).resize(250,200).placeholder(R.drawable.gradient_blue).into(holder.p_image);


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView p_name, p_sold, p_info;
        private ImageView p_image;
        ImageView p_dots;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            p_name = (TextView) itemView.findViewById(R.id.p_name);
            p_sold = (TextView) itemView.findViewById(R.id.p_sold);
            p_image=(ImageView)itemView.findViewById(R.id.p_image);
            p_dots=(ImageView)itemView.findViewById(R.id.popupdots);
            p_dots.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            // Toast.makeText(view.getContext(),products.get(pos).getP_name(),Toast.LENGTH_SHORT).show();
            switch (view.getId()) {
                case R.id.popupdots:
                    inflatepopup(view,pos);

                    break;
                default:
                    Intent intent = new Intent(context, ActProductInfo.class);
                    intent.putExtra("DATAINTENT", products.get(pos).getP_id());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;

            }


        }


    }
    private void inflatepopup(final View view, final int pos) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.popupmenu,
                popup.getMenu());
        popup.show();
        final SharedPreferences pref=view.getContext().getSharedPreferences("ABC",Context.MODE_PRIVATE);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Just a sec");
                progressDialog.show();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.base_url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                User user = new User();
                user.setP_id(products.get(pos).getP_id());
                user.setEmail(pref.getString(Constants.EMAIL, ""));
                user.setNoi("1");
                final ServerRequest request = new ServerRequest();
                request.setOperation(Constants.addtocart);
                request.setUser(user);
                RequestInterface requestInterface = retrofit.create(RequestInterface.class);
                Call<ProductResponse> response = requestInterface.operation(request);
                response.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        progressDialog.dismiss();
                        Toast.makeText(view.getContext(), "Successfully added", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(view.getContext(), "Try again ", Toast.LENGTH_SHORT).show();


                    }
                });
                return false;
            }
        });
    }


}
