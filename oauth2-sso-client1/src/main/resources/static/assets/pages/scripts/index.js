var Index = function () {
    var authorize_url = "http://localhost:18082/oauth/authorize?response_type=code&client_id=client_3&redirect_uri=http://localhost:18083/index";
    var access_token = "";
	var handleIndex = function() {
        //请求授权点击事件


        $('#user_btn').on("click",function(){
            alert("hello world on")
        })



        $("#request_auth_code_btn").click(function(){
            alert(" --------------------------------------------------------- ");
            window.open(authorize_url);
            /*$.ajax({
                url:authorize_url,
                type:'post',
                dataType:'json',
                withCredentials: true,
                success:function(res){
                    console.log(res);


                }
            });*/
        });

        //sso按钮点击事件
        $("#sso_btn").click(function(){
            /*  $.ajax({
                  url:authorize_url,
                  data:{
                      user_oauth_approval:false,
                      authorize:Deny
                  },
                  type:'post',
                  dataType:'json',
                  success:function(res){
                      console.log(res);
                  }
              })*/
        });

	};
    var requestAdditionalResources = function () {
       // window.open(authorize_url);
        //window.open(authorize_url);
       // window.open("http://localhost:18082/home");
        window.open("http://localhost:18082/oauth/authorize?response_type=code&client_id=client_3&redirect_uri=http://localhost:18082/home");

      /*  layer.open({
            type: 2,
            title: false,
            closeBtn: 0, //不显示关闭按钮
            shade: [0],
            area: ['340px', '215px'],
            offset: 'rb', //右下角弹出
            time: 2000, //2秒后自动关闭
            anim: 2,
            content: [authorize_url, 'no'], //iframe的url，no代表不显示滚动条
            end: function(){ //此处用于演示

            }
        });*/
        /*   $.ajax({
               url:authorize_url,
               type:'post',
               dataType:'json',
               withCredentials: true,
               success:function(data,textStatus,XMLHttpRequest){
                   console.log(data);


               },
               error:function(xhr,status,error){
                   console.log(xhr);
                   console.log(status);
                   console.log(error);
                   toastr.error("请求其他客户端资源出错.");
               }
           });*/
    };
    var getUserInfo = function () {

        $.ajax({
            url:"http://localhost:18082/api/user",
            data:{
                "access_token":access_token
            },
            type:'get',
            dataType:'json',
            withCredentials: true,
            success:function(data,textStatus,XMLHttpRequest){
                console.log(data);
                App.alert({
                    container: "#user_info",
                    message:JSON.stringify(data),
                    close: true,
                    icon: 'fa fa-user',
                    closeInSeconds: 1000
                });
            },
            error:function(xhr,status,error){
                console.log(xhr);
                console.log(status);
                console.log(error);
               var obj = JSON.parse(xhr.responseText);
               // window.location.href="http://localhost:18082/login";
                toastr.error(obj.message);
            }
        });
    };
    var getLocalUserInfo = function () {

        $.ajax({
            url:"http://localhost:18083/api/user",
            data:{
                "access_token":access_token
            },
            type:'get',
            dataType:'json',
            withCredentials: true,
            success:function(data,textStatus,XMLHttpRequest){
                console.log(data);
                App.alert({
                    container: "#localhost_user_info",
                    message:JSON.stringify(data),
                    close: true,
                    icon: 'fa fa-user',
                    closeInSeconds: 1000
                });
            },
            error:function(xhr,status,error){
                console.log(xhr);
                console.log(status);
                console.log(error);
                var obj = JSON.parse(xhr.responseText);
                // window.location.href="http://localhost:18082/login";
                toastr.error(obj.message);
            }
        });
    };

    var getLocalMsgInfo = function () {

        $.ajax({
            url:"http://localhost:18083/postMessages",
            data:{
                "access_token":access_token
            },
            type:'get',
            dataType:'text',
            withCredentials: true,
            success:function(data,textStatus,XMLHttpRequest){
                console.log(data);
                toastr.success(data);
            },
            error:function(xhr,status,error){
                console.log(xhr);
                console.log(status);
                console.log(error);
                var obj = JSON.parse(xhr.responseText);
                // window.location.href="http://localhost:18082/login";
                toastr.error(obj.message);
            }
        });
    };


    var getToken = function(){
        var code = getParam("code");
        $.ajax({
            //url:"127.0.0.1:18081/oauth/token?username=qiaorulai&password=123456&grant_type=password&client_id=client_3&client_secret=secret",
            url:"http://localhost:18082/oauth/token?grant_type=authorization_code&client_id=client_3&client_secret=secret&redirect_uri=http://localhost:18083/index&code="+code,
            type:'get',
            dataType:'json',
            withCredentials: true,
            success:function(data,textStatus,XMLHttpRequest){
                console.log(data);
                access_token = data.access_token;
            },
            error:function(xhr,status,error){
                toastr.error("请求获取token出现错误.");
            }
        });


        /**
         * 获取指定的URL参数值
         * URL:http://www.quwan.com/index?name=tyler
         * 参数：paramName URL参数
         * 调用方法:getParam("name")
         * 返回值:tyler
         */
        function getParam(paramName) {
            var  paramValue = "", isFound = !1;
            if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
                arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
                while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
            }
            return paramValue == "" && (paramValue = null), paramValue
        };
    }

    return {
        //main function to initiate the module
        init: function () {
            handleIndex();
            getToken();
        },
        authorization:function(){
            requestAdditionalResources();
        },
       userInfo:function(){
           getUserInfo();
        },
        localUserInfo:function(){
            getLocalUserInfo();
        },
        localMsgInfo:function(){
            getLocalMsgInfo();
        }


    };

}();

jQuery(document).ready(function() {
    Index.init();
});