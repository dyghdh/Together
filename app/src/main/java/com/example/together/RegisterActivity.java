package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText user_name,user_Email, user_Pw, user_Pw_cheack,user_PN;
    private Button registerBtn;
    private FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
         firebaseAuth = FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        user_Email = findViewById(R.id.new_Email);
        user_name = findViewById(R.id.new_name);
        user_Pw = findViewById(R.id.new_pw);
        user_Pw_cheack = findViewById(R.id.new_pw_cheack);
        user_PN = findViewById(R.id.new_pn);
        registerBtn = findViewById(R.id.btn_in_register);

        registerBtn.setOnClickListener(v ->{

                //가입 정보 가져오기
                final String email = user_Email.getText().toString().trim();
                String pwd = user_Pw.getText().toString().trim();
                String pwdcheck = user_Pw_cheack.getText().toString().trim();
                String name = user_name.getText().toString().trim();
                String phone = user_PN.getText().toString().trim();

                boolean email_check = Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email);
                boolean name_check = Pattern.matches("^[가-힣]*$", name);
                boolean phone_check = Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phone);
                boolean pw_check = Pattern.matches("^(?=.*\\d)(?=.*[a-z]).{6,}$",pwd);
                final String[] token = new String[1];
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String mToken = instanceIdResult.getToken();
                        token[0] = mToken;
                    }
                });
                String userTokken = token[0];
                boolean input = !email.equals("") && !pwd.equals("") && !pwdcheck.equals("") && !name.equals("") && !phone.equals("");
                boolean nullInput = email.equals("") && pwd.equals("") && pwdcheck.equals("") && name.equals("") && phone.equals("");
                boolean patternCheck = email_check && pwd.equals(pwdcheck) && name_check && phone_check && pw_check;
                boolean nullCheck = !email_check && !pwd.equals(pwdcheck) && !name_check && !phone_check;
                if (patternCheck && input && pwd.equals(pwdcheck)) {
                    Log.d(TAG, "등록 버튼 " + email + " , " + pwd);

                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, task -> {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        String uid = user.getUid();
                        if (task.isSuccessful()) {
                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("uid", uid);
                            hashMap.put("email",email);
                            hashMap.put("username", name);
                            hashMap.put("phone", phone);
                            hashMap.put("token", userTokken);


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("users");
                            reference.child(uid).setValue(hashMap);

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            return;  //해당 메소드 진행을 멈추고 빠져나감.
                        }
                });
                    //비밀번호 오류시
                }else {
                    String text = "";
                    if (nullInput || nullCheck) {
                        Toast.makeText(RegisterActivity.this, text + "다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                     if (!email_check|| email.equals(null))
                        text = text+"이메일";
                    if (!pwd.equals(pwdcheck)||pwd==""|| pwdcheck.equals(null)||!pw_check)
                        text= text + " 비밀번호";
                    if(!name_check|| name.equals(null))
                        text = text+" 이름";
                    if (!phone_check|| phone.equals(null))
                        text = text+" 전화번호";
                        Toast.makeText(RegisterActivity.this, text + "을(를) 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();

                    return;
                }

        });

    }
}
