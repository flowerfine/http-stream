CREATE TABLE `job_sync_offset`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `method`       varchar(64)         NOT NULL COMMENT '接口名称',
    `start_time`   datetime            NOT NULL COMMENT '修改起始时间',
    `end_time`     datetime            NOT NULL COMMENT '修改结束时间',
    `creator`      varchar(32)         NOT NULL DEFAULT 'system' COMMENT '创建人',
    `modifier`     varchar(32)         NOT NULL DEFAULT 'system' COMMENT '修改人',
    `is_deleted`   tinyint(4)          NOT NULL DEFAULT '0' COMMENT '删除标识。0: 未删除, 1: 已删除',
    `gmt_create`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_method` (`method`)
) ENGINE = InnoDB COMMENT ='同步进度位点';