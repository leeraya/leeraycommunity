$(document).ready(function () {
    $('#btn-uploadImg').click(function () {
        var formData = new FormData($( "#imgForm" )[0]);
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

    function monitor() {
        var img = $("#uploadImg").val();
        if (img.length != 0){
            $('#btn-uploadImg').removeClass("hidden");
        }
    }
    setInterval(monitor,1000);
});