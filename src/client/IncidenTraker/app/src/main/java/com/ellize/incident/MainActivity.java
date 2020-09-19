package com.ellize.incident;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ellize.incident.databinding.ActivityMainBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import static com.ellize.incident.MainViewModel.ACCOUNT_TYPE;


public class MainActivity extends AppCompatActivity implements InfoDialog.DialogInfoListener{
    MainViewModel viewModel;
    Menu mMenu;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private Drawable mDrawable;
    int inx = 0;
    BaseCategoryFragment[] categoryFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fragments for categories
        categoryFragments = new BaseCategoryFragment[getResources().getStringArray(R.array.categories).length];
        for(int i = 0; i < categoryFragments.length;++i){
            categoryFragments[i] = BaseCategoryFragment.newInstance(getResources().getStringArray(R.array.categories)[i],i);
        }
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.isAuth.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    if(getSupportFragmentManager().findFragmentByTag("scan")==null) {
                        getSupportFragmentManager().beginTransaction().
                                //replace(R.id.fl_container,ScanStartFragment.newInstance()).
                            replace(R.id.fl_container, CategoriesFragment.newInstance(), "cats").
                                commit();
                        Log.d("fragment", "cats fragment added");
//                        setAvatar();
                    }
                } else {
                    if(!(getSupportFragmentManager().findFragmentById(R.id.fl_container) instanceof LoginFragment)) {
                        getSupportFragmentManager().
                                beginTransaction().
                                replace(R.id.fl_container, LoginFragment.newInstance()).
                                commit();
                    }
                }
            }
        });
        viewModel.qRresult.observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                if(getSupportFragmentManager().findFragmentByTag("result")==null) {
                    getSupportFragmentManager().beginTransaction().
//                        todo    replace(R.id.fl_container, ResultFragment.newInstance(), "result").
                            addToBackStack("result").commit();
                    Log.d("fragment", "result fragment added");
                }
            }
        });


        binding.setLifecycleOwner(this);
        final EditText ed_test_locale = binding.edTestLocale;

        binding.btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.this.capture();

            }
        });
        final TextView tv_log = binding.tvLog;
        viewModel.test_log.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // tv_log.append("\n"+s+"\n");
            }
        });

        binding.setViewmodel(viewModel);



        if(savedInstanceState==null) {
            AccountManager accountManager = AccountManager.get(this);
            Account account = isAccountExist(accountManager);
            if(account != null){
                User user = new User(account.name,accountManager.getPassword(account));
                user.company = accountManager.getUserData(account,"company");
                user.inn = accountManager.getUserData(account,"inn");
                user.fio = accountManager.getUserData(account,"fio");
                user.avatar_url = accountManager.getUserData(account,"avatar");
                user.locale = "ru";
                viewModel.user.setValue(user);
                viewModel.login(!user.locale.equals("ru"));
            } else {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fl_container, LoginFragment.newInstance()).commit();
            }
        }
        viewModel.categoryFragmentTraker.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int id = findViewById(R.id.fl_container2) == null?R.id.fl_container : R.id.fl_container2;
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(id,categoryFragments[integer],"cat i").addToBackStack(null).commit();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},123);
            }
        }

    }


    Account isAccountExist(AccountManager accountManager){
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if(accounts.length >= 1) return accounts[0]; else return null;
    }
    public void setAvatar(){
        String url =viewModel.user.getValue().avatar_url;
        if(url!=null&&!url.equals("")&&mMenu!=null){
            final ImageView imageView = new ImageView(MainActivity.this);
            final MenuItem item = mMenu.findItem(R.id.mi_account);
            Picasso.get().load(url).resize(96,96).centerCrop().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    mDrawable = imageView.getDrawable();
                    item.setIcon(mDrawable);
                }

                @Override
                public void onError(Exception e) {

                }
            });

        }
    }
    void capture(){
//        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
//        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true/*autoFocus.isChecked()*/);
//        intent.putExtra(BarcodeCaptureActivity.UseFlash, true/*useFlash.isChecked()*/);
//        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public boolean mReturnResult = false;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(mReturnResult){
            mReturnResult = false;
//            getSupportFragmentManager().beginTransaction().
//                  todo  replace(R.id.fl_container, ScanQRFragment.newInstance(), "scan").commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 123) {
            //Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            return;
        }

//        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
//                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.write_permission_needed)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == RC_BARCODE_CAPTURE) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    //statusMessage.setText(R.string.barcode_success);
//                    viewModel.code.setValue(barcode.displayValue);
//                    //Toast.makeText(this,"barcode:"+barcode.displayValue,Toast.LENGTH_SHORT).show();
//                    //  viewModel.sendQR(false);
//                    /*;*/
////                    barcodeValue.setText(barcode.displayValue);
////                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
//                } else {
////                    statusMessage.setText(R.string.barcode_failure);
////                    Log.d(TAG, "No barcode captured, intent data is null");
//                    //Toast.makeText(this,"No barcode captured, intent data is null",Toast.LENGTH_SHORT).show();
//                }
//            } else {
////                statusMessage.setText(String.format(getString(R.string.barcode_error),
////                        CommonStatusCodes.getStatusCodeString(resultCode)));
//                //Toast.makeText(this,"Something wrong!",Toast.LENGTH_SHORT).show();
//            }
//        }
//        else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    public void onDialogExitClicked() {
        User user = viewModel.user.getValue();
        viewModel.user.setValue(new User(user.login,user.psw));
        viewModel.isAuth.setValue(false);
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        for(Account account:accounts){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                accountManager.removeAccount(account, this,null,null);
            } else {
                accountManager.removeAccount(account,null,null);
            }
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fl_container,new LoginFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        if(mDrawable == null){
//            setAvatar();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mi_account){
            InfoDialog.newInstance(viewModel.user.getValue()).show(getSupportFragmentManager(),"");
            return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}