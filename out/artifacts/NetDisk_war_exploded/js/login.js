function check() {
    var username = document.getElementById("username").value;
    if (username.length <= 2) {
        alert("用户名过短，请重新输入！");
        return false;
    } else if (username.length > 20) {
        alert("用户名过长，请重新输入！");
        return false;
    } else {
        var password = document.getElementById("password").value;
        if (password.length < 6) {
            alert("密码过短，请重新输入！");
            return false;
        } else if (password.length > 32) {
            alert("密码过长，请重新输入！");
            return false;
        } else {
            var email = document.getElementById("email").value;
            if (email.length > 32) {
                alert("邮箱过长，请重新输入！");
                return false;
            }
        }
    }
}