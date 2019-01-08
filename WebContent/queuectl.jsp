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
        <h1 class="title">智能排队叫号管理系统</h1>

        <div class="demo-item">
            <div class="demo-block">
                <ul class="ui-row-flex">
                    <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div>当前窗口号：{{localcfg.countername}}({{localcfg.counterno}})</div></li>
                    <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div>当前办理号：</div></li>
                    <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div>受理工号：{{localcfg.staffno}}</div></li>
                    <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div>等待数：</div></li>
                </ul>
            </div>
        </div>

        <div class="demo-item">
            <div class="demo-block">
                <ul class="ui-row-flex">
                    <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div><button class="ui-btn ui-btn-primary" v-on:click="welcome">开始受理</button></div><div>
                    </div></li>
                    <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div><button class="ui-btn ui-btn-primary" v-on:click="apprise">结束受理</button></div><div>
                    </div></li>
                    <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div><button class="ui-btn ui-btn-primary" v-on:click="pause">{{pausestatestr}}</button></div><div>
                    </div></li>
                    <li class="ui-col ui-flex ui-flex-ver ui-flex-align-start" style="background-color: white;border:0px;"><div><button class="ui-btn ui-btn-primary" v-on:click="logout">注销</button></div>
                    </li>
                </ul>
            </div>
        </div>
    </section>
</section>
<script type="text/javascript" src="lib/vue/vue.js" />
<script type=text/javascript src=lib/vue/app.b22ce679862c47a75225.js></script>
<script src="lib/axios.min.js"></script>
<script type="text/javascript">
    console.log(localStorage.getItem('localcfg'))

    var vue = new Vue({
        el:"#layout",
        data:{
            uri: "/hello",
            evaluri: "/evalctl",
            content:'Hello World',
            pausestate:4,
            pausestatestr:'暂停',
            localcfg: JSON.parse(localStorage.getItem('localcfg'))
        },
        methods: {
            test: function() {
                console.log(this.localcfg.staffno);
            },
            welcome: function() {
                //发送一个`POST`请求
                axios({
                    method:"POST",
                    url: this.evaluri,
                    withCredentials:true,
                    data:{"method":"STATE_EVALUATE",
                        "json": JSON.stringify({
                            "centerid":"","counterid":this.localcfg.counterno, "serviceper_no": this.localcfg.staffno, state: 1
                        })}
                }).then(function(res){
                    console.log(res.data);
                }).catch(function(err){
                    console.log(err);
                });
            },
            apprise: function() {
                //发送一个`POST`请求
                axios({
                    method:"POST",
                    url: this.evaluri,
                    withCredentials:true,
                    data:{"method":"STATE_EVALUATE",
                        "json": JSON.stringify({
                            "centerid":"","counterid":this.localcfg.counterno, "serviceper_no": this.localcfg.staffno, state: 2
                        })}
                }).then(function(res){
                    console.log(res.data);
                }).catch(function(err){
                    console.log(err);
                });
            },
            pause: function() {
                //发送一个`POST`请求
                axios({
                    method:"POST",
                    url: this.evaluri,
                    withCredentials:true,
                    data:{"method":"STATE_EVALUATE",
                        "json": JSON.stringify({
                            "centerid":"","counterid":"10D07AEA2A44","serviceper_no":"1",state:this.pausestate
                        })}
                }).then(function(res){
                    console.log(res.data);
                    if(this.pausestate == 4){
                        this.pausestate = 5;
                        pausestatestr = "取消暂停";
                    }
                    else{
                        this.pausestate = 4;
                        pausestatestr = "暂停";
                    }

                }.bind(this)).catch(function(err){
                    console.log(err);
                });
            },
            logout: function() {
                //发送一个`POST`请求
                axios({
                    method:"POST",
                    url: this.evaluri,
                    withCredentials:true,
                    data:{"method":"STATE_EVALUATE",
                        "json": JSON.stringify({
                            "centerid":"","counterid":"10D07AEA2A44","serviceper_no":"1",state:3
                        })}
                }).then(function(res){
                    console.log(res.data);
                    location.href="evallogin.jsp"

                }).catch(function(err){
                    console.log(err);
                });
            },
        }
    });
</script>
</body>
</html>