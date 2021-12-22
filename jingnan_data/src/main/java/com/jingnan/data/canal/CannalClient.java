package com.jingnan.data.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.jingnan.data.pojo.UserOrderMap;
import com.jingnan.data.smapper.SUserOrderMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

@Component
public class CannalClient implements InitializingBean {

    private final static int BATCH_SIZE = 1000;
    //private CanalConnector connector;

    @Autowired
    private CanalConfig canalConfig;
    @Autowired
    private SUserOrderMapper sUserOrderMapper;

    @Override
    public void afterPropertiesSet() throws Exception {


        initCanalClient();
    }

    private void initCanalClient() {
        canalConfig.getClients().forEach(c->{
            // 创建链接
            CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(c.getHostname(), c.getPort()), c.getDestination(), c.getUsername(), c.getPassword());
            //打开连接
            connector.connect();
            //订阅数据库表,全部表
            connector.subscribe(".*\\..*");
            //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();
            //启动客户端
            startClient(connector);
        });
    }

    private void startClient(CanalConnector connector) {
        System.out.println("<============================ Canal client["+connector+"] is running ============================>");
        new Thread(()->{
            try {
                while (true) {
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(BATCH_SIZE);
                    //获取批量ID
                    long batchId = message.getId();
                    //获取批量的数量
                    int size = message.getEntries().size();
                    //如果没有数据
                    if (batchId == -1 || size == 0) {
                        try {
                            //线程休眠2秒
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //如果有数据,处理数据
                        //printEntry(message.getEntries());
                        parseEntry(message.getEntries());
                    }
                    //进行 batch id 的确认。确认之后，小于等于此 batchId 的 Message 都会被确认。
                    connector.ack(batchId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connector.disconnect();
            }

        }).start();

    }

    /**
     * 打印canal server解析binlog获得的实体类信息
     */
    private void printEntry(List<CanalEntry.Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                //开启/关闭事务的实体类型，跳过
                continue;
            }
            //RowChange对象，包含了一行数据变化的所有特征
            //比如isDdl 是否是ddl变更操作 sql 具体的ddl sql beforeColumns afterColumns 变更前后的数据字段等等
            RowChange rowChage;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }
            //获取操作类型：insert/update/delete类型
            EventType eventType = rowChage.getEventType();
            //打印Header信息
            System.out.println(String.format("================》; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));
            //判断是否是DDL语句
            if (rowChage.getIsDdl()) {
                System.out.println("================》;isDdl: true,sql:" + rowChage.getSql());
            }
            //获取RowChange对象里的每一行数据，打印出来
            for (RowData rowData : rowChage.getRowDatasList()) {
                //如果是删除语句
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                    //如果是新增语句
                } else if (eventType == EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                    //如果是更新的语句
                } else {
                    //变更前的数据
                    System.out.println("------->; before");
                    printColumn(rowData.getBeforeColumnsList());
                    //变更后的数据
                    System.out.println("------->; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private void printColumn(List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

    /**
     * 分析Entity中的数据详细
     */
    private void parseEntry(List<CanalEntry.Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                //开启/关闭事务的实体类型，跳过
                continue;
            }
            //取表名，判断是否是需要处理的表
            //由于是处理用户订单异构表，所以只处理订单表即可
            String tableName = entry.getHeader().getTableName();
            if (!"tb_order".equals(tableName)) {
                continue;
            }
            //RowChange对象，包含了一行数据变化的所有特征
            //比如isDdl 是否是ddl变更操作 sql 具体的ddl sql beforeColumns afterColumns 变更前后的数据字段等等
            RowChange rowChage;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }
            //获取操作类型：insert/update/delete类型
            EventType eventType = rowChage.getEventType();
            //打印Header信息
            System.out.println(String.format("================》; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));
            //判断是否是DDL语句
            if (rowChage.getIsDdl()) {
                System.out.println("================》;isDdl: true,sql:" + rowChage.getSql());
                continue;
            }
            //获取RowChange对象里的每一行数据，打印出来
            for (RowData rowData : rowChage.getRowDatasList()) {
                //如果是删除语句
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                    //如果是新增语句
                } else if (eventType == EventType.INSERT) {
                    //printColumn(rowData.getAfterColumnsList());
                    //新增订单时，需要维护用户订单异构表
                    syncUserOrderSubTable(rowData.getAfterColumnsList());
                    //如果是更新的语句
                } else {
                    //变更前的数据
                    System.out.println("------->; before");
                    printColumn(rowData.getBeforeColumnsList());
                    //变更后的数据
                    System.out.println("------->; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    /**
     * 生成订单时，同步维护用户-订单异构子表
     * @param columns
     */
    private void syncUserOrderSubTable(List<Column> columns) {
        //取订单号取用户名,创建订单用户对象
        UserOrderMap userOrder = new UserOrderMap();
        columns.forEach(c->{
            if ("id".equals(c.getName())) {
                userOrder.setOrderId(c.getValue());
            } else if ("username".equals(c.getName())) {
                userOrder.setUsername(c.getValue());
            }
        });
        System.out.println(userOrder);
        //添加数据
        sUserOrderMapper.insert(userOrder);
    }
}