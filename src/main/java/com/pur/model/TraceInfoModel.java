package com.pur.model;

import kd.bos.form.chart.ItemValue;

import java.math.BigDecimal;
import java.util.List;

public class TraceInfoModel
{
    // 主币别
    private Long mainCurrencyId;
    // 总金额
    private BigDecimal sumMoney;
    // 收入笔数
    private Integer inTotal;
    // 支出笔数
    private Integer outTotal;
    // 收入票据张数
    private Integer inTicket;
    // 支出票据张数
    private Integer outTicket;
    // 收入总金额
    private BigDecimal inTotalSum;
    // 支出总金额
    private BigDecimal outTotalSum;
    // 收入票据总金额
    private BigDecimal inTicketSum;
    // 支出票据总金额
    private BigDecimal outTicketSum;

    /**
     * name: 币别id currencyId
     * value: 金额 price
     * pieData list中数据最好为3条
     */
    private List<ItemValue> pieData;

    public TraceInfoModel()
    {
    }

    public Long getMainCurrencyId()
    {
        return mainCurrencyId;
    }

    public void setMainCurrencyId(Long mainCurrencyId)
    {
        this.mainCurrencyId = mainCurrencyId;
    }

    public BigDecimal getSumMoney()
    {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney)
    {
        this.sumMoney = sumMoney;
    }

    public Integer getInTotal()
    {
        return inTotal;
    }

    public void setInTotal(Integer inTotal)
    {
        this.inTotal = inTotal;
    }

    public Integer getOutTotal()
    {
        return outTotal;
    }

    public void setOutTotal(Integer outTotal)
    {
        this.outTotal = outTotal;
    }

    public Integer getInTicket()
    {
        return inTicket;
    }

    public void setInTicket(Integer inTicket)
    {
        this.inTicket = inTicket;
    }

    public Integer getOutTicket()
    {
        return outTicket;
    }

    public void setOutTicket(Integer outTicket)
    {
        this.outTicket = outTicket;
    }

    public BigDecimal getInTotalSum()
    {
        return inTotalSum;
    }

    public void setInTotalSum(BigDecimal inTotalSum)
    {
        this.inTotalSum = inTotalSum;
    }

    public BigDecimal getOutTotalSum()
    {
        return outTotalSum;
    }

    public void setOutTotalSum(BigDecimal outTotalSum)
    {
        this.outTotalSum = outTotalSum;
    }

    public BigDecimal getInTicketSum()
    {
        return inTicketSum;
    }

    public void setInTicketSum(BigDecimal inTicketSum)
    {
        this.inTicketSum = inTicketSum;
    }

    public BigDecimal getOutTicketSum()
    {
        return outTicketSum;
    }

    public void setOutTicketSum(BigDecimal outTicketSum)
    {
        this.outTicketSum = outTicketSum;
    }

    public List<ItemValue> getPieData()
    {
        return pieData;
    }

    public void setPieData(List<ItemValue> pieData)
    {
        this.pieData = pieData;
    }
}
