$(document).ready(function () {
    $('#loginBtn').click(function () {
        var params = $("input").serialize();
        /*var username = $.trim($('#username').val());
        var password = $.trim($('#password').val());*/
        // console.log(username);
        $.ajax({
            type: 'post',
            url: '/doRegist',
            data: params,
            dataType: 'json',
            timeout: 1000,
            success: function (response) {
                if (response.code == 200) {
                    alert('注册成功，现在返回登录界面...');
                    location.href = "login";
                } else if (response.code == 3001) {
                    alert(response.message);
                }
            },
            error: function () {
                alert('error');
            }
        });
    });

    function refresh_captcha() {
        $("#captcha-img")[0].src = "captcha?" + Math.random();
        // document.getElementById("captcha-img").src="captcha?"+Math.random();
        $("#captcha")[0].value = "";
        // document.getElementById("captcha").value = "";
    }

    $("#captcha-img").click(function () {
        refresh_captcha();
    });
});
