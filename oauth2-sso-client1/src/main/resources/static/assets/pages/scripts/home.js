var Home = function () {

	var handleHome = function() {

        $("#client_1_btn").click(function () {
                $.ajax({
                    url:' http://127.0.0.1:18082/oauth/token?grant_type=client_credentials&client_id=client_3&client_secret=secret',
                    data:$(".login-form").serialize(),
                    type:'post',
                    dataType:'json',
                    withCredentials: true,
                    success:function(res){
                        console.log(res);
                        window.open("http://localhost:18083/index?access_token="+res.access_token);

                    }
                })
        })

        $("#client_2_btn").click(function () {

        })

		$("#client_3_btn").click(function () {

        })

	}


    return {
        //main function to initiate the module
        init: function () {
            handleHome();
        }
    };

}();

jQuery(document).ready(function() {
    Home.init();
});