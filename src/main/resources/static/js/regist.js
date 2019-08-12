$(document).ready(function () {
    $('#registBtn').click(function () {
        var params = $("input").serialize();
        $.ajax({
            type: 'post',
            url: '/doRegist',
            data: params,
            dataType: 'json',
            timeout: 1000,
            success: function (response) {
                if (response.code == 200) {
                    alert('注册成功，现在返回首页...');
                    location.href = "/";
                } else {
                    alert(response.message);
                    refresh_captcha();
                }
            },
            error: function () {
                alert('出问题啦！');
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
