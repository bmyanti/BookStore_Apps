package fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zxcvbn.bookstore.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Controller.Controller;
import activity.MainActivity;
import activity.OnScrollUpDownListener;
import adapter.BooksAdapter;
import adapter.CustomList;
import model.Book;
import model.DataContentProvider;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zxcvbn on 06/07/2016.
 */
public class BooksFragment extends Fragment{
    GridView gridview;
    ListView mListView;
    List<Book> list_book;
    String[] title;
    String[] price;
    int[] bookcover;
    Context context;
    TextView textView1;
    Animation show_fab_1;
    Animation hide_fab_1;
    Boolean klik=true;

    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    private boolean FAB_Status = false;

    ListView list;
    ScrollView sv;
    private static final String TAG = MainActivity.class.getSimpleName();
    public BooksFragment(){    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.w(TAG, "Call onActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.w(TAG, "Call onCreate");
        showAllBooks();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.w(TAG, "Call onCreateView");
        View view = inflater.inflate(R.layout.grid_book, container, false);
        textView1 = (TextView) view.findViewById(R.id.textView1);



        int[] imageId  = {
                R.drawable.business, R.drawable.art,
                R.drawable.fantasy, R.drawable.kids,
                R.drawable.fiction, R.drawable.no_fiction,
                R.drawable.religion, R.drawable.music,
                R.drawable.help, R.drawable.biography,
                R.drawable.sci_fi, R.drawable.triller,
                R.drawable.romance};

        show_fab_1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab1_hide);

        //Floating Action Buttons
        fab = (FloatingActionButton) view.findViewById(R.id.fabFilter);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab_3);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        final String[] getBookCategory = DataContentProvider.getBookCategory(getActivity());
        gridview = (GridView) view.findViewById(R.id.gridView1);

