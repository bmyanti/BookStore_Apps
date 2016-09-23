package adapter;

/**
 * Created by sartikahasibuan on 7/8/2016.
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.transition.Fade;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zxcvbn.bookstore.R;

import activity.Detail_Book;
import provider.GlobalData;
import model.DataContentProvider;

public class BooksAdapter extends BaseAdapter {

    String [] result;
    Context context;
    int [] imageId;
    String[] price;
    String[] getBookTitle1;
    String[] getBookTitle;
    String[] getBookPrice;
    Holder holder=new Holder();
    View rowView;

    private static LayoutInflater inflater=null;
    GlobalData global_data = new GlobalData();
    DataContentProvider dataContentProvider;
    private Animator mCurrentAnimator;

    private int mShortAnimationDuration;


    public BooksAdapter(Context c, String id) {
        // TODO Auto-generated constructor stub
        context= c;

        if(id!="sorting" && id!="null") {
           getBookTitle = DataContentProvider.getBookTitle(context, id);
           getBookPrice = DataContentProvider.getBookPrice(context, id);
        }

        else if(id=="sorting")
        {
            getBookPrice = DataContentProvider.getBkPrice(context);
            getBookTitle = DataContentProvider.getBkTit(context);



        }
        else if(id=="null")
        {
            getBookTitle = DataContentProvider.getBookTit(context);
            getBookPrice = DataContentProvider.getBookPr(context);
        }

        int[] getCover = DataContentProvider.getCover(context);
        result=getBookTitle;
        price = getBookPrice;

        imageId=getCover;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tvTitle;
        TextView tvPrice;
        ImageView img;

    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub

        final ImageView imageView = new ImageView(this.context);

        rowView = inflater.inflate(R.layout.book_list, null);
        holder.tvTitle=(TextView) rowView.findViewById(R.id.textView1);
        holder.tvPrice=(TextView) rowView.findViewById(R.id.textView2);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);


        holder.tvTitle.setText(result[position]);
        holder.tvPrice.setText(price[position]);
        holder.img.setImageResource(imageId[position]);
        imageView.setLayoutParams(new GridView.LayoutParams(200,150));


        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
                String res=result[position];
//                int id = (Integer) v.getTag();
                //zoomImageFromThumb(v, imageId[position]);
                Intent intent = new Intent(context, Detail_Book.class);
                intent.putExtra("book_name",res);

            /*    ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(this, , "robot");
                // start the new activity
                context.startActivity(intent, options.toBundle());*/

               context.startActivity(intent);
            }
        });

        return rowView;
    }
}
