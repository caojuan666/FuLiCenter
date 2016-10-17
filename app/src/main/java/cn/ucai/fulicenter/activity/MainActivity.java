package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    int index;
    RadioButton[] ra ;
    @BindView(R.id.btnNewGoods)
    RadioButton btnNewGoods;
    @BindView(R.id.btnBoutique)
    RadioButton btnBoutique;
    @BindView(R.id.btnCategory)
    RadioButton btnCategory;
    @BindView(R.id.btnCollect)
    RadioButton btnCollect;
    @BindView(R.id.tvshop)
    RadioButton tvshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("main", "MainActivity.onCreate()");
        intiView();
    }

    private void intiView() {
        ra = new RadioButton[5];
        ra[0]=btnNewGoods;
        ra[1]=btnBoutique;
        ra[2]=btnCategory;
        ra[3]=btnCollect;
        ra[4]= tvshop;

    }

    public void onCheckedChange(View view) {

        switch (view.getId()){
            case R.id.btnNewGoods:
                 index = 0;
            break;
            case R.id.btnBoutique:
                index = 1;
                break;
            case R.id.btnCategory:
                index = 2;
                break;
            case R.id.btnCollect:
                index = 3;
                break;
            case R.id.tvshop:
                index = 4;
                break;


        }
        setRadioButtonStatus();

    }

    private void setRadioButtonStatus() {
        for(int i=0;i<ra.length;i++){
            if(i==index){
                ra[i].setChecked(true);
            }else{
                ra[i].setChecked(false);
            }
        }
    }



}
