var Home = function () {

	var handleHome = function() {

        $("#client_1_btn").click(function () {
            window.open("http://localhost:18083/index");
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