var Index = function () {
    var authorize_url = "http://localhost:18082/oauth/authorize?response_type=code&client_id=client_3&redirect_uri=http://localhost:18082/login";
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

        layer.open({
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
        });
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
                toastr.error("请求获取其他服务登录人信息接口出错.");
            }
        });
    }

    return {
        //main function to initiate the module
        init: function () {
            handleIndex();
        },
        authorization:function(){
            requestAdditionalResources();
        },
       userInfo:function(){
           getUserInfo();
        }

    };

}();

jQuery(document).ready(function() {
    Index.init();
});