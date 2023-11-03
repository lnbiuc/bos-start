# 单据转换配置

https://vip.kingdee.com/school/detail/380772287051906048

## 开启参与单据转换

单据业务控制设置开启参与单据转换，采购申请和采购订单都需要开启



![image-20231025093647878](https://r2-img.lnbiuc.com/r2-image/2023/10/eb84917d235f8dff6fed3ea3eae6640e.png)

## 设置不参与转换字段

![image-20231025093938998](https://r2-img.lnbiuc.com/r2-image/2023/10/564a9075043d6f4ee1036894255fa2ee.png)

![image-20231025093949171](https://r2-img.lnbiuc.com/r2-image/2023/10/8defe1d4ccfb63c790cdb2d41b495bb5.png)

## 设置两张单据关联

采购订单

![image-20231025094210683](https://r2-img.lnbiuc.com/r2-image/2023/10/48b6bd0f9cc810d9c7ada812d005e11e.png)

## 转换路线

业务流开发

![image-20231025094623398](https://r2-img.lnbiuc.com/r2-image/2023/10/71479c7fbc9e5edbbfc3563be4d6ef95.png)

转换路线，新增

![image-20231025094657905](https://r2-img.lnbiuc.com/r2-image/2023/10/b21ca07b4828fba8e628745621f5c158.png)

## 转换条件

当状态 == 已审核的时候才进行转换

![image-20231025094812810](https://r2-img.lnbiuc.com/r2-image/2023/10/c587de7c1e3947741c6b2c2bb4e905f0.png)

![image-20231025095423892](https://r2-img.lnbiuc.com/r2-image/2023/10/23a8f18bdf459be60dfa112ec4d7124a.png)

![image-20231025095431332](https://r2-img.lnbiuc.com/r2-image/2023/10/4af70df7a190b7626118bc71f1e828bd.png)

![image-20231025095442795](https://r2-img.lnbiuc.com/r2-image/2023/10/9e46bc3588f2ce4cc33f2b7ea5541d49.png)

![image-20231025095828229](https://r2-img.lnbiuc.com/r2-image/2023/10/64885bcc2f158142b387134aec03c3e6.png)

![image-20231025104319094](https://r2-img.lnbiuc.com/r2-image/2023/10/c7a4df9a6aff656ae15f1ed6d46dc155.png)

# 反写规则配置

https://vip.kingdee.com/school/detail/380772787046599680?productLineId=29

https://vip.kingdee.com/school/detail/380774014820043776?productLineId=29

![image-20231025100114773](https://r2-img.lnbiuc.com/r2-image/2023/10/d94f7e2ff30a945c9e934a78f32cdc23.png)

![image-20231025100519610](https://r2-img.lnbiuc.com/r2-image/2023/10/08aa35b3522bc2c4a95b009e109c1c40.png)

![image-20231025100531935](https://r2-img.lnbiuc.com/r2-image/2023/10/e3913a10a2e910d875a6867339401b52.png)

![image-20231025100539830](https://r2-img.lnbiuc.com/r2-image/2023/10/781d596deaf3d82617c40208184adb94.png)

## 单据转换绑定到功能按钮

![image-20231025100701646](https://r2-img.lnbiuc.com/r2-image/2023/10/fbb67bc24e404414abc13a81d50de36d.png)

新增

![image-20231025100729521](https://r2-img.lnbiuc.com/r2-image/2023/10/3064d950a0a98ccbc1658f9b4ea42166.png)

使用默认设置

![image-20231025100854614](https://r2-img.lnbiuc.com/r2-image/2023/10/7771ab1a298bcfbd8fde2eb917e29b0a.png)

## 其他

基本信息：

合同号 必录 唯一

供应商带采购组织

单据类型，两个类型，进口采购（隐藏佣金信息） 出口采购（显示佣金信息）

备注 2000字，



表头：佣金信息



采购订单，字段相同



子分录，卷号\费用



物流信息：开始日期（申请单业务日期，不可编辑） 结束日期（不小于开始日期）交货日期（不小于到达日期）

名称 文本

基础资料

# 界面规则

## 前置条件

![image-20231027152659811](https://r2-img.lnbiuc.com/r2-image/2023/10/364c6279f8b6c3299fa7401ae1f09dfb.png)

## 可操作内容

锁定字段
解锁字段
显示字段
隐藏字段
锁定控件
解锁控件
显示控件
隐藏控件

![image-20231027152744813](https://r2-img.lnbiuc.com/r2-image/2023/10/394e9defebbae9ebbfddc87bcf4a0cde.png)

![image-20231027152800655](https://r2-img.lnbiuc.com/r2-image/2023/10/7df408a512e994b139be2b39f6955d35.png)

# 业务规则

## 前置条件

![image-20231027152842069](https://r2-img.lnbiuc.com/r2-image/2023/10/a5aa9fc7715c41c6092dd8b2018fa6b0.png)

## 可操作内容

计算定义公式的值并填写到指定列
携带基础资料属性到指定时许可

设置当前编辑字段值到指定字段
清除指定字段值
根据数量关联的单位自动计算目标数量

![image-20231027152917354](https://r2-img.lnbiuc.com/r2-image/2023/10/29a6f826fc69e04f8e496ffd94e4afea.png)

# 报表

[https://vip.kingdee.com/article/239024807860551936?productLineId=29&isKnowledge=2](https://vip.kingdee.com/article/239024807860551936?productLineId=29&isKnowledge=2)

![image-20231103092939803](https://r2-img.lnbiuc.com/r2-image/2023/11/39f51e23e952e284fdf798564bad9593.png)

![image-20231103093032869](https://r2-img.lnbiuc.com/r2-image/2023/11/12f66b51fd702678382db37345d1ff7f.png)

![image-20231103093202672](https://r2-img.lnbiuc.com/r2-image/2023/11/ab9174af8abf84f5754b704ea3301d25.png)

![image-20231103093216366](https://r2-img.lnbiuc.com/r2-image/2023/11/62d1c00c1930ebfb545c34d0dc12a424.png)

![image-20231103093227759](https://r2-img.lnbiuc.com/r2-image/2023/11/38029d93276da071341d7b17bb7d360a.png)

添加过滤条件

![image-20231103094342034](https://r2-img.lnbiuc.com/r2-image/2023/11/a44869b3a61300ca1778a53d5a60256c.png)

![image-20231103094423228](https://r2-img.lnbiuc.com/r2-image/2023/11/197d48a43975bd772079601c835e5f7b.png)
