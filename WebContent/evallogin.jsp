<%@page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE-edge">
    <title>FrozenUI Demo</title>
    <!-- 引入 FrozenUI -->

    <link rel="stylesheet" href="css/frozen.css"/>
    <link rel="stylesheet" href="css/evalstyle.css">
</head>
<body ontouchstart>
<section class="ui-container">
<section id="layout">
    <h1 class="title">大厅数字智能排队叫号管理系统</h1>
    <div class="demo-item">
        <div class="demo-block">
            <section class="ui-placehold-img" style="padding-bottom:10%;">
                <span style="background-image:url(images/midbg.jpg);background-size: 100% 100%;"></span>
            </section>
        </div>
    </div>

    <div class="demo-item">
        <div class="demo-block">
            <ul class="ui-row-flex">
                <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div>服务器IP：</div><div>
                    <div class="ui-input ui-border-radius" style="margin-left:0px;margin-right:50px;">
                        <input type="text" name="serip" v-model="localcfg.serip" placeholder="服务器IP">
                    </div>
                </div></li>
                <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div>窗口号：</div><div>
                    <div class="ui-input ui-border-radius" style="margin-left:0px;margin-right:50px;">
                        <input type="text" name="" v-model="localcfg.counterno" placeholder="窗口号">
                    </div>
                </div></li>
                <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div>登录工号：</div><div>
                    <div class="ui-input ui-border-radius" style="margin-left:0px;margin-right:50px;">
                        <input type="text" name="" v-model="localcfg.staffno" placeholder="登录工号">
                    </div>
                </div></li>
                <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div>登录密码：</div><div>
                    <div class="ui-input ui-border-radius" style="margin-left:0px;margin-right:50px;">
                        <input type="text" name="" v-model="localcfg.pwd" placeholder="登录密码">
                    </div>
                </div></li>
                <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div class="ui-btn-wrap" style="width:120px;">
                    <button class="ui-btn-lg ui-btn-primary" v-on:click="login">
                        登录
                    </button>
                    <button v-on:click="savecfg">
                        vue数据保存
                    </button>
                    <button v-on:click="getcfg">
                        取vue
                    </button>
                </div>
                </li>
            </ul>
        </div>
    </div>
</section>

</section>
<script type="text/javascript" src="lib/vue/vue.js" />
<script type=text/javascript src=lib/vue/app.b22ce679862c47a75225.js></script>
<script src="lib/axios.min.js"></script>
<script type="text/javascript" src="js/json2.js" ></script>
<script type="text/javascript">
    //localStorage.removeItem('localcfg');
    console.log(localStorage.getItem('localcfg'));
    var vue = new Vue({
        el:"#layout",
        data:{
            uri: "/queuectl",
            evaluri: "/evalctl",
            content:'Hello World',
            localcfg: localStorage.getItem('localcfg') == null ? {serip:'192.18.1.10', counterno:'1', staffno:'', pwd:''} : JSON.parse(localStorage.getItem('localcfg'))
        },
        methods: {
            login: function() {
                //发送一个`POST`请求
                axios({
                    method:"POST",
                    url: this.uri,
                    withCredentials:true,
                    data:{
                        interfacename:'counterlogin',
                        counterno:'1',
                        staffid:'1',
                        password:'1'
                    }
                }).then(function(res){
                    console.log(res.data);
                    this.logineval();
                }.bind(this)).catch(function(err){
                    console.log(err);
                });

            },
            logineval: function() {
                //发送一个`POST`请求
                axios({
                    method:"POST",
                    url: this.evaluri,
                    withCredentials:true,
                    data:{"method":"STATE_EVALUATE",
                    "json": JSON.stringify({
                        "centerid":"","counterid":this.localcfg.counterno,"serviceper_no":this.localcfg.staffno, state:0
                    })}
                }).then(function(res){
                    console.log(res.data);
                    if (res.data.context.data.countername)
                        this.localcfg.countername = res.data.context.data.countername;
                    localStorage.setItem('localcfg',JSON.stringify(this.localcfg));
                    console.log(localStorage.getItem('localcfg'))
                    location.href="queuectl.jsp";
                }.bind(this)).catch(function(err){
                    console.log(err);
                });
            },
            savecfg: function(){
                localStorage.setItem('localcfg',JSON.stringify(this.localcfg));
            },
            getcfg: function(){
                console.log(localStorage.getItem('localcfg'))
            }
        }
    });
</script>
</body>
</html>