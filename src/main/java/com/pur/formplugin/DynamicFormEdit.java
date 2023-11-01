package com.pur.formplugin;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.form.FormShowParameter;
import kd.bos.form.control.Control;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 实现附件回填功能： 附件表单 页面监听插件
 */
public class DynamicFormEdit extends AbstractBillPlugIn
{
    // 监听页面按钮点击
    @Override
    public void registerListener(EventObject e)
    {
        super.registerListener(e);
        this.addClickListeners("btncancel", "btnok"); // 监听确认和取消按钮
    }

    /**
     * Order 2
     * 打开
     * 打开动态表单之后获取原单数据，并将其显示到动态表单上
     * 获取showParameter.setCustomParams(map);中设置的参数
     */
    @Override
    public void afterCreateNewData(EventObject e)
    {
        super.afterCreateNewData(e);
        FormShowParameter showParameter = this.getView().getFormShowParameter();
        Map<String, Object> maps = showParameter.getCustomParams();
        Long applier = (Long) maps.get("tpv_applier");//用户
        this.getModel().setValue("tpv_applier", applier);
        String billno = (String) maps.get("tpv_billno");//单据编号
        this.getModel().setValue("tpv_billno", billno);
        if (maps.containsKey("tpv_attachment") && maps.get("tpv_attachment") != null) {
            @SuppressWarnings("unchecked")
            List<Long> attachIdSet = (List<Long>) maps.get("tpv_attachment");
            if (!attachIdSet.isEmpty()) {
                this.getModel().setValue("tpv_attachment", attachIdSet.toArray());
            }
        }
    }

    /**
     * Order 3
     * 关闭，将动态表单上内容返回给源单据，通过returnDataToParent(map)
     * 点击动态表单上取消or确认按钮
     */
    @Override
    public void click(EventObject evt)
    {
        super.click(evt);
        // 获取被点击的控件对象
        Control source = (Control) evt.getSource();
        // 点击确认按钮
        if (StringUtils.equals(source.getKey(), "btnok")) {
            HashMap<String, Object> map = new HashMap<>();
            DynamicObject dateEntity = this.getModel().getDataEntity(true);
            DynamicObjectCollection attachment = (DynamicObjectCollection) dateEntity.get("tpv_attachment");
            if (attachment != null && !attachment.isEmpty()) {
                // 获取源附件字段附件对象id集合
                List<Long> attachmentIdSet = new ArrayList<>();
                attachment.forEach(attach ->
                {
                    attachmentIdSet.add(attach.getDynamicObject("fbasedataId").getLong("id"));
                });
                // 判断附件数据是否为空
                if (!attachmentIdSet.isEmpty()) {
                    map.put("tpv_attachment", attachmentIdSet);
                }
            }
            this.getView().returnDataToParent(map);
            this.getView().close();
        } else if (StringUtils.equals(source.getKey(), "btncancel")) {
            // 被点击控件为取消则设置返回值为空并关闭页面（在页面关闭回调方法中必须验证返回值不为空，否则会报空指针）
            this.getView().returnDataToParent(null);
            this.getView().close();
        }
    }
}
