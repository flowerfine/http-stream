package cn.sliew.http.stream.remote.jst.client.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrdersSingleDO {

    /**
     * 是否货到付款
     */
    @JsonProperty("is_cod")
    private String isCod;

    /**
     * 快递单号
     */
    @JsonProperty("l_id")
    private String lId;

    /**
     * 发货日期
     */
    @JsonProperty("send_date")
    private String sendDate;

    /**
     * 支付时间
     */
    @JsonProperty("pay_date")
    private String payDate;

    /**
     * 运费，保留两位小数，单位（元）
     */
    @JsonProperty("freight")
    private String freight;

    /**
     * 收货地址
     */
    @JsonProperty("receiver_address")
    private String receiverAddress;

    /**
     * 区
     */
    @JsonProperty("receiver_district")
    private String receiverDistrict;

    /**
     * 发货仓编号
     */
    @JsonProperty("wms_co_id")
    private String wmsCoId;

    /**
     * 快递公司
     */
    @JsonProperty("logistics_company")
    private String logisticsCompany;

    /**
     * 抵扣金额
     */
    @JsonProperty("free_amount")
    private String freeAmount;

    /**
     * 店铺名称
     */
    @JsonProperty("shop_name")
    private String shopName;

    /**
     * 问题类型，仅当问题订单时有效
     */
    @JsonProperty("question_type")
    private String questionType;

    /**
     * 外部支付单号
     */
    @JsonProperty("outer_pay_id")
    private String outerPayId;

    /**
     * 线上订单号，最长不超过 20;唯一
     */
    @JsonProperty("so_id")
    private String soId;

    /**
     * 订单类型
     */
    @JsonProperty("type")
    private String type;

    /**
     * 订单来源
     */
    @JsonProperty("order_from")
    private String orderFrom;

    /**
     * 待付款：WaitPay；发货中：Delivering；被合并：Merged；异常：Question；被拆分：Split；等供销商|外仓发货：WaitOuterSent；已付款待审核：WaitConfirm；已客审待财审：WaitFConfirm；已发货：Sent；取消：Cancelled
     */
    @JsonProperty("status")
    private String status;

    /**
     * 应付金额，保留两位小数，单位（元）
     */
    @JsonProperty("pay_amount")
    private String payAmount;

    /**
     * 买家昵称
     */
    @JsonProperty("shop_buyer_id")
    private String shopBuyerId;

    /**
     * 平台订单状态
     */
    @JsonProperty("shop_status")
    private String shopStatus;

    /**
     * 手机
     */
    @JsonProperty("receiver_mobile")
    private String receiverMobile;

    /**
     * 下单时间
     */
    @JsonProperty("order_date")
    private String orderDate;

    /**
     * 问题描述
     */
    @JsonProperty("question_desc")
    private String questionDesc;

    /**
     * 收件信息-市
     */
    @JsonProperty("receiver_city")
    private String receiverCity;

    /**
     * 收件信息-省
     */
    @JsonProperty("receiver_state")
    private String receiverState;

    /**
     * 收件信息-收件人
     */
    @JsonProperty("receiver_name")
    private String receiverName;

    /**
     * ERP 内部订单号，唯一
     */
    @JsonProperty("o_id")
    private Long oId;

    /**
     * 店铺编号
     */
    @JsonProperty("shop_id")
    private Long shopId;

    /**
     * 公司编号
     */
    @JsonProperty("co_id")
    private Long coId;

    /**
     * 支付信息
     */
    private List<PayBean> pays;

    /**
     * 商品信息
     */
    private List<ItemDO> items;

    /**
     * 订单备注；卖家备注
     */
    @JsonProperty("remark")
    private String remark;

    /**
     * 分销商编号
     */
    @JsonProperty("drp_co_id_from")
    private String drpCoIdFrom;

    /**
     * 修改时间
     */
    @JsonProperty("modified")
    private String modified;

    /**
     * 多标签
     */
    @JsonProperty("labels")
    private String labels;

    /**
     * 实际支付金额
     */
    @JsonProperty("paid_amount")
    private String paidAmount;

    /**
     * 币种
     */
    @JsonProperty("currency")
    private String currency;

    /**
     * 买家留言
     */
    @JsonProperty("buyer_message")
    private String buyerMessage;

    /**
     * 物流公司编码
     */
    @JsonProperty("lc_id")
    private String lcId;

    /**
     * 发票抬头
     */
    @JsonProperty("invoice_title")
    private String invoiceTitle;

    /**
     * 发票类型
     */
    @JsonProperty("invoice_type")
    private String invoiceType;

    /**
     * 发票税号
     */
    @JsonProperty("buyer_tax_no")
    private String buyerTaxNo;

    /**
     * 业务员
     */
    @JsonProperty("creator_name")
    private String creatorName;

    /**
     * 计划发货时间
     */
    @JsonProperty("plan_delivery_date")
    private String planDeliveryDate;

    /**
     * 线下备注
     */
    @JsonProperty("node")
    private String node;

    /**
     * 收件信息-街道
     */
    @JsonProperty("receiver_town")
    private String receiverTown;

    /**
     * 供销商编号
     */
    @JsonProperty("drp_co_id_to")
    private String drpCoIdTo;

    /**
     * 运费成本
     */
    @JsonProperty("f_freight")
    private String fFreight;

    /**
     * 店铺站点信息
     */
    @JsonProperty("shop_site")
    private String shopSite;

    /**
     * 国际物流单号
     */
    @JsonProperty("un_lid")
    private String unLid;

    /**
     * 收货时间（仅限头条放心购、京东、拼多多）
     */
    @JsonProperty("end_time")
    private String endTime;

    /**
     * 国家代码;
     */
    @JsonProperty("receiver_country")
    private String receiverCountry;

    /**
     * 邮编
     */
    @JsonProperty("receiver_zip")
    private String receiverZip;

    /**
     * 旗帜(1红旗，2黄旗，3绿旗，4蓝旗，5紫旗)）
     */
    @JsonProperty("seller_flag")
    private Integer sellerFlag;

    @Getter
    @Setter
    public static class PayBean {

        /**
         * 是否支付
         */
        @JsonProperty("is_order_pay")
        private Boolean isOrderPay;

        /**
         * 支付帐号
         */
        @JsonProperty("buyer_account")
        private String buyerAccount;

        /**
         * 支付金额
         */
        @JsonProperty("amount")
        private BigDecimal amount;

        /**
         * 支付时间
         */
        @JsonProperty("pay_date")
        private String payDate;

        /**
         * 外部支付单号
         */
        @JsonProperty("outer_pay_id")
        private String outerPayId;

        /**
         * 支付单ID
         */
        @JsonProperty("pay_id")
        private String payId;

        /**
         * 支付渠道
         */
        @JsonProperty("payment")
        private String payment;
    }

    @Getter
    @Setter
    public static class ItemDO {

        /**
         * 是否赠品
         */
        @JsonProperty("is_gift")
        private Boolean isGift;

        /**
         * 商家编码，对应库存管理的 SKU
         */
        @JsonProperty("sku_id")
        private String skuId;

        /**
         * 商品名称
         */
        @JsonProperty("name")
        private String name;

        /**
         * 申请退款的状态 , 未申请：none；退款中：waiting；退款成功：success
         */
        @JsonProperty("refund_status")
        private String refundStatus;

        /**
         * 退款的唯一单号
         */
        @JsonProperty("refund_id")
        private String refundId;

        /**
         * 单价
         */
        @JsonProperty("price")
        private String price;

        /**
         * 子订单号，最长不超过 50
         */
        @JsonProperty("outer_oi_id")
        private String outerOiId;

        /**
         * 商品状态
         */
        @JsonProperty("item_status")
        private String itemStatus;

        /**
         * 款式编码
         */
        @JsonProperty("i_id")
        private String iId;

        /**
         * 属性
         */
        @JsonProperty("properties_value")
        private String propertiesValue;

        /**
         * 子订单号，最长不超过 20
         */
        @JsonProperty("oi_id")
        private Long oiId;

        /**
         * 总额
         */
        @JsonProperty("amount")
        private String amount;

        /**
         * 外部sku_id
         */
        @JsonProperty("shop_sku_id")
        private String shopSkuId;

        /**
         * 原始线上单号
         */
        @JsonProperty("raw_so_id")
        private String rawSoId;

        /**
         * 数量
         */
        @JsonProperty("qty")
        private Integer qty;

        /**
         * 是否预售
         */
        @JsonProperty("is_presale")
        private String isPresale;

        /**
         * 基本售价
         */
        @JsonProperty("base_price")
        private String basePrice;

        /**
         * 商品图片
         */
        @JsonProperty("pic")
        private String pic;

        /**
         * 商品类型
         */
        @JsonProperty("sku_type")
        private String skuType;
    }
}
