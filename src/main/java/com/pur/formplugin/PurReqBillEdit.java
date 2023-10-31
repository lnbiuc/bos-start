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
import java.math.RoundingMode;
import java.util.Date;

public class PurReqBillEdit extends AbstractBillPlugIn
{


    /**
     * 事件：表单模型某个字段值改变的时候就会触发此事件。
     *
     * @param e 事件对象
     */
    @Override
    public void propertyChanged(PropertyChangedArgs e)
    {
        // 获取更新字段的名称
        String propertyName = e.getProperty().getName();
        // 返回一条改变的数据
        ChangeData cd = e.getChangeSet()[0];
        // 当批量触发字段值改变事件时，返回多条数据
        ChangeData[] changeData = e.getChangeSet();
        // 获取分录行数，如果changeDate是单据头中的内容，那么行数为-1，否则为分录行数
        int rowIndex = changeData[0].getRowIndex();
        switch (propertyName) {
            //申请数量变化或单价变化时，计算分录金额
            case "tpv_applyqty":
            case "tpv_price":
                calcAmount(cd);
                break;
            //如果计量单位和基本计量单位不为空时，更具基本计量单位计算基本数量
            case "tpv_unit":
            case "tpv_baseunit":
                calcBaseNum(rowIndex);
                break;
            //供应商改变，清空分录内容
            case "tpv_supplier":
                cleanEntryData();
                break;
            //物料改变，携带规格型号和计量单位
            case "tpv_materia":
                materielChange(cd);
                break;
            //监听金额标识，计算总金额
            case "tpv_amount":
                amountCollect();//金额变化汇总金额变化
                break;
            //监听本位币，汇率表，结算币，汇率日期，计算汇率
            case "tpv_currency":
            case "tpv_tocurr":
            case "tpv_exratetable":
            case "tpv_exratedate":
                getExchangeRate();
                break;
        }
    }

    private void cleanEntryData()
    {
        this.getModel().deleteEntryData("tpv_app_proc_req_fl");
    }

    /**
     * 计算基本单位数量
     */
    private void calcBaseNum(Integer rowIndex)
    {
        //申请数量
        BigDecimal num = (BigDecimal) this.getModel().getValue("tpv_applyqty");
        // 如果申请数量小于0，清空基本单位数量和申请数量
        if (num.compareTo(BigDecimal.ZERO) <= 0) {
            this.getModel().setValue("tpv_baseunit", null, rowIndex);
            this.getModel().setValue("tpv_applyqty", null, rowIndex);
            return;
        }

        //物料
        DynamicObject materiel = (DynamicObject) this.getModel().getValue("tpv_materia");
        //计量单位
        DynamicObject srcUnit = (DynamicObject) this.getModel().getValue("tpv_unit");
        //基本计量单位
        DynamicObject descUnit = (DynamicObject) this.getModel().getValue("tpv_baseunit");
        if (materiel != null && srcUnit != null && descUnit != null) {
            Long materielId = materiel.getLong("id");
            Long unitId = srcUnit.getLong("id");
            Long baseUnitId = descUnit.getLong("id");
            //计算转换率
            BigDecimal rate = getUnitRateConv(materielId, unitId, baseUnitId);
            //计算基本数量
            BigDecimal baseNumber = num.multiply(rate);
            this.getModel().setValue("tpv_basicunitqtyfield", baseNumber, rowIndex);
        } else {
            this.getModel().setValue("tpv_basicunitqtyfield", BigDecimal.ZERO, rowIndex);
        }
    }


    /**
     * 获取单位换算率，保留10位小数，四舍五入
     */
    public static BigDecimal getUnitRateConv(Long materialId, Long srcUnitId, Long descUnitId)
    {
        BigDecimal rate = null;
        //物料、计量单位、基本计量单位为空，转换率为0
        if (materialId == null || srcUnitId == null || descUnitId == null) {
            rate = BigDecimal.ZERO;
            //计量单位和基本计量单位相同，转换率为1
        } else if (srcUnitId == (long) descUnitId) {
            rate = BigDecimal.ONE;
        } else {
            //获取单位换算率
            final DynamicObject convRate = BaseDataServiceHelper.getMUConv(materialId, srcUnitId, descUnitId);
            if (convRate != null && convRate.getInt("numerator") != 0) {
                // 保留10位小数，四舍五入
                rate = new BigDecimal(convRate.getInt("numerator")).divide(new BigDecimal(convRate.getInt("denominator")), 10, RoundingMode.HALF_UP);
            }
        }
        if (rate == null) {
            rate = BigDecimal.ZERO;
        }
        return rate;
    }


