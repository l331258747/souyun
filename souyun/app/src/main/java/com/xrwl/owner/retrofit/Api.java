package com.xrwl.owner.retrofit;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.Address2;
import com.xrwl.owner.bean.Address2zhongzhuandian;
import com.xrwl.owner.bean.Aliyunbank;
import com.xrwl.owner.bean.Auth;
import com.xrwl.owner.bean.Business;
import com.xrwl.owner.bean.Distance;
import com.xrwl.owner.bean.GongAnAuth;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.New;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.bean.PostOrder;
import com.xrwl.owner.bean.Receipt;
import com.xrwl.owner.bean.Register;
import com.xrwl.owner.bean.Update;
import com.xrwl.owner.bean.WxCode;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.module.me.bean.Bank;
import com.xrwl.owner.module.me.bean.Tixianlist;
import com.xrwl.owner.module.publish.bean.CarManageBean;
import com.xrwl.owner.module.publish.bean.CarManageSearchBean;
import com.xrwl.owner.module.publish.bean.CargoBean;
import com.xrwl.owner.module.publish.bean.Changtulingdan;
import com.xrwl.owner.module.publish.bean.CompanyManageBean;
import com.xrwl.owner.module.publish.bean.DzNameManageBean;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.Truck;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @POST @Body  这种适合Content-Type:applicaiton/json;charset=utf-8
 * @POST @FieldMap 这种适合Content-Type:application/x-www-form-urlencoded
 * Created by www.longdw.com on 2018/4/8 下午8:17.
 */
public interface Api {


 /** 微信登陆获取openid accessToken值用于后期操作*/
 @GET("sns/oauth2/access_token")
 Observable<BaseEntity<WxCode>> wxtoken(@QueryMap Map<String, String> params);


 /** 微信登陆获取微信的个人信息*/
 @GET("sns/userinfo")
 Observable<BaseEntity<WxCode>> wxuserinfo(@QueryMap Map<String, String> params);



// /** 确认收货 */
// @GET("Order/Confirmation")
// Observable<BaseEntity<OrderDetail>> confirmOwnerOrder(@QueryMap Map<String, String> params);





 /** 注册接口*/
 @FormUrlEncoded
 @POST("User/Register")
 Observable<BaseEntity<Register>> register(@FieldMap Map<String, String> params);

 /** 登陆接口*/
 @GET("User/Loginhuozhu")
 Observable<BaseEntity<Account>> login(@QueryMap Map<String, String> params);


    /** 微信登陆接口*/
    @GET("User/Loginhuozhuwx")
    Observable<BaseEntity<Account>> loginwx(@QueryMap Map<String, String> params);


 /** 短信验证码接口 */
 @GET("admin/map/sms")
 Observable<BaseEntity<MsgCode>> getCode(@QueryMap Map<String, String> params);


 /** 短信点击按钮操作模式--货主和司机相互点击 */
 @GET("api/map/type_sms")
 Observable<BaseEntity<MsgCode>> getCodeButton(@QueryMap Map<String, String> params);


 /** 群发短信接口 */
 @GET("admin/map/arrsms")
 Observable<BaseEntity> sendMsg(@QueryMap Map<String, String> params);


 /** 修改密码接口--暂时无用 */
 @GET("User/ModifyPwd")
 Observable<BaseEntity> modify(@QueryMap Map<String, String> params);


 /** 修改密码接口--暂时无用 */
 @GET("User/wxtel")
 Observable<BaseEntity> wxtel(@QueryMap Map<String, String> params);

 /** 同城计算两点间距离 */
 @GET("admin/map")
 Observable<BaseEntity<Distance>> calculateDistance(@QueryMap Map<String, String> params);

 /** 长途计算两点间距离 */
 @GET("admin/map/compre")
 Observable<BaseEntity<Distance>> calculateLongDistance(@QueryMap Map<String, String> params);

 /** 长途计算两点间距离 */
 @GET("admin/map/retrans")
 Observable<BaseEntity<Distance>> requestCityLonLat(@QueryMap Map<String, String> params);


 /** 身份证 */
 @FormUrlEncoded
 @POST("admin/idcar/cardids")
 Observable<BaseEntity<GongAnAuth>> shenfenzheng(@FieldMap Map<String, String> params);

 /** 检查更新 */
 @GET("Other/Update")
 Observable<BaseEntity<Update>> checkUpdate(@QueryMap Map<String, String> params);

 /** 下载apk */
 @GET
 Observable<ResponseBody> downloadApk(@Url String fileUrl);

 /** 获取好友列表 */
 @GET("Friend/ListFriend")
 Observable<BaseEntity<List<Friend>>> getFriendList(@QueryMap Map<String, String> params);

 /** 添加好友 */
 @FormUrlEncoded
 @POST("Friend/AddFriend")
 Observable<BaseEntity> addFriends(@FieldMap Map<String, String> params);




