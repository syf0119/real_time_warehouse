package cn.syf;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Date 2019/12/24
 */
public class AllowCombinationsDemo{

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 模拟数据源
        //输入事件：c d a a a d a b
        DataStreamSource<String> source = env.fromElements("c", "d", "a", "a", "a", "d", "a", "b");

        //设置模式
        Pattern<String, String> pattern = Pattern.<String>begin("start")
                .where(new SimpleCondition<String>() {
                    @Override
                    public boolean filter(String value) throws Exception {
                        return value.equals("c");
                    }
                })
                // 此处是宽松临近 第一个匹配到的a 只能穿透非a事件，不能穿透其他a事件
                .followedBy("middle").where(new SimpleCondition<String>() {
                    @Override
                    public boolean filter(String value) throws Exception {
                        return value.equals("a");
                    }
                })
                .oneOrMore()
               // .allowCombinations()  // 第二个和之后匹配到的a 为非确定宽松临近，可以穿透a和非a事件
                .followedBy("end").where(new SimpleCondition<String>() {
                    @Override
                    public boolean filter(String value) throws Exception {
                        return value.equals("b");
                    }
                });

        PatternStream<String> cep = CEP.pattern(source, pattern);
        SingleOutputStreamOperator<Object> res = cep.select(new PatternSelectFunction<String, Object>() {
            @Override
            public Object select(Map<String, List<String>> map) throws Exception {

                List<String> middle = map.get("middle");
                List<String> start = map.get("start");
                List<String> end = map.get("end");
                Tuple3 tuple3 = new Tuple3(start, middle, end);
                return tuple3;
            }
        });
        res.print();
        env.execute();
    }
}
