package jansen.com.searchdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jansen.com.searchdemo.bean.PYPerson;
import jansen.com.searchdemo.bean.Person;
import jansen.com.searchdemo.pinyinsearch.helper.PYNameHelper;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.sreach)
    Button mSreach;
    @Bind(R.id.result)
    TextView mResult;
    @Bind(R.id.keyword)
    EditText mKeyword;
    private ArrayList<Person> mPersons;
    String[] names = new String[]{"张三", "李四", "王五", "赵六", "孙七"};
    StringBuilder mBuilder = new StringBuilder();
    private ArrayList<PYPerson> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPersons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person mPerson = new Person();
            mPerson.setAge(i);
            mPerson.setName(names[i / 4] + i);
            mPersons.add(mPerson);
        }
        PYNameHelper.getInstance().setBaseGoods(mPersons);
    }

    @OnClick({R.id.sreach, R.id.result})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sreach:
                mBuilder.delete(0, mBuilder.length());
                results = (ArrayList<PYPerson>) PYNameHelper.getInstance().parseT9InputSearchPerson(mKeyword.getText().toString());
                Log.e("results", JSON.toJSONString(results));
                mBuilder.append("搜索关键字:").append(mKeyword.getText().toString()).append("\n");
                mBuilder.append("搜索结果:" + "\n");
                for (PYPerson mPYPerson : results) {
                    mBuilder.append(JSON.toJSONString(mPYPerson.getPerson())).append("\n");
                }
                mResult.setText(mBuilder.toString());
                break;
            case R.id.result:
                break;
        }
    }
}
