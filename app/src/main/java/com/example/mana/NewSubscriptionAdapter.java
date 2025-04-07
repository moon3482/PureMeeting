package com.example.mana;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mana.mainPage.IdealProfileLoad;

import java.util.ArrayList;

public class NewSubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewSubscriptionData> arrayList = null;
    private Context context;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;

    public NewSubscriptionAdapter(ArrayList<NewSubscriptionData> arrayList, Context context, RecyclerView recyclerView, LinearLayout linearLayout) {
        this.arrayList = arrayList;
        this.context = context;
        this.recyclerView = recyclerView;
        this.linearLayout = linearLayout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_subscription_item, parent, false);
        return new NewSubscription(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NewSubscriptionData newSubscriptionData = arrayList.get(position);
        NewSubscription holder1 = (NewSubscription) holder;
        //TODO("이미지 표시")
//        Glide.with(holder1.itemView.getContext()).load(newSubscriptionData.getProfileImage()).into(holder1.profile);
        NewMessageLookup(newSubscriptionData.youId);
        String detail = "<b><font color = \"#0000FF\" >[자세히보기]</font></b>";
        holder1.SubscriptionText.setText(Html.fromHtml(newSubscriptionData.getName() + "님이 대화신청을 하였습니다.\n"+detail) );
        holder1.Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("수락을 하시겠습니까?");
                builder.setPositiveButton("수락", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OkNewSub(newSubscriptionData.youId);
                        arrayList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();

            }
        });
        holder1.Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("정말 거절하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CancelNewSub(newSubscriptionData.youId);
                        arrayList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();

                if (arrayList.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IdealProfileLoad.class);
                intent.putExtra("id", newSubscriptionData.youId);
                intent.putExtra("shinchung", newSubscriptionData.myId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class NewSubscription extends RecyclerView.ViewHolder {
        //TODO("프로필 이미지 세팅")
//        CircleImageView profile;
        TextView SubscriptionText;
        Button Cancel, Ok;

        public NewSubscription(@NonNull View itemView) {
            super(itemView);
//            profile = itemView.findViewById(R.id.cvNewSubscription);
            SubscriptionText = itemView.findViewById(R.id.tvNewSubscription);
            Cancel = itemView.findViewById(R.id.btnSubscriptionCancel);
            Ok = itemView.findViewById(R.id.btnSubscriptionOk);

        }
    }

    public void CancelNewSub(String string) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("loginId", context.MODE_PRIVATE);
        String myid = sharedPreferences.getString("loginId", "");
        String url = new ServerIP().http+"Android/DeleteNewSubscription.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        simpleMultiPartRequest.setShouldCache(false);
//        simpleMultiPartRequest.addStringParam("id", myid);
//        simpleMultiPartRequest.addStringParam("you", string);
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(simpleMultiPartRequest);
    }

    public void OkNewSub(String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginId", context.MODE_PRIVATE);
        String myid = sharedPreferences.getString("loginId", "");
        String url = new ServerIP().http+"Android/OkNewSubscription.php";
        //TODO("수락 요청")
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.e("수락 성공", response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setTitle("수락을 완료하였습니다.");
//                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(context, chatPage.class);
//                                context.startActivity(intent);
//                            }
//
//                        });
//                        builder.show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        simpleMultiPartRequest.setShouldCache(false);
//        simpleMultiPartRequest.addStringParam("id", myid);
//        Log.e("무슨 아이디", myid);
//        Log.e("무슨 아이디", string);
//        simpleMultiPartRequest.addStringParam("you", string);
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(simpleMultiPartRequest);
    }

    public void NewMessageLookup(String youid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginId", context.MODE_PRIVATE);
        String myid = sharedPreferences.getString("loginId", "");
        String url = new ServerIP().http+"Android/NewMessageLookup.php";
        //TODO("메세지 읽기?")
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Log.e("리사이클", youid);
//        Log.e("리사이클", myid);
//
//        simpleMultiPartRequest.setShouldCache(false);
//        simpleMultiPartRequest.addStringParam("id", myid);
//        simpleMultiPartRequest.addStringParam("you", youid);
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(simpleMultiPartRequest);
    }
}
