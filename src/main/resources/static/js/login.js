$(document).ready(function () {
    $('#login_submit').click(function () {
        var params = $("input").serialize();
        var username = $("#aw-login-user-name").val();
        var password = $("#aw-login-user-password").val();
        if (username == null || username == '') {
            alert('用户名不能为空！');
            return;
        }
        if (password == null || password == '') {
            alert('密码不能为空！');
            return;
        }else if (password.length < 6 || password.length>30){
            alert('密码长度不符合要求！')
            return;
        }
        $.ajax({
            type: 'post',
            url: '/doLogin',
            data: params,
            dataType: 'json',
            timeout: 1000,
            success: function (response) {
                if (response.code == 200) {
                    alert('登录成功，现在返回首页...');
                    location.href = "/";
                } else {
                    alert(response.message);
                }
            },
            error: function () {
                alert('出问题啦！');
            }
        });
    });
});