 /** 添加在线点评 */
 @FormUrlEncoded
 @POST("Evaluate/AddEvaluate")
 Observable<BaseEntity> dianping(@FieldMap Map<String, String> params);

 /** 查看点评分数 */
 @GET("Pingjia/listpingjia")
 Observable<BaseEntity<OrderDetail>> aaa(@QueryMap Map<String, String> params);

 /** 添加银行卡 */
 @FormUrlEncoded
 @POST("Card/AddBankCard")
 Observable<BaseEntity> addBank(@FieldMap Map<String, String> params);

 /***对接第三方接口进行银行卡认证*/
 @GET("Card/aliyunbank")
 Observable<BaseEntity<Aliyunbank>> getbanksanyuansu(@QueryMap Map<String, String> params);



 /** 银行卡列表 */
 @GET("Card/ListBankCard")
 Observable<BaseEntity<List<Bank>>> getBankList(@QueryMap Map<String, String> params);


 /** 余额里面绑定银行卡接口 */
 @GET("Order/bangding")
 Observable<BaseEntity<OrderDetail>> confirmOwnerbangdingyinhangkaOrder(@QueryMap Map<String, String> params);


 /** 提现 */
 @GET("Order/TiXian")
 Observable<BaseEntity> tixian(@QueryMap Map<String, String> params);

 /** 提现列表 */
 @GET("Order/tixianlist")
 Observable<BaseEntity<List<Tixianlist>>> gettixianlist(@QueryMap Map<String, String> params);

 /** 余额支付 */
 @GET("Order/yuepay")
 Observable<BaseEntity> yuepay(@QueryMap Map<String, String> params);

 /** 余额退款 */
 @GET("Order/yuepayrefund")
 Observable<BaseEntity> yuepayrefund(@QueryMap Map<String, String> params);

 /** 红包随机金额 */
 @GET("Order/random")
 Observable<BaseEntity> hongbaomoney(@QueryMap Map<String, String> params);

 /** 红包钱存入余额 */
 @GET("hongbao/hongbao")
 Observable<BaseEntity> hongbao(@QueryMap Map<String, String> params);

 /** 发起支付 */
 @GET("Order/Pay")
 Observable<BaseEntity<PayResult>> pay(@QueryMap Map<String, String> params);

 /** 发起充值 */
 @GET("Order/addchongzhi")
 Observable<BaseEntity<PayResult>> czpay(@QueryMap Map<String, String> params);

 /** 货主--->发货微信支付成功 */
 @GET("weixin/wxhd")
 Observable<BaseEntity<PayResult>> wxonlinePay(@QueryMap Map<String, String> params);


 /** 货主--->发货微信支付成功---新接口支付全部都统一的接口 */
 @GET("Order/results")
 Observable<BaseEntity<PayResult>> results(@QueryMap Map<String, String> params);



 /** 货主--->发货微信支付成功代收货款 */
 @GET("weixin/wxhddaishou")
 Observable<BaseEntity<PayResult>> wxonlinedaishouPay(@QueryMap Map<String, String> params);

 /** 货主--->发货微信支付成功 */
 @GET("weixin/wxhd")
 Observable<BaseEntity<OrderDetail>> cancelpayOwnerOrder(@QueryMap Map<String, String> params);


 /** 货主--->发货微信支付----php */
 @FormUrlEncoded
 @POST("api/wx_pay/recharge")
 Observable<BaseEntity<PayResult>> xrwlwxpay(@FieldMap Map<String, String> params);




 /** 货主--->发货微信支付退款----php */
 @FormUrlEncoded
 @POST("api/wx_pay/wx_refund")
 Observable<BaseEntity<PayResult>> wx_refund(@FieldMap Map<String, String> params);




// /** 货主--->发货微信退款----php */
// @FormUrlEncoded
// @POST("api/wx_pay/wx_refund")
// Observable<BaseEntity<PayResult>> wx_refund(@FieldMap Map<String, String> params);



 /** 发货完成确定订单的时候 线上支付 */
 @GET("Order/onlinePay")
 Observable<BaseEntity<PayResult>> onlinePay(@QueryMap Map<String, String> params);


 /** 货主-->余额 */
 @GET("History/Balanceprice")
 Observable<BaseEntity<HistoryOrder>> getHistoryBalanceList(@QueryMap Map<String, String> params);

 /** 添加发货地址 */
 @GET("Order/AddAddress")
 Observable<BaseEntity> addAddress(@QueryMap Map<String, String> params);

 /** 获取发货地址列表 */
 @GET("Order/ListAddress")
 Observable<BaseEntity<List<Address2>>> getAddressList(@QueryMap Map<String, String> params);

