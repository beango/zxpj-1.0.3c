<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" href="css/common_<s:text name='sundyn.language' />.css" type="text/css" />
		<link rel="stylesheet" href="css/dialog.css" type="text/css" />
		<title><s:text name='sundyn.title' /></title>
		<script type="text/javascript" src="js/dojo.js"></script>
		<script type="text/javascript" src="js/dialog.js"></script>
		<script type="text/javascript" src="js/my_<s:text name='sundyn.language' />.js"></script>
	</head>
	<body>
		<div id="man_zone">
		    <div class="fengge">&nbsp;</div>
			<div class="title640"> <h3><s:text name='sundyn.playList.title' /></h3> </div>
			<div style="width:638px; height:290px;" class="kuang">
			    <div class="fengge" style="height:25px;">&nbsp;</div>
			    <div>
				    <table width="50%" border="0" cellspacing="0" cellpadding="0" style="border-color:#FFFFFF;">
				       <tr>
				         <td style="border-color:#FFFFFF;" align="center">	<input name="keyword" id="keyword" class="input_comm" /></td>
				         <td style="border-color:#FFFFFF;" align="left"><img src="<s:text name='sundyn.pic.query' />" width="55" height="25" onclick="playListQueryAjax()" class="hand"/></td>
				         <td style="border-color:#FFFFFF;" align="left"><img src="<s:text name='sundyn.pic.add' />" width="63" height="25" onclick="playListAddDialog()" class="hand"/></td>
				      </tr>
				     </table>
			    </div>
			    <div class="fengge" style="height:25px;">&nbsp;</div>
				<table width="90%"   cellpadding="0" cellspacing="0" style="border-top: 1px solid #bad6ec; border-right: 1px solid #bad6ec;">
				  <tr>
				    <td align="center" valign="middle" background="images/table_bg_03.jpg" class="px13_1"><s:text name='sundyn.column.playListId' /></td>
				    <td align="center" valign="middle" background="images/table_bg_03.jpg" class="px13_1"><s:text name='sundyn.column.playListName' /></td>
				    <td align="center" valign="middle" background="images/table_bg_03.jpg" class="px13_1"><s:text name='sundyn.column.operation' /></td>
				  </tr>
				   <c:forEach items="${pager.pageList}" var="playList">
						<tr>
							<td style="text-align: center;">
								${playList.playListId}
							</td>
							<td style="text-align: center;">
							   <div style="width:90px;height:20px;text-align: center;text-overflow:ellipsis;overflow:hidden;" >
								 ${playList.playListName}
								 </div>
							</td>
							<td style="text-align: center;">
								<a href="javascript:playListEditDialog(${playList.playListId});"><s:text name='sundyn.playList.editTitle' /></a><s:text name="sundyn.separator"/>
								<a href="javascript:playListDel(${playList.playListId});"><s:text name='sundyn.del' /></a><s:text name="sundyn.separator"/>
								<a href="javascript:playListUpdateDialog(${playList.playListId});"><s:text name='sudnyn.playList.updateM7' /></a><s:text name="sundyn.separator"/>
								<a href="javascript:playListConfigDialog(${playList.playListId});"><s:text name='sudnyn.playList.editConfigFile' /></a>
							</td>
						</tr>
					</c:forEach>
				</table>
			    <div class="fengge" style="height:15px;">&nbsp;</div>
				<div>${pager.pageTextAjax}</div>
		    </div> 
		</div>
		<div style="float: left;"><img height="7" width="763" border="0" src="images/man_zone_bottom.gif"/></div>
		<div id="dialog" style="width: 600px; display: none;">
	</body>
</html>