$(document).ready(function () {
    $('#updatePwdBtn').click(function () {
        var params = $("input").serialize();

        var password = $("#password").val();
        var confirm_password = $("#confirm_password").val();
        var old_password = $("#old_password").val();
        if (!password == confirm_password) {
            alert('新密码两次输入不一致！');
            return;
        }
        if (password == old_password) {
            alert('新密码与旧密码不能相同！');
            return;
        }
        $.ajax({
            type: 'post',
            url: '/user/doUpdatePwd',
            data: params,
            dataType: 'json',
            timeout: 1000,
            success: function (response) {
                if (response.code == 200) {
                    alert('修改密码成功，现在返回登录页...');
                    location.href = "/login";
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
        $("#captcha-img")[0].src = "/captcha?" + Math.random();
        // document.getElementById("captcha-img").src="captcha?"+Math.random();
        $("#captcha")[0].value = "";
        // document.getElementById("captcha").value = "";
    }

    $("#captcha-img").click(function () {
        refresh_captcha();
    });
});