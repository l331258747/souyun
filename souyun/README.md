* [1.登录](#1)
* [2.注册](#2)
* [3.验证码](#3)
* [4.修改密码](#4)
* [5.找回密码](#5)
* [6.实名认证](#6)
* [7.实名认证通过后的接口](#7)
* [8.添加好友](#8)
* [9.好友列表](#9)
* [10.群发的短信](#10)
* [11.添加银行卡](#11)
* [12.银行卡列表](#12)
* [13.历史交易](#13)
* [14.发货计算距离和单价](#14)
* [15.发布货源](#15)
* [16.发货选择车型](#16)
* [17.货主我的订单](#17)
* [18.货主我的订单详情](#18)
* [19.货主取消订单](#19)
* [20.货主确认收获](#20)
* [21.货主定位司机](#21)
* [22.货主确认司机在运输中接口](#22)
* [23.司机找货界面及搜索接口](#23)
* [24.司机我的订单](#24)
* [25.司机我的订单详情](#25)
* [26.司机线路导航](#26)
* [27.司机抢单接口](#27)
* [28.司机取消订单](#28)
* [29.司机确认完成接口](#29)
* [30.支付](#30)
* [31.司机修改订单图片上传](#31)
* [32.司机确认运输中](#32)
* [33.版本更新](#33)
* [34.司机实名认证](#34)
* [35.货主发货司机线上支付](#35)
* [36.添加货主发货选择地址](#35)
* [37.查询货主发货选择地址](#35)
* [38.司机发起体现](#35)
<h3 id="1">1.登录</h3>
---
##### 接口名称：`/User/Login`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    tel    |    true    |    用户注册过的手机号    |


##### 返回结果：
<pre>
<code>
{
	status_code:"200",
	msg:"",
	data:{
		"user_type":"0",//0货主  1司机
		"phone":"13603578806",
		"user_id":"1526"
	}
}
</code>
</pre>


<h3 id="2">2.注册</h3>
---
##### 接口名称：`/User/Register`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    username    |    true    |    用户名    |
|    password    |    true    |    密码   |
|    phone    |    true    |    电话   |
|    regdate    |    false    |    日期   |
|    type    |    true    |    用户类型  0货主 1司机 |
|    invitePhone    |    false    |    身份证   |
|    renzhengchexing    |    false    |    给默认值0   |
|    code    |    true    |    短信验证码   |

##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "注册成功",
	"data": {
		"username": "英文",
		"password": "33333",
		"phone": "18734737953",
		"regdate": "2018-4-27",
		"type": "1",
		"invitePhone": "142623198256925281"
	}
}
</code>
</pre>


<h3 id="3">3.验证码</h3>
---
##### 接口名称：`http://www.bangweibao.com/public/admin/map/sms`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    token    |    true    |    acd890f765efd007cbb5701fd1ac7ae0    |
|    mobile    |    true    |    手机号   |

##### 返回结果：
<pre>
<code>
{
	"status_code": 200,
	"msg": "成功",
	"data": {
		"code": 8142
	}
}
</code>
</pre>


    <h3 id="4">4.修改密码</h3>
---
##### 接口名称：`/User/ModifyPwd`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    userid    |    true    |    用户登录id    |
|    pwd    |    true    |    文本框上传的密码   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "密码修改成功",
	"data": {}
}
</code>
</pre>


<h3 id="5">5.找回密码</h3>
---
##### 接口名称：`/User/Retrieve`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    username    |    true    |    用户登录的用户名    |
|    tel    |    true    |    用户注册的手机号   |
|    pwd    |    true    |    用户上传的密码   |
|    code    |    true    |    短信验证码   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "密码设置成功",
	"data": {}
}
</code>
</pre>

<h3 id="6">6.实名认证</h3>
---
##### 接口名称：`http://47.94.6.241:810/XingRongAppServer/User/Authentication`
##### 请求类型：post
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    type    |    true    |    0--不是整车单位   1是整车单位   |
|    name    |    true    |    真实姓名   |
|    userid    |    true    |    用户登录id   |
|    pic_id    |    true    |    身份证图片   |
|    pic_avatar    |    true    |    头像   |
|    pic_licence    |    true    |    营业执照图片   |
|    check    |    true    |    默认是0--等待用户操作  1--提交审核后，按钮变成灰色，等待管理员审核，在进来显示提交过的图片   2--那就审核通过，那个提交审核的按钮去了，图片和认证的信息正常显示  3-- 审核未通过 按钮继续提交资料进行审核，现在界面为默认界面，上传的图片都不显示 |

##### 返回结果：
<pre>
{
	"status_code": "300",
	"check": "0",//未填写资料，等待操作
	"msg": "未填写资料，等待操作",
	"data": {}
}
{
	"status_code": "200",
	"check": "1",//资料填写完成，等待提交审核
	"msg": "发布成功，等待审核",
	"data": {}
}
{
	"status_code": "200",
	"check": "2",
	"msg": "审核通过",
	"data": {}
}
{
	"status_code": "200",
	"check": "3", //重新提交
	"msg": "发布成功，等待审核",
	"data": {}
}

</pre>


<h3 id="7">7.实名认证通过后的接口</h3>
---
##### 接口名称：`/User/AuthenticationList`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    userid    |    true    |    用户id   |

##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "读取个人资料成功",
	"data": {
		"userId": "1768",
		"phone": "18734737953",
		"unit": "龙腾",
		"type_review": "0",//相当于上面的check认证
		"name": "",
		"pic_id": "http://47.94.6.241:810/XingRongAppServer/renzheng/97753900-3081-4424-9ee9-4ebb306c1357_06.png",
		"pic_avatar": "http://47.94.6.241:810/XingRongAppServer/renzheng/fce78c5a-f8ba-4650-8acc-415f8470c81e_03.png",
		"pic_licence": "http://47.94.6.241:810/XingRongAppServer/renzheng/e686395c-e7e8-45b8-b6e2-d2f8286d79d6_01.png"
	}
}
</code>

</pre>



<h3 id="8">8.添加好友</h3>
---
##### 接口名称：`/Friend/AddFriend`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    name    |    true    |    好友的名字    |
|    userid    |    true    |    用户登录id    |
|    tel    |    true    |    好友电话    |
|    pic    |    false    |    好友照片    |
|    type    |    true    |    是否发送过短信默认为0     0未发送  1已发送    |
|    date    |    true    |    添加日期    |
|    check    |    true    |    默认是0--什么资料也没填写第一次进来的时候读取    1--提交好友    |
##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"check": "0",
	"msg": "请选择好友添加",
	"data": {}
}
{
	"status_code": "200",
	"check": "1",
	"msg": "添加好友成功",
	"data": {}
}
</code>
</pre>


<h3 id="9">9.好友列表</h3>
---
##### 接口名称：`/Friend/ListFriend`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    userid    |    true    |    用户登录id    |


##### 返回结果：

<pre>
<code>
{
	"status_code": "200",
	"msg": "读取好友成功",
	"data": [{
		"id": "15",
		"name": "白龙",
		"userid": "7",
		"tel": "12340 576 132",
		"type": "0",
		"date": "2018-04-27 15:44:07",
		"check": "1",
		"is_register": "0" //0：未注册  1：已注册
	}]
}
</code>
</pre>





<h3 id="10">10.群发的短信</h3>
---
##### 接口名称：`http://www.bangweibao.com/public/admin/map/arrsms?token=acd890f765efd007cbb5701fd1ac7ae0&mobile=18636735026`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    token    |    true    |    acd890f765efd007cbb5701fd1ac7ae0    |
|    mobile    |    false    |    单个：手机号    多个：手机号用,分割   |


##### 返回结果：
<pre>
<code>
{
	"status_code": 200,
	"msg": "成功"
}
</code>
</pre>






<h3 id="11">11.添加银行卡</h3>
---
##### 接口名称：`Card/AddBankCard`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    userid    |    true    |    用户登录id   |
|    num    |    true    |    卡号   |
|    account_bank    |    true    |    开户行   |
|    name    |    true    |    持卡人姓名   |
|    category    |    true    |    银行卡类别   |
|    bank    |    true    |    所属银行   |
|    check    |    true    |   0是第一次进来等待客户填写资料；1是已经填写好，可以提交   |
##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"check": "0",
	"msg": "等待客户添加银行卡资料",
	"data": {}
}
{
	"status_code": "200",
	"check": "1",
	"msg": "添加银行卡成功",
	"data": {}
}
</code>
</pre>

<h3 id="12">12.银行卡列表</h3>
---
##### 接口名称：`Card/ListBankCard`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    userid    |    true    |    用户登录id   |

##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "读取银行卡列表成功",
	"data": [{
		"id": "70",
		"num": "7",
		"category": "0",
		"bank": "2018-4-30",
		"check": "1"//添加成功后显示的列表
	}]
}
</code>
</pre>



<h3 id="13">13.历史交易</h3>
---
##### 接口名称：`/History/HistoryTradeList`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    userid    |    true    |    用户登录id   |
|    type    |    true    |    登录接口返回的user_type，表示  0--货主or    1--司机   |
|    pages    |    true    |    当前请求的页数，从0开始   |


##### 返回结果 上面头调用这两个接口，下面的订单列表调用已完成的接口，列表不让点进去：
<pre>
<code>
{
	"status_code": "200",
	"msg": "货主历史交易",
	"type": "0",
	"data": {
		"hasnext": "0",
		"num": "6",
		"amount": "156",
		"lists": [{
			"id": "42",
			"userid": "7",
			"truck": "小型面包",
			"start_post": "临汾",
			"end_pos": "宁波",
			"product_name": "苹果",
			"weight": "3",
			"area": "4",
			"num": "5",
			"date": "4",
			"driver": ""
		}, {
			"id": "43",
			"userid": "7",
			"truck": "小型面包",
			"start_post": "临汾",
			"end_pos": "宁波",
			"product_name": "苹果",
			"weight": "3",
			"area": "4",
			"num": "5",
			"date": "4",
			"driver": ""
		}, {
			"id": "49",
			"userid": "7",
			"truck": "小型面包",
			"start_post": "临汾",
			"end_pos": "宁波",
			"product_name": "苹果",
			"weight": "3",
			"area": "4",
			"num": "5",
			"date": "4",
			"driver": ""
		}, {
			"id": "50",
			"userid": "7",
			"truck": "小型面包",
			"start_post": "临汾",
			"end_pos": "宁波",
			"product_name": "苹果",
			"weight": "3",
			"area": "4",
			"num": "5",
			"date": "4",
			"driver": ""
		}]
	}
}




{
	"status_code": "200",
	"msg": "司机历史交易",
	"type": "0",
	"data": {
		"hasnext": "0",
		"num": "6",
		"amount": "156",
		"lists": [{
			"id": "55",
			"userid": "8",
			"truck": "小型面包",
			"start_post": "临汾",
			"end_pos": "西瓜",
			"product_name": "苹果1111",
			"weight": "3",
			"area": "4",
			"num": "5",
			"date": "4",
			"driver": "待分配"
		}]
	}
}


</code>
</pre>



<h3 id="14">14.发货计算距离和单价(3中计算方法)</h3>
---
##### 接口名称：`http://www.bangweibao.com/public/admin/map/compre?pstart=黑龙江&pend=广东&token=acd890f765efd007cbb5701fd1ac7ae0&startadd=哈尔滨&endadd=深圳`
##### 接口名称：`http://www.bangweibao.com/public/api/map?starty=29.901323&endx=121.554194&endy=29.869457&token=acd890f765efd007cbb5701fd1ac7ae0&type=0&startx=121.820163`
##### 接口名称：`http://www.bangweibao.com/public/api/map?token=acd890f765efd007cbb5701fd1ac7ae0&type=1&startadd=%E4%B8%B4%E6%B1%BE&endadd=%E6%B7%B1%E5%9C%B3`
##### 请求类型：GET

`当type=0时`
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
| token    |    true    |    acd890f765efd007cbb5701fd1ac7ae0    |
|    type    |    true    |    type=0  |
|    startx    |    true    |    起点x坐标   |
|    starty    |    true    |    起点y坐标   |
|    endx    |    true    |    终点x坐标   |
|    endy    |    true    |    终点y坐标   |

`当type=1时`
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    token    |    true    |    acd890f765efd007cbb5701fd1ac7ae0    |
|    type    |    true    |    type=1   |
|    startadd    |    true    |    城市名称   |
|    endadd    |    true    |    城市名称   |
|    pstart    |    true    |    山西省   |
|    pend    |    true    |    陕西省   |


##### 返回结果：
<pre>
<code>
{"status_code":200,"msg":"成功","data":{"ton":"0.1","square":"0.2","distance":3416.349}}
{"status_code":200,"msg":"成功","data":{"distance":33.736}}
{"status_code":200,"msg":"成功","data":{"distance":1835.867}}
</code>
</pre>

<h3 id="15">15.发布货源</h3>
---
##### 接口名称：`/Order/AddOrder`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    :---------:   |
| userid | true | 用户id |
|category| true | 配送类型 0：同城配送 1：长途整车 2：长途零担 |
| truckid | true | 所选车型id |
| start_lon | false | 发货经度，只有在category=0时传 |
| start_lat | false | 发货纬度，只有在category=0时传 |
| end_lon | false | 收货经度，只有在category=0时传 |
| end_lat | false | 收货纬度，只有在category=0时传 |
| start_province | false | 发货省，只有在category=1或2时传 |
| start_city | false | 发货市，在category=0/1/2时传 |
| end_province | false | 收货省，只有在category=1或2时传 |
| end_city | false | 收货市，在category=0/1/2时传 |
| product_name | true | 货名 |
| weight | true | 吨 |
| area | true | 方 |
| num | true | 件 |
| publish_phone | true | 发货电话 |
| get_person | true | 收货人 |
| get_phone | true | 收货电话 |
| remark | true | 备注信息 |
| pic | false | 图片二进制 |
| freight | true | 运费 |
| insurance | true | 保险，目前固定 0 50 100 200 |
| is_receipt | false | 是否需要发票，0：不需要，1：需要 |
| taxnum | false | 税号 |
| unitname | false | 单位名称 |
| email | false | 点击邮箱 |
| is_helppay | false | 是否需要代付，0：不需要，1：需要 |
| helppay_id | false | 代付人id |
| is_helpget | false | 是否需要代收，0：不需要 1：需要 |
| helpget_id | false | 代收人id |
| helpget_price | false | 货款金额 |
| coupon | false | 优惠券，目前固定 0：满1000减50 1：满2000减100 2：满3000减200 |
| total_price | true | 总价|
| kilo | true | 公里数|
| addtime | true |货主选择发货时间|


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "发布成功",
	"data": {
		"date":"2018-04-12",//发货日期
		"truck":"集装箱",//车型
		"start_pos":"发货地点",
		"end_pos":"到货地点",
		"order_id":"订单号"
	}
}
</code>
</pre>

<h3 id="16">16.发货选择车型</h3>
---
##### 接口名称：`/Car/ListCar`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
| type | true | 同城车型type=1 如果type=2时长途车型 |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "车辆列表读取成功",
	"data": [
		{
			"id": "1",
			"name": "随机车辆",
			"pic": "http://47.94.6.241:810/XingRongAppServer/chexing/tcsuiji.png",
			"length": "1",
			"width":"2",
			"height":"3",
			"start_price":"起步价",
			"start_kilo":"起步公里",
			"exceed_price":"超出价格",
            "capacity":"载重"
		},
		......
	]
}
</code>
</pre>

<h3 id="17">17.货主我的订单</h3>
---
##### 接口名称：`/Order/ListOrder`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    pages    |    true    |    分页参数0是第一页，默认是0   |
|    type    |    true    | 0就是未接单 1已接单 2运输中 3已完成   |
|    userid    |    true    |    货主id   |

##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "",
	"hasnext":"0/1",//0：没有下一页  1：有下一页
	"data": [
		{
			"id": "37",
            "userid": "7",
            "truck": "小型面包",
            "start_pos": "宁波市",
            "end_pos": "宁波市",
            "product_name": "知识",
            "weight": "3",
            "area": "4",
            "num": "5",
            "driverid": "10",
            "date": "4",
            "driver": "待分配"
		},
		......
	]
}
</code>
</pre>




<h3 id="18">18.货主我的订单详情</h3>
---
##### 接口名称：`Order/ListShow`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    d订单id   |
|    userid    |    true    |    用户id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "读取货物详情成功主要是图片",
	"data": {
		"id": "41",
		"userid": "7",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "宁波",
		"product_name": "苹果",
		"weight": "3",
		"area": "4",
		"num": "5",
		"pic": "http://47.94.6.241:810/XingRongAppServer/images/e8582bf7-6fda-48ed-a505-d95c753dc101_timg(2).jpg,http://47.94.6.241:810/XingRongAppServer/images/46ce365e-50e3-4932-baf1-1b8ac5906b4f_timg(1).jpg,http://47.94.6.241:810/XingRongAppServer/images/6e396179-fcd5-4475-b60c-78f189efb06a_timg(2).jpg",
		"kilo": "998",
		"type": "2",//这个是订单状态 0--取消订单显示，定位司机和确认收获不显示  1--取消订单不显示，定位司机和确认收获显示  2---取消订单不显示，定位司机和确认收获显示  3--取消订单、定位司机、确认收货都不显示
        "pay":"1"//1不显示支付宝和微信支付按钮，否则显示
	}
}
</code>

</pre>


<h3 id="19">19.货主取消订单</h3>
---
##### 接口名称：`Order/DeleteOrder`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |
|    userid    |    true    |    用户登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "取消订单成功",
	"data": {
		"Ddzhuangtai": "4"
	}
}
</code>
</pre>

<h3 id="20">20.货主确认收获</h3>
---
##### 接口名称：`Order/Confirmation`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |
|    user_id    |    true    |    用户登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "确认收获成功",
	"data": {
		"isquerenshouhuo": "1"
	}
}
</code>
</pre>

<h3 id="21">21.货主定位司机</h3>
---
##### 接口名称：`/Order/Coordinates`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |
|    userid    |    true    |    司机登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "读取司机坐标成功",
	"data": [{
		"userId": "1327",
		"start_pos": "111.330438",
		"end_pos": "35.947088",
		"addTime": "2018-05-04"
	}]
}
</code>
</pre>



<h3 id="22">22.货主确认司机在运输中接口</h3>
---
##### 接口名称：`/Order/Transportation`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |
|    userid    |    true    |    货主登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "确认司机运输成功",
	"data": {
		"ddzhuangtai": "2"
	}
}
</code>
</pre>




<h3 id="23">23.司机找货界面及搜索接口</h3>
---
##### 接口名称：`Order/DriverList`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    pages    |    true    |    分页参数0是第一页 默认是0    |
|    hwname    |    false    |    搜索中传的货名名称   |
|    pstype    |    false    |    搜索中传递配送类型   |

说明货物名称和配送类型为空，那就是默认读出货主发货列表未接单中的数据，如果有所搜名称不为空就按照搜索的内容搜索


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "读取司机主界面未抢单及搜索列表",
	"data": [{
		"id": "53",
		"userid": "7",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "西瓜",
		"product_name": "苹果",
		"weight": "3",
		"area": "4",
		"num": "5",
		"date": "4",
		"driver": ""
	}, {
		"id": "52",
		"userid": "7",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "西瓜",
		"product_name": "苹果",
		"weight": "3",
		"area": "4",
		"num": "5",
		"date": "4",
		"driver": ""
	}, {
		"id": "51",
		"userid": "7",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "西瓜",
		"product_name": "苹果",
		"weight": "3",
		"area": "4",
		"num": "5",
		"date": "4",
		"driver": ""
	}, {
		"id": "45",
		"userid": "7",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "上海",
		"product_name": "西瓜",
		"weight": "3",
		"area": "4",
		"num": "5",
		"date": "4",
		"driver": ""
	}, {
		"id": "44",
		"userid": "7",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "上海",
		"product_name": "西瓜",
		"weight": "3",
		"area": "4",
		"num": "5",
		"date": "4",
		"driver": ""
	}, {
		"id": "38",
		"userid": "7",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "上海",
		"product_name": "西瓜",
		"weight": "3",
		"area": "4",
		"num": "5",
		"date": "4",
		"driver": ""
	}, {
		"id": "37",
		"userid": "7",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "上海",
		"product_name": "西瓜",
		"weight": "3",
		"area": "4",
		"num": "5",
		"date": "4",
		"driver": ""
	}]
}
</code>
</pre>

<h3 id="24">24.司机我的订单</h3>
---
##### 接口名称：`Order/ListOrderDriver`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    pages    |    true    |    分页参数默认是0，0是第一页    |
|    type    |    true    |    1已接单   2运输中   3已完成   |
|    userid    |    true    |    司机登录的id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "读取司机发货列表成功",
	"data": [{
		"id": "53",
		"truck": "小型面包",
		"start_pos": "临汾",
		"end_pos": "西瓜",
		"product_name": "苹果",
		"weight": "3",
		"area": "4",
		"num": "5",
		"date": "4",
		"driver": "待分配"
	}]
}
</code>
</pre>



<h3 id="25">25.司机我的订单详情</h3>
---
##### 接口名称：`Order/DriverShow`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id   |
|    userid    |    true    |    司机登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "司机详情",
	"data": {
		"id": "99",
		"userid": "7",
		"truck": "金杯",
		"start_pos": "临汾市",
		"end_pos": "临汾市",
		"product_name": "香烟",
		"weight": "7",
		"area": "4",
		"num": "1",
		"pic": "http://47.94.6.241:810/XingRongAppServer/images/53f3350d-3539-4de1-856f-13275f644739_152548240075635.jpg",
		"driver": "1327",
		"kilo": "38.088",
		"type": "0",//这个是订单状态0---抢单显示，其余不显示 1--抢单不显示、取消订单显示、线路导航显示，中转显示，确认到货不显示  2--抢单不显示，取消订单不显示，线路导航显示、中转显示、确认到货显示    3--、抢单不显示、取消订单不显示、中转不显示、线路导航不显示、确认到货都不显示
		"Receiving": "0"  //状态等于1不显示上传图片按钮，否则就司机详情页面一直显示上传图片按钮
	}
}

</code>

</pre>




<h3 id="31">31.司机修改单详情图片上传</h3>
---
##### 接口名称：`/Order/DriverPic`
##### 请求类型：post
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id   |
|    userid    |    true    |    司机登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "上传成功",
	"date": {}
}
</code>

</pre>




<h3 id="26">26.司机线路导航</h3>
---
##### 接口名称：`/Order/Navigation`
##### 请求类型：GET
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "读取司机端线路导航成功",
	"data": {
		"id": "100",
		"category": "0",
		"start_lon": "111.536451",
		"start_lat": "36.059845",
		"end_lon": "111.686898",
		"end_lat": "36.270351"
	}
}
</code>
</pre>

<h3 id="27">27.司机抢单接口</h3>
---
##### 接口名称：`Order/Grab`
##### 请求类型：POST
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |
|    userid    |    true    |    司机登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "司机抢单成功",
	"data": {
		"ddzhuangtai": "1"
	}
}
</code>
</pre>




<h3 id="28">28.司机取消订单</h3>
---
##### 接口名称：`/Order/CancelOrder`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |
|    userid    |    true    |    司机登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "司机取消订单成功",
	"data": {
		"ddzhuangtai": "0"
	}
}
</code>
</pre>


<h3 id="29">29.司机确认完成接口</h3>
---
##### 接口名称：`Order/DriverFinish`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |
|    userid    |    true    |    司机登录id   |

##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "司机确认订单完成成功",
	"data": {
		"ddzhuangtai": "3"
	}
}
</code>
</pre>





<h3 id="30">30.支付</h3>
---
##### 接口名称：`/Order/Pay`
##### 请求类型：post
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    type    |    true    |    1：微信 2：支付宝   |
|    appname    |    true    |    16飕镖局   |
|    product_name    |    true    |    商品名称   |
|    describe    |    false    |    商品描述   |
|    orderid    |    true    |    订单id   |
|    price    |    true    |    产品价格   |

##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "订单支付成功",
	"data": {
		"noncestr": "52pcpzbBOLEFTsO0",
		"package": "Sign=WXPay",
		"appid": "wxdf3767c77cfe9197",
		"sign": "6F5DA432001F07B0F32BE25646540597",
		"trade_type": "APP",
		"return_msg": "OK",
		"result_code": "SUCCESS",
		"partnerid": "1494190492",
		"return_code": "SUCCESS",
		"prepayid": "wx05105845488611d8dc14b78a1301698277",
		"timestamp": "1525489125"
	}
}
</code>

</pre>


<h3 id="32">32.司机确认运输中</h3>
---
##### 接口名称：`Order/DriverTransportation`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |
|    userid    |    true    |    司机登录id   |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "司机确认运输中成功",
	"data": {
		"ddzhuangtai": "2"
	}
}
</code>
</pre>

<h3 id="33">33.版本更新</h3>
---
##### 接口名称：`Other/Update`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    version  |    true    |    当前版本号，如1.0.0   |

##### 服务器判断，比对app这边传过去的版本号和数据库中最新的版本做比较

##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "",
	"data": {
		"update":"0/1",//版本比较后如果小于服务器存储的版本号则返回1表示需要更新，否则返回0
		"url":"更新地址",
		"remark":"更新内容"
	}
}
</code>
</pre>

<h3 id="34">34.司机实名认证</h3>
---
##### 接口名称：`http://47.94.6.241:810/XingRongAppServer/User/DriverAuthentication`
##### 请求类型：post
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    type    |    true    |    0--不是整车单位   1是整车单位   |
|    name    |    true    |    真实姓名   |
|    userid    |    true    |    用户登录id   |
|    pic_id    |    true    |    身份证图片   |
|    pic_avatar    |    true    |    头像   |
|    pic_licence    |    true    |    营业执照图片   |
|    check    |    true    |    默认是0--等待用户操作  1--提交审核后，按钮变成灰色，等待管理员审核，在进来显示提交过的图片   2--那就审核通过，那个提交审核的按钮去了，图片和认证的信息正常显示  3-- 审核未通过 按钮继续提交资料进行审核，现在界面为默认界面，上传的图片都不显示

|    category    |    true    |    配送类型| 1是同城   2时长途整车  3是长途零担
|    pic_drive    |    true    |    驾驶本
|    pic_train    |    true    |    行车本
##### 返回结果：
<pre>
{
	"status_code": "300",
	"check": "0",//未填写资料，等待操作
	"msg": "未填写资料，等待操作",
	"data": {}
}
{
	"status_code": "200",
	"check": "1",//资料填写完成，等待提交审核
	"msg": "发布成功，等待审核",
	"data": {}
}
{
	"status_code": "200",
	"check": "2",
	"msg": "审核通过",
	"data": {}
}
{
	"status_code": "200",
	"check": "3", //重新提交
	"msg": "发布成功，等待审核",
	"data": {}
}

</pre>



<h3 id="35">35.货主发货司机线上支付</h3>
---
##### 接口名称：`Order/CashonlinepaymentServlet`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    id    |    true    |    订单id    |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "司机确认订单完成成功",
	"data": {
		
	}
}
</code>
</pre>



<h3 id="36">36.添加货主发货选择地址</h3>
---
##### 接口名称：`/Order/AddAddress`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    start_lon    |    true    |    发货经度   |
|    start_lat    |    true    |    发货纬度   |
|    end_lon    |    true    |    收货经度    |
|    end_lat    |    true    |    收货纬度    |
|    start_city    |    true    |    发货市    |
|    end_city    |    true    |    收货市    |
|    start_desc    |    true    |    发货具体位置    |
|    end_desc    |    true    |    到货具体位置    |
|    addtime    |    true    |    添加时间    |
|    userid    |    true    |    用户id    |

##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "添加货主发货选择地址成功",
	"data": {
		
	}
}
</code>
</pre>

<h3 id="37">37.查询货主发货选择地址</h3>
---
##### 接口名称：`Order/ListAddress`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    userid    |    true    |    用户id    |


##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "地址查询成功",
	"data": {
		"start_lon": "xxxx",
		"start_lat": "xxx",
		"end_lon": "xxxx",
		"end_lat": "xxx",
		"start_city": "xx",
		"end_city": "xxxx",
		"start_desc": "xxx",
		"end_desc": "xxx",
		"addtime": "xxx",
		"userid": "xxxxx"
	}
}
</code>
</pre>

<h3 id="38">38.司机体现</h3>
---
##### 接口名称：`/Order/TiXian`
##### 请求类型：get
##### 参数如下：
|         参数    |    必须    |    参数说明      |
|    ------     |    :-------:  |    ---------:   |
|    userid    |    true    |   用户id   |
|    jine    |    true    |    体现金额   |
|    addtime    |    true    |    发起时间    |
|    type    |    true    |    是否审核通过默认为0    |



##### 返回结果：
<pre>
<code>
{
	"status_code": "200",
	"msg": "发起体现成功成功",
	"data": {
		
	}
}
</code>
</pre>