        OnScrollUpDownListener.Action scrollAction = new OnScrollUpDownListener.Action() {

            @Override
            public void up() {
                fab.show();
            }

            @Override
            public void down() {
                fab.show();
            }

        };
        gridview.setOnScrollListener(new OnScrollUpDownListener(gridview, 8, scrollAction));
        gridview.setAdapter(new BooksAdapter(getActivity(),"null"));



        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<imageId.length;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("cat", getBookCategory[i]);
            hm.put("img", imageId[i]+"");
            aList.add(hm);
        }
        String[] from={"cat","img"};//string array
        int[] to={R.id.Itemname,R.id.icon};//int array of views id's

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.mylist, from, to);
        list=(ListView)view.findViewById(R.id.list);
        sv=(ScrollView)view.findViewById(R.id.sc);
        /*Button button = (Button) view.findViewById(R.id.btn_filter);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(sv.getVisibility()==View.GONE || sv.getVisibility()==View.INVISIBLE) {
                    Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
                    sv.startAnimation(animation1);
                    sv.setVisibility(View.VISIBLE);
                }
                else
                {
                    sv.setVisibility(View.GONE);
                }
            }
        });
*/
        list.setAdapter(adapter);
        list.setDivider(new ColorDrawable(0x99F10529));   //0xAARRGGBB
        list.setDividerHeight(1);
        textView1.setText("");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                gridview.setAdapter(new BooksAdapter(getActivity(),Integer.toString(position+1)));
                String kl = model.DataContentProvider.getBookCatbyId(getActivity(),Integer.toString(position+1));
                textView1.setVisibility(View.VISIBLE);
                textView1.setText("Filter by: "+kl);
            }
        });



        /*gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Log.w(TAG, "Berhasil clicked");
                //Toast.makeText(getApplicationContext(),"Berhasil", Toast.LENGTH_SHORT).show();
            }
        });*/

        return view;
    }

    private void expandFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sv.getVisibility()==View.GONE || sv.getVisibility()==View.INVISIBLE) {
                    Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
                    sv.startAnimation(animation1);
                    sv.setVisibility(View.VISIBLE);
                }
                else
                {
                    sv.setVisibility(View.GONE);
                }
            }
        });

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(show_fab_1);
        fab2.setClickable(true);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                klik=false;
                    String k = "sorting";
                    gridview.setAdapter(new BooksAdapter(getActivity(), k));
                if(klik=false)
                {
                    gridview.setAdapter(new BooksAdapter(getActivity(), "null"));
                }
            }
        });

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin += (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(show_fab_1);
        fab3.setClickable(true);
    }


    private void hideFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(hide_fab_1);
        fab2.setClickable(false);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin -= (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(hide_fab_1);
        fab3.setClickable(false);
    }

    private void showAllBooks(){
        Log.w(TAG, "Call showAllBooks");

        //call api
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Book>> call = apiService.getBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                list_book=  response.body();
                Log.w(TAG, "Response to get Books ");
                Log.w(TAG, "Number of book received: " + list_book.get(0).getName());
                Log.w(TAG, "Number of book received: " + list_book.get(0).getCategory_id());
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.w(TAG, t.toString());
            }
        });
    }

    /*private void showAllBooks() {
        Log.w(TAG, "Call showAllBooks");
        if (getView() == null) {
            return;
        }
        new AsyncTask<Void, Void, List<Book>>(){
            @Override
            protected List<Book> doInBackground(Void... params) {
                //call api
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

                Call<List<Book>> call = apiService.getBooks();
                call.enqueue(new Callback<List<Book>>() {
                    @Override
                    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                        list_book=  response.body();

                        Log.w(TAG, "Response to get Books ");
                        Log.w(TAG, "Number of book received: " + list_book.get(0).getName());
                    }

                    @Override
                    public void onFailure(Call<List<Book>> call, Throwable t) {
                        Log.w(TAG, t.toString());
                    }
                });
                return list_book;
            }

           @Override
            protected void onPostExecute(List<Book> books) {
               list_book=books;
                //updateView(speakers);
               //gridview.setAdapter(new BooksAdapter(getActivity(), getTitle(),getPrice(), getCover()));
            }

        }.execute();
    }*/

    public int[] getCover(){
        int[] arr;
        if(list_book.size() == 0 && list_book== null){
            arr = new int[8];
            for(int i=0;i < arr.length; i++){
                if(i%2 == 0){
                    arr[i] = R.drawable.ex_book3;
                }else{
                    arr[i] = R.drawable.ex_book4;
                }
            }

        }else{
            arr = new int[list_book.size()];
            for(int i=0;i < arr.length; i++){
                if(i%2 == 0){
                    arr[i] = R.drawable.ex_book3;
                }else{
                    arr[i] = R.drawable.ex_book4;
                }
            }

        }
        return arr;
    }

    public  List<Book> dummydata(){
        List<Book> result = new ArrayList<Book>();

        Book A = new Book("ABC", "RM 0.0");
        Book B = new Book("ABC", "RM 0.0");
        Book C = new Book("ABC", "RM 0.0");
        Book D = new Book("ABC", "RM 0.0");
        Book E = new Book("ABC", "RM 0.0");
        Book F = new Book("ABC", "RM 0.0");
        Book G = new Book("ABC", "RM 0.0");
        Book H = new Book("ABC", "RM 0.0");
        Book I = new Book("ABC", "RM 0.0");

        result.add(A);result.add(B);result.add(C);
        result.add(D);result.add(E);result.add(F);
        result.add(G);result.add(H);result.add(I);

        return  result;
    }
    public String[] getTitle(){
        if(list_book==null){
            list_book = dummydata();
        }
        String[] arr = new String[list_book.size()];
        int i=0;
        for(Book model : list_book){
            arr[i] = model.getName();
            i++;
        }
        return arr;
    }
    public String[] getPrice(){
        if(list_book==null){
            list_book = dummydata();
        }
        String[] arr = new String[list_book.size()];
            /*for(int i=0;i < list_book.size(); i++){
                for(Book model : list_book){
                    arr[i] = model.getPrice();
                }
            }*/

        int i=0;
        for(Book model : list_book){
            arr[i] = model.getPrice();
            i++;
        }
        return arr;

    }


    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
