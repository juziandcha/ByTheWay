package com.example.wmj.bytheway.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.ConnSup.MD5;
import com.example.wmj.bytheway.R;
import com.example.wmj.bytheway.Util.GetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by wmj on 2018/1/4.
 */

public class Dialog_changedata extends DialogFragment {
    private EditText edit_name;
    private Spinner spin_gender;
    private EditText edit_phone;
    private Button btn_change;

    private String userGender;//在界面选中的值
    private Dialog_changedata.DialogClickListener listener;

    public interface DialogClickListener{
        void onDialogClick(String Name,String Password);
    }

    public void setOnDialogClick(Dialog_changedata.DialogClickListener listener){
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.dialog_changedata, null);

        edit_name=(EditText)view.findViewById(R.id.new_user_name);
        spin_gender=(Spinner)view.findViewById(R.id.spinner);
        edit_phone=(EditText)view.findViewById(R.id.new_phone_number);
        btn_change=(Button)view.findViewById(R.id.change_data);

        spin_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //拿到被选择项的值
                userGender = (String) spin_gender.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                userGender="";
            }
        });
        //获取当前用户数据显示各个栏目中
        try {
            String sql = "SELECT * FROM Person WHERE ID=?;";
            JSONObject keyValue = new JSONObject();
            keyValue.put("1", ByTheWayActivity.userData.getID());

            //获取结果并显示
            GetData.runGetData(sql, "query", keyValue);
            JSONArray jsonArray = new JSONArray(ByTheWayActivity.dataResult);
            JSONObject jsonProfile= jsonArray.getJSONObject(0);

            //如果之前有资料，那么就显示在对应的编辑框内
            if(!(jsonProfile.getString("Name").equals("")))
                edit_name.setText(jsonProfile.getString("Name"));
            if(!(jsonProfile.getString("PhoneNumber").equals("")))
                edit_phone.setText(jsonProfile.getString("PhoneNumber"));

            String gender=jsonProfile.getString("Gender");//数据中存储的性别
            if(!gender.equals("")){
                gender=jsonProfile.getString("Gender");
                if(gender.equals("male"))
                    spin_gender.setSelection(0);//Note: spinner从0开始
                else if(gender.equals("female"))
                    spin_gender.setSelection(1);
                else
                    spin_gender.setSelection(2);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            Toast.makeText(getActivity(), "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
        }

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edit_name.getText().toString().trim();
                String gender=userGender;
                String phoneNumber=edit_phone.getText().toString().trim();



                try {
                    String sql = "UPDATE Person SET Name=?,Gender=?,PhoneNumber=? WHERE ID=?";
                    JSONObject keyValue = new JSONObject();
                    keyValue.put("1", name);
                    keyValue.put("2", gender);
                    keyValue.put("3", phoneNumber);
                    keyValue.put("4", ByTheWayActivity.userData.getID());

                    GetData.runGetData(sql,"update",keyValue);
                    JSONObject jsonObject=new JSONObject(ByTheWayActivity.dataResult);
                    if(jsonObject.getString("succeed").equals("true")){
                        Toast.makeText(getActivity(), "修改资料成功", Toast.LENGTH_SHORT).show();

                        //更新当前应用内保存的用户数据
                        ByTheWayActivity.userData.UserData(ByTheWayActivity.userData.getID(),name,gender,phoneNumber);
                        TextView user_name=getActivity().findViewById(R.id.user_name);
                        if(name.equals(""))
                            user_name.setText(ByTheWayActivity.userData.getID());
                        else
                            user_name.setText(name);

                        dismiss();
                    } else{
                        Toast.makeText(getActivity(), "修改资料失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex){
                    ex.printStackTrace();
                } catch (Exception ex){
                    Toast.makeText(getActivity(), "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);

        return builder.create();
    }
}
