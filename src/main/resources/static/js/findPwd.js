$(document).ready(function () {
    $('#findPwdBtn').click(function () {
        var username = $("#username").val();
        if (username == null || username == '') {
            alert('用户名不能为空!');
            return;
        }
        var params = $("input").serialize();
        $.ajax({
            type: 'post',
            url: '/user/doFindPwd',
            data: params,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    alert('邮件发送成功，请使用验证码修改...');
                    location.href = "/user/toFindPwdStep2";
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

    $('#findPwdStep2Btn').click(function () {
        var username = $("#username").val();
        var password = $("#password").val();
        var confirm_password = $("#confirm_password").val();
        var emailVerifCode = $("#emailVerifCode").val();
        if (username == null || username == '') {
            alert('用户名不能为空!');
            return;
        }
        if (!password == confirm_password) {
            alert('新密码两次输入不一致！');
            return;
        }
        var params = $("input").serialize();
        $.ajax({
            type: 'post',
            url: '/user/doFindPwdStep2',
            data: params,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    alert('密码修改成功，请使用新密码登录...');
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