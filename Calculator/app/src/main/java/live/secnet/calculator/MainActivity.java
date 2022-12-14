package live.secnet.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaCas;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView opText;
    TextView rstText;

    private float total = 0;

    private ArrayList<Float> nums;
    private ArrayList<Character> ops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();
        getWindow().setStatusBarColor(Color.rgb(15,15,15));
    }

    private void initVars()
    {
        opText = findViewById(R.id.opText);
        rstText = findViewById(R.id.resultText);

        total = 0;
        nums = new ArrayList<Float>();
        ops = new ArrayList<Character>();
    }

    private boolean validNumber(String _num)
    {
        try
        {
            float cnvtNum = Float.parseFloat(_num);
            return true;
        }
        catch(Exception _e)
        {
            return false;
        }
    }

    public void onSumClick(View _view)
    {
        //Check If Number Is Valid
        if(!validNumber(rstText.getText().toString()))
            return;

        ops.add('+');
        nums.add(Float.parseFloat(rstText.getText().toString()));

        //Check If We Can Sum
        if(nums.size() >= 2)
        {
            setOpText(nums.get(0).toString() + " " +ops.get(0).toString() + " " + nums.get(1).toString());
            rstText.setText("");

            onEqualClick(null);
            onSumClick(_view);
            return;
        }

        setOpText(nums.get(0).toString() + " " + ops.get(0).toString());
        rstText.setText("");

    }
    public void onSubClick(View _view)
    {


        //Check If Number Is Valid
        if(!validNumber(rstText.getText().toString()))
            return;

        ops.add('-');
        nums.add(Float.parseFloat(rstText.getText().toString()));

        //Check If We Can Sum
        if(nums.size() >= 2)
        {
            setOpText(nums.get(0).toString() + " " +ops.get(0).toString() + " " + nums.get(1).toString());
            rstText.setText("");

            onEqualClick(null);
            onSubClick(_view);
            return;
        }

        setOpText(nums.get(0).toString() + " " + ops.get(0).toString());
        rstText.setText("");
    }
    public void onMulClick(View _view)
    {

        //Check If Number Is Valid
        if(!validNumber(rstText.getText().toString()))
            return;

        ops.add('*');
        nums.add(Float.parseFloat(rstText.getText().toString()));

        //Check If We Can Sum
        if(nums.size() >= 2)
        {
            setOpText(nums.get(0).toString() + " " +ops.get(0).toString() + " " + nums.get(1).toString());
            rstText.setText("");

            onEqualClick(null);
            onMulClick(_view);
            return;
        }

        setOpText(nums.get(0).toString() + ops.get(0).toString());
        rstText.setText("");
    }
    public void onDivClick(View _view)
    {

        //Check If Number Is Valid
        if(!validNumber(rstText.getText().toString()))
            return;

        ops.add('/');
        nums.add(Float.parseFloat(rstText.getText().toString()));

        //Check If We Can Sum
        if(nums.size() >= 2)
        {
            setOpText(nums.get(0).toString() + " " +ops.get(0).toString() + " " + nums.get(1).toString());
            rstText.setText("");

            onEqualClick(null);
            onDivClick(_view);
            return;
        }

        setOpText(nums.get(0).toString() + " " + ops.get(0).toString());
        rstText.setText("");
    }
    public void onEqualClick(View _view)
    {
        if(_view != null) {
            if (!validNumber(rstText.getText().toString()))
                return;

            nums.add(Float.parseFloat(rstText.getText().toString()));
        }
            if(nums.size() < 2 || ops.size() < 1)
                return;

            switch(ops.get(0)) {
                case '+':
                    total = nums.get(0) + nums.get(1);
                    break;

                case '-':
                    total = nums.get(0) - nums.get(1);
                    break;

                case '/':
                    total = nums.get(0) / nums.get(1);
                    break;

                case '*':
                    total = nums.get(0) * nums.get(1);
                    break;

                default:
                    break;
            }

            setOpText(nums.get(0).toString() + " " + ops.get(0).toString() + " " + nums.get(1).toString());
            rstText.setText("" + total);

            nums.clear();
            ops.clear();

            return;
    }


    public void addValue(View _view)
    {
        String _val = ((Button)(findViewById(_view.getId()))).getText().toString();

        rstText.setText(rstText.getText().toString() + _val);
    }

    public void onCClick(View _view)
    {
        nums.clear();
        ops.clear();

        opText.setText("");
        rstText.setText("");
    }

    public void onCEClick(View _view)
    {
        if(rstText.getText().toString().length() <= 0)
            return;

        rstText.setText(rstText.getText().toString().substring(0, rstText.getText().toString().length() - 1));
    }

    public void onPIClick(View _view)
    {
        rstText.setText("" + ((float)(Math.PI)));
    }

    private void setOpText(String _text)
    {
        opText.setText(_text);
    }

}