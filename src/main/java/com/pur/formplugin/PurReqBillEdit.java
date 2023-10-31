package com.pur.formplugin;

import com.alibaba.druid.util.StringUtils;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.entity.datamodel.events.AfterDeleteRowEventArgs;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;

import java.math.BigDecimal;
import java.util.Date;

public class PurReqBillEdit extends AbstractBillPlugIn {


    /**
     * 事件：表单模型某个字段值改变的时候就会触发此事件。
     * @param e 事件对象
     */
    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        //触发时机：表单模型某个字段值改变的时候就会触发此事件。
        String propertyName = e.getProperty().getName();
        ChangeData cd = e.getChangeSet()[0];

        ChangeData[] changeData = e.getChangeSet();
        int rowIndex = changeData[0].getRowIndex();//获取分录行数
        switch (propertyName) {
            //监听申请数量与建议采购单价
            case "tpv_applyqty":
                applyqtyAlter(cd);//申请数量变化，金额改变
                break;
            case "tpv_price":
                priceChange(cd);//建议采购单价变化，金额改变
                break;
            //监听计量单位与基本计量单位
            case "tpv_unit":
            case "tpv_unitfield":
                getBaseunitqty(rowIndex);//申请数量改变，计算基本单位数量
                break;
            //监听供应商标识
            case "tpv_supplier":
                this.getModel().deleteEntryData("tpv_entryentity");// 供应商改变清空分录
                break;
            //监听物料标识
            case "tpv_materia":
                materielChange(cd);//物料变化
                break;
            //监听金额标识
            case "tpv_amount":
                amountCollect();//金额变化汇总金额变化
                break;
            //监听本位币，汇率表，结算币，汇率日期
            case "tpv_currency":
            case "tpv_tocurr":
            case "tpv_exratetable":
            case "tpv_exratedate":
                showExchangerate();
                break;
        }
    }

    private void carryDepartment() {
        //判断申请人是否改变
        //获取申请人的值
        DynamicObject applier =  (DynamicObject)this.getModel().getValue("tpv_applier");
        //判断申请人是否为空
        if (applier != null){
            //获取申请人对象
            DynamicObjectCollection entryentity = applier.getDynamicObjectCollection("entryentity");
            if (entryentity.size() > 0){
                for (DynamicObject dynamicObject : entryentity) {
                    DynamicObject dpt = dynamicObject.getDynamicObject("dpt");
                    long id = dpt.getLong("id");
                    //判断dpt是否为空
                    this.getModel().setValue("tpv_applyorg",id);
                }
            }else {
                this.getModel().setValue("tpv_applyorg",null);
            }
        }else {
            this.getModel().setValue("tpv_applyorg",null);
        }
    }

    //申请数量改变
    //计算基本单位数量
    private void getBaseunitqty(Integer row) {
        BigDecimal num = (BigDecimal) this.getModel().getValue("tpv_applyqty");//基本单位数量

        if (num.compareTo(BigDecimal.ZERO) <= 0) {
            this.getModel().setValue("tpv_basicunitqtyfield", null, row);
            this.getModel().setValue("tpv_applyqty", null, row);
            return;
        }

        //物料
        DynamicObject materiel = (DynamicObject) this.getModel().getValue("tpv_materia");
        //计量单位
        DynamicObject srcUnit = (DynamicObject) this.getModel().getValue("tpv_unit");
        //基本计量单位
        DynamicObject desUnit = (DynamicObject) this.getModel().getValue("tpv_unitfield");
        if (materiel != null && srcUnit != null && desUnit != null) {
            Long materielId = materiel.getLong("id");
            Long srcUnitId = srcUnit.getLong("id");
            Long desUnitId = desUnit.getLong("id");
            //计算转换率
            BigDecimal rate = getUnitRateConv(materielId, srcUnitId, desUnitId);
            //计算单位数量
            BigDecimal basenum = num.multiply(rate);//乘法
            this.getModel().setValue("tpv_basicunitqtyfield", basenum, row);
        } else {
            this.getModel().setValue("tpv_basicunitqtyfield", BigDecimal.ZERO, row);
        }
    }


    //计算单位间的转换率
    public static BigDecimal getUnitRateConv(Long materialId, Long srcUnitId, Long desUnitId) {
        BigDecimal unitRate = null;
        if (materialId == null || srcUnitId == null || desUnitId == null) {
            unitRate = BigDecimal.ZERO;
        } else if (srcUnitId == (long) desUnitId) {
            unitRate = BigDecimal.ONE;
        } else {
            final DynamicObject muConv = BaseDataServiceHelper.getMUConv(materialId, srcUnitId, desUnitId);
            if (muConv != null && muConv.getInt("numerator") != 0) {
                unitRate = new BigDecimal(muConv.getInt("numerator")).divide(new BigDecimal(muConv.getInt("denominator")), 10, 4);
            }
        }
        if (unitRate == null) {
            unitRate = BigDecimal.ZERO;
        }
        return unitRate;
    }


    //物料变化携带规格型号和计量单位
    private void materielChange(ChangeData cd) {
        // 设置规格型号
        int index = cd.getRowIndex();//行号
        if (cd.getNewValue() != null) {
            //获得物料
            long materiaId = (long) ((DynamicObject) cd.getNewValue()).getPkValue();

            DynamicObject materia = (DynamicObject) BusinessDataServiceHelper.loadSingle(materiaId, "bd_material");
            //获得规格型号
            OrmLocaleValue modelnum = (OrmLocaleValue) materia.get("modelnum");
            //判断得到的规格型号是否为空
            //为空以长宽高填写
            String s = modelnum.getLocaleValue();
            if (StringUtils.isEmpty(s) || s.equals("")) {
                BigDecimal length = (BigDecimal) materia.get("length");//长

                BigDecimal width = (BigDecimal) materia.get("width");//宽

                BigDecimal height = (BigDecimal) materia.get("height");//高

                if (length != null && width != null && height != null && length.compareTo(BigDecimal.ZERO) > 0
                        && width.compareTo(BigDecimal.ZERO) > 0 && height.compareTo(BigDecimal.ZERO) > 0) {
                    String model = length.longValue() + "*" + width.longValue() + "*" + height.longValue();
                    this.getModel().setValue("tpv_model", model, index);
                }
            } else {
                this.getModel().setValue("tpv_model", modelnum, index);
            }

            // 设置计量单位
            //获得基本计量单位
            DynamicObject unit = (DynamicObject) materia.get("baseunit");
            //获得基本计量单位的Id
            Object unitId = unit.getPkValue();
            this.getModel().setValue("tpv_unit", unitId, index);
        } else {
            this.getModel().setValue("tpv_model", null, index);
            this.getModel().setValue("tpv_unit", null, index);
        }
    }


    //建议采购单价变化，计算分录金额
    private void priceChange(ChangeData cd) {
        //判断采购单价改变后是否为空
        if (cd.getNewValue() == null) {//判断建议采购单价是否为空
            this.getModel().setValue("tpv_amount", null);//设置金额为空
        } else if (((BigDecimal) cd.getNewValue()).longValue() <= 0) {//判断建议采购单价是否小于等于0
            this.getView().showMessage("注意建议采购单价不能小于等于0");
            Double priceValue = Double.parseDouble(cd.getNewValue().toString());//获得采购单价
            Double baseunitqty = Double.parseDouble(this.getModel().getValue("tpv_applyqty").toString());//获得申请数量数量
            Double amount = priceValue * baseunitqty;//计算金额
            BigDecimal bigDecimal = BigDecimal.valueOf(amount) == null ? BigDecimal.ZERO : BigDecimal.valueOf(amount);
            this.getModel().setValue("tpv_amount", bigDecimal);//赋值金额
        } else {
            Long priceValue = ((BigDecimal) cd.getNewValue()).longValue();
            Double baseunitqty = Double.parseDouble(this.getModel().getValue("tpv_applyqty").toString());
            Double amount = priceValue * baseunitqty;
            BigDecimal bigDecimal = BigDecimal.valueOf(amount) == null ? BigDecimal.ZERO : BigDecimal.valueOf(amount);
            this.getModel().setValue("tpv_amount", bigDecimal);
        }
    }

    private void amountCollect() {
        //获得分录
        DynamicObjectCollection entrys = this.getModel().getEntryEntity("tpv_app_proc_req_fl");
        BigDecimal amount = BigDecimal.ZERO;
        //循环分录 计算每行金额的汇总
        for (DynamicObject entry : entrys) {
            amount = amount.add(entry.getBigDecimal("tpv_amount"));
        }
        //将汇总金额赋值表头总金额字段
        this.getModel().setValue("tpv_amount2", amount);
    }

    //获取汇率
    private void showExchangerate() {
        //获取汇率表
        DynamicObject exratetable = (DynamicObject) this.getModel().getValue("tpv_exratetable");
        //获取目标币
        DynamicObject fromcurr = (DynamicObject) this.getModel().getValue("tpv_tocurr");
        //获取本位币
        DynamicObject tocurr = (DynamicObject) this.getModel().getValue("tpv_currency");
        //获取申请日期
        Date applyDate = (Date) this.getModel().getValue("tpv_exratedate");
        //判断目标币和本位币是否相同
        if (exratetable != null && fromcurr != null && tocurr != null & applyDate != null) {
            if (fromcurr.equals(tocurr)) {
                this.getModel().setValue("tpv_exrate", BigDecimal.ONE);
                return;
            }
            //获得汇率表、目标币、本位币的Id
            Long exratetableId = exratetable.getLong("id");
            Long fromcurrId = fromcurr.getLong("id");
            Long tocurrId = tocurr.getLong("id");
            if (fromcurrId == null || tocurrId == null) {
                return;
            }
            //计算汇率
            BigDecimal exchangeRate = BaseDataServiceHelper.getExchangeRate(exratetableId, fromcurrId, tocurrId, applyDate);
            this.getModel().setValue("tpv_exrate", exchangeRate);
        } else {
            this.getModel().setValue("tpv_exrate", BigDecimal.ZERO);
        }
    }

    @Override
    public void afterDeleteRow(AfterDeleteRowEventArgs e) {
        //通过单据体行删除事件来触发
        String entryName = e.getEntryProp().getName();
        if ("tpv_app_proc_req_fl".equals(entryName)) {
            amountCollect();
        }
    }

    //申请数量变化，计算分录金额
    private void applyqtyAlter(ChangeData cd) {
        //判断申请数量改变后是否为空
        int index = cd.getRowIndex();//行号
        if (cd.getNewValue() == null) {//判断建议采购单价是否为空
            this.getModel().setValue("tpv_amount", null);//设置金额为空
        } else if (((BigDecimal) cd.getNewValue()).longValue() <= 0) {//判断建议采购单价是否小于等于0
            this.getView().showMessage("注意申请数量不能小于等于0");
            Double priceValue = Double.parseDouble(cd.getNewValue().toString());//获得采购单价
            Double baseunitqty = Double.parseDouble(this.getModel().getValue("tpv_price").toString());//获得建议采购单价
            Double amount = priceValue * baseunitqty;//计算金额
            this.getModel().setValue("tpv_amount", amount);//赋值金额
        } else {
            Long priceValue = ((BigDecimal) cd.getNewValue()).longValue();
            Double baseunitqty = Double.parseDouble(this.getModel().getValue("tpv_price").toString());
            Double amount = priceValue * baseunitqty;
            this.getModel().setValue("tpv_amount", amount, index);
        }
    }

}