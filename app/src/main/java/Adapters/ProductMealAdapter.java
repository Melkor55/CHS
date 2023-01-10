package Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sma.AddFoodActivity;
import com.example.sma.R;
import com.example.sma.SearchFoodActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

import Models.Meal;
import Models.Product;

public class ProductMealAdapter extends RecyclerView.Adapter<ProductMealAdapter.ViewProcessHolder> {

    Context context;
    private String table;
    private ArrayList<Product> products; //memanggil modelData
    private Meal meal;
    private Product clickedProduct;
    private ItemClickListener mClickListener;

    public ProductMealAdapter(Context context, ArrayList<Product> products, Meal meal) {
        this.context = context;
        this.products = products;
        this.meal = meal;
    }

    public void filterList(ArrayList<Product> filteredList) {
        // below line is to add our filtered
        // list in our course array list.
        products = filteredList;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    public Product getClickedProduct() {
        return clickedProduct;
    }

    public void setClickedProduct(Product clickedProduct) {
        this.clickedProduct = clickedProduct;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @NonNull
    @Override
    public ViewProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list, parent, false); //memanggil layout list recyclerview
        ViewProcessHolder processHolder = new ViewProcessHolder(view);
        //System.out.println("nuuuuuuu");
        return processHolder;
    }
    /*
        @NonNull
        @Override
        public ViewProcessHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }
    */
    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, int position) {

        final Product data = products.get(position);
        //System.out.println("dadadad");
        //holder.id.setText(""+data.getId());
        holder.foodName.setText(data.getName());
        holder.foodBrand.setText(data.getBrand());
        holder.foodDetails.setText("- " + data.getCalories() + " calories per " + data.getWeight() + " " + data.getUnit());
        //int position = holder.getAdapterPosition();
//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Aliment theRemovedItem = aliments.get(holder.getAdapterPosition());
//
//                // remove your item from data base
//
//                String  username, aliment;
//                username = Login.getUsername();
//                aliment = theRemovedItem.getAliment();
//
//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        //Starting Write and Read data with URL
//                        //Creating array for parameters
//                        String[] field = new String[3];
//                        field[0] = "table";
//                        field[1] = "username";
//                        field[2] = "aliment";
//
//                        //Creating array for data
//                        String[] data = new String[3];
//                        data[0] = table;
//                        data[1] = username;
//                        data[2] = aliment;
//
//                        //PutData putData = new PutData("http://192.168.1.102/LikedFoods/addFood.php", "POST", field, data);
//                        PutData putData = new PutData(new Database_URL("/LikedFoods", "/deleteFood.php").getURL(), "POST", field, data);
//                        if (putData.startPut()) {
//                            if (putData.onComplete()) {
//                                String result = putData.getResult();
//                                System.out.println("--->" + result);
//                                //End ProgressBar (Set visibility to GONE)
//                                if (result.equals("Food Deleted Succefully")) {
//                                    System.out.println("Good job!");
//                                    Toast.makeText(context.getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    System.out.println("Bad job!");
//                                    Toast.makeText(context.getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//                                }
//                            } else
//                                System.out.println("Incomplete!");
//                        } else
//                            System.out.println("Data is not put!");
//                        //End Write and Read data with URL
//                    }
//                });
//
//
//                System.out.println("<< " + theRemovedItem + " >>" + "--> Position :" + holder.getAdapterPosition());
//                aliments.remove(theRemovedItem);  // remove the item from list
//                notifyItemRemoved(holder.getAdapterPosition()); // notify the adapter about the removed item
//            }
//        });
        //notifyItemRemoved(holder.getAdapterPosition()); // notify the adapter about the removed item
        //notifyItemChanged(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView foodName, foodBrand, foodDetails;
        FloatingActionButton removeFoodButton;

        public ViewProcessHolder(@NonNull View itemView) {
            super(itemView);

            foodName = (TextView) itemView.findViewById(R.id.foodName);
            foodBrand = (TextView) itemView.findViewById(R.id.foodBrand);
            foodDetails = (TextView) itemView.findViewById(R.id.foodDetails);
            removeFoodButton = (FloatingActionButton) itemView.findViewById(R.id.removeFoodButton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            //Toast.makeText(view.getContext(), "Item " + getAdapterPosition() + " clicked",Toast.LENGTH_LONG).show();
            //System.out.println(products.get(getAdapterPosition()));
            clickedProduct = products.get(getAdapterPosition());

            System.out.println(view);
            Intent intentHomeActivity = new Intent("get-selected-product-home-activity");
            intentHomeActivity.putExtra("clickedProduct", (Serializable) clickedProduct);
            intentHomeActivity.putExtra("Meal", (Serializable) meal);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentHomeActivity);
        }
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}