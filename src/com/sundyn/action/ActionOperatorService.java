package com.sundyn.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ActionOperatorService extends ActionSupport {

    @Override
    public String execute(){
        ActionContext.getContext().put("request_dispatcher", "转发操作的request请求");
        return Action.SUCCESS;
    }
}