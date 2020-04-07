package cn.syf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;

import java.net.InetSocketAddress;
import java.util.List;

public class CanalClient {


    public static void main(String args[]) {
        // 创建链接
        //单机模式
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("summer",
                11111), "shop", "root", "0119");

//        //高可用模式
//        CanalConnector connector = CanalConnectors.newClusterConnector("node01:2181,node02:2181,node03:2181",
//                "shop", "", "");
//


        int batchSize = 1000; //批量数据大小
        int emptyCount = 0;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            int totalEmptyCount = 12000;
            while (emptyCount < totalEmptyCount) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId(); //每一批数据的id
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    //System.out.println("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000); //1s钟轮询一次
                    } catch (InterruptedException e) {
                    }
                } else { //说明是有数据更新的
                    emptyCount = 0;
                    //解析数据
                    printEntry(message.getEntries());
                }

                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }

            System.out.println("empty too many times, exit");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }

    private static void printEntry(List<CanalEntry.Entry> entries) throws Exception {

        for (CanalEntry.Entry entry : entries) {

            CanalEntry.Header header = entry.getHeader();
            //通过header获取表约束信息
            long logfileOffset = header.getLogfileOffset();
            String schemaName = header.getSchemaName(); //库名
            String tableName = header.getTableName();//表名
            CanalEntry.EventType eventType = header.getEventType(); //操作类型
            String logfileName = header.getLogfileName(); //文件名
            long executeTime = header.getExecuteTime();

            //拼接字段
            String binlog = logfileName + "." + logfileOffset;
            String table = schemaName + "." + tableName;

            //解析binlog,获取列名和列值以及列类型
            ByteString storeValue = entry.getStoreValue();
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
            List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
            for (CanalEntry.RowData rowData : rowDatasList) {
                if (eventType == CanalEntry.EventType.DELETE) {

                    parseData(rowData.getBeforeColumnsList(), binlog, table, executeTime, eventType);
                } else {
                    parseData(rowData.getAfterColumnsList(), binlog, table, executeTime, eventType);
                }
            }
        }
    }

    private static void parseData(List<CanalEntry.Column> columnsList, String binlog, String table, long executeTime, CanalEntry.EventType eventType) {
        JSONArray jsonArray = new JSONArray();
        //columnsList包含一整行binlog数据中的每一列数据（列值，列名，列类型）
        for (CanalEntry.Column column : columnsList) {
            JSONObject jsonObject = new JSONObject();
            String name = column.getName();
        String value = column.getValue();
        String mysqlType = column.getMysqlType();
        jsonObject.put("col", name);
        jsonObject.put("val", value);
        jsonObject.put("type", mysqlType);
        jsonArray.add(jsonObject);
    }

        //新建json，封装日志数据
        JSONObject json = new JSONObject();
        json.put("eventType", eventType);
        json.put("exeTime", executeTime);
        json.put("binlog", binlog);
        json.put("cols", jsonArray);
        json.put("table", table);
        System.out.println(json.toString());

        //发送kafka
        KafkaUtil.sendData("order",json.toString());
    }
}