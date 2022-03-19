package cn.sliew.http.stream.dao.entity.jst;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 聚水潭订单查询接口
 */
@Getter
@Setter
public class JstOrder extends BaseSyncOffset {

    /**
     * 是否货到付款
     */
    private String isCod;

    /**
     * 快递单号
     */
    private String lId;

    /**
     * 发货日期
     */
    private Date sendDate;

    /**
     * 支付时间
     */
    private Date payDate;

    /**
     * 运费，保留两位小数，单位（元）
     */
    private String freight;

    /**
     * 收货地址
     */
    private String receiverAddress;

    /**
     * 区
     */
    private String receiverDistrict;

    /**
     * 发货仓编号
     */
    private String wmsCoId;

    /**
     * 快递公司
     */
    private String logisticsCompany;

    /**
     * 抵扣金额
     */
    private String freeAmount;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 问题类型，仅当问题订单时有效
     */
    private String questionType;

    /**
     * 外部支付单号
     */
    private String outerPayId;

    /**
     * 线上订单号，最长不超过 20;唯一
     */
    private String soId;

    /**
     * 订单类型
     */
    private String type;

    /**
     * 订单来源
     */
    private String orderFrom;

    /**
     * 待付款：WaitPay；发货中：Delivering；被合并：Merged；异常：Question；被拆分：Split；等供销商|外仓发货：WaitOuterSent；已付款待审核：WaitConfirm；已客审待财审：WaitFConfirm；已发货：Sent；取消：Cancelled
     */
    private String status;

    /**
     * 应付金额，保留两位小数，单位（元）
     */
    private String payAmount;

    /**
     * 买家昵称
     */
    private String shopBuyerId;

    /**
     * 平台订单状态
     */
    private String shopStatus;

    /**
     * 手机
     */
    private String receiverMobile;

    /**
     * 下单时间
     */
    private Date orderDate;

    /**
     * 问题描述
     */
    private String questionDesc;

    /**
     * 收件信息-市
     */
    private String receiverCity;

    /**
     * 收件信息-省
     */
    private String receiverState;

    /**
     * 收件信息-收件人
     */
    private String receiverName;

    /**
     * ERP 内部订单号，唯一
     */
    private Long oId;

    /**
     * 店铺编号
     */
    private Long shopId;

    /**
     * 公司编号
     */
    private Long coId;

    /**
     * 支付信息
     */
    private String pays;

    /**
     * 购买商品列表
     */
    private String items;

    /**
     * 订单备注；卖家备注
     */
    private String remark;

    /**
     * 分销商编号
     */
    private String drpCoIdFrom;

    /**
     * 修改时间
     */
    private Date modified;

    /**
     * 多标签
     */
    private String labels;

    /**
     * 实际支付金额
     */
    private String paidAmount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 买家留言
     */
    private String buyerMessage;

    /**
     * 物流公司编码
     */
    private String lcId;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 发票类型
     */
    private String invoiceType;

    /**
     * 发票税号
     */
    private String buyerTaxNo;

    /**
     * 业务员
     */
    private String creatorName;

    /**
     * 计划发货时间
     */
    private Date planDeliveryDate;

    /**
     * 线下备注
     */
    private String node;

    /**
     * 收件信息-街道
     */
    private String receiverTown;

    /**
     * 供销商编号
     */
    private String drpCoIdTo;

    /**
     * 运费成本
     */
    private String fFreight;

    /**
     * 店铺站点信息
     */
    private String shopSite;

    /**
     * 国际物流单号
     */
    private String unLid;

    /**
     * 收货时间（仅限头条放心购、京东、拼多多）
     */
    private Date endTime;

    /**
     * 国家代码
     */
    private String receiverCountry;

    /**
     * 邮编
     */
    private String receiverZip;

    /**
     * 旗帜(1红旗，2黄旗，3绿旗，4蓝旗，5紫旗)）
     */
    private Integer sellerFlag;

}