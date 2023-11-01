package com.pur.formplugin;

import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.user.UserServiceHelper;

import java.util.List;

public class SetListFilter extends AbstractListPlugin
{

    @Override
    public void setFilter(SetFilterEvent e)
    {
        // 获取用户id
        long userId = UserServiceHelper.getCurrentUserId();
        // 获取过滤规则列表
        List<QFilter> qFilterList = e.getQFilters();
        // 清空过滤规则
        qFilterList.clear();
        // 只查看采购组织名称不为空的单据
        qFilterList.add(new QFilter("tpv_org.name", "!=", null));
        // 只能查看当前用户自己创建的
        qFilterList.add(new QFilter("tpv_applier", "=", userId));
        String[] status = new String[]{"A", "B", "C"};
        // 不显示已关闭的单据
        qFilterList.add(new QFilter("billstatus", QCP.in, status));

    }
}
