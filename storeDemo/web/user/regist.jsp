<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link href="${pageContext.request.contextPath }/user/css/style.css" rel='stylesheet' type='text/css'/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--webfonts-->
    <!--//webfonts-->
    <script src="js/setDate.js" type="text/javascript"></script>
    <script src="/js/jquery-3.3.1.js" type="text/javascript"></script>
    <script>
        //校验邮箱
        function checkEmail() {
            var email = $("#email").val();
            var reg_email = /^\w+@\w+\.\w+$/;

            var flag = reg_email.test(email);

            if (flag) {
                $("#email").css("border", "");
            } else {
                $("#email").css("border", "2px solid red");
                if (email == "") {
                    $("#email").css("border", "");
                }
            }
            return flag;
        }

        $(function () {
            $("#registerForm").submit(function () {
                return checkEmail();
            });

            $("#email").blur(checkEmail);
        });


        $(function () {

            $("#username").blur(function () {
                var username = $("#username").val();
                if (username == "") {
                    //$("#InspectName").html("不能为空 ");
                } else {
                    url = "${pageContext.request.contextPath}/UserServlet";
                    params = "op=judgeForm&username=" + $("#username").val();
                    $.post(url, params, function (data) {
                        if ("0" == data) {
                            $("#InspectName").html("用户名可使用");
                            $("#InspectName").css("color", "green");
                        } else {
                            $("#InspectName").html("用户名已注册");
                            $("#InspectName").css("color", "red");
                        }
                    })
                }
            });
        });


    </script>
</head>


<body>
<div class="main" align="center">
    <div class="header">
        <h1>创建一个免费的新帐户！</h1>
    </div>
    <p></p>
    <form id="registerForm" method="post" action="${pageContext.request.contextPath }/UserServlet">
        <input type="hidden" name="op" value="regist"/>
        <ul class="left-form">
            <li>
                ${msg.error.username }<br/>
                <span id="InspectName"></span>
                <input type="text" name="username" placeholder="用户名" id="username" value="${msg.username}"
                       required="required"/>
                <a href="#" class="icon ticker"> </a>
                <div class="clear"></div>
            </li>
            <li>
                ${msg.error.nickname }<br/>
                <input type="text" name="nickname" placeholder="昵称" value="${msg.nickname}" required="required"/>
                <a href="#" class="icon ticker"> </a>
                <div class="clear"></div>
            </li>
            <li>
                ${msg.error.email }<br/>
                <input id="email" type="text" name="email" placeholder="邮箱" value="${msg.email}" required="required"/>
                <a href="#" class="icon ticker"> </a>
                <div class="clear"></div>
            </li>
            <li>
                ${msg.error.password }<br/>
                <input type="password" name="password" placeholder="密码" value="${msg.password}" required="required"/>
                <a href="#" class="icon into"> </a>
                <div class="clear"></div>
            </li>
            <li>
                ${msg.error.birthday }<br/>
                <input type="text" placeholder="出生日期" name="birthday" value="${msg.birthday}" size="15"/>
                <div class="clear"></div>
            </li>
            <li>
                <input type="submit" value="创建账户">
                <div class="clear"></div>
            </li>
        </ul>

        <div class="clear"></div>

    </form>

</div>
<!-----start-copyright---->

<!-----//end-copyright---->

</body>

</html>