    /**
     * 物料变化携带规格型号和计量单位
     */
    private void materielChange(ChangeData cd)
    {
        // 设置规格型号
        int index = cd.getRowIndex();//行号
        if (cd.getNewValue() != null) {
            //获得物料
            Long materiaId = (long) ((DynamicObject) cd.getNewValue()).getPkValue();
            //读取单张实体数据
            DynamicObject materia = BusinessDataServiceHelper.loadSingle(materiaId, "bd_material");
            //获得规格型号
            OrmLocaleValue modelnum = (OrmLocaleValue) materia.get("modelnum");
            String s = modelnum.getLocaleValue();
            //判断规格型号是否为空，如果为空，使用长*宽*高
            if (StringUtils.isEmpty(s) || s.isEmpty()) {
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

            //设置计量单位
            //获得基本计量单位
            DynamicObject unit = (DynamicObject) materia.get("baseunit");
            //获得基本计量单位的Id
            Object unitId = unit.getPkValue();
            this.getModel().setValue("tpv_unit", unitId, index);
        } else {
            //物料为空
            this.getModel().setValue("tpv_model", null, index);
            this.getModel().setValue("tpv_unit", null, index);
        }
    }

    /**
     * 累加分录各行金额，计算总金额
     */
    private void amountCollect()
    {
        //获得分录
        DynamicObjectCollection entries = this.getModel().getEntryEntity("tpv_app_proc_req_fl");
        BigDecimal amount = BigDecimal.ZERO;
        //循环分录 计算每行金额的汇总
        for (DynamicObject entry : entries) {
            amount = amount.add(entry.getBigDecimal("tpv_amount"));
        }
        //将汇总金额赋值表头总金额字段
        this.getModel().setValue("tpv_amount2", amount);
    }

    //获取汇率
    private void getExchangeRate()
    {
        //获取汇率表
        DynamicObject exRateTable = (DynamicObject) this.getModel().getValue("tpv_exratetable");
        //获取目标币
        DynamicObject fromCurr = (DynamicObject) this.getModel().getValue("tpv_tocurr");
        //获取本位币
        DynamicObject toCurr = (DynamicObject) this.getModel().getValue("tpv_currency");
        //获取汇率日期
        Date applyDate = (Date) this.getModel().getValue("tpv_exratedate");
        //判断目标币和本位币是否相同，设置汇率为1
        if (exRateTable != null && fromCurr != null && toCurr != null & applyDate != null) {
            if (fromCurr.equals(toCurr)) {
                this.getModel().setValue("tpv_exrate", BigDecimal.ONE);
                return;
            }
            //获得汇率表、目标币、本位币的Id
            Long exratetableId = exRateTable.getLong("id");
            Long fromCurrId = fromCurr.getLong("id");
            Long tocurrId = toCurr.getLong("id");

            //计算汇率
            BigDecimal exchangeRate = BaseDataServiceHelper.getExchangeRate(exratetableId, fromCurrId, tocurrId, applyDate);
            this.getModel().setValue("tpv_exrate", exchangeRate);
        } else {
            this.getModel().setValue("tpv_exrate", BigDecimal.ZERO);
        }
    }

    /**
     * 删除分录行时，重新计算总金额
     */
    @Override
    public void afterDeleteRow(AfterDeleteRowEventArgs e)
    {
        //通过单据体行删除事件来触发
        String entryName = e.getEntryProp().getName();
        if ("tpv_app_proc_req_fl".equals(entryName)) {
            amountCollect();
        }
    }

    /**
     * 申请数量变化，计算分录金额
     *
     * @param cd 改变的数据
     */
    private void calcAmount(ChangeData cd)
    {
        //行号
        int index = cd.getRowIndex();
        //判断申请数量改变后是否为空
        if (cd.getNewValue() == null) {
            //设置金额为空
            this.getModel().setValue("tpv_amount", null);
            //判断建议采购单价是否小于等于0
        } else if (((BigDecimal) cd.getNewValue()).longValue() <= 0) {
            //弹出框提示
            this.getView().showMessage("申请数量不能小于等于0");
            // 仅在单价不为空时计算
        } else if (this.getModel().getValue("tpv_price") != null) {
            // 申请数量
            Long number = ((BigDecimal) cd.getNewValue()).longValue();
            // 单价
            Double price = Double.parseDouble(this.getModel().getValue("tpv_price").toString());
            // 计算金额
            Double amount = number * price;
            this.getModel().setValue("tpv_amount", amount, index);
        }
    }
}