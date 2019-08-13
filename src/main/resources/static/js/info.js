$(document).ready(function () {
    //修改图片呢
    $('#btn-uploadImg').click(function () {
        var formData = new FormData($("#imgForm")[0]);
        $.ajax({
            type: 'post',
            url: '/file/uploadImg',
            data: formData,
            dataType: 'json',
            contentType: false,
            processData: false,
            success: function (response) {
                if (response.code == 200) {
                    window.location.reload();
                } else {
                    alert(response.message);
                }
            },
            error: function () {
                alert('出问题啦！');
            }
        });
    });

    //获取一开始的用户名和邮箱
    var preName = $("#info-name").val();
    var preEmail = $("#info-email").val();

    //更新用户信息
    $('#confirmInfo').click(function () {
        var infoName = $("#info-name").val();
        var infoEmail = $("#info-email").val();
        if (infoName == preName && infoEmail == preEmail){
            alert('内容未修改！');
            return;
        }
        if (infoName == null){
            alert('用户名不能为空！');
            return;
        }
        if(infoEmail == null || infoEmail == ''){
            alert('邮箱不能为空！');
            return;
        }
        var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if (!myreg.test(infoEmail)) {
            alert('邮箱格式错误！');
            return;
        }
        var params = $("input").serialize();
        console.log(params);
        $.ajax({
            type: 'post',
            url: '/user/updateInfo',
            data: params,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    window.location.reload();
                } else {
                    alert(response.message);
                }
            },
            error: function () {
                alert('出问题啦！');
            }
        });
    })

    //监视器
    function monitor() {
        var img = $("#uploadImg").val();
        if (img.length != 0) {
            $('#btn-uploadImg').removeClass("hidden");
        }
    }

    setInterval(monitor, 1000);

    //点击修改，放开input
    $('#updateInfo').click(function () {
        $('#info-name').removeAttr("readonly");
        $('#info-email').removeAttr("readonly");
    });
});