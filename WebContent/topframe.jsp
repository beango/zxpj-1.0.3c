<%@ page pageEncoding="UTF-8"%>
<%@page import="com.sundyn.util.SundynSet"%>
<%
String path=request.getRealPath("/");
SundynSet sundynSet=SundynSet.getInstance(path);
String url=sundynSet.getM_content().get("requestAddress").toString();
 %>
 <%@taglib prefix="s" uri="/struts-tags" %>
 <link rel="stylesheet" href="css/common_<s:text name='sundyn.language' />.css" type="text/css" />

		<div class="header_content" style="">
			<div class="logo" style="float:left;width:30%;">
			 	<img src="<s:text name='top.img'/>" alt="<s:text name='sundyn.title'/>" />
			</div>
			<div id="nav" style="float:left; background:none;">
				<ul>
					<%-- <li id="man_nav_1" onclick="list_sub_nav(id,'<s:text name="left.menu.dh1"/>')"
						class="bg_image_onclick">
						<s:text name="main.homePage" />
					</li>
					
					<li id="man_nav_2" onclick="list_sub_nav(id,'<s:text name="left.menu.dh2"/>')"
						class="bg_image">
						<s:text name="left.menu.dh2" />
					</li>
					<li id="man_nav_3" onclick="list_sub_nav(id,'<s:text name="left.menu.dh3"/>')"
						class="bg_image">
						<s:text name="left.menu.dh3" />
					</li>
					<li id="man_nav_4" onclick="list_sub_nav(id,'<s:text name="left.menu.dh4"/>')"
						class="bg_image">
						<s:text name="left.menu.dh4" />
					</li>
					<li id="man_nav_7" onclick="list_sub_nav(id,'<s:text name="left.menu.dh7"/>')"
						class="bg_image">
						<s:text name="left.menu.dh7" />
					</li>
					<li id="man_nav_5" onclick="list_sub_nav(id,'<s:text name="left.menu.dh5"/>')" class="bg_image">
						<s:text name="left.menu.dh5" />
					</li> --%>
					<!-- 
					<li id="man_nav_6" onclick="login()" class="bg_image">
						大液晶评价
					</li>
					 -->
				</ul>
			</div>
			<%-- <div id="nav_tip">
				<span id="show_text"><s:text name="main.welcome" /></span>
				<span id="back"> <!-- <a href="#" onclick="back()">返回</a> -->   </span>
			</div> --%>
			<div id="nav_userinfo" style="float:left; background:none;color:white;margin-top:5px;">
				<div id="user_info" style="color:white;"><s:text name="left.welcome"/><strong>${realname}</strong>
				<br />[<a href="#" style="color:white;">${name}</a><s:text name="sundyn.comma" /> <a href="managerLogout.action" style="color:white;"><s:text name="left.logout"/></a>]</div>
			</div>
		</div>
		
		<script type="text/javascript" src="js/easyui/jquery.min.js"></script>
		<script type="text/javascript">
		$(function(){
			$.ajax({
            	url :"/sundyn/getMenu.action",
            	dataType:"json",
            	success:function(data){
            		$.each(data, function (i, menu) {
	            		if(menu.parentId==0){
	            			var cls = "bg_image";
	            			if(i == 0)
	            				cls = "bg_image_onclick";
	            			$("#nav ul").append("<li id=\"man_nav_"+(i+1)+"\" onclick=\"list_sub_nav(id,'"+menu.menuName+"',"+menu.id+")\" class=\""+cls+"\">"+menu.menuName+"</li>")
	            			
	            		}
            		});
            	}
            });
		});
		var preClassName = "man_nav_1";
		function list_sub_nav(Id,sortname,menuid){
		   if(preClassName != ""){
		      getObject(preClassName).className="bg_image";
		   }
		   if(getObject(Id).className == "bg_image"){
			  //addPanel(Id, sortname);
		      getObject(Id).className="bg_image_onclick";
		      preClassName = Id;
			  //showInnerText(Id);
			  //window.top.frames['leftFrame'].queryLeft(0);
			  //console.log(window.top.frames['leftFrame'].contentWindow.queryLeft(0));
			  window.top.queryLeft(menuid);
			  //$(window.top.frames['leftFrame'])[0].outlookbar.getdefaultnav(sortname);
			  
			  //window.top.frames['leftFrame'].outlookbar.getdefaultnav(sortname); //queryLeft(menuid);
			  //window.top.frames['leftFrame'].outlookbar.getbytitle(sortname);
			  //window.top.frames['leftFrame'].outlookbar.initaccordion();
		   }
		}
		function addPanel(Id, sortname){
			 var switchId = parseInt(Id.substring(8));
	            var linkUrl = "";
	            switch(switchId){
			    case 1:
			    	linkUrl = 'queryIndex.action'; 
				   showText =  "<s:text name='main.welcome' />";
		 		   break;
			    case 2:
			    	linkUrl = 'queryDept.action'; 
				   showText =  "<s:text name='left.menu.dh2' />";
				   break;
			    case 3:
			    	linkUrl = 'analyseTotal.action'; 
				   showText =  "<s:text name='left.menu.dh3' />";
				   break;		   
			    case 4:
			       window.top.managerQx();
 				   showText =  "<s:text name='left.menu.dh4' />";
				   break;	
			    case 5:
				   showText =  "<s:text name='main.help' />";
				   linkUrl = 'baseHelp.action';
				   break;	
				case 7:
	 				 showText =  "<s:text name='left.menu.dh7' />";
	 					linkUrl = 'adviceShowAnserTable.action';
	 				 break;
				case 6:
 				 showText =  "<s:text name='main.fullScreen' />"; 
				break;  	   		   
			}
	            window.top.addTab(sortname, linkUrl);
	            
	        }
		function showInnerText(Id){
		    var switchId = parseInt(Id.substring(8));
		    var tip= document.getElementById("nav_tip")
		      tip.style.backgroundImage="url(images/menu_top"+switchId+".gif)";   
		      tip.style.backgroundPositionX="0px";   
		      tip.style.backgroundPositionY="0px";   
		 	var showText = "<s:text name='main.nomsg' />";
		    	switch(switchId){
			    case 1:
			       window.top.frames['manFrame'].location = 'queryIndex.action'; 
				   showText =  "<s:text name='main.welcome' />";
		 		   break;
			    case 2:
			    window.top.frames['manFrame'].location.href = 'queryDept.action'; 
				   showText =  "<s:text name='left.menu.dh2' />";
				   break;
			    case 3:
			    window.top.frames['manFrame'].location = 'analyseTotal.action'; 
				   showText =  "<s:text name='left.menu.dh3' />";
				   break;		   
			    case 4:
			    managerQx();
 				   showText =  "<s:text name='left.menu.dh4' />";
				   break;	
			    case 5:
				   showText =  "<s:text name='main.help' />";
				   window.top.frames['manFrame'].location = 'baseHelp.action';
				   break;	
				case 7:
	 				 showText =  "<s:text name='left.menu.dh7' />";
	 				 window.top.frames['manFrame'].location = 'adviceShowAnserTable.action';
	 				 break;
				case 6:
 				 showText =  "<s:text name='main.fullScreen' />"; 
				break;  	   		   
			}
			getObject('show_text').innerHTML = showText;
		}
		 //获取对象属性兼容方法
		 function getObject(objectId) {
		    if(document.getElementById && document.getElementById(objectId)) {
			// W3C DOM
			return document.getElementById(objectId);
		    } else if (document.all && document.all(objectId)) {
			// MSIE 4 DOM
			return document.all(objectId);
		    } else if (document.layers && document.layers[objectId]) {
			// NN 4 DOM.. note: this won't find nested layers
			return document.layers[objectId];
		    } else {
			return false;
		    }
		}
		function back(){
		 parent.manFrame.history.back();
		
		}
		function login(){
		 parent.location.href="<%=url%>"
 		}
		</script>

     