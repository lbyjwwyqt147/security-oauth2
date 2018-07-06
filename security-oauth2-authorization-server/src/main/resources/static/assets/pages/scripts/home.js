var Home = function () {

	var handleHome = function() {

        $("#client_1_btn").click(function () {
            window.open("http://localhost:18083/index");
        })

        $("#client_2_btn").click(function () {
            $.ajax({
                url:"http://localhost:18083/api/user",
                data:{

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
                    toastr.error("请求获取其他服务登录人信息接口出错.");
                }
            });
        })

		$("#client_3_btn").click(function () {

        })

	}


    return {
        //main function to initiate the module
        init: function () {
            toastr.options = {
                "closeButton": true,//是否显示关闭按钮
                "debug": false,//是否使用debug模式
                "positionClass": "toast-bottom-right",//弹出窗的位置
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
        }
    };

}();

jQuery(document).ready(function() {
    Home.init();
});