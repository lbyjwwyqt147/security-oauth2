var Home = function () {
    var access_token = "";
	var handleHome = function() {

        $("#client_1_btn").click(function () {
           // window.open("http://localhost:18083/index");
            window.open("http://localhost:18082/oauth/authorize?response_type=code&client_id=client_3&redirect_uri=http://localhost:18083/index");
        })

        $("#client_2_btn").click(function () {
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
                        container: "#user_info",
                        message:JSON.stringify(data),
                        close: true,
                        icon: 'fa fa-user',
                        closeInSeconds: 1000
                    });
                    toastr.success("登录人信息",JSON.stringify(data));
                },
                error:function(xhr,status,error){
                    console.log(xhr);
                    toastr.error("请求获取localhost:18083/api/user服务登录人信息接口出错.");
                }
            });
        })

		$("#client_3_btn").click(function () {

            $.ajax({
                url:"http://localhost:18083/api/test",
                data:{
                    "access_token":access_token
                },
                type:'get',
                dataType:'json',
                withCredentials: true,
                success:function(data,textStatus,XMLHttpRequest){
                    console.log(data);
                    toastr.success(JSON.stringify(data));
                },
                error:function(xhr,status,error){
                    console.log(xhr);
                    toastr.error("请求获取localhost:18083/api/test服务登录人信息接口出错.");
                }
            });
        });

	}

    var getToken = function() {
        var code = getParam("code");
        //密码 模式获取token
        $.ajax({
            url: "http://localhost:18082/oauth/token?grant_type=authorization_code&client_id=client_3&client_secret=secret&redirect_uri=http://localhost:18082/home&code=" + code,
            type: 'get',
            dataType: 'json',
            withCredentials: true,
            success: function (data, textStatus, XMLHttpRequest) {
                console.log(data);
                access_token = data.access_token;
            },
            error: function (xhr, status, error) {
                toastr.error("请求获取token出现错误.");
            }
        });
    }

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

    return {
        //main function to initiate the module
        init: function () {
            toastr.options = {
                "closeButton": true,//是否显示关闭按钮
                "debug": false,//是否使用debug模式
                "positionClass": "toast-top-right",//弹出窗的位置
                "onclick": null,
                "showDuration": "300",//显示的动画时间
                "hideDuration": "1000",//消失的动画时间
                "timeOut": "5000",//展现时间
                "extendedTimeOut": "1000",//加长展示时间
                "showEasing": "swing",//显示时的动画缓冲方式
                "hideEasing": "linear",//消失时的动画缓冲方式
                "showMethod": "fadeIn",//显示时的动画方式
                "hideMethod": "fadeOut" //消失时的动画方式
            }
            handleHome();
            getToken();
        }
    };

}();

jQuery(document).ready(function() {
    Home.init();
});