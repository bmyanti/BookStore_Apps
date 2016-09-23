package activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import Controller.Controller;

import com.example.zxcvbn.bookstore.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.BooksAdapter;
import model.Book;
import model.DataContentProvider;
import model.ShoppingCartEntry;

public class Detail_Book extends AppCompatActivity{

    TextView author_,summary_,price_,nameBookDet;
    Context context;
    FloatingActionButton fab;
    List<String> getBookAut = new ArrayList<>();
    ListView list;
    private View headerView;
    private View headerSpace;
    TextView notif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__book);
        headerView = findViewById(R.id.header);


       /* author_=(TextView) findViewById(R.id.txtAuthor);
        summary_=(TextView) findViewById(R.id.txtSummary);
        price_=(TextView) findViewById(R.id.txtPrice);*/
        nameBookDet = (TextView) findViewById(R.id.nameBookDet);
        Intent intent = getIntent();
        String a = intent.getStringExtra("book_name");
        getBookAut = DataContentProvider.getDetailBk(this,a);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        List<String> getBookDes = new ArrayList<>();
        getBookDes.add(getBookAut.get(3));
        getBookDes.add(getBookAut.get(2));
        getBookDes.add(getBookAut.get(7));
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        int[] imageId  = {
                R.drawable.business, R.drawable.art,
                R.drawable.fantasy};

        String[] stockArr = new String[getBookDes.size()];
        stockArr = getBookDes.toArray(stockArr);

        for(int i=0;i<imageId.length;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("cat", stockArr[i]);
            hm.put("img", imageId[i]+"");
            aList.add(hm);
        }
        String[] from={"cat","img"};//string array
        int[] to={R.id.nameBookDet,R.id.iconDetBook};//int array of views id's

        SimpleAdapter adapter = new SimpleAdapter(this, aList, R.layout.mylist_det_book, from, to);

        setListViewHeader();
        list=(ListView)findViewById(R.id.listDetBook);
        list.setAdapter(adapter);
        list.setDivider(new ColorDrawable(0x99F10529));   //0xAARRGGBB
        list.setDividerHeight(1);

        list.setOnScrollListener(onScrollListener());
        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                gridview.setAdapter(new BooksAdapter(getActivity(),Integer.toString(position+1)));
                String kl = model.DataContentProvider.getBookCatbyId(getActivity(),Integer.toString(position+1));
                textView1.setVisibility(View.VISIBLE);
                textView1.setText("Filter by: "+kl);
            }
        });*/


        //Get Global Controller Class object (see application tag in AndroidManifest.xml)

        /******************  Create Dummy Products Data  ***********/

        Book productObject = null;
            // Create product model class object
        productObject = new Book("id"+getBookAut.get(0)
                ,getBookAut.get(1)
                ,getBookAut.get(2)
                ,getBookAut.get(3)
                ,getBookAut.get(4)
                ,getBookAut.get(5)
                ,getBookAut.get(6)
                ,getBookAut.get(7));
        //store product object to arraylist in controller
        Controller.setBooks(productObject);



        /******************  Products Data Creation End   ***********/


        /******* Create view elements dynamically and show on activity ******/

        //Product arraylist size
        int ProductsSize = Controller.getBooksArraylistSize();

        // create the layout params that will be used to define how your
        // button will be displayed

        /******** Dynamically create view elements - Start **********/

        for(int j=0;j< ProductsSize;j++)
        {
            // Get probuct data from product data arraylist
            String pName = Controller.getBooks(j).getName();
            String pPrice   = Controller.getBooks(j).getPrice();

            final int index = j;

            //Create click listener for dynamically created button
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Clicked button index
                    Log.i("TAG", "index :" + index);

                    // Get product instance for index
                    Book tempProductObject = Controller.getBooks(index);

                    //Check Product already exist in Cart or Not
                    if(!Controller.getCart().checkProductInCart(tempProductObject))
                    {

                        // Product not Exist in cart so add product to
                        // Cart product arraylist

                        Controller.getCart().setProducts(tempProductObject);

                       /* Toast.makeText(getApplicationContext(),
                                "Now Cart size: "+Controller.getCart().getCartSize(),
                                Toast.LENGTH_LONG).show();*/

                    }
                    else
                    {
                        // Cart product arraylist contains Product
                        Toast.makeText(getApplicationContext(),
                                "Product "+(index+1)+" already added in cart.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

}


    private AbsListView.OnScrollListener onScrollListener () {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // Check if the first item is already reached to top
                if (list.getFirstVisiblePosition() == 0) {
                    View firstChild = list.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }

                   // int headerTopY = headerSpace.getTop();

                    // Set the image to scroll half of the amount that of ListView
                    headerView.setY(topY * 0.5f);
                }
            }
        };
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

        Slide slide = new Slide();
        fade.setDuration(1000);
        getWindow().setReturnTransition(slide);
    }

    private void setListViewHeader() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View listHeader = inflater.inflate(R.layout.header_det_book, null, false);
        //headerSpace = listHeader.findViewById(R.id.header_space);
//        list.addHeaderView(listHeader);
    }

}