 /** 删除地址 */
 @FormUrlEncoded
 @POST("Car/deleteaddress")
 Observable<BaseEntity> CancelAddress(@FieldMap Map<String, String> params);


 /** 事实新闻 */
 @GET("Order/new")
 Observable<BaseEntity<List<New>>> getDatanew(@QueryMap Map<String, String> params);



 /** 事实新闻详情 */
 @GET("Order/newshow")
 Observable<BaseEntity<List<New>>> getDatanewshow(@QueryMap Map<String, String> params);



 /** 获取业务列表 */
 @GET("Order/xiaohongdian")
 Observable<BaseEntity<List<Business>>> getBusinessList(@QueryMap Map<String, String> params);

 /** 标记业务列表已读 */
 @GET("Order/showxiaohongdian")
 Observable<BaseEntity> markBusinessRead(@QueryMap Map<String, String> params);


 /**标记系统业务列表（通知、公告）*/
 @GET("system/notice")
 Observable<BaseEntity<List<Business>>> getSystemList(@QueryMap Map<String, String> params);

 /** 添加发票 */
 @GET("Friend/Addreceipt")
 Observable<BaseEntity> addReceipt(@QueryMap Map<String, String> params);

 /** 获取发票列表 */
 @GET("Friend/Listreceipt")
 Observable<BaseEntity<List<Receipt>>> getReceiptList(@QueryMap Map<String, String> params);


 /** 获取实名认证信息 */
 @GET("User/AuthenticationListhuozhu")
 Observable<BaseEntity<Auth>> getAuthInfo(@QueryMap Map<String, String> params);

 /** 货主提交实名认证信息 */
 @Multipart
 @POST("User/Authentication")
 Observable<BaseEntity> postAuthInfo(@PartMap Map<String, RequestBody> params);


 /** 货主提交实名认证信息对接公安接口 */
 @Multipart
 @POST("admin/idcar/cardids")
 Observable<BaseEntity> postAuthInfoshenfenzheng(@PartMap Map<String, RequestBody> params);



 /** 货主提交实名认证信息普通认证 */
 @GET("User/Authenticationputong")
 Observable<BaseEntity> postputongAuthInfo(@QueryMap Map<String, String> params);


 /** 货主--->线路导航 */
 @GET("Order/Navigation")
 Observable<BaseEntity<OrderDetail>> nav(@QueryMap Map<String, String> params);


 /** 我的订单 */
 @GET("Order/ListOrder")
 Observable<BaseEntity<List<Order>>> getOwnerOrderList(@QueryMap Map<String, String> params);

 /** 订单详情 */
 @GET("Order/ListShow")
 Observable<BaseEntity<OrderDetail>> getOwnerOrderDetail(@QueryMap Map<String, String> params);

 /** 定位司机 */
 @GET("Order/Coordinates")
 Observable<BaseEntity<OrderDetail>> locationDriver(@QueryMap Map<String, String> params);

 /** 取消订单 */
 @GET("Order/DeleteOrder")
 Observable<BaseEntity<OrderDetail>> cancelOwnerOrder(@QueryMap Map<String, String> params);


 /** 确认收货 */
 @GET("Order/Confirmation")
 Observable<BaseEntity<OrderDetail>> confirmOwnerOrder(@QueryMap Map<String, String> params);


 /** 线下支付提示给司机 */
 @GET("Order/Confirmationtixing")
 Observable<BaseEntity<OrderDetail>> confirmtixingOwnerOrder(@QueryMap Map<String, String> params);


 /** 确认开始运输 */
 @GET("Order/kaishiyunshu")
 Observable<BaseEntity<OrderDetail>> confirmOwnerkaishiyunshuOrder(@QueryMap Map<String, String> params);


 /** 确认收获按钮点击让司机在点击到达 */
 @GET("Order/CancelOrderkaishiyunshuhuozhu")
 Observable<BaseEntity<OrderDetail>> cancelDriverkaishiyunshuOrder(@QueryMap Map<String, String> params);


 /** 获取发货地址中转点列表 */
 @GET("Order/ListAddresszhongzhuandian")
 Observable<BaseEntity<List<Address2zhongzhuandian>>> getAddresszhongzhuandianList(@QueryMap Map<String, String> params);


 /** 提交订单 */
 @Multipart
 @POST("Order/AddOrder")
 Observable<BaseEntity<PostOrder>> postOrder(@PartMap Map<String, RequestBody> params);


 /** 历史交易 */
 @GET("History/HistoryTradeList")
 Observable<BaseEntity<HistoryOrder>> getHistoryList(@QueryMap Map<String, String> params);


 /** 获取车型---暂时闲置 */
 @GET("Car/ListCar")
 Observable<BaseEntity<List<Truck>>> getTruckList(@QueryMap Map<String, String> params);


