package com.example.smartagritech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForcastAdapter extends RecyclerView.Adapter<ForcastAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ForcastModel> weatherRVModelArrayList;

    public ForcastAdapter(Context context, ArrayList<ForcastModel> weatherRVModelArrayList) {
        this.context = context;
        this.weatherRVModelArrayList = weatherRVModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.weather_forcast,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForcastModel model= weatherRVModelArrayList.get(position);
        holder.temperatureTV.setText(model.getMintemperature()+"°c");
        holder.temperatureTV1.setText(model.getMaxtemperature()+"°c");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.conditionIv);
        holder.windTV.setText(model.getHumidity() +"%");
        SimpleDateFormat input=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output=new SimpleDateFormat("EEEE");
        try {
            Date t= input.parse(model.getdate());
            holder.timeTV.setText(output.format(t));
        } catch(ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRVModelArrayList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView windTV,temperatureTV,temperatureTV1,timeTV;
        private ImageView conditionIv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTV=itemView.findViewById(R.id.idTVTime111);
            temperatureTV=itemView.findViewById(R.id.idTVTemp2);
            temperatureTV1=itemView.findViewById(R.id.idTVTemp12);
            timeTV=itemView.findViewById(R.id.idTVTime);
            conditionIv=itemView.findViewById(R.id.idIVCondition);
        }
    }
}
