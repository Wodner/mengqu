package zkk.com.mengqu.game.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zkk.com.mengqu.R;
import zkk.com.mengqu.game.adapter.GameInfoAdapter;
import zkk.com.mengqu.game.bean.GameInfoBean;
import zkk.com.mengqu.game.bean.InfoItemBean;
import zkk.com.mengqu.retrofit2.api.MengquApi;
import zkk.com.mengqu.simplepreferences.SharePreferences;
import zkk.com.mengqu.util.Utils;
import zkk.com.mengqu.view.refresh.LoadMoreListView;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class GameInfoFragment  extends Fragment{

    private  View view;
    List<InfoItemBean> infoItemBeens=new ArrayList<InfoItemBean>();
    private GameInfoAdapter gameInfoAdapter;
    private Context context;

    private LoadMoreListView listView;
    private SwipeRefreshLayout sf_lv_news_item_swipe_refresh;
    private boolean isLoad = false;
    private boolean hasMore = true;
    private  String lastTime;//最后一个的时间

    Observer<GameInfoBean> observer=new Observer<GameInfoBean>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            sf_lv_news_item_swipe_refresh.setRefreshing(false);
            Log.d("zkk---","错误-"+e);
        }

        @Override
        public void onNext(GameInfoBean result) {
            sf_lv_news_item_swipe_refresh.setRefreshing(false);
            String strJson = "";
            Gson gson = new Gson();
            strJson = gson.toJson(result);
            if(strJson!=null&&!"".equals(strJson)
                    &&strJson.equals(SharePreferences.getInstance(context).getGameInfo())){
                SharePreferences.getInstance(context).setGameInfo(strJson);
                return ;
            }

            SharePreferences.getInstance(context).setGameInfo(strJson);
            infoItemBeens.clear();
            List<InfoItemBean> info=new ArrayList<InfoItemBean>();
            //对数据进行处理
           List<GameInfoBean.PostBean.DataBean> postBean=result.getPost().getData();
           for(int i=0;i<postBean.size();i++){

               for(int j=0;j<postBean.get(i).getList().size();j++){
                   InfoItemBean infoItem;
                   if(j==0){
                       infoItem=new InfoItemBean(0,postBean.get(i).getList().get(j).getId(),postBean.get(i).getList().get(j).getTitle()
                       , postBean.get(i).getList().get(j).getCoverImage(), postBean.get(i).getList().get(j).getCategory(),postBean.get(i).getTime());
                   }else{
                       infoItem=new InfoItemBean(1,postBean.get(i).getList().get(j).getId(),postBean.get(i).getList().get(j).getTitle()
                               , postBean.get(i).getList().get(j).getCoverImage(), postBean.get(i).getList().get(j).getCategory(),postBean.get(i).getTime());
                   }
                   info.add(infoItem);
               }
           }
            infoItemBeens.addAll(info);
            gameInfoAdapter.notifyDataSetChanged();
        }
    };



    Observer<GameInfoBean> observer2=new Observer<GameInfoBean>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.d("zkk---","错误-"+e);
        }

        @Override
        public void onNext(GameInfoBean result) {

            List<InfoItemBean> info=new ArrayList<InfoItemBean>();
            //对数据进行处理
            List<GameInfoBean.PostBean.DataBean> postBean=result.getPost().getData();
            for(int i=0;i<postBean.size();i++){

                for(int j=0;j<postBean.get(i).getList().size();j++){
                    InfoItemBean infoItem;
                    if(j==0){
                        infoItem=new InfoItemBean(0,postBean.get(i).getList().get(j).getId(),postBean.get(i).getList().get(j).getTitle()
                                , postBean.get(i).getList().get(j).getCoverImage(), postBean.get(i).getList().get(j).getCategory(),postBean.get(i).getTime());
                    }else{
                        infoItem=new InfoItemBean(1,postBean.get(i).getList().get(j).getId(),postBean.get(i).getList().get(j).getTitle()
                                , postBean.get(i).getList().get(j).getCoverImage(), postBean.get(i).getList().get(j).getCategory(),postBean.get(i).getTime());
                    }
                    info.add(infoItem);
                }
            }
            if(info!=null&&info.size()>0){
                lastTime=info.get(info.size()-1).getTime();
            }

            infoItemBeens.addAll(info);
            gameInfoAdapter.notifyDataSetChanged();
        }
    };


    public static GameInfoFragment newInstance(){

        GameInfoFragment gameInfoFragment=new GameInfoFragment();
        return gameInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_gameinfo,container,false);
        context=getParentFragment().getActivity();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void getAppIndexData(int dataType,int pageNo3){

        infoItemBeens.clear();
        if(!Utils.isNull(SharePreferences.getInstance(context).getGameInfo())){
            Gson gson = new Gson();
            GameInfoBean list = gson.fromJson(SharePreferences.getInstance(context).getGameInfo(),GameInfoBean.class );

            List<InfoItemBean> info=new ArrayList<InfoItemBean>();
            //对数据进行处理
            List<GameInfoBean.PostBean.DataBean> postBean=list.getPost().getData();
            for(int i=0;i<postBean.size();i++){

                for(int j=0;j<postBean.get(i).getList().size();j++){
                    InfoItemBean infoItem;
                    if(j==0){
                        infoItem=new InfoItemBean(0,postBean.get(i).getList().get(j).getId(),postBean.get(i).getList().get(j).getTitle()
                                , postBean.get(i).getList().get(j).getCoverImage(), postBean.get(i).getList().get(j).getCategory(),postBean.get(i).getTime());
                    }else{
                        infoItem=new InfoItemBean(1,postBean.get(i).getList().get(j).getId(),postBean.get(i).getList().get(j).getTitle()
                                , postBean.get(i).getList().get(j).getCoverImage(), postBean.get(i).getList().get(j).getCategory(),postBean.get(i).getTime());
                    }
                    info.add(infoItem);
                }
            }
            if(info!=null&&info.size()>0){
                lastTime=info.get(info.size()-1).getTime();
            }

            infoItemBeens.addAll(info);
            gameInfoAdapter.notifyDataSetChanged();
        }


        //http://www.moeju.cn/api//gamenews?lastTime=0&dk=5ed40069e20354be&ak=~68EECA63-0FB4-1EB8-628D-CDCF663CEB41&v=2.1.0&os=a&channel=Xiaomi
        MengquApi.getwwwInstance().getgamenews("0","5ed40069e20354be","~68EECA63-0FB4-1EB8-628D-CDCF663CEB41","2.1.0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    private void getAppIndexMore(String dataType,String pageNo3){
        isLoad =false;
        //http://www.moeju.cn/api//gamenews?lastTime=0&dk=5ed40069e20354be&ak=~68EECA63-0FB4-1EB8-628D-CDCF663CEB41&v=2.1.0&os=a&channel=Xiaomi
        MengquApi.getwwwInstance().getgamenews(dataType,"5ed40069e20354be","~68EECA63-0FB4-1EB8-628D-CDCF663CEB41","2.1.0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }


    private  void  init(){
        listView=(LoadMoreListView) view.findViewById(R.id.lv_gameinfo);
        gameInfoAdapter=new GameInfoAdapter(infoItemBeens,context);
        listView.setAdapter(gameInfoAdapter);

        sf_lv_news_item_swipe_refresh=(SwipeRefreshLayout)view.findViewById(R.id.sf_gameinfo_swipe_refresh);
        sf_lv_news_item_swipe_refresh.setColorSchemeColors(Color.GREEN, Color.RED, Color.YELLOW);
        sf_lv_news_item_swipe_refresh.setEnabled(true);


        listView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {

                if (isLoad || !hasMore) {
                    listView.setFooter(LoadMoreListView.Mode.NOMORE);
                    return;
                }
                isLoad = true;
                getAppIndexMore(lastTime,"");
                listView.setFooter(LoadMoreListView.Mode.LOAD);
            }
        });
        sf_lv_news_item_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppIndexData(0,0);
            }
        });

        getAppIndexData(0,0);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }
}