 /** 服务器返回价格接口 */
 @FormUrlEncoded
 @POST("api/count")
 Observable<BaseEntity<Integer>> calculateDistancecount(@FieldMap Map<String, String> params);


 /** 服务器返回价格接口---长途零担 */
 @FormUrlEncoded
 @POST("api/count")
 Observable<BaseEntity<Changtulingdan>> calculateDistancecountlingdan(@FieldMap Map<String, String> params);

 /** 服务器返回车型表---暂时闲置 */
 @FormUrlEncoded
 @POST("api/count/car_type")
 Observable<BaseEntity<List<Truck>>> getListTrucks(@FieldMap Map<String, String> params);

 /** 找回密码 */
 @GET("User/Retrieve")
 Observable<BaseEntity> retrieve(@QueryMap Map<String, String> params);


 /**这个是货主版，下面司机的暂时是没有用的配置*/


 /** 司机提交实名认证信息 */
 @Multipart
 @POST("User/DriverAuthentication")
 Observable<BaseEntity> postDriverAuthInfo(@PartMap Map<String, RequestBody> params);

 /** 司机找货列表 */
 @GET("Order/DriverList")
 Observable<BaseEntity<List<Order>>> getFindList(@QueryMap Map<String, String> params);

 /** 司机-->我的订单 */
 @GET("Order/ListOrderDriver")
 Observable<BaseEntity<List<Order>>> getDriverOrderList(@QueryMap Map<String, String> params);

 /** 司机-->订单详情 */
 @GET("Order/DriverShow")
 Observable<BaseEntity<OrderDetail>> getDriverOrderDetail(@QueryMap Map<String, String> params);

 /** 司机端每隔5min发送一次经纬度 */
 @FormUrlEncoded
 @POST("Order/Coordinate")
 Observable<BaseEntity> postLonLat(@FieldMap Map<String, String> params);

 /** 司机--->确认收货 */
 @GET("Order/DriverFinish")
 Observable<BaseEntity<OrderDetail>> confirmDriverOrder(@QueryMap Map<String, String> params);

 /** 司机--->取消订单 */
 @GET("Order/CancelOrder")
 Observable<BaseEntity<OrderDetail>> cancelDriverOrder(@QueryMap Map<String, String> params);

// /** 司机--->导航 */
// @GET("Order/Navigation")
// Observable<BaseEntity<OrderDetail>> nav(@QueryMap Map<String, String> params);

 /** 开始运输 */
 @GET("Order/DriverTransportation")
 Observable<BaseEntity<OrderDetail>> trans(@QueryMap Map<String, String> params);

 /** 抢单 */
 @GET("Order/Grab")
 Observable<BaseEntity<OrderDetail>> grapOrder(@QueryMap Map<String, String> params);

 /** 司机--->订单详情--->上传图片 */
 @Multipart
 @POST("Order/OwnerPic")
 Observable<BaseEntity<OrderDetail>> uploadDriverImages(@PartMap Map<String, RequestBody> params);

 /** 大宗地址管理-列表 */
 @GET("Car/dezhilist")
 Observable<BaseEntity<List<CompanyManageBean>>> getCompany(@QueryMap Map<String, String> params);

 /** 大宗地址管理-添加 */
 @GET("Car/dezhiadd")
 Observable<BaseEntity> addCompany(@QueryMap Map<String, String> params);

 /** 大宗名称管理-列表 */
 @GET("Car/mingchenglist")
 Observable<BaseEntity<List<DzNameManageBean>>> getDzName(@QueryMap Map<String, String> params);

 /** 大宗名称管理-添加 */
 @GET("Car/mingchengadd")
 Observable<BaseEntity> addDzName(@QueryMap Map<String, String> params);

 /** 大宗车辆管理-列表 */
 @GET("Car/chelianglist")
 Observable<BaseEntity<List<CarManageBean>>> getCar(@QueryMap Map<String, String> params);

 /** 大宗车辆管理-添加 */
 @GET("Car/cheliangadd")
 Observable<BaseEntity> addCar(@QueryMap Map<String, String> params);

 /** 大宗车辆管理-搜索 */
 @GET("Car/chelianglistsousuo")
 Observable<BaseEntity<List<CarManageSearchBean>>> searchCar(@QueryMap Map<String, String> params);

 /** 大宗实际总量修改 */
 @GET("Order/UpdateOrderdun")
 Observable<BaseEntity> updateOrderdun(@QueryMap Map<String, String> params);

 /** 大宗到货总量修改 */
 @GET("Order/UpdateOrderdundaoda")
 Observable<BaseEntity> updateOrderdundaoda(@QueryMap Map<String, String> params);



 /** 发布货物名称一级分类 */
 @GET("typeall")
 Observable<BaseEntity<List<CargoBean>>> onetypeall(@QueryMap Map<String, String> params);

}